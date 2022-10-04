package com.inswave.appplatform.wedgemanager.service.scm.distribute;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.service.domain.StandardDomain;
import com.inswave.appplatform.svn.SvnManager;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDist;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDistResultDetail;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDistSchedule;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalDistResultDetailFileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@TableGenerator(
name = "TS_DIST_TASK_GENERATOR",
pkColumnValue = "TS_DIST_TASK_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_dist_task")
@Entity
public class DistributeTask extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_DIST_TASK_GENERATOR")
    private Long          id;
    @Column
    private String        repositoryName;
    @Column
    private Boolean       beforeClean;
    @Builder.Default
    @Column
    private Boolean       executed               = false;
    @Column
    private ZonedDateTime executeDate;
    @Column
    private Long          terminalDistScheduleId = null;
    @Column
    private String        terminalDistIds        = null;

    @Builder.Default
    @Transient
    private DistributeTaskStatusType status                    = DistributeTaskStatusType.WAIT;
    @Builder.Default
    @Transient
    private String                   statusMessage             = DistributeTaskStatusType.WAIT.toString();
    @Builder.Default
    @Transient
    private Integer                  currentStatusTaskPosition = 0;
    @Builder.Default
    @Transient
    private Integer                  currentStatusTaskCount    = 0;
    @Builder.Default
    @Transient
    private Integer                  totalTaskPosition         = 0;
    @Builder.Default
    @Transient
    private Integer                  totalTaskCount            = 0;
    @Transient
    private String                   solutionPath;
    @Transient
    private String                   distributePath;
    @SuppressWarnings("JpaAttributeTypeInspection")
    @Builder.Default
    @Transient
    private List<TerminalDist>       terminalDists             = new ArrayList<>();

    public void init() throws IOException {
        JsonNode accessMap = SvnManager.getInstance().getAccessMapJson(repositoryName);
        JsonNode solutionPathNode = accessMap.at("/config/solutionPath");
        JsonNode distributePathNode = accessMap.at("/config/distributePath");

        String solutionPath = solutionPathNode != null ? solutionPathNode.asText() : null;
        String distributePath = distributePathNode != null ? distributePathNode.asText() : null;

        if (solutionPath != null && distributePath != null) {
            this.solutionPath = solutionPath;
            this.distributePath = distributePath;
        } else {
            new Exception("solutionPath, distributePath are not detect. please run after configuration.");
        }
        String[] strArrIds = terminalDistIds.split(",");
        List<String> strListIds = Arrays.stream(strArrIds).collect(Collectors.toList());
        List<Long> ids = strListIds.stream().map(Long::valueOf).collect(Collectors.toList());
        terminalDists = DistributeService.getTerminalDists(ids);
        if (terminalDists.size() == 0) {
            new Exception(String.format("Distribute targets are not exists.[%s]", terminalDistIds));
        }
    }

    public void execute() throws IOException {
        init();

        executeDate = ZonedDateTime.now();
        executed = true;
        DistributeService.saveDistributeTask(this);

        Path srcPath = Paths.get(FilenameUtils.separatorsToSystem(SvnManager.getInstance().getSvnWorkingCopyPath(repositoryName).toString()));
        Path distPath = Paths.get(FilenameUtils.separatorsToSystem(distributePath));

        // 테스트
        if (!Files.isReadable(srcPath)) {
            log.warn("배포소스 경로에 읽기 권한이 없습니다. 배포를 종료합니다. (src: {})", srcPath);
            return;
        }
        if (!Files.isWritable(distPath)) {
            log.warn("배포목적지 경로에 쓰기 권한이 없습니다. 배포를 종료합니다. (dest: {})", distPath);
            return;
        }

        ZonedDateTime version = ZonedDateTime.now();
        //  scan
        Map<String, Map<TerminalDist, List<Path>>> scanPath = scan();
        //  beforeClean
        if (beforeClean) {
            scanPath.get("dirCleanTarget").entrySet().forEach(entry -> {
                clean(entry.getValue());
            });
        }
        DistributeService.saveDistributeTask(this);

        //  distribute
        List<TerminalDistResultDetail> terminalDistList = new ArrayList<>();
        scanPath.get("fileDistTarget").entrySet().forEach(entry -> {
            TerminalDist terminalDist = entry.getKey();
            TerminalDistResultDetail terminalDistResultDetail = TerminalDistResultDetail.from(terminalDist);
            terminalDistResultDetail.setRepositoryName(repositoryName);
            terminalDistResultDetail.setDistVersion(version);
            terminalDistResultDetail.setTaskType(terminalDistScheduleId != null ? "schedule" : "normal");
            terminalDistResultDetail.setExecuteStartDate(ZonedDateTime.now());
            List<TerminalDistResultDetailFileVO> resultDetails = distribute(entry.getValue());
            terminalDistResultDetail.setDistResultTotalFileCount(resultDetails.size());
            terminalDistResultDetail.setDistResultTotalFileSize(resultDetails.stream()
                                                                             .map(TerminalDistResultDetailFileVO::getPathDestinationFileSize)
                                                                             .collect(Collectors.reducing(Long::sum))
                                                                             .get());
            terminalDistResultDetail.setDistResultDetailOrigin(resultDetails);
            terminalDistResultDetail.setExecuteEndDate(ZonedDateTime.now());
            terminalDistResultDetail.setExecuteSuccessYn(true);
            terminalDistList.add(terminalDistResultDetail);
        });
        DistributeService.saveDistributeTask(this);

        //  result save
        saveResult(terminalDistList);
        DistributeService.saveDistributeTask(this);

        updateVersion(version);
        DistributeService.saveDistributeTask(this);

        status = DistributeTaskStatusType.COMPLETED;
        DistributeService.saveDistributeTask(this);
    }

    public Map<String, Map<TerminalDist, List<Path>>> scan() {
        status = DistributeTaskStatusType.INPROGRESS_SCANNING;

        Map<String, Map<TerminalDist, List<Path>>> listMap = new HashMap<>();

        AtomicInteger posLocal = new AtomicInteger();
        currentStatusTaskCount = terminalDists.size();
        totalTaskCount += currentStatusTaskCount;
        terminalDists.forEach(terminalDist -> {
            currentStatusTaskPosition = posLocal.getAndIncrement();
            totalTaskPosition++;
            updateStatusMessage(terminalDist.getDistName());
            List<Path> fileDistTarget = new ArrayList<>();
            List<Path> dirCleanTarget = new ArrayList<>();
            listMap.put("fileDistTarget", new HashMap<TerminalDist, List<Path>>() {{
                put(terminalDist, fileDistTarget);
            }});
            listMap.put("dirCleanTarget", new HashMap<TerminalDist, List<Path>>() {{
                put(terminalDist, dirCleanTarget);
            }});
            Path distSrc = Paths.get(FilenameUtils.separatorsToSystem(SvnManager.getInstance().getSvnWorkingCopyPath(repositoryName).toString()),
                                     FilenameUtils.separatorsToSystem(solutionPath),
                                     FilenameUtils.separatorsToSystem(terminalDist.getPathSource()));
            Path distDest = Paths.get(FilenameUtils.separatorsToSystem(distributePath),
                                      FilenameUtils.separatorsToSystem(terminalDist.getPathDestination()));
            try {
                if (Files.exists(distDest) && Files.isDirectory(distDest)) {
                    dirCleanTarget.add(distDest);
                }
                Stream<Path> stream = terminalDist.getIncludeSubdir() ? Files.walk(distSrc) : Files.walk(distSrc, 1);
                stream.filter(path -> !Files.isDirectory(path))
                      .forEach(path -> {
                          fileDistTarget.add(path);
                      });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return listMap;
    }

    public void clean(List<Path> target) {
        status = DistributeTaskStatusType.INPROGRESS_BEFORE_CLEANING;

        AtomicInteger posLocal = new AtomicInteger();
        currentStatusTaskCount = target.size();
        totalTaskCount += currentStatusTaskCount;
        target.forEach(path -> {
            currentStatusTaskPosition = posLocal.getAndIncrement();
            totalTaskPosition++;

            updateStatusMessage(path.toString());
            try {
                FileUtils.deleteDirectory(path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<TerminalDistResultDetailFileVO> distribute(List<Path> target) {
        status = DistributeTaskStatusType.INPROGRESS_COPY;
        List<TerminalDistResultDetailFileVO> resultDetails = new ArrayList<>();
        Path srcPath = Paths.get(FilenameUtils.separatorsToUnix(SvnManager.getInstance().getSvnWorkingCopyPath(repositoryName).toString()),
                                 FilenameUtils.separatorsToUnix(solutionPath));
        Path destPath = Paths.get(FilenameUtils.separatorsToUnix(distributePath));

        AtomicInteger posLocal = new AtomicInteger();
        currentStatusTaskCount = target.size();
        totalTaskCount += currentStatusTaskCount;
        for (int i = 0; i < target.size(); i++) {
            currentStatusTaskPosition = posLocal.getAndIncrement();
            totalTaskPosition++;

            TerminalDistResultDetailFileVO distResultDetail = TerminalDistResultDetailFileVO.builder().build();
            Path src = target.get(i);
            Path dest = destPath.resolve(srcPath.relativize(src));
            distResultDetail.setPathSource(src.toAbsolutePath().toString());
            distResultDetail.setPathDestination(dest.toAbsolutePath().toString());
            //            log.info("src -> dest : {} -> {}", src, dest);
            updateStatusMessage(String.format("src -> dest : %s -> %s", src, dest));
            try {
                if (!Files.exists(dest.getParent())) Files.createDirectories(dest.getParent());
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                distResultDetail.setPathDestinationFileSize(Files.size(dest));
            } catch (IOException e) {
                distResultDetail.setPathDestinationFileSize((long) -1);
                e.printStackTrace();
            }
            resultDetails.add(distResultDetail);
        }
        return resultDetails;
    }

    public void saveResult(List<TerminalDistResultDetail> terminalDistResultDetails) {
        status = DistributeTaskStatusType.INPROGRESS_RESULT_SAVE;

        AtomicInteger posLocal = new AtomicInteger();
        currentStatusTaskCount = terminalDistResultDetails.size();
        totalTaskCount += currentStatusTaskCount;
        terminalDistResultDetails.forEach(terminalDistResultDetail -> {
            currentStatusTaskPosition = posLocal.getAndIncrement();
            totalTaskPosition++;

            updateStatusMessage(terminalDistResultDetail.getDistName());
            DistributeService.saveTerminalDistDetailResult(terminalDistResultDetail);
        });
    }

    private void updateVersion(ZonedDateTime version) {
        status = DistributeTaskStatusType.INPROGRESS_UPDATE_VERSION;
        AtomicInteger posLocal = new AtomicInteger();
        currentStatusTaskCount = terminalDists.size();
        totalTaskCount += currentStatusTaskCount;
        terminalDists.forEach(terminalDist -> {
            currentStatusTaskPosition = posLocal.getAndIncrement();
            totalTaskPosition++;

            updateStatusMessage(terminalDist.getDistType());
            terminalDist.setLastDistVersion(version);
        });
        DistributeService.saveTerminalDists(terminalDists);

        if (terminalDistScheduleId != null) {
            Optional<TerminalDistSchedule> terminalDistSchedule = DistributeService.findTerminalDistScheduleById(terminalDistScheduleId);
            terminalDistSchedule.ifPresent(terminalDistSchedule1 -> {
                terminalDistSchedule1.setLastExecuteDate(ZonedDateTime.now());
                DistributeService.saveTerminalDistSchedule(terminalDistSchedule1);
            });
        }
    }

    private void updateStatusMessage(String msg) {
        switch (status) {
        case INPROGRESS_SCANNING:
            statusMessage = String.format("배포소스를 스캔합니다. [%s]", msg);
            break;
        case INPROGRESS_BEFORE_CLEANING:
            statusMessage = String.format("배포대상을 삭제합니다. [%s]", msg);
            break;
        case INPROGRESS_COPY:
            statusMessage = String.format("배포대상을 복사합니다. [%s]", msg);
            break;
        case INPROGRESS_RESULT_SAVE:
            statusMessage = String.format("배포결과를 저장합니다. [%s]", msg);
            break;
        case INPROGRESS_UPDATE_VERSION:
            statusMessage = String.format("배포버전을 업데이트 합니다. [%s]", msg);
            break;
        }
    }
}
