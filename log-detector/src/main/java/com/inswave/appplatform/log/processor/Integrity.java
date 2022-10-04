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
     * result   100, 200, 300 정상  이외의 것들이 들어 온다.
     *
     * 신한은행ATOP 장애단말예측 관련 데이터 (정합성 재실행 실패 시 원격제어용 데이터로 IntegrityLog 만 해당)
     */
    private void processShinhanbankatopIntegrityLogResult(List<RuleAlertLog> ruleAlertLogs) {

        for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
            for(IntegrityLogDataCommon integrityLogDataCommon : integrityLog.getIntegrity().getIntegrityData() ) {

                // 1.그룹별 장애 등록 여부
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

                    // 알림 체크 여부
                    ruleAlertLog.setIsCheck(false);

                    // 장애단말예측 시스템으로 전송
                    ruleAlertLog.setIsShinhanbankAtopTerminalError(true);
                    ruleAlertLog.setIsSendTerminalErrorSystem(false); // 등록 여부
                    ruleAlertLog.setTimeSendTerminalErrorSystem(null); // 등록 일시

                    ruleAlertLog.setIsFollowUp(false); // 장애 조치 완료 여부
                    //ruleAlertLog.setTimeFollowUp(null); // 장애 조치 완료 일시

                    ////////////////////////////////////////////////////////////////////////////////////////////////////////
                    for (IntegrityLogDataRule integrityLogDataRule : integrityLogDataCommon.getRules()) {
                        /*  0 : 검증 성공
                            1 : 검증 에러 + 복구 성공
                            2 : 검증 에러 + 복구 에러 ------
                            3 : 항상 실행 + Script 성공
                            4 : 항상 실행 + Script 에러 ------
                            10 : 검증 SKIP 조건 (재택PC 제외 조건에 따름 : isTargetHome)
                            20 : 오류 수동 복구 */
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