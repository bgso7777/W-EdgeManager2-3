package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.processor.rule.RuleAlert;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.translate.WindowsEventSystemErrorAll;
import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.WindowsEventSystemErrorAllLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.WindowsEventSystemErrorAllLogOriginal;
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

public class WindowsEvent implements MessageRuleProcessor {

    private List<WindowsEventSystemErrorAllLog> windowsEventSystemErrorAllLogs = new ArrayList<>();

    @Override
    public void parse(String message) throws Exception {

        JavaMelodyMonitor.printInfo("RealtimeEventRuleProcessor Listener Counter", "");
        JavaMelodyMonitor.printInfo("WindowsEventRuleProcessor", "");

        message = message.replace("\uFEFF", "");
        Parse parse = new Parse();
        JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
        for (Object object: jSONArray) {
            WindowsEventSystemErrorAll windowsEventSystemErrorAll = new WindowsEventSystemErrorAll();
            WindowsEventSystemErrorAllLogOriginal windowsEventSystemErrorAllLogOriginal = windowsEventSystemErrorAll.getOriginalObject((LinkedHashMap) object);
            List<WindowsEventSystemErrorAllLog> tempWindowsEventSystemErrorAllLogs = windowsEventSystemErrorAll.reconstructDocument(windowsEventSystemErrorAllLogOriginal);
            for (WindowsEventSystemErrorAllLog windowsEventSystemErrorAllLog : tempWindowsEventSystemErrorAllLogs) {
                windowsEventSystemErrorAllLogs.add(windowsEventSystemErrorAllLog);
                if (Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)
                ) {
                    JavaMelodyMonitor.printInfo("WindowsEventRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(windowsEventSystemErrorAllLog.getTimeCurrent()));
                    JavaMelodyMonitor.printInfo("WindowsEventRuleProcessor", "source-->>" + windowsEventSystemErrorAllLog.getSource());
                    JavaMelodyMonitor.printInfo("WindowsEventRuleProcessor", "date-->>" + (new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX)).format(windowsEventSystemErrorAllLog.getTimeCurrent()) + " deviceId-->>" + windowsEventSystemErrorAllLog.getDeviceId());
                }
            }
        }

    }

    @Override
    public List<RuleAlertLog> process(List<Rule> rules) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ScriptException {
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        for (WindowsEventSystemErrorAllLog windowsEventSystemErrorAllLog : windowsEventSystemErrorAllLogs) {
            for (Rule rule : rules) {
                String patternData = rule.getPattern();
                Field[] fields = ObjectUtil.getFields(windowsEventSystemErrorAllLog);
                for (Field field : fields) {
                    if (patternData.indexOf(field.getName()) != -1) {
                        String fieldValue = ObjectUtil.getFieldValueMethod(windowsEventSystemErrorAllLog, field.getName());
                        patternData = patternData.replaceAll(field.getName(), fieldValue);
                    }
                }
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                Boolean result = (Boolean) engine.eval(patternData.toString().trim());
                if (result) {
                    RuleAlert ruleAlertProcessor = new RuleAlert();
                    ruleLogs.add(ruleAlertProcessor.getRuleLog(rule, patternData, windowsEventSystemErrorAllLog));
                }
            }
        }
        return ruleLogs;
    }

}
