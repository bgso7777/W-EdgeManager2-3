package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.processor.rule.RuleAlert;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientHWInfoResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientHWInfoResourceLogOriginal;
import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertLog;
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

public class ClientHWInfoResource implements MessageRuleProcessor {

    private List<ClientHWInfoResourceLog> clientHWInfoResourceLogs = new ArrayList<>();

    @Override
    public void parse(String message) throws Exception {

        JavaMelodyMonitor.printInfo("RealtimeEventRuleProcessor Listener Counter", "");
        JavaMelodyMonitor.printInfo("ClientHWInfoResourceLogRuleProcessor", "");

        message = message.replaceAll("\uFEFF", "");
        Parse parse = new Parse();
        JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
        for (Object object : jSONArray) {
            com.inswave.appplatform.log.translate.ClientHWInfoResource clientHWInfoResource = new com.inswave.appplatform.log.translate.ClientHWInfoResource();
            ClientHWInfoResourceLogOriginal clientHWInfoResourceLogOriginal = clientHWInfoResource.getOriginalObject((LinkedHashMap) object);
            List<ClientHWInfoResourceLog> tempClientHWInfoResourceLogs = clientHWInfoResource.reconstructDocument(clientHWInfoResourceLogOriginal);
            for (ClientHWInfoResourceLog clientHWInfoResourceLog : tempClientHWInfoResourceLogs) {
                clientHWInfoResourceLogs.add(clientHWInfoResourceLog);
                if (Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)
                ) {
                    JavaMelodyMonitor.printInfo("ClientHWInfoResourceRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(clientHWInfoResourceLog.getTimeCurrent()));
                    JavaMelodyMonitor.printInfo("ClientHWInfoResourceProcessor", "source-->>" + clientHWInfoResourceLog.getSource());
                    JavaMelodyMonitor.printInfo("ClientHWInfoResourceRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(clientHWInfoResourceLog.getTimeCurrent()) + " deviceId-->>" + clientHWInfoResourceLog.getDeviceId());
                }
            }
        }
    }

    @Override
    public List<RuleAlertLog> process(List<Rule> rules) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ScriptException {
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        for (ClientHWInfoResourceLog clientHWInfoResourceLog : clientHWInfoResourceLogs) {
            for (Rule rule : rules) {
                String patternData = rule.getPattern();
                Field[] fields = ObjectUtil.getFields(clientHWInfoResourceLog);
                for (Field field : fields) {
                    if (patternData.indexOf(field.getName()) != -1) {
                        String fieldValue = ObjectUtil.getFieldValueMethod(clientHWInfoResourceLog, field.getName());
                        patternData = patternData.replaceAll(field.getName(), fieldValue);
                    }
                }
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                Boolean result = (Boolean) engine.eval(patternData.toString().trim());
                if (result) {
                    RuleAlert ruleAlertProcessor = new RuleAlert();
                    ruleLogs.add(ruleAlertProcessor.getRuleLog(rule, patternData, clientHWInfoResourceLog));
                }
            }
        }
        return ruleLogs;
    }

}
