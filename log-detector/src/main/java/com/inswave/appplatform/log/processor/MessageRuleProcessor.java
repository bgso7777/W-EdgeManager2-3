package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertLog;

import java.util.List;

public interface MessageRuleProcessor {

    public void parse(String message) throws Exception;
    public List<RuleAlertLog> process(List<Rule> rules) throws Exception;

}
