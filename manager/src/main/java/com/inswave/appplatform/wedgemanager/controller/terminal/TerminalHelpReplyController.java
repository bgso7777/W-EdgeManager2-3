package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.wedgemanager.dao.TerminalHelpReplyDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalHelpReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/terminal/help-reply")
public class TerminalHelpReplyController {

    private TerminalHelpReplyDao terminalHelpReplyDao;

    public TerminalHelpReplyController(TerminalHelpReplyDao terminalHelpReplyDao) {
        this.terminalHelpReplyDao = terminalHelpReplyDao;
    }

    @PostMapping(path = "/select")
    public ResponseEntity<List<TerminalHelpReply>> select(@RequestBody JsonNode req) {
        String screenId = req.get("screenId").asText();
        return ResponseEntity.ok(terminalHelpReplyDao.findByScreenIdAndPIdIsNull(screenId));
    }

    @PostMapping(path = "/insert")
    public ResponseEntity insert(@RequestBody JsonNode req) {
        Long parentId = req.get("parentId") != null ? req.get("parentId").asLong() : null;
        String screenId = req.get("screenId") != null ? req.get("screenId").asText() : null;
        String screenName = req.get("screenName") != null ? req.get("screenName").asText() : null;
        String contents = req.get("contents") != null ? req.get("contents").asText() : null;
        String createUserName = req.get("createUserName") != null ? req.get("createUserName").asText() : null;
        String createUserId = req.get("createUserId") != null ? req.get("createUserId").asText() : null;

        TerminalHelpReply terminalHelpReply = TerminalHelpReply.builder()
                                                               .parentId(parentId)
                                                               .screenId(screenId)
                                                               .screenName(screenName)
                                                               .contents(contents)
                                                               .build();
        terminalHelpReply.setCreateDate(ZonedDateTime.now());
        terminalHelpReply.setCreateUserName(createUserName);
        terminalHelpReply.setCreateUserId(createUserId);

        return ResponseEntity.ok(terminalHelpReplyDao.save(terminalHelpReply));
    }

    @PostMapping(path = "/update")
    public ResponseEntity update(@RequestBody JsonNode req) {
        Long id = req.get("id").asLong();
        String contents = req.get("contents").asText();
        String updateUserName = req.get("updateUserName").asText();
        String updateUserId = req.get("updateUserId").asText();
        Optional<TerminalHelpReply> terminalHelpReply = terminalHelpReplyDao.findById(id);
        if (terminalHelpReply.isPresent()) {
            terminalHelpReply.get().setContents(contents);
            terminalHelpReply.get().setUpdateDate(ZonedDateTime.now());
            terminalHelpReply.get().setUpdateUserName(updateUserName);
            terminalHelpReply.get().setUpdateUserId(updateUserId);
            terminalHelpReplyDao.save(terminalHelpReply.get());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@RequestBody JsonNode req) {
        Long id = req.get("id").asLong();
        terminalHelpReplyDao.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
