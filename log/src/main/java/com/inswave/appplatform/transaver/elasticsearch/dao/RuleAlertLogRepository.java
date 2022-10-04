package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RuleAlertLogRepository extends ElasticsearchRepository<RuleAlertLog, String>,Document2Repository<RuleAlertLog> {

    List<RuleAlertLog> findByRuleLevelNameAndIsCheck(String ruleLevelName, Boolean isCheck);
    List<RuleAlertLog> findByTimeRegisteredGreaterThanAndTimeRegisteredLessThan(String from, String to);
    List<RuleAlertLog> findByTimeRegisteredGreaterThanAndTimeRegisteredLessThanAndRuleLevelNameAndIsCheck(String from, String to, String ruleLevelName, Boolean isCheck);

    void deleteByRuleLevelName(String ruleLevelName);

//    List<RuleAlertLog> findByIsSendTerminalErrorSystemAndUserId(Boolean isSendTerminalError, String userId);
//    List<RuleAlertLog> findByTimeSendTerminalErrorSystemBetweenAndTimeSendTerminalErrorSystem(String from, String to);
//    List<RuleAlertLog> findByTimeSendTerminalErrorSystemBetweenAndTimeSendTerminalErrorSystemAndIsSendTerminalErrorSystem(String from, String to, Boolean isSendTerminalError);
//    List<RuleAlertLog> findByTimeSendTerminalErrorSystemBetweenAndTimeSendTerminalErrorSystemAndIsSendTerminalErrorSystemAndUserId(String from, String to, Boolean isSendTerminalError, String userId);
//
//    List<RuleAlertLog> findDistinctByUserId();
//    List<RuleAlertLog> findDistinctTopByUserId();
//    List<RuleAlertLog> findDistinctFirstByUserId();
//
//    List<RuleAlertLog> findByUserIdOrderByTimeCurrentDesc(String userId);

}