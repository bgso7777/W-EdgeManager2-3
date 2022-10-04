package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.dao.RdbDaoPackage;
import com.inswave.appplatform.log.dao.RuleDao;
import com.inswave.appplatform.log.dao.RuleLevelDao;
import com.inswave.appplatform.log.dao.RuleReceiverDao;
import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.log.domain.RuleLevel;
import com.inswave.appplatform.log.domain.RuleLevelType;
import com.inswave.appplatform.log.domain.RuleReceiver;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.processor.rule.*;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.RuleAlertExclusionRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.RuleAlertLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import com.inswave.appplatform.transaver.util.BeanUtils;
import com.inswave.appplatform.util.FileUtil;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRuleProcessorManager {

    private static ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);

    private static MessageRuleProcessorManager ruleProcessor;
    private static final Logger logger = LoggerFactory.getLogger(MessageRuleProcessorManager.class);

    private static HashMap<Long, Rule> rules = new HashMap<Long, Rule>();
    private static HashMap<Long, RuleReceiver> ruleReceivers = new HashMap<Long, RuleReceiver>();
    private static HashMap<Long, RuleLevel> ruleLevels = new HashMap<Long, RuleLevel>();
    private static HashMap<String, RuleAlertExclusion> ruleAlertExclusions = new HashMap<String, RuleAlertExclusion>();

//    private static AssociationAnalysis associationAnalysis = null;

    private static RuleAlertEmergency ruleAlertEmergency = null;
    private static RuleAlertRisk ruleAlertRisk = null;
    private static RuleAlertHigh ruleAlertHigh = null;
    private static RuleAlertMedium ruleAlertMedium = null;
    private static RuleAlertLow ruleAlertLow = null;

    public static MessageRuleProcessorManager getInstance() {
        if(ruleProcessor==null) {
            ruleProcessor = new MessageRuleProcessorManager();
        }
        return ruleProcessor;
    }

    private MessageRuleProcessorManager() {
        initialRuleInfo();
        runRuleAlertSender();
//        associationAnalysis = new AssociationAnalysis();
//        associationAnalysis.start();
    }

    private void runRuleAlertSender() {
        ruleAlertEmergency = new RuleAlertEmergency();
        ruleAlertEmergency.start();

        ruleAlertRisk = new RuleAlertRisk();
        ruleAlertRisk.start();

        ruleAlertHigh = new RuleAlertHigh();
        ruleAlertHigh.start();

        ruleAlertMedium = new RuleAlertMedium();
        ruleAlertMedium.start();

        ruleAlertLow = new RuleAlertLow();
        ruleAlertLow.start();
    }

    public static HashMap<Long, Rule> getRules() {
        return rules;
    }

    public static HashMap<Long, RuleReceiver> getRuleReceivers() {
        return ruleReceivers;
    }

    public static HashMap<Long, RuleLevel> getRuleLevels() {
        return ruleLevels;
    }

    public static HashMap<String, RuleAlertExclusion> getRuleAlertExclusions() {
        return ruleAlertExclusions;
    }

    /**
     * 변경 사항에 대해 외부에서 주기적으로 업데이트를 호출한다.
     */
    public void initialRuleInfo() {

        try{
            rules.clear();
            RuleDao ruleDao = (RuleDao) RdbDaoPackage.getInstance().getDao(Rule.class.getSimpleName());
            List<Rule> tempRules = ruleDao.findAllByActive(true);
            for (Rule rule : tempRules) {
                rules.put(rule.getRuleId(),rule);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        try{
            ruleReceivers.clear();
            RuleReceiverDao ruleReceiverDao = (RuleReceiverDao) RdbDaoPackage.getInstance().getDao(RuleReceiver.class.getSimpleName());
            List<RuleReceiver> tempRuleReceivers = ruleReceiverDao.findAll();
            for (RuleReceiver ruleReceiver : tempRuleReceivers) {
                ruleReceivers.put(ruleReceiver.getRuleReceiverId(),ruleReceiver);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        try{
            ruleLevels.clear();
            RuleLevelDao ruleLevelDao = (RuleLevelDao) RdbDaoPackage.getInstance().getDao(RuleLevel.class.getSimpleName());
            List<RuleLevel> tempRuleLevels = ruleLevelDao.findAll();
            for (RuleLevel ruleLevel : tempRuleLevels) {
                ruleLevels.put(ruleLevel.getRuleLevelId(),ruleLevel);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        try{
            ruleAlertExclusions.clear();
            RuleAlertExclusionRepository ruleAlertExclusionRepository = (RuleAlertExclusionRepository)ElasticsearchDaoPackage.getInstance().getElasticsearchDao(RuleAlertExclusion.class.getSimpleName());
            DynamicIndexBean.setIndexName(RuleAlertExclusion.class.getSimpleName().toLowerCase());
            List<RuleAlertExclusion> tempRuleAlertExclusions = ruleAlertExclusionRepository.findAll();
            for (RuleAlertExclusion ruleAlertExclusion : tempRuleAlertExclusions) {
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "ruleAlertExclusion.getExclusionType()-->>"+ruleAlertExclusion.getExclusionType());
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "ruleAlertExclusion.getUserId()-->>"+ruleAlertExclusion.getUserId());
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "ruleAlertExclusion.getHostName()-->>"+ruleAlertExclusion.getHostName());
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "ruleAlertExclusion.getIp()-->>"+ruleAlertExclusion.getIp());
                ruleAlertExclusions.put(ruleAlertExclusion.getDocumentId(),ruleAlertExclusion);
            }
            JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "ruleAlertExclusions.size()-->>"+ruleAlertExclusions.size());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void saveRuleLog(List<RuleAlertLog> ruleAlertLogs)  {

        if(ruleAlertLogs.size()>0) {
            RuleAlertLogRepository ruleAlertLogRepository = (RuleAlertLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(RuleAlertLog.class.getSimpleName());
            if (!elasticsearchHighLevelClient.existIndices(RuleAlertLog.class.getSimpleName().toLowerCase())) {
                do {
                    try {
                        ruleAlertLogs.get(0).setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
                        DynamicIndexBean.setIndexName(ruleAlertLogs.get(0).getIndexName());
                        ruleAlertLogRepository.setIndexName(ruleAlertLogs.get(0).getIndexName());
                        ruleAlertLogRepository.createIndex(ruleAlertLogs.get(0));
                    } catch (UncategorizedElasticsearchException e) { // already exist (다른 서버에서 생성한 경우)
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (elasticsearchHighLevelClient.existIndices(RuleAlertLog.class.getSimpleName().toLowerCase()))
                        break;
                    else
                        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "not create index : " + RuleAlertLog.class.getSimpleName().toLowerCase());
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                } while (true);
            }
            List<Document2> document2s = new ArrayList<>();
            for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
                ruleAlertLog.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
                ruleAlertLog.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
                document2s.add(ruleAlertLog);
            }
            try {
                BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
                if (bulkResponse.hasFailures())
                    logger.error(bulkResponse.buildFailureMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
                initExistSendData(ruleAlertLog.getRuleLevelName());
            }
        }
    }

    public void initExistSendData(String savedRuleLevel) {
        if( savedRuleLevel!=null && savedRuleLevel.equals(RuleLevelType.EMERGENCY.toString()) )
            ruleAlertEmergency.existSendData(true);
        else if( savedRuleLevel!=null && savedRuleLevel.equals(RuleLevelType.RISK.toString()) )
            ruleAlertRisk.existSendData(true);
        else if( savedRuleLevel!=null && savedRuleLevel.equals(RuleLevelType.HIGH.toString()) )
            ruleAlertHigh.existSendData(true);
        else if( savedRuleLevel!=null && savedRuleLevel.equals(RuleLevelType.MEDIUM.toString()) )
            ruleAlertMedium.existSendData(true);
        else if( savedRuleLevel!=null && savedRuleLevel.equals(RuleLevelType.LOW.toString()) )
            ruleAlertRisk.existSendData(true);
        else
            ;
    }

    private static ServerResourceObserver serverResourceObserver = null;
    /**
     *
     * @param message
     */
    public void processServerResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ServerResourceLogger",""  );

        List<Rule> serverResourceLogRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ServerResourceLog.class.getSimpleName()))
                serverResourceLogRules.add(rule);
        }
        ServerResource serverResource = new ServerResource();
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(serverResourceLogRules.size()>0) {
            serverResource.parse(message);
            ruleLogs = serverResource.process(serverResourceLogRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }
        if(Config.getInstance().getLog().getIsRunServerresourcelogObserver()) {
            if(serverResourceObserver==null) {
                serverResourceObserver = new ServerResourceObserver();
                serverResourceObserver.start();
            }
            if(serverResource.getServerResourceLog()==null)
                serverResource.parse(message);
            serverResourceObserver.add(serverResource.getServerResourceLog());
        }
    }

    /**
     * @param message
     */
    public void processWindowsEventSystemErrorAllLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "WindowsEventSystemErrorAllLogger",""  );

        List<Rule> windowEventLogRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(WindowsEventSystemErrorAllLog.class.getSimpleName()))
                windowEventLogRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(windowEventLogRules.size()>0) {
            WindowsEvent windowsEvent = new WindowsEvent();
            windowsEvent.parse(message);
            ruleLogs = windowsEvent.process(windowEventLogRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }
    }


    /**
     * @param message
     */
    public void processClientPerformanceResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientPerformanceResourceLogger",""  );

        List<Rule> clientPerformanceResourceLogRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientPerformanceResourceLog.class.getSimpleName()))
                clientPerformanceResourceLogRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientPerformanceResourceLogRules.size()>0) {
            ClientResource clientResource = new ClientResource();
            clientResource.parse(message);
            ruleLogs = clientResource.process(clientPerformanceResourceLogRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    /**
     * @param message
     */
    public void processClientProcessResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientProcessResourceLogger",""  );

        List<Rule> clientProcessResourceRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientProcessResourceLog.class.getSimpleName()))
                clientProcessResourceRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientProcessResourceRules.size()>0) {
            ClientProcessResource clientProcessResource = new ClientProcessResource();
            clientProcessResource.parse(message);
            ruleLogs = clientProcessResource.process(clientProcessResourceRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processApplicationErrorLog(String message) throws Exception {
        
        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ApplicationErrorLogger","");

        List<Rule> applicationErrorRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ApplicationErrorLog.class.getSimpleName()))
                applicationErrorRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(applicationErrorRules.size()>0) {
            ApplicationError applicationError = new ApplicationError();
            applicationError.parse(message);
            ruleLogs = applicationError.process(applicationErrorRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }
    }

    public void processClientDefragAnalysisResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientDefragAnalysisResourceLogger","" );

        List<Rule> clientDefragAnalysisResourceRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientDefragAnalysisResourceLog.class.getSimpleName()))
                clientDefragAnalysisResourceRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientDefragAnalysisResourceRules.size()>0) {
            ClientDefragAnalysisResource clientDefragAnalysisResource = new ClientDefragAnalysisResource();
            clientDefragAnalysisResource.parse(message);
            ruleLogs = clientDefragAnalysisResource.process(clientDefragAnalysisResourceRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }
    }

    public void processClientHWInfoResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientHWInfoResourceLogger","" );

        List<Rule> clientHWInfoResourceRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientHWInfoResourceLog.class.getSimpleName()))
                clientHWInfoResourceRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientHWInfoResourceRules.size()>0) {
            ClientHWInfoResource clientHWInfoResource = new ClientHWInfoResource();
            clientHWInfoResource.parse(message);
            ruleLogs = clientHWInfoResource.process(clientHWInfoResourceRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processClientMBRResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientMBRResourceLogger",""  );

        List<Rule> clientMBRResourceLogRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientMBRResourceLog.class.getSimpleName()))
                clientMBRResourceLogRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientMBRResourceLogRules.size()>0) {
            ClientMBRResource clientMBRResource = new ClientMBRResource();
            clientMBRResource.parse(message);
            ruleLogs = clientMBRResource.process(clientMBRResourceLogRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processClientProgramListResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo("ClientProgramListResourceLogger",""  );

        List<Rule> clientProgramListResourceRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientProgramListResourceLog.class.getSimpleName()))
                clientProgramListResourceRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientProgramListResourceRules.size()>0) {
            ClientProgramList clientProgramList = new ClientProgramList();
            clientProgramList.parse(message);
            ruleLogs = clientProgramList.process(clientProgramListResourceRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processDeviceErrorLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo("DeviceErrorLogger",""  );

        List<Rule> deviceErrorRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(DeviceErrorLog.class.getSimpleName()))
                deviceErrorRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(deviceErrorRules.size()>0) {
            DeviceError deviceError = new DeviceError();
            deviceError.parse(message);
            ruleLogs = deviceError.process(deviceErrorRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processHWErrorLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "HWErrorLogger",""  );

        List<Rule> hWErrorRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(HWErrorLog.class.getSimpleName()))
                hWErrorRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(hWErrorRules.size()>0) {
            HWError hWError = new HWError();
            hWError.parse(message);
            ruleLogs = hWError.process(hWErrorRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processIntegrityLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "IntegrityLogger",""  );

        List<Rule> integrityRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(IntegrityLog.class.getSimpleName()))
                integrityRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(integrityRules.size()>0) {
            Integrity integrity = new Integrity();
            integrity.parse(message);
            ruleLogs = integrity.process(integrityRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processPcOnOffEventLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo("PcOnOffEventLogger",""  );

        List<Rule> pcOnOffEventRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(PcOnOffEventLog.class.getSimpleName()))
                pcOnOffEventRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(pcOnOffEventRules.size()>0) {
            PcOnOffEvent pcOnOffEvent = new PcOnOffEvent();
            pcOnOffEvent.parse(message);
            ruleLogs = pcOnOffEvent.process(pcOnOffEventRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processWindowsBlueScreenLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "WindowsBlueScreenLogger",""  );

        List<Rule> windowsBlueScreenRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(WindowsBlueScreenLog.class.getSimpleName()))
                windowsBlueScreenRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(windowsBlueScreenRules.size()>0) {
            WindowsBlueScreen windowsBlueScreen = new WindowsBlueScreen();
            windowsBlueScreen.parse(message);
            ruleLogs = windowsBlueScreen.process(windowsBlueScreenRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processClientWindowsUpdateListResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientWindowsUpdateListResourceLogger","" );

        List<Rule> clientWindowsUpdateListRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientWindowsUpdateListResourceLog.class.getSimpleName()))
                clientWindowsUpdateListRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientWindowsUpdateListRules.size()>0) {
            ClientWindowsUpdateList clientWindowsUpdateList = new ClientWindowsUpdateList();
            clientWindowsUpdateList.parse(message);
            ruleLogs = clientWindowsUpdateList.process(clientWindowsUpdateListRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processClientUserTermMonitorLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientUserTermMonitorLog","");

        List<Rule> clientUserTermMonitorRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientUserTermMonitorLog.class.getSimpleName()))
                clientUserTermMonitorRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientUserTermMonitorRules.size()>0) {
            ClientUserTermMonitor clientUserTermMonitor = new ClientUserTermMonitor();
            clientUserTermMonitor.parse(message);
            ruleLogs = clientUserTermMonitor.process(clientUserTermMonitorRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }

    public void processClientActivePortListResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientActivePortListResourceLog","");

        List<Rule> clientActivePortListRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientActivePortListResourceLog.class.getSimpleName()))
                clientActivePortListRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientActivePortListRules.size()>0) {
            ClientActivePortList clientActivePortList = new ClientActivePortList();
            clientActivePortList.parse(message);
            ruleLogs = clientActivePortList.process(clientActivePortListRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }
    }


    /**
     * @param message
     */
    public void processClientProcessCreationLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientProcessCreationLogger",""  );

        List<Rule> clientProcessResourceRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientProcessResourceLog.class.getSimpleName()))
                clientProcessResourceRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientProcessResourceRules.size()>0) {
            ClientProcessResource clientProcessResource = new ClientProcessResource();
            clientProcessResource.parse(message);
            ruleLogs = clientProcessResource.process(clientProcessResourceRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }

    }


    public void processClientControlProcessResourceLog(String message) throws Exception {

        JavaMelodyMonitor.printInfo( "RealtimeEventRuleProcessor Listener Counter","");
        JavaMelodyMonitor.printInfo( "ClientControlProcessResourceLog","");

        List<Rule> clientControlProcessResourceLogRules = new ArrayList<Rule>();
        //rules.forEach((ruleId, rule)->{
        for(Map.Entry<Long, Rule> entry : rules.entrySet()) {
            Rule rule = entry.getValue();
            if (rule.getClassNames().equals(ClientControlProcessResourceLog.class.getSimpleName()))
                clientControlProcessResourceLogRules.add(rule);
        }
        List<RuleAlertLog> ruleLogs = new ArrayList<RuleAlertLog>();
        if(clientControlProcessResourceLogRules.size()>0) {
            ClientControlProcessResource clientControlProcessResource = new ClientControlProcessResource();
            clientControlProcessResource.parse(message);
            ruleLogs = clientControlProcessResource.process(clientControlProcessResourceLogRules);
//            if(ruleLogs.size()>0) {
//                associationAnalysis.addAssociation(ruleLogs);
//                if(associationAnalysis.size()>0)
//                    associationAnalysis.analysisAssociation(ruleLogs);
//            }
            saveRuleLog(ruleLogs);
        }
    }


    public static void main(String argv[]) {
        try{
            MessageRuleProcessorManager ruleManager = MessageRuleProcessorManager.getInstance();

            List<Rule> rules = new ArrayList<Rule>();

            StringBuffer messageFile = FileUtil.getFileToStringBuffer("C:\\Temp\\ServerResourceLogger.message"); //
            String[] messages = messageFile.toString().split("\n");
            for (int i = 0; i < messages.length; i++) {
                System.out.println(messages[i]);
                ruleManager.processServerResourceLog((messages[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
