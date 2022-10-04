package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.service.scm.distribute.DistributeTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistributeTaskDao extends StandardDao<DistributeTask, Long> {
    @Query(" SELECT a"
           + " FROM DistributeTask a "
           + " WHERE a.executed = false "
           + " AND a.executeDate IS NULL "
           + " AND a.terminalDistScheduleId = :terminalDistScheduleId ")
    List<DistributeTask> countUnExecutedScheduleTask(@Param("terminalDistScheduleId") Long terminalDistScheduleId);

    //    @Query(" SELECT TOP 1 t "
    //           + "FROM DistributeTask t "
    //           + "WHERE t.executed = false "
    //           + "AND t.executeDate IS NULL "
    //           + "ORDER BY t.id DESC ")
    //    DistributeTask findTopUnExecutedTask();

    Optional<DistributeTask> findFirstByExecutedAndExecuteDateNullOrderByIdDesc(boolean executed);
}