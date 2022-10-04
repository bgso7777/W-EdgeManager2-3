package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDistSchedule;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDistScheduleTarget;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminalDistScheduleTargetDao extends StandardDao<TerminalDistScheduleTarget, Long> {
    List<TerminalDistScheduleTarget> findByDistSchedule(TerminalDistSchedule distSchedule);
}


