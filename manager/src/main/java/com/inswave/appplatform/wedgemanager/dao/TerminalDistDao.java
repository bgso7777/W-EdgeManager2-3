package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDist;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminalDistDao extends StandardDao<TerminalDist, Long> {
    List<TerminalDist> findByRepositoryName(String repositoryName, Sort sort);
}