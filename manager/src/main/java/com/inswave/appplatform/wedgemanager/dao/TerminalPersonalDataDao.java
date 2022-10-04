package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalPersonalData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TerminalPersonalDataDao extends StandardDao<TerminalPersonalData, Long> {
    List<TerminalPersonalData> findByUserIdAndDataType(String userId, String datatType);
    Optional<TerminalPersonalData> findByUserIdAndDataTypeAndDataId(String userId, String datatType, Long dataId);
}