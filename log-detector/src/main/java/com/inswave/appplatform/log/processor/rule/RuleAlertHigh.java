package com.inswave.appplatform.log.processor.rule;

import com.inswave.appplatform.log.domain.RuleLevelType;
import com.inswave.appplatform.util.DateUtil;

public class RuleAlertHigh extends Thread {

    private boolean isExistSenddData = false;

    public RuleAlertHigh() {
    }

    public void run() {
        do {
            try {
                // 일자선이 바뀌기 전에 처리가 고려 되야 함. ??????????????

                int currentHour = DateUtil.getHour();
                int currentMinute = DateUtil.getMinute();
                if (currentMinute==0||
                    currentMinute==30) {
                    try {
                        send();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(60000);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }

    public void existSendData(boolean isExistSendData) {
        this.isExistSenddData = isExistSendData;
    }

    private void send() {
        if(isExistSenddData) {
            try {
                RuleAlert ruleAlertProcessor = new RuleAlert(RuleLevelType.HIGH.toString());
                if(ruleAlertProcessor.getRuleAlertLogsSize()>0) {
                    ruleAlertProcessor.createRuleAlertSendLogs();
                    ruleAlertProcessor.sendRuleAlertSendLogs();
                    ruleAlertProcessor.saveRuleAlertSendLog();
                    ruleAlertProcessor.completeRuleAlertLog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isExistSenddData=false;
        }
    }
}