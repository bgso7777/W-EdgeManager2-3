package com.inswave.appplatform.wedgemanager.service.wgear;

import com.inswave.appplatform.util.DateUtil;
import com.inswave.appplatform.wedgemanager.dao.TerminalDeviceErrorDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDeviceError;
import com.inswave.appplatform.wedgemanager.dto.BaseDataFormatDTO;
import com.inswave.appplatform.wedgemanager.websocket.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class infoDeviceError implements WebsocketService {
    private TerminalDeviceErrorDao terminalDeviceErrorDao;

    public infoDeviceError(TerminalDeviceErrorDao terminalDeviceErrorDao) {
        this.terminalDeviceErrorDao = terminalDeviceErrorDao;
    }

    @Override
    public void excute(WebSocketSession session, BaseDataFormatDTO reqData) {
        terminalDeviceErrorDao.save(TerminalDeviceError.builder()
                                                       .appId(reqData.getHeader().getAppId())
                                                       .deviceId(reqData.getHeader().getDeviceId())
                                                       .errorType((String) reqData.getBody().getOrDefault("errorType", "unknown"))
                                                       .errorDesc((String) reqData.getBody().getOrDefault("errorDesc", "unknown"))
                                                       .errorTime(DateUtil.getDate((String) reqData.getBody().getOrDefault("errorTime", "")))
                                                       .build());
    }

}
