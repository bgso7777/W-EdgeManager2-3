package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertSendLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;

public interface RuleAlertSendLogRepository extends ElasticsearchRepository<RuleAlertSendLog, String>,Document2Repository<RuleAlertSendLog> {

    List<RuleAlertSendLog> findByUserId(String userId);
    List<RuleAlertSendLog> findByDeviceIdAndIntegrityPolicyGroupName(String userId, String integrityPolicyGroupName);
    List<RuleAlertSendLog> findByIntegrityPolicyGroupName(String integrityPolicyGroupName);

    List<RuleAlertSendLog> findByTimeSendBetween(Date from, Date to);
    List<RuleAlertSendLog> findByTimeSendBetweenAndUserId(Date from, Date to, String userId);

    List<RuleAlertSendLog> findByTimeSendGreaterThanAndTimeSendLessThan(Date from, Date to);
    List<RuleAlertSendLog> findByTimeSendGreaterThanAndTimeSendLessThanAndUserId(Date from, Date to, String userId);

//    List<RuleAlertSendLog> findByIsShinhanbankAtopTerminalErrorAndTimeSendBetweenAndTimeSend(Boolean isShinhanbankAtopTerminalError, String from, String to);
//    List<RuleAlertSendLog> findByIsShinhanbankAtopTerminalErrorAndTimeSendBetweenAndTimeSendAndUserId(Boolean isShinhanbankAtopTerminalError, String from, String to, String userId);

}

