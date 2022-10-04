package com.inswave.appplatform.log.processor.rule;

import com.inswave.appplatform.log.domain.RuleLevelType;

public class RuleAlertEmergency extends Thread {

    private boolean isExistSenddData = false;

    public RuleAlertEmergency() {
    }

    public void run() {
        do {
            try {
                Thread.sleep(1);
                try {
                    send();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
            }
        } while (true);
    }

    public void existSendData(boolean isExistSendData) {
        this.isExistSenddData = isExistSendData;
    }

    private void send() {
        if(isExistSenddData) {
            try {
                RuleAlert ruleAlertProcessor = new RuleAlert(RuleLevelType.EMERGENCY.toString());
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
