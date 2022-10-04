package com.inswave.appplatform.log.processor.rule;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.domain.RuleLevelType;
import com.inswave.appplatform.util.DateUtil;

public class RuleAlertMedium extends Thread {

    private boolean isExistSendData = false;

    public RuleAlertMedium() {
    }

    public void run() {
        do {
            try {
                // 일자선이 바뀌기 전에 처리가 고려 되야 함. ??????????????

                int currentHour = DateUtil.getHour();
                int currentMinute = DateUtil.getMinute();
                if (currentMinute==0) {
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
        this.isExistSendData = isExistSendData;
    }

    private void send() {
        if(isExistSendData) {
            try {
                RuleAlert ruleAlertProcessor = new RuleAlert(RuleLevelType.MEDIUM.toString());
                if(ruleAlertProcessor.getRuleAlertLogsSize()>0) {
                    ruleAlertProcessor.createRuleAlertSendLogs();
                    ruleAlertProcessor.sendRuleAlertSendLogs();
                    ruleAlertProcessor.saveRuleAlertSendLog();
                    boolean tempIsExistSendData = false;
                    tempIsExistSendData = ruleAlertProcessor.completeRuleAlertLog();
                    if(!isExistSendData && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)) {
                        // 신한은행 ATOP terminal에러의 경우 대상 파일이 원복되는 시간대(00~09)가 존재함
                        isExistSendData = tempIsExistSendData;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isExistSendData=false;
        }
    }
}