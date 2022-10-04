package com.inswave.appplatform.wedgemanager.domain.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inswave.appplatform.log.entity.*;
import com.inswave.appplatform.wedgemanager.hazelcast.HazelcastClusterManager;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Builder
public class ServerResourceVO {
    @Builder.Default
    @Getter @Setter private String clusterUuid = HazelcastClusterManager.getLocalUuid();
    @Getter @Setter private String hostName;
    @Getter @Setter private String osType;
    @Getter @Setter private double cpuUsage;
    @Getter @Setter private long   memoryAvailable;
    @Getter @Setter private long   memoryTotal;
    @Getter @Setter private long   memoryUsed;
    @Getter @Setter private double memoryUsage;
    @Getter @Setter private double diskTotal;
    @Getter @Setter private long   diskUsed;
    @Getter @Setter private double diskUsage;
    @Getter @Setter private double networkSent;
    @Getter @Setter private double networkReceived;

    @Builder.Default
    @Setter private ZonedDateTime timeCreated = ZonedDateTime.now();

    public String getTimeCreated() {
        return timeCreated != null ? timeCreated.toLocalDateTime().toString() : "";
    }

    @JsonIgnore
    public ZonedDateTime getTimeCreatedOrigin() {
        return timeCreated;
    }

    public static ServerResourceVO from(SystemInfo systemInfo) {
        double cpuUsage = systemInfo.getCpu().stream().mapToDouble(CPUInfo::getUsage).sum();
        cpuUsage = cpuUsage == 0 ? 0 : (cpuUsage / systemInfo.getCpu().size());
        MemoryInfo memoryInfo = systemInfo.getMem().get(0);
        DiskInfo diskInfo = systemInfo.getDisk().get(0);
        List<NetworkInfo> networkInfosBefore = systemInfo.getNetwork();
        List<NetworkInfo> networkInfosAfter = systemInfo.getNetworkAfter();

        double networkSent = 0;
        double networkReceived = 0;

        for (int i = 0; i < networkInfosBefore.size(); i++) {
            NetworkInfo net = networkInfosBefore.get(i);
            NetworkInfo netAfter = networkInfosAfter.get(i);

            networkSent += (netAfter.getSent() - net.getSent());
            networkReceived += (netAfter.getReceived() - net.getReceived());
//            log.debug("Sent:{}, Received:{}, Speed:{}, up:{}, loopback:{}, virtual:{}, p2p:{}, Ipv4:{}, Ipv6:{}, Macaddr:{}, Name:{}",
//                      (netAfter.getSent() - net.getSent()),
//                      (netAfter.getReceived() - net.getReceived()),
//                      net.getSpeed(),
//                      net.isUp(),
//                      net.isLoopback(),
//                      net.isVirtual(),
//                      net.isPointToPoint(),
//                      net.getIpv4addr(),
//                      net.getIpv6addr(),
//                      net.getMacaddr(),
//                      net.getInterfaceName()
//            );
        }

        return ServerResourceVO.builder()
                               .hostName(systemInfo.getHostName())
                               .osType(systemInfo.getOsInfo().getDistro() + " " + systemInfo.getOsInfo().getRelease())
                               .cpuUsage(cpuUsage)
                               .memoryAvailable(memoryInfo.getFree())
                               .memoryTotal(memoryInfo.getTotal())
                               .memoryUsed(memoryInfo.getUsed())
                               .memoryUsage(memoryInfo.getUsage())
                               .diskTotal(diskInfo.getTotal())
                               .diskUsed(diskInfo.getUsed())
                               .diskUsage(diskInfo.getUse())
                               .networkSent(networkSent)
                               .networkReceived(networkReceived)
                               .build();
    }
}

