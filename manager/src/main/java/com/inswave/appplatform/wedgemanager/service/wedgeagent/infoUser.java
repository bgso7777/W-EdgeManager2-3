package com.inswave.appplatform.wedgemanager.service.wedgeagent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.StartManagerSpringApplication;
import com.inswave.appplatform.wedgemanager.dao.TerminalDeviceInfoDao;
import com.inswave.appplatform.wedgemanager.domain.device.Device;
import com.inswave.appplatform.wedgemanager.domain.device.DeviceService;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDeviceInfo;
import com.inswave.appplatform.wedgemanager.dto.BaseDataFormatDTO;
import com.inswave.appplatform.wedgemanager.dto.ClientDistInfoDTO;
import com.inswave.appplatform.wedgemanager.enums.WebsocketState;
import com.inswave.appplatform.wedgemanager.websocket.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class infoUser implements WebsocketService {
    private TerminalDeviceInfoDao terminalDeviceInfoDao;
    private DeviceService         deviceService;
    private int                   index;

    public infoUser(TerminalDeviceInfoDao terminalDeviceInfoDao,
                    DeviceService deviceService) {
        this.terminalDeviceInfoDao = terminalDeviceInfoDao;
        this.deviceService = deviceService;
    }

    @Override
    public void excute(WebSocketSession session, BaseDataFormatDTO reqData) {

        Device device = setDevice(session, reqData);

        //header
        BaseDataFormatDTO resData = new BaseDataFormatDTO();
        resData.setHeader(reqData.getHeader());
        reqData.getHeader().setService("infoServer");
        resData.setBody(new HashMap<>());

        index = StartManagerSpringApplication.mDomainCount % StartManagerSpringApplication.mDomainName.length;

        //policy
        Map<String, Object> policy = new HashMap<>();
        policy.put("url", StartManagerSpringApplication.mProtocol + "://" + StartManagerSpringApplication.mDomainName[index] + ":" + StartManagerSpringApplication.mPort + "/api/wem/manager/da/update/policy/empty");
        resData.getBody().put("policy", policy);

        //deploy
        Map<String, Object> deploy = new HashMap<>();
        deploy.put("url", StartManagerSpringApplication.mProtocol + "://" + StartManagerSpringApplication.mDomainName[index] + ":" + StartManagerSpringApplication.mPort + "/deploy/info");
        deploy.put("stateUrl", StartManagerSpringApplication.mProtocol + "://" + StartManagerSpringApplication.mDomainName[index] + ":" + StartManagerSpringApplication.mPort + "/deploy/state/set");
        resData.getBody().put("deploy", deploy);

        StartManagerSpringApplication.mDomainCount++;
        log.debug("{}", reqData);

        try {
            String deviceUserInfo = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(reqData.getBody());
            Optional<TerminalDeviceInfo> optionalTerminalDeviceInfo = terminalDeviceInfoDao.findById(DeviceKey.from(device));
            TerminalDeviceInfo terminalDeviceInfo = null;
            if (optionalTerminalDeviceInfo.isPresent()) {
                terminalDeviceInfo = optionalTerminalDeviceInfo.get();
                terminalDeviceInfo.setDeviceInfoUser(deviceUserInfo);
                terminalDeviceInfo.setUpdateDate(ZonedDateTime.now());
            } else {
                terminalDeviceInfo = TerminalDeviceInfo.builder()
                                                       .appId(device.getAppId())
                                                       .deviceId(device.getDeviceId())
                                                       .deviceInfoUser(deviceUserInfo)
                                                       .build();
            }
            terminalDeviceInfoDao.save(terminalDeviceInfo);

            session.sendMessage(new TextMessage(mapper.writeValueAsString(resData)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Device setDevice(WebSocketSession session, BaseDataFormatDTO reqData) {
        String departCode = reqData.getHeader().getBrNo();

        Device connectedDevice = new Device();
        connectedDevice.setAppId(reqData.getHeader().getAppId());
        connectedDevice.setDeviceId(reqData.getHeader().getDeviceId());
        connectedDevice.setOsType(reqData.getHeader().getOsType());
        connectedDevice.setIp((String) reqData.getBody().get("ip"));
        connectedDevice.setDepartCode(StringUtils.isNotEmpty(departCode) ? departCode : "000000");

        ClientDistInfoDTO distInfo = mapper.convertValue(reqData.getBody().get("distInfo"), ClientDistInfoDTO.class);
        if (distInfo != null) {
            connectedDevice.setDistVersion(distInfo.getVersion());
            connectedDevice.setDistState(distInfo.getState());
        }

        Device registeredDevice = deviceService.findOne(DeviceKey.from(connectedDevice));
        if (registeredDevice != null) {
            registeredDevice.setIp(connectedDevice.getIp());
            registeredDevice.setOsType(connectedDevice.getOsType());
            registeredDevice.setUpdateVersion(connectedDevice.getUpdateVersion());
            registeredDevice.setDepartCode(connectedDevice.getDepartCode());
        } else {
            registeredDevice = connectedDevice;
        }

        if (connectedDevice.getDistVersion() != null) {
            registeredDevice.setDistVersion(connectedDevice.getDistVersion());
        }

        if (connectedDevice.getDistState() != null) {
            registeredDevice.setState(connectedDevice.getDistState());
        }

        registeredDevice.setSessionState(WebsocketState.CL_WEBSOCKET_STATE_ACTIVE);
        deviceService.save(registeredDevice);
        WHubWebsocketHandler.addIdentityToSession(session, WHubIdentity.from(registeredDevice));

        return registeredDevice;
    }
}

