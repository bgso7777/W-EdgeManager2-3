package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDistSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TerminalDistScheduleDao extends StandardDao<TerminalDistSchedule, Long> {
    @Query(" SELECT a "
           + " FROM TerminalDistSchedule a"
           + " WHERE isActive = true"
           + " AND  ("
           + "          (scheduleType='Once' AND lastExecuteDate is null)"
           + "      OR  (scheduleType='Repeat' AND ( lastExecuteDate is null OR lastExecuteDate < :now) )"
           + "      ) ")
    List<TerminalDistSchedule> findScheduleUpdateTarget(@Param("now") ZonedDateTime now);
}
