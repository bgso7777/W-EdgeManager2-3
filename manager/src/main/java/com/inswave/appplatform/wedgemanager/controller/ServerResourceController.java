package com.inswave.appplatform.wedgemanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.log.entity.SystemInfo;
import com.inswave.appplatform.util.SystemUtil;
import com.inswave.appplatform.wedgemanager.domain.wrapper.ServerResourceVO;
import com.inswave.appplatform.wedgemanager.hazelcast.messaging.data.SseSubscriberIdentity;
import com.inswave.appplatform.wedgemanager.service.ClusterSseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/manager/sr")
public class ServerResourceController {

    private ObjectMapper mapper = new ObjectMapper();

    public ServerResourceController() {
    }

    @Scheduled(fixedDelay = 2000)   // 딜레이2 + 슬립1 = 3초
    public void publishServerResource() throws JsonProcessingException {
        Map<Integer, SseSubscriberIdentity> sseSubscriberIdentityMap = ClusterSseEmitterService.getAllIdentities(this.getClass().getSimpleName());

        if (sseSubscriberIdentityMap.size() > 0) {
            SystemInfo systemInfo = new SystemInfo();
            SystemUtil.initHostName(systemInfo);
            SystemUtil.initOSInfo(systemInfo);
            SystemUtil.initCpuInfo(systemInfo);
            SystemUtil.initMemoryInfo(systemInfo);
            SystemUtil.initDiskInfo(systemInfo);
            SystemUtil.initNetworkInfo3(systemInfo.getHardware().getNetworkIFs(), systemInfo, 1000);
            systemInfo.setRunTime(System.currentTimeMillis());

            String serverResource = mapper.writeValueAsString(ServerResourceVO.from(systemInfo));
            sseSubscriberIdentityMap.forEach((hashCode, sseSubscriberIdentity) -> {
                ClusterSseEmitterService.sendMessage(hashCode, serverResource);
            });
        }
    }

    @RequestMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable String userId) {

        SseSubscriberIdentity sseSubscriberIdentity = SseSubscriberIdentity.builder()
                                                                           .subscriberId(userId)
                                                                           .subscribeType(this.getClass().getSimpleName())
                                                                           .subscribeTarget(this.getClass().getSimpleName())
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

        return emitter;
    }

}