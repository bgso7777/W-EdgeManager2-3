package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalStatementDetail;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TerminalStatementDetailDao extends StandardDao<TerminalStatementDetail, Long> {
    Optional<TerminalStatementDetail> findByConvertedId(String convertedId);
}
