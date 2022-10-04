package com.inswave.appplatform.wedgemanager.service.scm.distribute;

import com.inswave.appplatform.wedgemanager.dao.*;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDist;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDistResultDetail;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDistSchedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DistributeService {
    private static DistributeTaskDao           distributeTaskDao;
    private static TerminalDistDao             terminalDistDao;
    private static TerminalDistResultDao       terminalDistResultDao;
    private static TerminalDistResultDetailDao terminalDistResultDetailDao;
    private static TerminalDistScheduleDao     terminalDistScheduleDao;

    public DistributeService(DistributeTaskDao distributeTaskDao,
                             TerminalDistDao terminalDistDao,
                             TerminalDistResultDao terminalDistResultDao,
                             TerminalDistResultDetailDao terminalDistResultDetailDao,
                             TerminalDistScheduleDao terminalDistScheduleDao) {
        this.distributeTaskDao = distributeTaskDao;
        this.terminalDistDao = terminalDistDao;
        this.terminalDistResultDao = terminalDistResultDao;
        this.terminalDistResultDetailDao = terminalDistResultDetailDao;
        this.terminalDistScheduleDao = terminalDistScheduleDao;
    }

    public static TerminalDistResultDetail saveTerminalDistDetailResult(TerminalDistResultDetail terminalDistResultDetail) {
        return terminalDistResultDetailDao.save(terminalDistResultDetail);
    }

    public static List<TerminalDist> saveTerminalDists(List<TerminalDist> terminalDists) {
        return terminalDistDao.saveAll(terminalDists);
    }

    public static List<TerminalDist> getTerminalDists(List<Long> ids) {
        return terminalDistDao.findAllById(ids);
    }

    public static TerminalDistSchedule saveTerminalDistSchedule(TerminalDistSchedule terminalDistSchedule) {
        return terminalDistScheduleDao.save(terminalDistSchedule);
    }

    public static Optional<TerminalDistSchedule> findTerminalDistScheduleById(Long terminalDistScheduleId) {
        return terminalDistScheduleDao.findById(terminalDistScheduleId);
    }

    public static DistributeTask saveDistributeTask(DistributeTask distributeTask) {
        return distributeTaskDao.save(distributeTask);
    }

    public static List<DistributeTask> countUnExecutedScheduleTask(Long terminalDistScheduleId) {
        return distributeTaskDao.countUnExecutedScheduleTask(terminalDistScheduleId);
    }

    public static Optional<DistributeTask> findFirstByExecutedAndExecuteDateNullOrderByIdDesc() {
        return distributeTaskDao.findFirstByExecutedAndExecuteDateNullOrderByIdDesc(false);
    }

}
