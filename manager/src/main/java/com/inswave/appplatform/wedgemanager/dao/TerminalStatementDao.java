package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalStatement;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TerminalStatementDao extends StandardDao<TerminalStatement, Long> {
    Optional<TerminalStatement> findByConvertedId(String convertedId);

    List<TerminalStatement> findByConvertedIdStartingWithAndConvertedIdStartingWith(String category, String filter);
}
