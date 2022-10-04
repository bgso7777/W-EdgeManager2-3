package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.wedgemanager.dao.TerminalPersonalDataDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalPersonalData;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalPersonalDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/terminal/personal-data")
public class TerminalPersonalDataController {

    private TerminalPersonalDataDao terminalPersonalDataDao;

    public TerminalPersonalDataController(TerminalPersonalDataDao terminalPersonalDataDao) {
        this.terminalPersonalDataDao = terminalPersonalDataDao;
    }

    @PostMapping(path = "/select")
    public ResponseEntity<TerminalPersonalDataVO> selectOne(@RequestBody JsonNode req) {
        String userId = req.get("userId").asText();
        String dataType = req.get("dataType").asText();
        Long dataId = req.get("dataId").asLong();

        Optional<TerminalPersonalData> terminalPersonalData = terminalPersonalDataDao.findByUserIdAndDataTypeAndDataId(userId, dataType, dataId);

        return ResponseEntity.ok(TerminalPersonalDataVO.from(terminalPersonalData.orElse(null)));
    }

    @PostMapping(path = "/select-all")
    public ResponseEntity<List<TerminalPersonalDataVO>> selectAll(@RequestBody JsonNode req) {
        String userId = req.get("userId").asText();
        String dataType = req.get("dataType").asText();

        List<TerminalPersonalData> terminalPersonalDataList = terminalPersonalDataDao.findByUserIdAndDataType(userId, dataType);
        return ResponseEntity.ok(terminalPersonalDataList.stream().map(TerminalPersonalDataVO::from).collect(Collectors.toList()));
    }

    @PostMapping(path = "/insert")
    public ResponseEntity insert(@RequestBody JsonNode req) {
        String userId = req.get("userId").asText();
        String dataType = req.get("dataType").asText();
        String dataJson = req.get("dataJson").toString();
        return ResponseEntity.ok(terminalPersonalDataDao.save(TerminalPersonalData.builder()
                                                                                  .userId(userId)
                                                                                  .dataType(dataType)
                                                                                  .dataJson(dataJson)
                                                                                  .createDate(ZonedDateTime.now())
                                                                                  .build()));
    }

    @PostMapping(path = "/update")
    public ResponseEntity update(@RequestBody JsonNode req) {
        String userId = req.get("userId").asText();
        String dataType = req.get("dataType").asText();
        String dataJson = req.get("dataJson").toString();
        Long dataId = req.get("dataId").asLong();
        Optional<TerminalPersonalData> terminalPersonalData = terminalPersonalDataDao.findByUserIdAndDataTypeAndDataId(userId, dataType, dataId);
        if (terminalPersonalData.isPresent()) {
            terminalPersonalData.get().setDataJson(dataJson);
            terminalPersonalData.get().setUpdateDate(ZonedDateTime.now());
            terminalPersonalDataDao.save(terminalPersonalData.get());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@RequestBody JsonNode req) {
        Long dataId = req.get("dataId").asLong();
        terminalPersonalDataDao.deleteById(dataId);
        return ResponseEntity.ok().build();
    }
}
