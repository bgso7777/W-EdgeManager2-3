package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.processor.rule.RuleAlert;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import com.inswave.appplatform.util.ObjectUtil;
import org.json.simple.JSONArray;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Integrity implements MessageRuleProcessor {

    private IntegrityLog integrityLog = new IntegrityLog();

    @Override
    public void parse(String message) throws Exception {

        JavaMelodyMonitor.printInfo("RealtimeEventRuleProcessor Listener Counter", "");
        JavaMelodyMonitor.printInfo("IntegrityRuleProcessor", "");

        message = message.replace("\uFEFF", "");
        Parse parse = new Parse();
        JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
        for (Object object: jSONArray) {
            com.inswave.appplatform.log.translate.Integrity integrity = new com.inswave.appplatform.log.translate.Integrity();
            IntegrityLog tempIntegrityLog = integrity.getIntegrityLogObject((LinkedHashMap) object);
            tempIntegrityLog.setResult(tempIntegrityLog.getIntegrity().getResult());
            tempIntegrityLog.setTimeCurrent(tempIntegrityLog.getIntegrity().getStartTime());
            tempIntegrityLog.setIndexName(IntegrityLog.class.getSimpleName().toLowerCase());
            tempIntegrityLog.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
            integrityLog = tempIntegrityLog;
            if (Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)
            ) {
                JavaMelodyMonitor.printInfo("IntegrityRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(tempIntegrityLog.getTimeCurrent()));
                JavaMelodyMonitor.printInfo("IntegrityErrorRuleProcessor", "source-->>" + tempIntegrityLog.getSource());
                JavaMelodyMonitor.printInfo("IntegrityRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(tempIntegrityLog.getTimeCurrent()) + " deviceId-->>" + tempIntegrityLog.getDeviceId());
            }
        }
    }

    @Override
    public List<RuleAlertLog> process(List<Rule> rules) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ScriptException {
        List<RuleAlertLog> ruleAlertLogs = new ArrayList<RuleAlertLog>();
        for (Rule rule : rules) {
            String patternData = rule.getPattern();
            Field[] fields = ObjectUtil.getFields(integrityLog);
            for (Field field : fields) {
                if (patternData.indexOf(field.getName()) != -1) {
                    String fieldValue = ObjectUtil.getFieldValueMethod(integrityLog, field.getName());
                    patternData = patternData.replaceAll(field.getName(), fieldValue);
                }
            }
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            Boolean result = (Boolean) engine.eval(patternData.toString().trim());
            if (result) {
                RuleAlert ruleAlertProcessor = new RuleAlert();
//                ruleAlertLogs.add(ruleAlertProcessor.getRuleLog(rule, patternData, integrityLog));
                boolean isExcluesion=false; String isExclusionStr="";
                for(String key : MessageRuleProcessorManager.getRuleAlertExclusions().keySet()) {
                    RuleAlertExclusion ruleAlertExclusion = MessageRuleProcessorManager.getRuleAlertExclusions().get(key);
                    if( ruleAlertExclusion.getExclusionType()==1 &&
                        ruleAlertExclusion.getClassName().equals(IntegrityLog.class.getSimpleName())) {
                        if( ruleAlertExclusion.getUserId().equals(integrityLog.getUserId())) {
                            isExcluesion = true;
                            isExclusionStr = "exclusion id --> "+integrityLog.getUserId();
                        } else if( ruleAlertExclusion.getHostName().equals(integrityLog.getHostName())) {
                            isExcluesion = true;
                            isExclusionStr = "exclusion hostName --> "+integrityLog.getHostName();
                        } else if( ruleAlertExclusion.getIp().equals(integrityLog.getIp())) {
                            isExcluesion = true;
                            isExclusionStr = "exclusion ip --> "+integrityLog.getIp();
                        }
                    }
                }
                if(isExcluesion)
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),isExclusionStr);
                else
                    ruleAlertLogs.add(ruleAlertProcessor.getRuleLog(rule,patternData,integrityLog));
            }
        }

        if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) )
            processShinhanbankatopIntegrityLogResult(ruleAlertLogs);

        return ruleAlertLogs;
    }

    /**
     * result   100, 200, 300 ??????  ????????? ????????? ?????? ??????.
     *
     * ????????????ATOP ?????????????????? ?????? ????????? (????????? ????????? ?????? ??? ??????????????? ???????????? IntegrityLog ??? ??????)
     */
    private void processShinhanbankatopIntegrityLogResult(List<RuleAlertLog> ruleAlertLogs) {

        for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
            for(IntegrityLogDataCommon integrityLogDataCommon : integrityLog.getIntegrity().getIntegrityData() ) {

                // 1.????????? ?????? ?????? ??????
                if( integrityLogDataCommon.getFaultReg() ) {

                    RuleAlertLogShinhanbankAtopTerminalError ruleAlertLogShinhanbankAtopTerminalError = new RuleAlertLogShinhanbankAtopTerminalError();

                    ruleAlertLogShinhanbankAtopTerminalError.setUserId(integrityLog.getUserId());
                    ruleAlertLogShinhanbankAtopTerminalError.setUserName(integrityLog.getUserName());

                    ruleAlertLogShinhanbankAtopTerminalError.setInterityPolicyGroupName(integrityLogDataCommon.getName());
                    ruleAlertLogShinhanbankAtopTerminalError.setDescription(integrityLogDataCommon.getDescription());
                    ruleAlertLogShinhanbankAtopTerminalError.setResult(integrityLog.getIntegrity().getResult());
                    ruleAlertLogShinhanbankAtopTerminalError.setNetClient5(integrityLog.getIntegrity().getInfo().get("NetClient5"));
                    ruleAlertLogShinhanbankAtopTerminalError.setIsTargetHome(integrityLogDataCommon.getIsTargetHome());
                    ruleAlertLogShinhanbankAtopTerminalError.setPcType(integrityLogDataCommon.getPcType());
                    ruleAlertLogShinhanbankAtopTerminalError.setRemoteCode(integrityLogDataCommon.getRemoteCode());
                    ruleAlertLogShinhanbankAtopTerminalError.setFaultReg(integrityLogDataCommon.getFaultReg());
                    ruleAlertLogShinhanbankAtopTerminalError.setSendErrCnt(integrityLogDataCommon.getSendErrCnt());

                    // ?????? ?????? ??????
                    ruleAlertLog.setIsCheck(false);

                    // ?????????????????? ??????????????? ??????
                    ruleAlertLog.setIsShinhanbankAtopTerminalError(true);
                    ruleAlertLog.setIsSendTerminalErrorSystem(false); // ?????? ??????
                    ruleAlertLog.setTimeSendTerminalErrorSystem(null); // ?????? ??????

                    ruleAlertLog.setIsFollowUp(false); // ?????? ?????? ?????? ??????
                    //ruleAlertLog.setTimeFollowUp(null); // ?????? ?????? ?????? ??????

                    ////////////////////////////////////////////////////////////////////////////////////////////////////////
                    for (IntegrityLogDataRule integrityLogDataRule : integrityLogDataCommon.getRules()) {
                        /*  0 : ?????? ??????
                            1 : ?????? ?????? + ?????? ??????
                            2 : ?????? ?????? + ?????? ?????? ------
                            3 : ?????? ?????? + Script ??????
                            4 : ?????? ?????? + Script ?????? ------
                            10 : ?????? SKIP ?????? (??????PC ?????? ????????? ?????? : isTargetHome)
                            20 : ?????? ?????? ?????? */
                        if (integrityLogDataRule.getResult() == 2 || integrityLogDataRule.getResult() == 4) {
                            ruleAlertLogShinhanbankAtopTerminalError.getFailIds().add(integrityLogDataRule.getId());
                            ruleAlertLogShinhanbankAtopTerminalError.getFailNames().add(integrityLogDataRule.getName());
                            ruleAlertLogShinhanbankAtopTerminalError.getFailDescriptions().add(integrityLogDataRule.getDescription());
                        }
                    }
                    ruleAlertLogShinhanbankAtopTerminalError.setFailCount(ruleAlertLogShinhanbankAtopTerminalError.getFailIds().size());
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////

                    ruleAlertLog.setFailCount(ruleAlertLog.getFailCount() + ruleAlertLogShinhanbankAtopTerminalError.getFailCount());

                    if(ruleAlertLogShinhanbankAtopTerminalError.getFailCount()>0)
                        ruleAlertLog.getRuleAlertLogShinhanbankAtopTerminalError().add(ruleAlertLogShinhanbankAtopTerminalError);
                }
            }
        }

    }

}