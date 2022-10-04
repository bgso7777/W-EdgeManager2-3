package com.inswave.appplatform.wedgemanager.service.scm.distribute;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.svn.SvnManager;
import com.inswave.appplatform.svn.domain.LogMessageVO;
import com.inswave.appplatform.util.StringUtil;
import com.inswave.appplatform.wedgemanager.controller.terminal.TerminalDistributeController;
import com.inswave.appplatform.wedgemanager.dao.TerminalDistDao;
import com.inswave.appplatform.wedgemanager.dao.TerminalDistScheduleDao;
import com.inswave.appplatform.wedgemanager.dao.TerminalDistScheduleTargetDao;
import com.inswave.appplatform.wedgemanager.dao.TerminalPreferenceDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.*;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.DistributeTaskStatusVO;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalDistResultDetailFileVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.subversion.javahl.SubversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;
import org.tigris.subversion.javahl.ClientException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DistributeManager {
    @Value("${wedgemanager.isMasterClusterNode:#{false}}")
    private       boolean                       isMasterNode = false;
    private final DistributeTask                curruntTask  = null;
    private       SvnManager                    svnManager;
    private final BlockingQueue<DistributeTask> taskQueue    = new LinkedBlockingDeque<>();

    @Autowired
    private TerminalDistDao               terminalDistDao;
    @Autowired
    private TerminalDistScheduleDao       terminalDistScheduleDao;
    @Autowired
    private TerminalDistScheduleTargetDao terminalDistScheduleTargetDao;
    @Autowired
    private TerminalPreferenceDao         terminalPreferenceDao;
    @Autowired
    private TerminalDistributeController  terminalDistributeController;

    @PostConstruct
    public void postConstruct() {
        svnManager = SvnManager.getInstance();
        //        generateDistSchedulePreset();

        Thread taskExecutionThread = new Thread("dist-task-execute-thread") {
            public void run() {
                //noinspection InfiniteLoopStatement
                while (true) {
                    try {
                        DistributeTask task = taskQueue.take();
                        updateWorkingCopy(task.getRepositoryName());
                        task.execute();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                        log.warn("배포실행 오류: {}", e.getMessage(), e);
                    }
                }
            }
        };
        taskExecutionThread.setDaemon(true);
        taskExecutionThread.start();
        log.info("{} 쓰레드 시작됨", taskExecutionThread.getName());
    }

    public void updateWorkingCopy(String repositoryName) {
        SvnManager svnManager = SvnManager.getInstance();
        svnManager.updateWorkingCopy(repositoryName);
    }

    public boolean cancelTask(TerminalDist terminalDist) {
        // TODO. 배포대기열에서 특정 배포Task를 삭제. (식별자 생성필요)
        // TODO. 배포Task queue를 DB형태로 만든다? 클러스터 환경에서 필요할것으로 보임..
        return false;
    }

    public List<DistributeTask> isAddedTask(TerminalDist terminalDist) {
        // TODO. 해당 배포대상이 포함된 배포대기열을 반환.
        return null;
    }

    public boolean isAddedTask(TerminalDistSchedule terminalDistSchedule) {
        Long id = terminalDistSchedule.getId();
        if (id != null) {
            return DistributeService.countUnExecutedScheduleTask(id).size() > 0;
        }
        return false;
    }

    public DistributeTask addTask(String repositoryName, List<TerminalDist> terminalDists, boolean beforeClean) throws IOException {
        return addTask(repositoryName, terminalDists, beforeClean, null);
    }

    public DistributeTask addTask(String repositoryName, List<TerminalDist> terminalDists, boolean beforeClean, TerminalDistSchedule terminalDistSchedule) throws IOException {
        DistributeTask distributeTask = DistributeTask.builder()
                                                      .repositoryName(repositoryName)
                                                      .beforeClean(beforeClean)
                                                      .terminalDistIds(String.join(",", terminalDists.stream().map(terminalDist -> String.valueOf(terminalDist.getId())).collect(Collectors.toList())))
                                                      .terminalDistScheduleId(terminalDistSchedule.getId())
                                                      .build();

        return DistributeService.saveDistributeTask(distributeTask);
    }

    public DistributeTaskStatusVO getDistributeTaskStatus() {
        return curruntTask != null ? DistributeTaskStatusVO.from(curruntTask) : null;
    }

    @Scheduled(fixedDelay = 30000) // 30초 간격, 자동배포 스캔 및 실행
    public void distributeByRevision() {
        if (isMasterNode && svnManager.configured) {
            log.debug("autoDistribute : {}", ZonedDateTime.now());
            svnManager.getRepositories().forEach(repositoryVO -> {
                String repositoryName = repositoryVO.getName();
                String TAG_DIST_RUNNING = String.format("distribute.terminal.%s.distRunning", repositoryName);
                String TAG_DIST_TYPE = String.format("distribute.terminal.%s.distType", repositoryName);
                String TAG_LAST_REVISION = String.format("distribute.terminal.%s.lastRevision", repositoryName);
                //                String TAG_PATH_SOURCE = String.format("distribute.terminal.%s.pathSource", repositoryName);
                //                String TAG_PATH_DESTINATION = String.format("distribute.terminal.%s.pathDestination", repositoryName);

                Optional<TerminalPreference> optionalDistRunning = terminalPreferenceDao.findByDataKey(TAG_DIST_RUNNING);
                Optional<TerminalPreference> optionalDistType = terminalPreferenceDao.findByDataKey(TAG_DIST_TYPE);
                //                Optional<TerminalPreference> optionalPathSource = terminalPreferenceDao.findByDataKey(TAG_PATH_SOURCE);
                //                Optional<TerminalPreference> optionalPathDestination = terminalPreferenceDao.findByDataKey(TAG_PATH_DESTINATION);

                try {
                    if (optionalDistRunning.isPresent()) {
                        TerminalPreference distRunning = optionalDistRunning.get();
                        if (distRunning.getDataValue().equalsIgnoreCase("true")) {

                            JsonNode accessMap = svnManager.getAccessMapJson(repositoryName);
                            JsonNode solutionPathNode = accessMap.at("/config/solutionPath");
                            JsonNode distributePathNode = accessMap.at("/config/distributePath");
                            JsonNode distributePathSrcNode = accessMap.at("/config/distributePathSrc");

                            String solutionPath = solutionPathNode != null ? solutionPathNode.asText() : null;
                            String distributePath = distributePathNode != null ? distributePathNode.asText() : null;
                            String distributePathSrc = distributePathSrcNode != null ? distributePathSrcNode.asText() : null;

                            if (solutionPath != null && distributePath != null) {
                                optionalDistType.ifPresent(distType -> {
                                    if (distType.getDataValue().equalsIgnoreCase("auto")) {
                                        // auto (자동배포), normal(일반배포)
                                        Optional<TerminalPreference> optionalLastRevision = terminalPreferenceDao.findByDataKey(TAG_LAST_REVISION);
                                        Long lastDistributedRevision = 1L;  // Revision 초기화 -> 1
                                        if (optionalLastRevision.isPresent()) { // 배포된 적 있을 경우 lastRevision 초기화
                                            lastDistributedRevision = Long.valueOf(optionalLastRevision.get().getDataValue());
                                        }

                                        try {
                                            Long currentRevision = svnManager.getLatestRevision(repositoryName);
                                            if (currentRevision.compareTo(lastDistributedRevision) > 0) {    // 현재버전(currentRevision:svn)이 마지막배포버전(lastDistributedRevision:was)보다 높을 때
                                                updateWorkingCopy(repositoryName);

                                                //                                                String srcPathSub = optionalPathSource.isPresent() ? optionalPathSource.get().getDataValue() : "";
                                                //                                                String destPathSub = optionalPathDestination.isPresent() ? optionalPathDestination.get().getDataValue() : "";

                                                Path srcPath = Paths.get(FilenameUtils.separatorsToUnix(svnManager.getSvnWorkingCopyPath(repositoryName).toString()));
                                                srcPath = srcPath.resolve(StringUtil.rebuildPath("/", distributePathSrc));
                                                Path destPath = Paths.get(FilenameUtils.separatorsToUnix(distributePath));

                                                TerminalDistResultDetail terminalDistResultDetail = TerminalDistResultDetail.builder()
                                                                                                                            .distType(distType.getDataValue())
                                                                                                                            .distName(distType.getDataValue())
                                                                                                                            //                                                                                                                            .pathSource(srcPathSub)
                                                                                                                            .pathSource(srcPath.toString())
                                                                                                                            //                                                                                                                            .pathDestination(destPathSub)
                                                                                                                            .pathDestination(destPath.toString())
                                                                                                                            .includeSubdir(true)
                                                                                                                            .build();
                                                terminalDistResultDetail.setRepositoryName(repositoryName);
                                                terminalDistResultDetail.setDistVersion(ZonedDateTime.now());
                                                terminalDistResultDetail.setTaskType("auto");
                                                terminalDistResultDetail.setExecuteStartDate(ZonedDateTime.now());

                                                try {
                                                    List<TerminalDistResultDetailFileVO> resultDetails = new ArrayList<>();
                                                    List<LogMessageVO> logMessageVOS = svnManager.getLogMessage(repositoryName,
                                                                                                                true,
                                                                                                                org.tigris.subversion.javahl.Revision.getInstance(lastDistributedRevision + 1),
                                                                                                                org.tigris.subversion.javahl.Revision.getInstance(currentRevision))
                                                                                                 .stream()
                                                                                                 .sorted(Comparator.comparing(LogMessageVO::getRevision))
                                                                                                 .collect(Collectors.toList());

                                                    Path finalSrcPath = srcPath;
                                                    logMessageVOS.forEach(logMessageVO -> {
                                                        logMessageVO.getChangedFiles().forEach(logMessageFileVO -> {

                                                            char action = logMessageFileVO.getAction();
                                                            String path = logMessageFileVO.getPath();

                                                            Path svnPath = Paths.get(StringUtil.rebuildPath("/", path));
                                                            Path svnDistSrcPath = Paths.get(StringUtil.rebuildPath("/", distributePathSrc));

                                                            if (svnPath.toString().startsWith(svnDistSrcPath.toString())) {
                                                                svnPath = svnDistSrcPath.relativize(svnPath);

                                                                Path src = Paths.get("/" + StringUtil.rebuildPath("/", finalSrcPath.toString(), svnPath.toString()));
                                                                Path dest = Paths.get("/" + StringUtil.rebuildPath("/", destPath.toString(), svnPath.toString()));

                                                                log.debug("action:{}  src->{} dest->{}", action, src, dest);

                                                                TerminalDistResultDetailFileVO distResultDetail = TerminalDistResultDetailFileVO.builder().build();
                                                                switch (action) {   // 'A'dd, 'D'elete, 'R'eplace, 'M'odify : 실제로 R은 없음 (A, D로 동작)
                                                                case 'D':
                                                                    try {
                                                                        if (Files.isDirectory(dest)) {
                                                                            FileUtils.deleteDirectory(dest.toFile());
                                                                        } else {
                                                                            Files.deleteIfExists(dest);
                                                                        }
                                                                    } catch (IOException e) {
                                                                        distResultDetail.setSuccess(false);
                                                                        distResultDetail.setDesc(e.toString());
                                                                        e.printStackTrace();
                                                                    }
                                                                    break;
                                                                case 'A':   //기본동작
                                                                case 'M':   //기본동작
                                                                case 'R':   //기본동작
                                                                default:    //기본동작
                                                                    try {
                                                                        if (Files.exists(src)) { // 마지막 working copy를 기준으로 배포를 시도하기 때문에, 없는 파일이 존재할 수 있다.
                                                                            if (Files.isDirectory(dest)) {
                                                                                Files.createDirectories(dest);
                                                                            } else {
                                                                                if (!Files.exists(dest.getParent())) {
                                                                                    Files.createDirectories(dest.getParent());
                                                                                }
                                                                                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                                                                            }
                                                                        }
                                                                    } catch (IOException e) {
                                                                        distResultDetail.setSuccess(false);
                                                                        distResultDetail.setDesc(e.toString());
                                                                        e.printStackTrace();
                                                                    }
                                                                    break;
                                                                }

                                                                distResultDetail.setAction(String.valueOf(action));
                                                                distResultDetail.setPathSource(src.toString());
                                                                distResultDetail.setPathDestination(dest.toString());
                                                                distResultDetail.setPathDestinationFileSize(0L);
                                                                if (Files.exists(src)) {
                                                                    try {
                                                                        distResultDetail.setPathDestinationFileSize(Files.size(src));

//                                                                        Files.setPosixFilePermissions(dest, PosixFilePermissions.fromString("rwxrwxrwx"));
                                                                        dest.toFile().setReadable(true, false);          //r
                                                                        dest.toFile().setWritable(true, false);          //w
                                                                        dest.toFile().setExecutable(true, false);      //x
                                                                    } catch (IOException e) {
//                                                                        e.printStackTrace();
                                                                    }
                                                                }

                                                                resultDetails.add(distResultDetail);
                                                                String message = String.format("[%d-%s] %s, %s byte%s",
                                                                                               logMessageVO.getRevision(),
                                                                                               distResultDetail.getActionName(),
                                                                                               distResultDetail.getPathDestination(),
                                                                                               distResultDetail.getPathDestinationFileSize(),
                                                                                               distResultDetail.getDesc() != null ? ", : " + distResultDetail.getDesc() : ""
                                                                );
                                                                terminalDistributeController.publish(repositoryName, message);
                                                                //                                                            }

                                                            }
                                                        });
                                                    });

                                                    terminalDistResultDetail.setDistResultTotalFileCount(resultDetails.size());
                                                    terminalDistResultDetail.setDistResultTotalFileSize(resultDetails.stream()
                                                                                                                     .map(TerminalDistResultDetailFileVO::getPathDestinationFileSize)
                                                                                                                     .collect(Collectors.reducing(Long::sum))
                                                                                                                     .orElse(0L));
                                                    terminalDistResultDetail.setDistResultDetailOrigin(resultDetails);
                                                    terminalDistResultDetail.setExecuteEndDate(ZonedDateTime.now());
                                                    terminalDistResultDetail.setExecuteSuccessYn(true);

                                                    if (optionalLastRevision.isPresent()) {
                                                        optionalLastRevision.get().setDataValue(String.valueOf(currentRevision));
                                                        optionalLastRevision.get().setUpdateDate(ZonedDateTime.now());
                                                        terminalPreferenceDao.save(optionalLastRevision.get());
                                                    } else {
                                                        terminalPreferenceDao.save(TerminalPreference.builder()
                                                                                                     .dataKey(TAG_LAST_REVISION)
                                                                                                     .dataValue(String.valueOf(currentRevision))
                                                                                                     .build());
                                                    }

                                                    DistributeService.saveTerminalDistDetailResult(terminalDistResultDetail);
                                                } catch (ClientException e) {
                                                    e.printStackTrace();
                                                    terminalDistResultDetail.setExecuteSuccessYn(false);
                                                    DistributeService.saveTerminalDistDetailResult(terminalDistResultDetail);
                                                }
                                            }
                                        } catch (SubversionException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //    @Scheduled(fixedDelay = 15000) // 30초 간격, 2. 작업대기열 스캔 및 작업실행(스레드 주입)
    public void distributeScheduledTaskMonitor() {
        if (isMasterNode && svnManager.configured) {
            Optional<DistributeTask> optionalDistributeTask = DistributeService.findFirstByExecutedAndExecuteDateNullOrderByIdDesc();
            optionalDistributeTask.ifPresent(task -> {
                try {
                    taskQueue.put(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Scheduled(fixedDelay = 30000) // 30초 간격 스케쥴배포 스캔 및 실행
    public void distributeBySchedule() {
        if (isMasterNode && svnManager.configured) {
            List<TerminalDistSchedule> terminalDistScheduleList = terminalDistScheduleDao.findScheduleUpdateTarget(ZonedDateTime.now());

            terminalDistScheduleList.forEach(terminalDistSchedule -> {

                String repositoryName = terminalDistSchedule.getRepositoryName();
                String TAG_DIST_RUNNING = String.format("distribute.terminal.%s.distRunning", repositoryName);
                String TAG_DIST_TYPE = String.format("distribute.terminal.%s.distType", repositoryName);
                String TAG_LAST_REVISION = String.format("distribute.terminal.%s.lastRevision", repositoryName);

                Optional<TerminalPreference> optionalDistRunning = terminalPreferenceDao.findByDataKey(TAG_DIST_RUNNING);
                Optional<TerminalPreference> optionalDistType = terminalPreferenceDao.findByDataKey(TAG_DIST_TYPE);

                if (optionalDistType.isPresent() && optionalDistType.get().getDataValue().equals("schedule")
                    && optionalDistRunning.isPresent() && optionalDistRunning.get().getDataValue().equals("true")) {

                    Optional<TerminalPreference> optionalLastRevision = terminalPreferenceDao.findByDataKey(TAG_LAST_REVISION);
                    Long lastDistributedRevision = 1L;  // Revision 초기화 -> 1
                    if (optionalLastRevision.isPresent()) { // 배포된 적 있을 경우 lastRevision 초기화
                        lastDistributedRevision = Long.valueOf(optionalLastRevision.get().getDataValue());
                    }

                    try {
                        Long currentRevision = svnManager.getLatestRevision(repositoryName);
                        if (currentRevision.compareTo(lastDistributedRevision) > 0) {

                            ZonedDateTime currentDate = ZonedDateTime.now();
                            ZonedDateTime lastExecuteDate = terminalDistSchedule.getLastExecuteDateOrigin();
                            ZonedDateTime createDate = terminalDistSchedule.getCreateDateOrigin();

                            ZonedDateTime nextDate = null;
                            if (lastExecuteDate != null) {
                                nextDate = nextDate(terminalDistSchedule.getSchedule(), lastExecuteDate);
                            } else {
                                nextDate = nextDate(terminalDistSchedule.getSchedule(), createDate);
                            }

                            if (nextDate.isBefore(currentDate) || nextDate.isEqual(currentDate)) {

                                try {
                                    terminalDistributeController.execute(terminalDistSchedule.getRepositoryName(), "/", true, "schedule");
                                    terminalDistSchedule.setLastExecuteDate(ZonedDateTime.now());
                                    DistributeService.saveTerminalDistSchedule(terminalDistSchedule);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SubversionException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (optionalLastRevision.isPresent()) {
                                optionalLastRevision.get().setDataValue(String.valueOf(currentRevision));
                                optionalLastRevision.get().setUpdateDate(ZonedDateTime.now());
                                terminalPreferenceDao.save(optionalLastRevision.get());
                            } else {
                                terminalPreferenceDao.save(TerminalPreference.builder()
                                                                             .dataKey(TAG_LAST_REVISION)
                                                                             .dataValue(String.valueOf(currentRevision))
                                                                             .build());
                            }
                        }
                    } catch (SubversionException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public ZonedDateTime nextDate(String cronExpr, ZonedDateTime compareTarget) {
        return CronExpression.parse(cronExpr).next(compareTarget);
    }

    public void generateDistSchedulePreset() {
        List<TerminalDist> terminalDists = terminalDistDao.findByRepositoryName("KPIC", Sort.by("distName"));
        TerminalDistSchedule terminalDistScheduleOnce = terminalDistScheduleDao.save(TerminalDistSchedule.builder()
                                                                                                         .repositoryName("KPIC")
                                                                                                         .schedule("0 0/10 * * * *")
                                                                                                         .scheduleType("Once")
                                                                                                         .isActive(true)
                                                                                                         .lastExecuteDate(null)
                                                                                                         .build());
        TerminalDistSchedule terminalDistScheduleRepeat = terminalDistScheduleDao.save(TerminalDistSchedule.builder()
                                                                                                           .repositoryName("KPIC")
                                                                                                           .schedule("0 0/1 * * * *")
                                                                                                           .scheduleType("Once")
                                                                                                           .isActive(true)
                                                                                                           .lastExecuteDate(null)
                                                                                                           .build());

        terminalDists.forEach(terminalDist -> {
            terminalDistScheduleTargetDao.save(TerminalDistScheduleTarget.builder()
                                                                         .distSchedule(terminalDistScheduleOnce)
                                                                         .targetDist(terminalDist)
                                                                         .build());
            terminalDistScheduleTargetDao.save(TerminalDistScheduleTarget.builder()
                                                                         .distSchedule(terminalDistScheduleRepeat)
                                                                         .targetDist(terminalDist)
                                                                         .build());
        });

    }

}
