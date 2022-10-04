package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalPreference;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TerminalPreferenceDao extends StandardDao<TerminalPreference, Long> {
    Optional<TerminalPreference> findByDataKey(String dataKey);

    List<TerminalPreference> findByDataKeyLike(String dataKey);

    List<TerminalPreference> findByDataKeyStartingWith(String dataKey);
}