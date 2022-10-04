package com.inswave.appplatform.wedgemanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inswave.appplatform.wedgemanager.dao.TerminalDeviceErrorDao;
import com.inswave.appplatform.wedgemanager.dao.TerminalDeviceInfoDao;
import com.inswave.appplatform.wedgemanager.domain.device.Device;
import com.inswave.appplatform.wedgemanager.domain.device.DeviceKey;
import com.inswave.appplatform.wedgemanager.domain.device.DeviceRepository;
import com.inswave.appplatform.wedgemanager.domain.organization.Department;
import com.inswave.appplatform.wedgemanager.domain.organization.DepartmentRepository;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDeviceError;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDeviceInfo;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalUserVO;
import com.inswave.appplatform.wedgemanager.domain.user.User;
import com.inswave.appplatform.wedgemanager.domain.user.UserRepository;
import com.inswave.appplatform.wedgemanager.domain.wrapper.DeviceVO;
import com.inswave.appplatform.wedgemanager.hazelcast.messaging.data.SseSubscriberIdentity;
import com.inswave.appplatform.wedgemanager.service.ClusterSseEmitterService;
import com.inswave.appplatform.wedgemanager.service.ClusterWebSocketService;
import com.inswave.appplatform.wedgemanager.websocket.WHubIdentity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/manager/da")
public class DeviceAccessController {

    private ObjectMapper           mapper            = new ObjectMapper();
    private TerminalDeviceInfoDao  terminalDeviceInfoDao;
    private TerminalDeviceErrorDao terminalDeviceErrorDao;
    private DeviceRepository       deviceRepository;
    private UserRepository         userRepository;
    private DepartmentRepository   departmentRepository;
    private ResourceLoader         resourceLoader;
    private Map<String, String>    collectorParamMap = new HashMap<>();

    public DeviceAccessController(TerminalDeviceInfoDao terminalDeviceInfoDao,
                                  TerminalDeviceErrorDao terminalDeviceErrorDao,
                                  DeviceRepository deviceRepository,
                                  UserRepository userRepository,
                                  DepartmentRepository departmentRepository,
                                  ResourceLoader resourceLoader,
                                  @Value("${wedgemanager.collector.url:#{'http://localhost:8080/api/wem/manager'}}") String collectorUrl,
                                  @Value("${wedgemanager.deploy.url:#{'http://localhost:8080/deploy'}}") String deployUrl) {
        this.terminalDeviceInfoDao = terminalDeviceInfoDao;
        this.terminalDeviceErrorDao = terminalDeviceErrorDao;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.resourceLoader = resourceLoader;

        collectorParamMap.put("COLLECTOR_URL", collectorUrl);
        collectorParamMap.put("DEPLOY_URL", deployUrl);
    }

    @GetMapping(path = "/websockets")
    public ResponseEntity<Map<String, WHubIdentity>> websockets() {
        return ResponseEntity.ok(ClusterWebSocketService.getAllIdentities());
    }

    @GetMapping(path = "/sses")
    public ResponseEntity<Map<Integer, SseSubscriberIdentity>> sses() {
        return ResponseEntity.ok(ClusterSseEmitterService.getAllIdentities());
    }

    @GetMapping(path = "/connections")
    public ResponseEntity<String> connections() throws JsonProcessingException {
        String connections = mapper.writerWithDefaultPrettyPrinter()
                                   .writeValueAsString(new HashMap<String, Map>() {
                                       {
                                           put("websockets", ClusterWebSocketService.getAllIdentities());
                                           put("sseEmitters", ClusterSseEmitterService.getAllIdentities());
                                       }
                                   });
        return ResponseEntity.ok(String.format("<pre>%s</pre>", connections));
    }

    @GetMapping(path = "/devices")
    public ResponseEntity<List<DeviceVO>> getDevices() {
        Map<String, WHubIdentity> websocketIdentities = ClusterWebSocketService.getAllIdentities();
        List<Device> devices = deviceRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        List<User> users = userRepository.findAll();

        Map<String, String> departmentMap = departments.stream()
                                                       .collect(Collectors.toMap(Department::getDepartCode,
                                                                                 Department::getDepartName));
        Map<String, TerminalUserVO> userMap = users.stream()
                                                   .collect(Collectors.toMap(User::getUserId, TerminalUserVO::from));

        List<DeviceVO> deviceVOS = new ArrayList<>();
        devices.forEach(device -> {
            DeviceVO deviceVO = DeviceVO.from(device);
            TerminalUserVO userVO = userMap.getOrDefault(deviceVO.getUserId(), TerminalUserVO.builder()
                                                                                             .name("")
                                                                                             .build());

            deviceVO.setDepartName(departmentMap.getOrDefault(deviceVO.getDepartCode(), ""));
            deviceVO.setUserName(userVO.getName());
            deviceVO.setActiveTerminal(userVO.isLogin());
            WHubIdentity wHubIdentity = websocketIdentities.get(WHubIdentity.getIdentity(device));
            if (wHubIdentity != null) {
                deviceVO.setActiveAgent(true);
            }

            deviceVOS.add(deviceVO);
        });

        return ResponseEntity.ok(deviceVOS.stream()
                                          .sorted((o1, o2) -> o2.getDepartName().compareTo(o1.getDepartName()))
                                          .sorted((o1, o2) -> o2.getUserName().compareTo(o1.getUserName()))
                                          .collect(Collectors.toList()));
    }

    @PostMapping(path = "/log/{logType}")
    public ResponseEntity receiveLog(@PathVariable("logType") String logType, @RequestBody JsonNode data) {
        try {
            String bodyStr = ((ArrayNode) data).get(0).get("body").asText();
            ((ObjectNode) data.get(0)).set("body", mapper.readTree(bodyStr));

            JsonNode body = data.get(0).get("body").get(0);
            String ip = body.get("ip").asText();
            String appId = body.get("appId").asText();
            String deviceId = body.get("deviceId").asText();

            ClusterSseEmitterService.sendMessage(this.getClass().getSimpleName(),
                                                 String.format("%s|%s", appId, deviceId),
                                                 mapper.writeValueAsString(data));

            if (log.isDebugEnabled()) {
                log.debug("logType: {}, identity: {}|{}", logType, appId, deviceId);
                //                try {
                //                    log.debug("logType: {}, data: \n{}", logType, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
                //                } catch (JsonProcessingException e) {
                //                    e.printStackTrace();
                //                }
                //                log.debug(String.format("%s|%s", appId, deviceId));
                //                Files.write(Paths.get("/WebTop/monitoringLog")
                //                                 .resolve(String.format("%s_%s.json", ip, logType)), Collections.singleton(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/update/policy/{collectionType}")
    public ResponseEntity<String> updatePolicy(@PathVariable(required = false) String collectionType) {
        if (collectionType == null) {
            collectionType = "empty";
        }

        try {
            return ResponseEntity.ok(getCollectionPolicy(collectionType));
        } catch (IOException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping("/send/policy/{collectionType}")
    public ResponseEntity<String> updateInfoServer(@PathVariable(required = false) String collectionType) throws IOException {
        if (collectionType == null) {
            collectionType = "empty";
        }

        collectorParamMap.put("COLLECTION_TYPE", collectionType);
        String policy = getUpdateCollectionPolicyMessage(collectionType);
        ClusterWebSocketService.sendMessage(getUpdateCollectionPolicyMessage(collectionType));
        return ResponseEntity.ok(policy);
    }

    @RequestMapping("/send/policy/{collectionType}/{identity}")
    public ResponseEntity<String> updateDeviceNewInfoPolicy(@PathVariable String collectionType,
                                                            @PathVariable String identity) {
        collectorParamMap.put("COLLECTION_TYPE", collectionType);
        String policy = "";
        try {
            policy = getUpdateCollectionPolicyMessage(collectionType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClusterWebSocketService.sendMessage(identity, policy);
        return ResponseEntity.ok(policy);
    }

    @RequestMapping("/subscribe/{userId}/{deivceIdentity}")
    public SseEmitter subscribe(@PathVariable String userId,
                                @PathVariable String deivceIdentity) {

        SseSubscriberIdentity sseSubscriberIdentity = SseSubscriberIdentity.builder()
                                                                           .subscriberId(userId)
                                                                           .subscribeType(this.getClass().getSimpleName())
                                                                           .subscribeTarget(deivceIdentity)
                                                                           .build();

        SseSubscriberIdentity duplicatedSseSubscriberIdentity = ClusterSseEmitterService.getIdentity(sseSubscriberIdentity.hashCode());
        if (duplicatedSseSubscriberIdentity != null) {
            ClusterSseEmitterService.remove(sseSubscriberIdentity.hashCode());
        }

        //        SseEmitter emitter = new SseEmitter();
        SseEmitter emitter = new SseEmitter((long) 60 * 60 * 1000); // 60분
        //        SseEmitter emitter = new SseEmitter((long) 5 * 60 * 1000); // 5분

        log.debug("connected id : {}", sseSubscriberIdentity);
        emitter.onTimeout(() -> {
            ClusterSseEmitterService.remove(sseSubscriberIdentity);
            updateDeviceNewInfoPolicy("empty", deivceIdentity);
        });
        emitter.onCompletion(() -> {
            ClusterSseEmitterService.remove(sseSubscriberIdentity);
            updateDeviceNewInfoPolicy("empty", deivceIdentity);
        });

        ClusterSseEmitterService.put(sseSubscriberIdentity, emitter);
        updateDeviceNewInfoPolicy("realtime", deivceIdentity);

        return emitter;
    }

    @RequestMapping("/deviceerror/{deviceIdentity}")
    private ResponseEntity<List<TerminalDeviceError>> getDeviceError(@PathVariable String deviceIdentity) {
        DeviceKey deviceKey = DeviceKey.from(deviceIdentity);
        return ResponseEntity.ok(terminalDeviceErrorDao.findByAppIdAndDeviceIdOrderByCreateDateDesc(deviceKey.getAppId(), deviceKey.getDeviceId()));
    }

    @RequestMapping("/deviceinfo/{deviceIdentity}")
    private ResponseEntity<TerminalDeviceInfo> getDeviceInfo(@PathVariable String deviceIdentity) {
        return ResponseEntity.ok(terminalDeviceInfoDao.findById(DeviceKey.from(deviceIdentity))
                                                      .orElse(TerminalDeviceInfo.builder()
                                                                                .build()));
        //        String deviceInfoFileName = String.format("%s.json", Objects.hash(deviceIdentity));
        //        String deviceInfo = "";
        //        try {
        //            deviceInfo = new String(Files.readAllBytes(uploadPath.resolve(deviceInfoFileName)));
        //        } catch (IOException e) {
        //            log.debug("파일을 찾을 수 없음. : {}", e.getMessage());
        //            return "";
        //        }
        //        return deviceInfo;
    }

    private String getCollectionPolicy(String collectionPolicyType) throws IOException {
        String path = String.format("classpath:policies/collection_%s.json", collectionPolicyType);
        log.info(path);
        String policy = new String(Files.readAllBytes(resourceLoader.getResource(path).getFile().toPath()));

        return replaceBySubstitutor(policy, collectorParamMap);
    }

    private String getUpdateCollectionPolicyMessage(String collectionPolicyType) throws IOException {
        String path = "classpath:policies/newInfoCollection.json";
        String policy = new String(Files.readAllBytes(resourceLoader.getResource(path).getFile().toPath()));

        return replaceBySubstitutor(policy, collectorParamMap);
    }

    private String replaceBySubstitutor(String str, Map<String, String> values) {
        StringSubstitutor substitutor = new StringSubstitutor(values);
        String rtn = substitutor.replace(str);
        return rtn;
    }
}