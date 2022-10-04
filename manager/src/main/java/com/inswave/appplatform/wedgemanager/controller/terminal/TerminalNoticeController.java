package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.service.dao.FileAttachementMapDao;
import com.inswave.appplatform.service.rdbdao.StandardServiceHelper;
import com.inswave.appplatform.wedgemanager.dao.TerminalNoticeDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalNotice;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalNoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/terminal/notice")
public class TerminalNoticeController {

    private TerminalNoticeDao     terminalNoticeDao;
    private FileAttachementMapDao fileAttachementMapDao;

    public TerminalNoticeController(TerminalNoticeDao terminalNoticeDao,
                                    FileAttachementMapDao fileAttachementMapDao) {
        this.terminalNoticeDao = terminalNoticeDao;
        this.fileAttachementMapDao = fileAttachementMapDao;
    }

    @PostMapping(path = "/select-all")
    public ResponseEntity<Map<String, Object>> selectAll(@RequestBody JsonNode req) {
        JsonNode pageable = req.get("pageable");
        JsonNode where = req.get("where");
        PageRequest pageRequest = StandardServiceHelper.toPageRequest(pageable);
        Specification specification = StandardServiceHelper.toSpecification(where);
        Page<TerminalNotice> noticeList = terminalNoticeDao.findAll(specification, pageRequest);

        List<TerminalNoticeVO> terminalNoticeVOS = noticeList.getContent().stream().map(TerminalNoticeVO::from).collect(Collectors.toList());
        terminalNoticeVOS.forEach(terminalNoticeVO -> {
            terminalNoticeVO.setFileCount(fileAttachementMapDao.countBySourceTypeAndSourceCode("TerminalNotice", String.valueOf(terminalNoticeVO.getId())));
        });

        Map<String, Object> result = new HashMap<>();

        result.put(Constants.TAG_TABLE_ENTITY_ROWS, terminalNoticeVOS);
        result.put(Constants.TAG_PAGE_ROW_COUNT, noticeList.getTotalElements());
        result.put(Constants.TAG_PAGE_NUMBER, noticeList.getNumber());
        result.put(Constants.TAG_PAGE_SIZE, noticeList.getSize());
        result.put(Constants.TAG_PAGE_COUNT, noticeList.getTotalPages());

        return ResponseEntity.ok(result);
    }
}
