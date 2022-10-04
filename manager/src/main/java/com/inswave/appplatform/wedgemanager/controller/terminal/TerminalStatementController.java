package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.wedgemanager.dao.TerminalStatementDao;
import com.inswave.appplatform.wedgemanager.dao.TerminalStatementDetailDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalStatement;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalStatementDetail;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalStatementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/terminal/statement")
public class TerminalStatementController {

    private TerminalStatementDao       terminalStatementDao;
    private TerminalStatementDetailDao terminalStatementDetailDao;

    public TerminalStatementController(TerminalStatementDao terminalStatementDao,
                                       TerminalStatementDetailDao terminalStatementDetailDao) {
        this.terminalStatementDao = terminalStatementDao;
        this.terminalStatementDetailDao = terminalStatementDetailDao;
    }

    @GetMapping("/user-data1-all")
    public ResponseEntity<List<TerminalStatementVO>> getUserData1All() {
        return ResponseEntity.ok(terminalStatementDao.findAll()
                                                     .stream()
                                                     .map(TerminalStatementVO::from)
                                                     .collect(Collectors.toList()));
    }

    @GetMapping("/user-data1/{convertedId}")
    public ResponseEntity<List<TerminalStatementVO>> getUserData1(@PathVariable("convertedId") Optional<String> convertedId) {
        if (convertedId.isPresent()) {
            Optional<TerminalStatement> optionalTerminalStatement = terminalStatementDao.findByConvertedId(convertedId.get());
            List<TerminalStatement> terminalStatements = new ArrayList<>();
            optionalTerminalStatement.ifPresent(terminalStatement -> {
                terminalStatements.add(terminalStatement);
            });
            return ResponseEntity.ok(terminalStatements.stream()
                                                       .map(TerminalStatementVO::from)
                                                       .collect(Collectors.toList()));
        } else {
            return ResponseEntity.ok(terminalStatementDao.findAll()
                                                         .stream()
                                                         .map(TerminalStatementVO::from)
                                                         .collect(Collectors.toList()));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> defaultPost(@RequestParam(value = "service") Optional<String> service,
                                                           @RequestParam(value = "category") Optional<String> category,
                                                           @RequestParam(value = "target") Optional<String> target,
                                                           @RequestParam(value = "nameFilter") Optional<String> nameFilter) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (!service.isPresent()) {
                throw new Exception("Parameter[service]가 필요합니다.");
            }
            if (!category.isPresent()) {
                throw new Exception("Parameter[category]가 필요합니다.");
            }

            if ("retrieve_service_list".equals(service.get())) {
                Map<String, Object> data = new HashMap<>();
                List<TerminalStatement> terminalStatements = terminalStatementDao.findByConvertedIdStartingWithAndConvertedIdStartingWith(category.orElse(""), nameFilter.orElse(""));
                List<TerminalStatementVO> terminalStatementVOs = terminalStatements.stream()
                                                                                   .map(TerminalStatementVO::from)
                                                                                   .sorted((o1, o2) -> (o1.getId()).compareToIgnoreCase(o2.getId()))
                                                                                   .collect(Collectors.toList());
                result.put("returnCode", "1");
                result.put("errormessage", "");
                result.put("data", data);
                data.put("record", terminalStatementVOs);
            } else if ("get_service_detail".equals(service.get())) {
                Optional<TerminalStatementDetail> optionalTerminalStatement = terminalStatementDetailDao.findByConvertedId(target.orElse(""));
                if (!optionalTerminalStatement.isPresent()) {
                    throw new Exception("해당 전문 없음");
                } else {
                    Map<String, Object> data = new HashMap<>();
                    Map<String, Object> datacollections = new HashMap<>();
                    TerminalStatementDetail terminalStatementDetail = optionalTerminalStatement.get();
                    result.put("returnCode", "1");
                    result.put("errormessage", "");
                    result.put("data", data);
                    data.put("desc", "");
                    data.put("datacollections", datacollections);

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode rtn = mapper.readTree(terminalStatementDetail.getConvertedContents());
                        datacollections.put("datacollection", rtn);
                    } catch (Exception e) {
                        datacollections.put("datacollection", mapper.createArrayNode());
                    }
                }
            } else {
                throw new Exception("Parameter[service:" + service.get() + "]가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            result.put("returnCode", "-1");
            result.put("errormessage", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
