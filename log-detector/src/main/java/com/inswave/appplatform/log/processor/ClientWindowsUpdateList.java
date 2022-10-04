package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.processor.rule.RuleAlert;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.log.translate.ClientWindowsUpdateListResource;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientWindowsUpdateListResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientWindowsUpdateListResourceLogOriginal;
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

public class ClientWindowsUpdateList implements MessageRuleProcessor {

    private List<ClientWindowsUpdateListResourceLog> clientWindowsUpdateListResourceLogs = new ArrayList<>();

    @Override
    public void parse(String message) throws Exception {

        JavaMelodyMonitor.printInfo("RealtimeEventRuleProcessor Listener Counter", "");
        JavaMelodyMonitor.printInfo("ClientWindowsUpdateListRuleProcessor", "");

        message = message.replace("\uFEFF", "");
        Parse parse = new Parse();
        JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
        for (Object object: jSONArray) {
            ClientWindowsUpdateListResource clientWindowsUpdateListResource = new ClientWindowsUpdateListResource();
            ClientWindowsUpdateListResourceLogOriginal clientWindowsUpdateListResourceLogOriginal = clientWindowsUpdateListResource.getOriginalObject((LinkedHashMap) object);
            List<ClientWindowsUpdateListResourceLog> tempClientWindowsUpdateListResourceLogs = clientWindowsUpdateListResource.reconstructDocument(clientWindowsUpdateListResourceLogOriginal);
            for (ClientWindowsUpdateListResourceLog clientWindowsUpdateListResourceLog : tempClientWindowsUpdateListResourceLogs) {
                clientWindowsUpdateListResourceLogs.add(clientWindowsUpdateListResourceLog);
                if (Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)
                ) {
                    JavaMelodyMonitor.printInfo("ClientWindowsUpdateListRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(clientWindowsUpdateListResourceLog.getTimeCurrent()));
                    JavaMelodyMonitor.printInfo("ClientWindowsUpdateListRuleProcessor", "source-->>" + clientWindowsUpdateListResourceLog.getSource());
                    JavaMelodyMonitor.printInfo("ClientWindowsUpdateListRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(clientWindowsUpdateListResourceLog.getTimeCurrent()) + " deviceId-->>" + clientWindowsUpdateListResourceLog.getDeviceId());
                }
            }
        }

    }

    @Override
    public List<RuleAlertLog> process(List<Rule> rules) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ScriptException {
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        for (ClientWindowsUpdateListResourceLog clientWindowsUpdateListResourceLog : clientWindowsUpdateListResourceLogs) {
            for (Rule rule : rules) {
                String patternData = rule.getPattern();
                Field[] fields = ObjectUtil.getFields(clientWindowsUpdateListResourceLog);
                for (Field field : fields) {
                    if (patternData.indexOf(field.getName()) != -1) {
                        String fieldValue = ObjectUtil.getFieldValueMethod(clientWindowsUpdateListResourceLog, field.getName());
                        patternData = patternData.replaceAll(field.getName(), fieldValue);
                    }
                }
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                Boolean result = (Boolean) engine.eval(patternData.toString().trim());
                if (result) {
                    RuleAlert ruleAlertProcessor = new RuleAlert();
                    ruleLogs.add(ruleAlertProcessor.getRuleLog(rule, patternData, clientWindowsUpdateListResourceLog));
                }
            }
        }
        return ruleLogs;
    }

}
