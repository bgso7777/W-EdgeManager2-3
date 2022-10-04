package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.processor.rule.RuleAlert;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientMBRResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientMBRResourceLogOriginal;
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

public class ClientMBRResource implements MessageRuleProcessor {

    private List<ClientMBRResourceLog> clientMBRResourceLogs = new ArrayList<>();

    @Override
    public void parse(String message) throws Exception {

        JavaMelodyMonitor.printInfo("RealtimeEventRuleProcessor Listener Counter", "");
        JavaMelodyMonitor.printInfo("ClientMBRResourceProcessor", "");

        message = message.replaceAll("\uFEFF", "");
        Parse parse = new Parse();
        JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
        for (Object object: jSONArray) {
            com.inswave.appplatform.log.translate.ClientMBRResource clientMBRResource = new com.inswave.appplatform.log.translate.ClientMBRResource();
            ClientMBRResourceLogOriginal clientMBRResourceLogOriginal = clientMBRResource.getOriginalObject((LinkedHashMap) object);
            List<ClientMBRResourceLog> tempClientMBRResourceLogs = clientMBRResource.reconstructDocument(clientMBRResourceLogOriginal);
            for (ClientMBRResourceLog clientMBRResourceLog : tempClientMBRResourceLogs) {
                clientMBRResourceLogs.add(clientMBRResourceLog);
                if (Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)
                ) {
                    JavaMelodyMonitor.printInfo("ClientMBRResourceRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(clientMBRResourceLog.getTimeCurrent()));
                    JavaMelodyMonitor.printInfo("ClientMBRResourceRuleProcessor", "source-->>" + clientMBRResourceLog.getSource());
                    JavaMelodyMonitor.printInfo("ClientMBRResourceRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(clientMBRResourceLog.getTimeCurrent()) + " deviceId-->>" + clientMBRResourceLog.getDeviceId());
                }
            }
        }

    }

    @Override
    public List<RuleAlertLog> process(List<Rule> rules) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ScriptException {
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        for (ClientMBRResourceLog clientMBRResourceLog : clientMBRResourceLogs) {
            for (Rule rule : rules) {
                String patternData = rule.getPattern();
                Field[] fields = ObjectUtil.getFields(clientMBRResourceLog);
                for (Field field : fields) {
                    if (patternData.indexOf(field.getName()) != -1) {
                        String fieldValue = ObjectUtil.getFieldValueMethod(clientMBRResourceLog, field.getName());
                        patternData = patternData.replaceAll(field.getName(), fieldValue);
                    }
                }
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                Boolean result = (Boolean) engine.eval(patternData.toString().trim());
                if (result) {
                    RuleAlert ruleAlertProcessor = new RuleAlert();
                    ruleLogs.add(ruleAlertProcessor.getRuleLog(rule, patternData, clientMBRResourceLog));
                }
            }
        }
        return ruleLogs;
    }

}
