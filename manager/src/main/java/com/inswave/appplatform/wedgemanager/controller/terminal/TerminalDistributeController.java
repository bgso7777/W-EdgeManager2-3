package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.inswave.appplatform.svn.SvnManager;
import com.inswave.appplatform.svn.domain.SvnItemVO;
import com.inswave.appplatform.util.StringUtil;
import com.inswave.appplatform.util.ZipUtil;
import com.inswave.appplatform.wedgemanager.dao.TerminalPreferenceDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDistResultDetail;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalDistResultDetailFileVO;
import com.inswave.appplatform.wedgemanager.service.scm.RepositoryService;
import com.inswave.appplatform.wedgemanager.service.scm.distribute.DistributeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.subversion.javahl.ClientException;
import org.apache.subversion.javahl.SubversionException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/wem/terminal/distribute")
public class TerminalDistributeController {

    private              RepositoryService       repositoryService;
    private              TerminalPreferenceDao   terminalPreferenceDao;
    private static final Map<String, SseEmitter> SSE_CLIENTS = new ConcurrentHashMap<>();

    public TerminalDistributeController(RepositoryService repositoryService,
                                        TerminalPreferenceDao terminalPreferenceDao) {
        this.repositoryService = repositoryService;
        this.terminalPreferenceDao = terminalPreferenceDao;
    }

    public String getSvnDir(String repositoryName) throws IOException {
        JsonNode accessMap = SvnManager.getInstance().getAccessMapJson(repositoryName);
        JsonNode distributeSrcPathNode = accessMap.at("/config/distributePathSrc");

        String distributeSrcPath = distributeSrcPathNode != null ? distributeSrcPathNode.asText() : null;

        return distributeSrcPath;
        //        return "/home";
    }

    public String getServerDir(String repositoryName) throws IOException {
        JsonNode accessMap = SvnManager.getInstance().getAccessMapJson(repositoryName);
        JsonNode distributePathNode = accessMap.at("/config/distributePath");

        String distributePath = distributePathNode != null ? distributePathNode.asText() : null;

        return distributePath;
    }

    @PostMapping("/svn-dirs")
    public ResponseEntity<List<Map<String, String>>> getSvnDirs(@RequestBody JsonNode reqBody,
                                                                HttpServletRequest request,
                                                                HttpServletResponse response) throws IOException, ClientException {
        String repositoryName = reqBody.get("repositoryName").asText();
        SvnManager svnManager = SvnManager.getInstance();
        List<SvnItemVO> svnItemVOS = svnManager.getRepositoryListItemCache(repositoryName);

        List<Map<String, String>> result = svnItemVOS.stream()
                                                     .filter(svnItemVO -> svnItemVO.getNodeKind().equals("dir") && !StringUtils.isEmpty(svnItemVO.getPath()))
                                                     .map(svnItemVO -> new HashMap<String, String>() {
                                                         {
                                                             put("path", svnItemVO.getPath());
                                                         }
                                                     }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/get-paths")
    public ResponseEntity<Map<String, Object>> getPaths(@RequestBody JsonNode reqBody,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException, ClientException {

        String repositoryName = reqBody.get("repositoryName").asText();
        Path svnSrcPath = Paths.get(StringUtil.rebuildPath("/", getSvnDir(repositoryName)));
        List<SvnItemVO> svnItemVOs = repositoryService.getSolutionListItem(repositoryName, false, true);
        String svnSrcPathStr = StringUtil.rebuildPath("/", svnSrcPath.toString());

        List<Map<String, Object>> svnList = svnItemVOs.stream()
                                                      .filter(svnItemVO -> {
                                                          String svnItemVOPathStr = StringUtil.rebuildPath("/", svnItemVO.getPath());
                                                          return svnItemVOPathStr.startsWith(svnSrcPathStr) && !svnSrcPathStr.equals(svnItemVOPathStr); // 디렉토리만 && 루트제거
                                                      })
                                                      .map(svnItemVO -> {
                                                          Path svnPath = Paths.get(StringUtil.rebuildPath("/", svnItemVO.getPath()));

                                                          Map<String, Object> fileInfo = new HashMap<>();
                                                          //                                                          Path e = svnSrcPath.relativize(svnPath);
                                                          Path e = Paths.get(svnPath.toString().substring(svnSrcPath.toString().length()));
                                                          fileInfo.put("path", StringUtil.rebuildPath("/", e.toString()));
                                                          fileInfo.put("name", e.getFileName().toString());
                                                          fileInfo.put("depth", e.getNameCount());
                                                          fileInfo.put("size", svnItemVO.getSize());
                                                          fileInfo.put("lastModified", svnItemVO.getLastChangedDate().getTime());

                                                          return fileInfo;
                                                      }).collect(Collectors.toList());

        Path targetPath = Paths.get(getServerDir(repositoryName));
        List<Map<String, Object>> destList = Files.walk(targetPath, Integer.MAX_VALUE)
                                                  .filter(path -> Files.isDirectory(path) && targetPath.compareTo(path) != 0) // 디렉토리만 && 루트제거
                                                  .map(path -> {
                                                      Path e = targetPath.relativize(path);
                                                      Map<String, Object> fileInfo = new HashMap<>();
                                                      fileInfo.put("path", StringUtil.rebuildPath("/", e.toString()));
                                                      fileInfo.put("name", e.getFileName().toString());
                                                      fileInfo.put("depth", e.getNameCount());
                                                      try {
                                                          fileInfo.put("size", Files.size(path));
                                                          fileInfo.put("lastModified", Files.getLastModifiedTime(path).toMillis());
                                                      } catch (IOException ex) {
                                                          ex.printStackTrace();
                                                      }
                                                      return fileInfo;
                                                  })
                                                  .collect(Collectors.toList());
        //        Map<String, List<Map<String, Object>>> ret = new HashMap<>();
        Map<String, Object> ret = new HashMap<>();
        ret.put("srcPath", svnSrcPath.toString());
        ret.put("destPath", targetPath.toString());
        ret.put("src", svnList);
        ret.put("dest", destList);

        return ResponseEntity.ok(ret);
    }

    @PostMapping("/dest-list")
    public ResponseEntity<List<Map<String, Object>>> destList(@RequestBody JsonNode reqBody,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) throws IOException {

        String repositoryName = reqBody.get("repositoryName").asText();
        boolean recursive = reqBody.hasNonNull("recursive") ? reqBody.get("recursive").asBoolean(false) : false;
        boolean onlyDir = reqBody.hasNonNull("onlyDir") ? reqBody.get("onlyDir").asBoolean(false) : false;

        Path targetPath = Paths.get(getServerDir(repositoryName));

        List<Map<String, Object>> list = Files.walk(targetPath, recursive ? Integer.MAX_VALUE : 1)
                                              .filter(path -> onlyDir ? Files.isDirectory(path) : true)
                                              .map(path -> {
                                                  Path e = targetPath.relativize(path);
                                                  Map<String, Object> fileInfo = new HashMap<>();
                                                  fileInfo.put("path", e.toString());
                                                  fileInfo.put("name", e.getFileName().toString());
                                                  fileInfo.put("isdir", Files.isDirectory(path));
                                                  fileInfo.put("depth", e.getNameCount());
                                                  try {
                                                      fileInfo.put("size", Files.size(path));
                                                      fileInfo.put("lastModified", Files.getLastModifiedTime(path).toMillis());
                                                  } catch (IOException ex) {
                                                      ex.printStackTrace();
                                                  }
                                                  return fileInfo;
                                              })
                                              .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @PostMapping("/mkdir")
    public ResponseEntity mkdir(@RequestBody JsonNode reqBody,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        String repositoryName = reqBody.get("repositoryName").asText();
        String target = reqBody.get("target").asText();
        Path targetPath = Paths.get(getServerDir(repositoryName), target);

        if (Files.notExists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity remove(@RequestBody JsonNode reqBody,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        String repositoryName = reqBody.get("repositoryName").asText();
        String target = reqBody.get("target").asText();
        Path targetPath = Paths.get(getServerDir(repositoryName), target);

        if (Files.isDirectory(targetPath)) {
            FileUtils.deleteDirectory(targetPath.toFile());
        } else {
            Files.deleteIfExists(targetPath);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/upload-to-path", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity uploadToPath(@RequestParam(value = "fileData") MultipartFile fileData,
                                       @RequestParam(value = "fileName") String fileName,
                                       @RequestParam(value = "destDir") String destDir,
                                       @RequestParam(value = "decompress", required = false) Optional<Boolean> optionalDecompress,
                                       @RequestParam(value = "deleteAfterDecompress", required = false) Optional<Boolean> optionalDeleteAfterDecompress,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        Path destPath = Paths.get(destDir);
        Path destPathFile = destPath.resolve(fileName);
        fileData.transferTo(destPathFile);

        if (optionalDecompress.isPresent()) {
            if (optionalDecompress.get()) {
                ZipUtil.unzip(destPathFile, destPath);
            }
            if (optionalDeleteAfterDecompress.isPresent()) {
                if (optionalDeleteAfterDecompress.get()) {
                    Files.deleteIfExists(destPathFile);
                }
            }
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/save-config")
    public ResponseEntity saveConfig(@RequestBody JsonNode reqBody,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscribe/{repositoryName}/{userId}")
    public SseEmitter subscribe(@PathVariable("repositoryName") String repositoryName,
                                @PathVariable("userId") String userId) {
        String key = String.format("%s-%s", repositoryName, userId);
        SseEmitter duplicatedEmitter = SSE_CLIENTS.get(key);
        if (duplicatedEmitter != null) {
            try {
                duplicatedEmitter.send(new HashMap<String, String>() {
                    {
                        put("type", "error");
                        put("msg", "duplicated user has been connected.");
                    }
                });
                return duplicatedEmitter;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //            duplicatedEmitter.complete();
        }

        SseEmitter emitter = new SseEmitter((long) 60 * 60 * 1000); // 60분
        //        SseEmitter emitter = new SseEmitter((long) 30000); // 30초
        //        SseEmitter emitter = new SseEmitter((long) 15000); // 15초
        SSE_CLIENTS.put(key, emitter);

        log.info("connected id : {}", key);
        emitter.onTimeout(() -> SSE_CLIENTS.remove(key));
        emitter.onCompletion(() -> SSE_CLIENTS.remove(key));
        try {
            emitter.send("connected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emitter;
    }

    @PostMapping("/publish/{repositoryName}")
    public void publish(@PathVariable("repositoryName") String repositoryName,
                        @RequestBody String message) {

        //    public void publish(String repositoryName, String message) {
        Set<String> deadIds = new HashSet<>();
        String key = String.format("%s-", repositoryName);

        SSE_CLIENTS.forEach((id, emitter) -> {
            try {
                if (id.startsWith(key)) {
                    emitter.send(message);
                    //                    emitter.send(message, MediaType.APPLICATION_JSON);
                }
            } catch (IOException e) {
                deadIds.add(id);
                emitter.complete();
                log.info("disconnected id : {}", id);
            }
        });

        deadIds.forEach(SSE_CLIENTS::remove);
    }

    @PostMapping("/execute")
    public void execute(@RequestBody JsonNode reqBody) {
        String repositoryName = reqBody.get("repositoryName").asText();
        boolean includeSubdir = reqBody.get("includeSubdir").asBoolean();
        ArrayNode targetPaths = (ArrayNode) reqBody.get("targetPaths");

        targetPaths.forEach(jsonNode -> {
            String targetPath = ((TextNode) jsonNode).asText();
            try {
                execute(repositoryName, targetPath, includeSubdir, "normal");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SubversionException e) {
                e.printStackTrace();
            }
        });
    }

    public void execute(String repositoryName, String targetPath, boolean includeSubdir, String taskType) throws IOException, SubversionException {
        SvnManager svnManager = SvnManager.getInstance();
        JsonNode accessMap = svnManager.getAccessMapJson(repositoryName);
        JsonNode solutionPathNode = accessMap.at("/config/solutionPath");
        JsonNode distributePathNode = accessMap.at("/config/distributePath");
        JsonNode distributePathSrcNode = accessMap.at("/config/distributePathSrc");

        String solutionPath = solutionPathNode != null ? solutionPathNode.asText() : null;
        String distributePath = distributePathNode != null ? distributePathNode.asText() : null;
        String distributePathSrc = distributePathSrcNode != null ? distributePathSrcNode.asText() : null;

        if (Files.exists(Paths.get(distributePath))) {
            if (solutionPath != null && distributePath != null) {
                long lastRevision = svnManager.updateWorkingCopy(repositoryName);

                Path srcPath = Paths.get(FilenameUtils.separatorsToUnix(svnManager.getSvnWorkingCopyPath(repositoryName).toString()));
                srcPath = srcPath.resolve(StringUtil.rebuildPath("/", distributePathSrc));
                Path destPath = Paths.get(FilenameUtils.separatorsToUnix(distributePath));

                TerminalDistResultDetail terminalDistResultDetail = TerminalDistResultDetail.builder()
                                                                                            .distType(taskType)
                                                                                            .distName(taskType)
                                                                                            .includeSubdir(includeSubdir)
                                                                                            .build();
                terminalDistResultDetail.setRepositoryName(repositoryName);
                terminalDistResultDetail.setDistVersion(ZonedDateTime.now());
                terminalDistResultDetail.setTaskType(taskType);
                terminalDistResultDetail.setExecuteStartDate(ZonedDateTime.now());

                Path distPath = srcPath.resolve(StringUtil.rebuildPath("/", targetPath));
                Path distDestPath = destPath.resolve(StringUtil.rebuildPath("/", targetPath));

                terminalDistResultDetail.setPathSource(distPath.toString());
                terminalDistResultDetail.setPathDestination(distDestPath.toString());

                List<TerminalDistResultDetailFileVO> resultDetails = new ArrayList<>();
                Files.walk(distPath, includeSubdir ? Integer.MAX_VALUE : 1)
                     .forEach(src -> {
                         TerminalDistResultDetailFileVO distResultDetail = TerminalDistResultDetailFileVO.builder().build();
                         Path dest = distDestPath.resolve(distPath.relativize(src));

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

                         distResultDetail.setAction("A");
                         distResultDetail.setPathSource(src.toString());
                         distResultDetail.setPathDestination(dest.toString());
                         distResultDetail.setPathDestinationFileSize(0L);
                         if (Files.exists(dest)) {
                             try {
                                 distResultDetail.setPathDestinationFileSize(Files.size(dest));

                                 //                                 Files.setPosixFilePermissions(dest, PosixFilePermissions.fromString("rwxrwxrwx"));
                                 dest.toFile().setReadable(true, false);          //r
                                 dest.toFile().setWritable(true, false);          //w
                                 dest.toFile().setExecutable(true, false);      //x
                             } catch (IOException e) {
                                 //                                 e.printStackTrace();
                             }
                         }

                         resultDetails.add(distResultDetail);
                         String message = String.format("[%d-%s] %s, %s byte%s",
                                                        lastRevision,
                                                        distResultDetail.getActionName(),
                                                        distResultDetail.getPathDestination(),
                                                        distResultDetail.getPathDestinationFileSize(),
                                                        distResultDetail.getDesc() != null ? ", : " + distResultDetail.getDesc() : ""
                         );
                         publish(repositoryName, message);
                     });

                terminalDistResultDetail.setDistResultTotalFileCount(resultDetails.size());
                terminalDistResultDetail.setDistResultTotalFileSize(resultDetails.stream()
                                                                                 .map(TerminalDistResultDetailFileVO::getPathDestinationFileSize)
                                                                                 .collect(Collectors.reducing(Long::sum))
                                                                                 .orElse(0L));
                terminalDistResultDetail.setDistResultDetailOrigin(resultDetails);
                terminalDistResultDetail.setExecuteEndDate(ZonedDateTime.now());
                terminalDistResultDetail.setExecuteSuccessYn(true);

                DistributeService.saveTerminalDistDetailResult(terminalDistResultDetail);
            }
        } else {
            publish(repositoryName, " 배포대상 경로를 확인하십시오. [" + distributePath + "]");
        }
    }
}
