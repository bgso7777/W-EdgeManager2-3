package com.inswave.appplatform.wedgemanager.domain.wrapper;

import com.inswave.appplatform.wedgemanager.domain.device.Device;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceVO {
    private String appId;
    private String deviceId;
    private String osType;
    private String userId;
    private String ip;
    private String departCode;

    private String  departName;
    private String  userName;
    private boolean activeAgent;
    private boolean activeTerminal;

    //    private String         state;
    //    private Date           lastDate;
    //    private WebsocketState sessionState;
    //    private String         updateVersion;
    //    private String         updateState;
    //    private String         updateScope;
    //    private Date           updateUpdateDate;
    //    private String         distVersion;
    //    private String         distState;
    //    private String         distScope;
    //    private Date           distUpdateDate;
    //    private String         stateMsg;
    //    private Date           createDate;
    //    private Date           updateDate;
    
    public static DeviceVO from(Device device) {
        return DeviceVO.builder()
                       .appId(device.getAppId())
                       .deviceId(device.getDeviceId())
                       .osType(device.getOsType())
                       .userId(device.getUserId())
                       .ip(device.getIp())
                       .departCode(device.getDepartCode())
                       .build();
    }
}

