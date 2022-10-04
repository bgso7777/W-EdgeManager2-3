package com.inswave.appplatform.wedgemanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.wedgemanager.dto.BaseDataFormatDTO;
import com.inswave.appplatform.wedgemanager.dto.DataHeader;
import com.inswave.appplatform.wedgemanager.dto.LogFileDTO;
import com.inswave.appplatform.wedgemanager.hazelcast.messaging.data.SseSubscriberIdentity;
import com.inswave.appplatform.wedgemanager.service.ClusterSharedJsonService;
import com.inswave.appplatform.wedgemanager.service.ClusterSseEmitterService;
import com.inswave.appplatform.wedgemanager.service.ClusterWebSocketService;
import com.inswave.appplatform.wedgemanager.websocket.WHubIdentity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/manager/dc")
public class DeviceCommandController {
    private ObjectMapper        mapper          = new ObjectMapper();
    private ResourceLoader      resourceLoader;
    private Map<String, String> commandParamMap = new HashMap<>();

    public DeviceCommandController(ResourceLoader resourceLoader,
                                   @Value("${wedgemanager.collector.url:#{'http://localhost:8080/api/wem/manager'}}") String managerUrl) {
        this.resourceLoader = resourceLoader;
        commandParamMap.put("MANAGER_URL", managerUrl);
    }

    // 관리자 접속
    @RequestMapping("/subscribe/{userId}/{deivceIdentity}")
    public SseEmitter subscribe(@PathVariable String userId,
                                @PathVariable String deivceIdentity) throws IOException {

        SseSubscriberIdentity sseSubscriberIdentity = SseSubscriberIdentity.builder()
                                                                           .subscriberId(userId)
                                                                           .subscribeType(this.getClass().getSimpleName())
                                                                           .subscribeTarget(deivceIdentity)
                                                                           .build();

        SseSubscriberIdentity duplicatedSseSubscriberIdentity = ClusterSseEmitterService.getIdentity(sseSubscriberIdentity.hashCode());
        if (duplicatedSseSubscriberIdentity != null) {
            ClusterSseEmitterService.remove(sseSubscriberIdentity.hashCode());
        }

        SseEmitter emitter = new SseEmitter((long) 60 * 60 * 1000); // 60분

        log.debug("connected id : {}", sseSubscriberIdentity);
        emitter.onTimeout(() -> {
            ClusterSseEmitterService.remove(sseSubscriberIdentity);
        });
        emitter.onCompletion(() -> {
            ClusterSseEmitterService.remove(sseSubscriberIdentity);
        });

        ClusterSseEmitterService.put(sseSubscriberIdentity, emitter);
        sendCommand(Constants.DIST_SERVICE_NAME_LOGFILE, deivceIdentity, null);

        return emitter;
    }

    // 단말의 command에 대한 파일회신
    @RequestMapping(path = "/summit/multipart/LogDownload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity summitResult(MultipartHttpServletRequest multipartHttpServletRequest) throws JsonProcessingException {

        Map<String, String[]> reqData = multipartHttpServletRequest.getParameterMap();
        String appId = reqData.get(Constants.TAG_HEADER_APPID)[0];
        String deviceId = reqData.get(Constants.TAG_HEADER_DEVICEID)[0];
        String deviceIdentity = WHubIdentity.getIdentity(appId, deviceId);

        try {
            Iterator<String> i = multipartHttpServletRequest.getFileNames();
            List<LogFileDTO> allList = new LinkedList<>();
            String name;

            while (i.hasNext()) {
                name = i.next();
                List<MultipartFile> list = multipartHttpServletRequest.getFiles(name);
                for (MultipartFile multipartFile : list) {
                    CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartFile;
                    FileItem fileItem = commonsMultipartFile.getFileItem();
                    String fileName = fileItem.getName();
                    long fileSize = fileItem.getSize();
                    LogFileDTO logFileDTO = new LogFileDTO();
                    logFileDTO.setFileName(fileName);
                    logFileDTO.setSize(String.valueOf(fileSize));
                    logFileDTO.setData(Base64.getEncoder().encodeToString(multipartFile.getBytes()));
                    allList.add(logFileDTO);
                }
            }
            BaseDataFormatDTO res = new BaseDataFormatDTO();
            res.setHeader(new DataHeader());
            res.getHeader().setDeviceId(deviceId);
            res.getHeader().setAppId(appId);
            res.getHeader().setService(Constants.DIST_SERVICE_NAME_LOGDOWNLOAD);
            res.setBody(new HashMap<>());
            res.getBody().put("fileResult", allList);

            ClusterSseEmitterService.sendMessage(this.getClass().getSimpleName(),
                                                 deviceIdentity,
                                                 mapper.writeValueAsString(res));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    // 단말의 command에 대한 회신
    @RequestMapping("/summit/{commandType}")
    public ResponseEntity summitResult(@PathVariable String commandType,
                                       @RequestBody BaseDataFormatDTO reqData) throws JsonProcessingException {
        //        log.debug("commandType: {}, msg: {}", commandType, reqData);

        String deviceIdentity = WHubIdentity.getIdentity(reqData.getHeader());
        ClusterSseEmitterService.sendMessage(this.getClass().getSimpleName(), deviceIdentity, mapper.writeValueAsString(reqData));
        return ResponseEntity.ok().build();
    }

    // 단말에 command 전송
    @RequestMapping("/send/{commandType}/{deviceIdentity}")
    public ResponseEntity<String> sendCommand(@PathVariable String commandType,
                                              @PathVariable String deviceIdentity,
                                              @RequestBody(required = false) BaseDataFormatDTO reqData) throws IOException {

        String message = "";

        switch (commandType) {
        case Constants.DIST_SERVICE_NAME_LOGDOWNLOAD:
            String commandId = String.format("%s-%d", sharedJsonKey(deviceIdentity), System.currentTimeMillis());
            commandParamMap.put("COMMAND_ID", commandId);
            message = getCommandMessage(commandType);

            List<String> fileList = (List<String>) reqData.getBody().get("fileList");
            log.debug("{}", fileList);
            ClusterSharedJsonService.put(commandId, mapper.writeValueAsString(fileList));
            ClusterWebSocketService.sendMessage(deviceIdentity, message);
            log.debug("{}", message);
            //            ClusterSharedJsonService.putAsync(sharedJsonKey(deviceIdentity), mapper.writeValueAsString(fileList))
            //                                    .whenComplete((hazelcastJsonValue, throwable) -> {
            //                                        ClusterWebSocketService.sendMessage(deviceIdentity, message);
            //                                    });
            break;
        default:
            message = getCommandMessage(commandType);
            ClusterWebSocketService.sendMessage(deviceIdentity, message);
            break;
        }

        return ResponseEntity.ok("");
    }

    // 단말의 command에 대한 policy 요청
    @RequestMapping("/policy/{commandType}")
    public ResponseEntity<String> getPolicyCommand(@PathVariable String commandType,
                                                   @RequestBody BaseDataFormatDTO reqData) throws IOException {
        String message = "";
        switch (commandType) {
        case Constants.DIST_SERVICE_NAME_LOGDOWNLOAD:
            String commandId = (String) reqData.getBody().get("commandId");

            String deviceIdentity = WHubIdentity.getIdentity(reqData.getHeader());
            String fileList = ClusterSharedJsonService.getString(commandId, true);

            commandParamMap.put("COMMAND_ID", commandId);
            commandParamMap.put("ARRAY_FILE_LIST", fileList);
            message = getPolicyCommandMessage(commandType);

            log.debug("{} / {}", reqData, message);
            break;
        default:
            message = getPolicyCommandMessage(commandType);
            break;
        }
        return ResponseEntity.ok(message);
    }

    //
    //    @RequestMapping("/send/log-upload/{deivceIdentity}")
    //    public ResponseEntity<String> sendFileUploadCommand(@PathVariable String deivceIdentity,
    //                                                        @RequestBody BaseDataFormatDTO reqData) throws IOException {
    //        commandParamMap.put("COMMAND_TYPE", Constants.DIST_SERVICE_NAME_LOGDOWNLOAD);
    //        LogSendBodyDTO body = mapper.convertValue(reqData.getBody(), LogSendBodyDTO.class);
    //
    //        String message = getLogUploadCommandMessage();
    //        ClusterWebSocketService.sendMessage(deivceIdentity, message);
    //
    //        return ResponseEntity.ok(message);
    //    }
    //
    //    private String getLogUploadCommandMessage() throws IOException {
    //        String path = "classpath:command/command_logUpload.json";
    //        String policy = new String(Files.readAllBytes(resourceLoader.getResource(path).getFile().toPath()));
    //
    //        return replaceBySubstitutor(policy, commandParamMap);
    //    }

    private String getCommandMessage(String commandType) throws IOException {
        commandParamMap.put("COMMAND_TYPE", commandType);
        String path = "classpath:command/command_sample.json";
        String message = replaceBySubstitutor(new String(Files.readAllBytes(resourceLoader.getResource(path)
                                                                                          .getFile()
                                                                                          .toPath())),
                                              commandParamMap);
        log.debug(message);
        return message;
    }

    private String getPolicyCommandMessage(String commandType) throws IOException {
        commandParamMap.put("COMMAND_TYPE", commandType);
        String path = "classpath:command/command_sample.json";

        switch (commandType) {
        case Constants.DIST_SERVICE_NAME_LOGFILE:
            path = "classpath:command/command_upload_log_file_list.json";
            break;
        case Constants.DIST_SERVICE_NAME_LOGDOWNLOAD:
            path = "classpath:command/command_upload_log_file.json";
            break;
        }

        String message = replaceBySubstitutor(new String(Files.readAllBytes(resourceLoader.getResource(path)
                                                                                          .getFile()
                                                                                          .toPath())),
                                              commandParamMap);
        log.debug(message);
        return message;
    }

    private String replaceBySubstitutor(String str, Map<String, String> values) {
        StringSubstitutor substitutor = new StringSubstitutor(values);
        String rtn = substitutor.replace(str);
        return rtn;
    }

    private String sharedJsonKey(String deviceIdentity) {
        return String.format("%s-%s", this.getClass().getSimpleName(), deviceIdentity);
    }
}
