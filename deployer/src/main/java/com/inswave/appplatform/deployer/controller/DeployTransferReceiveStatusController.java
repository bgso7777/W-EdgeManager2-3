package com.inswave.appplatform.deployer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.deployer.dao.DeployTransferReceiveStatusDao;
import com.inswave.appplatform.deployer.data.DeployTransferReceiveStatusVO;
import com.inswave.appplatform.deployer.domain.DeployTransferReceiveStatus;
import com.inswave.appplatform.util.DateUtil;
import com.inswave.appplatform.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/deployer/transfer-receive-status")
public class DeployTransferReceiveStatusController {

    private DeployTransferReceiveStatusDao deployTransferReceiveStatusDao;

    public DeployTransferReceiveStatusController(DeployTransferReceiveStatusDao deployTransferReceiveStatusDao) {
        this.deployTransferReceiveStatusDao = deployTransferReceiveStatusDao;
    }

    @PostMapping(path = "/select")
    public ResponseEntity<Map<String, Object>> select(@RequestBody JsonNode req) {
        Long deployTransferId = req.get("deployTransferId").asLong();
        Map<String, Object> result = new HashMap<>();

        String executedDate = "";
        String finishedDate = "";
        Integer transferCount = 0;

        Optional<DeployTransferReceiveStatus> optionalDeployTransferReceiveStatus = deployTransferReceiveStatusDao.findById(deployTransferId);
        List<DeployTransferReceiveStatusVO> deployTransferReceiveStatusVOS = new ArrayList<>();
        if (optionalDeployTransferReceiveStatus.isPresent()) {
            if (!StringUtils.isEmpty(optionalDeployTransferReceiveStatus.get().getReceiveStatus())) {
                deployTransferReceiveStatusVOS = JsonUtil.toList(optionalDeployTransferReceiveStatus.get().getReceiveStatus(), DeployTransferReceiveStatusVO.class);
            }
            deployTransferReceiveStatusVOS.forEach(DeployTransferReceiveStatusVO::updateReceiveStatus);
            executedDate = DateUtil.getDate(optionalDeployTransferReceiveStatus.get().getCreatedDate(), "yyyy-MM-dd hh:mm:ss");
            finishedDate = DateUtil.getDate(optionalDeployTransferReceiveStatus.get().getFinishedDate(), "yyyy-MM-dd hh:mm:ss");
            transferCount = optionalDeployTransferReceiveStatus.get().getTransferCount();
        }
//        result.put("data", deployTransferReceiveStatusVOS);

        result.put("deployTransferId", deployTransferId);
        result.put("executedDate", executedDate);
        result.put("finishedDate", finishedDate);
        result.put("transferCount", transferCount);
        result.put("reactiveClientCount", deployTransferReceiveStatusVOS.size());
        result.put("receiveClientRate", 0);
        OptionalDouble receiveCountAvg = deployTransferReceiveStatusVOS.stream()
                                                                       .map(vo -> vo.getReceiveCount())
                                                                       .collect(Collectors.toList())
                                                                       .stream()
                                                                       .mapToInt(Integer::intValue)
                                                                       .average();
        if (receiveCountAvg.isPresent()) {
            result.put("receiveClientRate", (int) ((receiveCountAvg.getAsDouble() / transferCount) * 100));
        }

        return ResponseEntity.ok(result);
    }
}
