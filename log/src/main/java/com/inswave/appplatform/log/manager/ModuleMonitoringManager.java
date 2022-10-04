package com.inswave.appplatform.log.manager;

import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.log.monitor.KafkaListenerMonitor;
import com.inswave.appplatform.log.monitor.LoggerMonitor;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.ModuleMonitoringLog;
import com.inswave.appplatform.util.DateUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleMonitoringManager extends Thread {

    public static ModuleMonitoringManager moduleMonitoring = null;

    public static ModuleMonitoringManager getInstance() {
        if( moduleMonitoring == null ) {
            moduleMonitoring = new ModuleMonitoringManager();
            moduleMonitoring.start();
        }
        return moduleMonitoring;
    }

    // key -->> instanceId, topicName, groupId
    private static ConcurrentHashMap<String, ModuleMonitoringLog> moduleMonitoringLogs = new ConcurrentHashMap<>();
    private static List<LoggerMonitor> loggerMonitors = new ArrayList<LoggerMonitor>();
    private static List<KafkaListenerMonitor> kafkaListenerMonitors = new ArrayList<KafkaListenerMonitor>();

    public void addKafkaListenerMonitor(KafkaListenerMonitor kafkaListenerMonitor) {
        kafkaListenerMonitors.add(kafkaListenerMonitor);
    }

    public void addLoggerMonitor(LoggerMonitor loggerMonitor) {
        loggerMonitors.add(loggerMonitor);
    }

    private void initKafkaListenerMonitor(KafkaListenerMonitor kafkaListenerMonitor) {

        String key = "";
        if( kafkaListenerMonitor.getTopicName()!=null && kafkaListenerMonitor.getGroupId()!=null )
            key = kafkaListenerMonitor.getTopicName()+"_"+kafkaListenerMonitor.getGroupId();

        ModuleMonitoringLog moduleMonitoringLog = moduleMonitoringLogs.get(key);
        if(moduleMonitoringLog==null) {
            moduleMonitoringLog = new ModuleMonitoringLog();
            moduleMonitoringLog.setKey(key);
            moduleMonitoringLog.setInstanceId(kafkaListenerMonitor.getInstanceId());
            moduleMonitoringLog.setTopicName(kafkaListenerMonitor.getTopicName());
            moduleMonitoringLog.setGroupId(kafkaListenerMonitor.getGroupId());
        }

        moduleMonitoringLog.setTimeCurrent( DateTimeConvertor.getDate(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z, kafkaListenerMonitor.getTimeCurrent()) );
        moduleMonitoringLog.setYear(DateUtil.getYear());
        moduleMonitoringLog.setMonth(DateUtil.getMonth());
        moduleMonitoringLog.setDay(DateUtil.getDay());
        moduleMonitoringLog.setDayOfWeek(DateUtil.getDayOfWeek());
        moduleMonitoringLog.setHour(DateUtil.getHour());
        moduleMonitoringLog.setMinutely10(DateUtil.getMinute10());

        moduleMonitoringLog.setPartitionId(kafkaListenerMonitor.getPartitionId());
        moduleMonitoringLog.setOffset(kafkaListenerMonitor.getOffset());

        if(moduleMonitoringLog.getBeginOffsets()<=0)
            moduleMonitoringLog.setBeginOffsets(kafkaListenerMonitor.getEndOffsets());
        moduleMonitoringLog.setEndOffsets(kafkaListenerMonitor.getEndOffsets());

        moduleMonitoringLog.setProcessOffsets(moduleMonitoringLog.getEndOffsets()-moduleMonitoringLog.getBeginOffsets());

        moduleMonitoringLog.setProcessDateTime(kafkaListenerMonitor.getProcessDateTime());
        moduleMonitoringLog.setProcessByte(moduleMonitoringLog.getProcessByte()+kafkaListenerMonitor.getProcessByte());
        moduleMonitoringLog.setProcessTime(moduleMonitoringLog.getProcessTime()+kafkaListenerMonitor.getRunningTime());
        moduleMonitoringLog.setProcessCount(moduleMonitoringLog.getProcessCount()+kafkaListenerMonitor.getProcessCount());
        try{moduleMonitoringLog.setBpms(moduleMonitoringLog.getProcessByte()/(moduleMonitoringLog.getProcessTime()));} catch(Exception e) {}
        try{moduleMonitoringLog.setBps(moduleMonitoringLog.getProcessByte()/(moduleMonitoringLog.getProcessTime()/1000));} catch(Exception e) {}

        moduleMonitoringLogs.put(key,moduleMonitoringLog);
    }

    private void initLoggerMonitor(LoggerMonitor loggerMonitor) {

        String key = "";
        if( loggerMonitor.getTopicName()!=null && loggerMonitor.getGroupId()!=null )
            key = loggerMonitor.getTopicName()+"_"+loggerMonitor.getGroupId();

        ModuleMonitoringLog moduleMonitoringLog = moduleMonitoringLogs.get(key);
        if(moduleMonitoringLog==null) {
            moduleMonitoringLog = new ModuleMonitoringLog();
            moduleMonitoringLog.setKey(key);
            moduleMonitoringLog.setInstanceId(loggerMonitor.getInstanceId());
            moduleMonitoringLog.setTopicName(loggerMonitor.getTopicName());
            moduleMonitoringLog.setGroupId(loggerMonitor.getGroupId());
        }

        moduleMonitoringLog.setObjectConvertTime(moduleMonitoringLog.getObjectConvertTime()+loggerMonitor.getObjectConvertTime());

        moduleMonitoringLog.setRowSize(moduleMonitoringLog.getRowSize()+loggerMonitor.getRowSize());

        moduleMonitoringLog.setLastRowSize(loggerMonitor.getRowSize());
        if(moduleMonitoringLog.getMinRowSize()>moduleMonitoringLog.getLastRowSize())
            moduleMonitoringLog.setMinRowSize(moduleMonitoringLog.getLastRowSize());
        if(moduleMonitoringLog.getMaxRowSize()<moduleMonitoringLog.getLastRowSize())
            moduleMonitoringLog.setMaxRowSize(moduleMonitoringLog.getLastRowSize());

        moduleMonitoringLog.setElasticsearchCurrentInsert(moduleMonitoringLog.getElasticsearchCurrentInsert()+loggerMonitor.getElasticsearchCurrentInsert());

        moduleMonitoringLog.setDailyRowSize(moduleMonitoringLog.getDailyRowSize()+loggerMonitor.getDailyRowSize());
        moduleMonitoringLog.setElasticsearchDailyInsert(moduleMonitoringLog.getElasticsearchDailyInsert()+loggerMonitor.getElasticsearchDailyInsert());

        moduleMonitoringLog.setJsonProcessingExceptionCount(moduleMonitoringLog.getJsonProcessingExceptionCount()+loggerMonitor.getJsonProcessingExceptionCount());
        moduleMonitoringLog.setCommitSyncExceptionCount(moduleMonitoringLog.getCommitSyncExceptionCount()+loggerMonitor.getCommitSyncExceptionCount());
        moduleMonitoringLog.setExceptionCount(moduleMonitoringLog.getExceptionCount()+loggerMonitor.getExceptionCount());

        moduleMonitoringLog.setRunningTime(moduleMonitoringLog.getRunningTime()+loggerMonitor.getRunningTime());

        moduleMonitoringLogs.put(key,moduleMonitoringLog);

    }

    private void initModuleMonitoringLogs() {
        for (LoggerMonitor loggerMonitor : loggerMonitors) {
            try {
                initLoggerMonitor(loggerMonitor);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        loggerMonitors.clear();
        loggerMonitors = new ArrayList<LoggerMonitor>();

        for (KafkaListenerMonitor kafkaListenerMonitor : kafkaListenerMonitors) {
            try{
                initKafkaListenerMonitor(kafkaListenerMonitor);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        kafkaListenerMonitors.clear();
        kafkaListenerMonitors = new ArrayList<KafkaListenerMonitor>();
    }

    private void saveAndClearInitModuleMonitoringLogs() {

        List<Document2> saveModuleMonitoringLogs = new ArrayList<>();

        Enumeration keys = moduleMonitoringLogs.keys();
        while(keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            ModuleMonitoringLog moduleMonitoringLog = moduleMonitoringLogs.get(key);
            if(moduleMonitoringLog.getTimeCurrent()!=null) {
                moduleMonitoringLog.setIndexName(DateTimeConvertor.getIndexName(ModuleMonitoringLog.class.getSimpleName().toLowerCase(), moduleMonitoringLog.getTimeCurrent()));
                try {
                    saveModuleMonitoringLogs.add(moduleMonitoringLog.cloneModuleMonitoringLog());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        moduleMonitoringLogs.clear();
        moduleMonitoringLogs = new ConcurrentHashMap<>();

//        try {
//            ModuleMonitoringLogger moduleMonitoringLogger = new ModuleMonitoringLogger();
//            moduleMonitoringLogger.insert("", saveModuleMonitoringLogs);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }

        IData iData = new NodeData();
        try {
			Class<?> cls = Class.forName(ConstantsTranSaver.CLASS_OF_MODULE_MONITORING);
			Constructor<?> constructor = cls.getConstructor();
			Object node = constructor.newInstance();
			Method method = cls.getMethod("insert", String.class, List.class);
			iData = (IData)method.invoke(node, "", saveModuleMonitoringLogs);
		} catch (Throwable e) {
			e.printStackTrace();
		}

        saveModuleMonitoringLogs.clear();
    }

    public void run() {
        String saveMinute="";
        do {
            try {
                int currentHour = DateUtil.getHour();
                int currentMinute = DateUtil.getMinute();
                String currentSaveMinute = DateTimeConvertor.getSaveValue(new Date());

                if(saveMinute.equals(""))
                    saveMinute=currentSaveMinute;
                if(saveMinute.equals(currentSaveMinute)) {
                    initModuleMonitoringLogs();
                } else {
                    saveMinute=currentSaveMinute;
                    initModuleMonitoringLogs();
                    saveAndClearInitModuleMonitoringLogs();
                }
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }

    public static void main(String argv[]) {
        ModuleMonitoringManager.getInstance();
    }

}
