package com.inswave.appplatform.log.processor.rule;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.legacy.Mailer;
//import com.inswave.appplatform.legacy.ShinhanbankGoldWing;
//import com.inswave.appplatform.legacy.ShinhanbankTerminalErrorAlertSystem;
import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.log.domain.RuleLevel;
import com.inswave.appplatform.log.domain.RuleLevelType;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.processor.MessageRuleProcessorManager;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.IntegrityLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.RuleAlertLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.RuleAlertSendLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import com.inswave.appplatform.transaver.util.BeanUtils;
import com.inswave.appplatform.util.DateUtil;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RuleAlert {

    public RuleAlert(){
    }

    public RuleAlertLog getRuleLog(Rule rule, String patternData, Document2 document2) {

        RuleAlertLog ruleAlertLog = new RuleAlertLog();
        ruleAlertLog.setRuleId(rule.getRuleId());
        ruleAlertLog.setName(rule.getName());
        ruleAlertLog.setDescription(rule.getDescription());
        ruleAlertLog.setClassNames(rule.getClassNames());
        ruleAlertLog.setDisplayClassNames(rule.getDisplayClassNames());
        ruleAlertLog.setFieldNames(rule.getFieldNames());
        ruleAlertLog.setDisplayFieldNames(rule.getDisplayFieldNames());
        ruleAlertLog.setPattern(rule.getPattern());
        ruleAlertLog.setPatternData(patternData);
        ruleAlertLog.setRuleLevelId(rule.getRuleLevelId());
        RuleLevel ruleLevel = MessageRuleProcessorManager.getInstance().getRuleLevels().get(rule.getRuleLevelId());
        if(ruleLevel!=null)ruleAlertLog.setRuleLevelName(ruleLevel.getName());
        if(ruleLevel!=null)ruleAlertLog.setRuleLevelDescription(ruleLevel.getDescription());
        if(rule.getRuleReceiverId1()!=null) {
            RuleAlertLogReceiverData ruleAlertLogReceiverData = new RuleAlertLogReceiverData();
            ruleAlertLogReceiverData.setRuleReceiverId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId1()).getRuleReceiverId());
            ruleAlertLogReceiverData.setName(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId1()).getName());
            ruleAlertLogReceiverData.setUniqueId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId1()).getUniqueId());
            ruleAlertLogReceiverData.setIsEmail(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId1()).getIsEmail());
            ruleAlertLogReceiverData.setEmail(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId1()).getEmail());
            ruleAlertLogReceiverData.setIsUrl(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId1()).getIsUrl());
            ruleAlertLogReceiverData.setSenderId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId1()).getSenderId());
            ruleAlertLogReceiverData.setSenderName(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId1()).getSenderName());
            ruleAlertLog.getRuleAlertLogReceiverData().add(ruleAlertLogReceiverData);
        }
        if(rule.getRuleReceiverId2()!=null) {
            RuleAlertLogReceiverData ruleAlertLogReceiverData = new RuleAlertLogReceiverData();
            ruleAlertLogReceiverData.setRuleReceiverId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId2()).getRuleReceiverId());
            ruleAlertLogReceiverData.setName(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId2()).getName());
            ruleAlertLogReceiverData.setUniqueId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId2()).getUniqueId());
            ruleAlertLogReceiverData.setIsEmail(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId2()).getIsEmail());
            ruleAlertLogReceiverData.setEmail(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId2()).getEmail());
            ruleAlertLogReceiverData.setIsUrl(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId2()).getIsUrl());
            ruleAlertLogReceiverData.setSenderId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId2()).getSenderId());
            ruleAlertLogReceiverData.setSenderName(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId2()).getSenderName());
            ruleAlertLog.getRuleAlertLogReceiverData().add(ruleAlertLogReceiverData);
        }
        if(rule.getRuleReceiverId3()!=null) {
            RuleAlertLogReceiverData ruleAlertLogReceiverData = new RuleAlertLogReceiverData();
            ruleAlertLogReceiverData.setRuleReceiverId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId3()).getRuleReceiverId());
            ruleAlertLogReceiverData.setName(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId3()).getName());
            ruleAlertLogReceiverData.setUniqueId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId3()).getUniqueId());
            ruleAlertLogReceiverData.setIsEmail(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId3()).getIsEmail());
            ruleAlertLogReceiverData.setEmail(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId3()).getEmail());
            ruleAlertLogReceiverData.setIsUrl(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId3()).getIsUrl());
            ruleAlertLogReceiverData.setSenderId(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId3()).getSenderId());
            ruleAlertLogReceiverData.setSenderName(MessageRuleProcessorManager.getInstance().getRuleReceivers().get(rule.getRuleReceiverId3()).getSenderName());
            ruleAlertLog.getRuleAlertLogReceiverData().add(ruleAlertLogReceiverData);
        }
        ruleAlertLog.setTimeCurrent(document2.getTimeCreated());
        ruleAlertLog.setUpdateDate(DateTimeConvertor.replaceDate(rule.getUpdateDate()));
        ruleAlertLog.setRegistrationDate(DateTimeConvertor.replaceDate(rule.getRegistrationDate()));

        ruleAlertLog.setService(document2.getService());
        ruleAlertLog.setAppId(document2.getAppId());
        ruleAlertLog.setOsType(document2.getOsType());
        ruleAlertLog.setSource(document2.getSource());
        ruleAlertLog.setDeviceId(document2.getDeviceId());
        ruleAlertLog.setIp(document2.getIp());
        ruleAlertLog.setHostName(document2.getHostName());
        ruleAlertLog.setTimeCreated(document2.getTimeCreated());

        ruleAlertLog.setUserId(document2.getUserId());
        ruleAlertLog.setTermNo(document2.getTermNo());
        ruleAlertLog.setSsoBrNo(document2.getSsoBrNo());
        ruleAlertLog.setBrNo(document2.getBrNo());
        ruleAlertLog.setHwnNo(document2.getHwnNo());
        ruleAlertLog.setDeptName(document2.getDeptName());
        ruleAlertLog.setUserName(document2.getUserName());
        ruleAlertLog.setSsoType(document2.getSsoType());
        ruleAlertLog.setPcName(document2.getPcName());
        ruleAlertLog.setPhoneNo(document2.getPhoneNo());
        ruleAlertLog.setJKGP(document2.getJKGP());
        ruleAlertLog.setJKWI(document2.getJKWI());
        ruleAlertLog.setMaxAddress(document2.getMaxAddress());
        ruleAlertLog.setFirstWork(document2.getFirstWork());

        return ruleAlertLog;
    }

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    private String ruleType;
    private RuleAlertLogRepository ruleAlertLogRepository = null;
    private RuleAlertSendLogRepository ruleAlertSendLogRepository = null;
    private IntegrityLogRepository integrityLogRepository = null;
    private List<RuleAlertLog> ruleAlertLogs = new ArrayList<RuleAlertLog>();
    private Hashtable<String, RuleAlertSendLog> ruleAlertSendLogs = new Hashtable<String, RuleAlertSendLog>();
    private Hashtable<String, RuleAlertSendLog> currentRuleAlertSendLogsForShinhanbankatop = new Hashtable<String, RuleAlertSendLog>();
    private ArrayList<RuleAlertSendLog> saveRuleAlertSendLogs = new ArrayList<>();

    public RuleAlert(String ruleType){

        this.ruleType=ruleType;
        this.ruleAlertLogRepository = (RuleAlertLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(RuleAlertLog.class.getSimpleName());
        this.ruleAlertSendLogRepository = (RuleAlertSendLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(RuleAlertSendLog.class.getSimpleName());
        this.integrityLogRepository = (IntegrityLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(IntegrityLog.class.getSimpleName());

        // ????????? ?????? ????????? ?????? ?????? ( EMERGENCY RISK HIGH MEDIUM LOW )
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "RuleAlert constructor "+ruleType+" begin");
        if(Config.getInstance().getLog().getIsMasterRuleProcessor()) {
            JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "Master Rule Processor ");
            DynamicIndexBean.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
            ruleAlertLogRepository.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
            this.ruleAlertLogs = this.ruleAlertLogRepository.findByRuleLevelNameAndIsCheck(ruleType,false);
        } else {
            JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "Slave Rule Processor "); // ???????????? ATOP??? 10??? 11??? 12????????? ?????? ??????.
            int currentHour = DateUtil.getHour();
            if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) ) {
                if( ruleType.equals(RuleLevelType.MEDIUM.toString()) &&
                    (currentHour==10||currentHour==11||currentHour==12) ) {
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "sleepSlaveRuleProcessorMs-->>" + Config.getInstance().getLog().getSleepSlaveRuleProcessorMs());
                    try {Thread.sleep(Config.getInstance().getLog().getSleepSlaveRuleProcessorMs());} catch (Exception e) {}
                    String yyyyMMdd=DateUtil.getCurrentDate(Constants.TAG_DATE_PATTERN_YYYY_MM_DD), fromDate="", toDate="";
                    if(currentHour==10) {
                        fromDate = yyyyMMdd + "T00:00:00.000Z";
                        toDate = yyyyMMdd + "T09:59:59.999Z";
                    } else if(currentHour==11) {
                        fromDate = yyyyMMdd + "T10:00:00.000Z";
                        toDate = yyyyMMdd + "T10:59:59.999Z";
                    } else if(currentHour==12) {
                        fromDate = yyyyMMdd + "T11:00:00.000Z";
                        toDate = yyyyMMdd + "T11:59:59.999Z";
                    }
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "fromDate-->>" + fromDate);
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "toDate-->>" + toDate);
                    DynamicIndexBean.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
                    ruleAlertLogRepository.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
                    this.ruleAlertLogs = this.ruleAlertLogRepository.findByTimeRegisteredGreaterThanAndTimeRegisteredLessThan(fromDate,toDate);
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "ruleAlertLogs.size()->"+ruleAlertLogs.size()+"");
                    if(this.ruleAlertLogs.size()<=0) {
                        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "master all process!");
                        return;
                    }
                } else {
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "return");
                    return;
                }
            } else {
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "sleepSlaveRuleProcessorMs-->>" + Config.getInstance().getLog().getSleepSlaveRuleProcessorMs());
                try {Thread.sleep(Config.getInstance().getLog().getSleepSlaveRuleProcessorMs());} catch (Exception e) {}
            }
        }
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "ruleAlertLogs.size()->"+ruleAlertLogs.size()+"");

        try {
            DynamicIndexBean.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
            ruleAlertLogRepository.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
            ruleAlertLogRepository.deleteByRuleLevelName(ruleType);

            ////////// slave??? sleep ?????? ?????? ??????
            int checkCount=0;
            Long increceSleepTime=200L;
            do {
                Long sleepTime=increceSleepTime*checkCount;
                try { Thread.sleep(sleepTime); } catch (Exception e) {}
                DynamicIndexBean.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
                ruleAlertLogRepository.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
                List<RuleAlertLog> afterDeleteRuleAlertLogs = this.ruleAlertLogRepository.findByRuleLevelNameAndIsCheck(ruleType, false);
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "sleepTime->"+sleepTime+" afterDeleteRuleAlertLogs.size()->"+afterDeleteRuleAlertLogs.size());
                checkCount++;
                if(afterDeleteRuleAlertLogs.size()==0)
                    break;
            } while (checkCount<=20);
            ///////

        } catch(Exception e) {
            e.printStackTrace();
        }
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "RuleAlert constructor "+ruleType+" end");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public int getRuleAlertLogsSize() {
        return this.ruleAlertLogs.size();
    }

    /**
     * 1. ?????? ?????? (RuleAlertSendLog) ??????
     * @return
     */
    public Hashtable<String, RuleAlertSendLog> createRuleAlertSendLogs() {

        String currentDate = DateUtil.getCurrentDate("yyyyMMddhhmmss");

        // ????????????, ??????????????? ?????? ??????
        for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
            if (Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) &&
                ruleAlertLog.getClassNames().equals(IntegrityLog.class.getSimpleName())) {
                // ??? ????????? ???????????? ???????????? ???????????? ?????? ????????? ?????? createAndSendShinhanbankatopIntegrityLog ??????????????? ?????? ??????.
            } else {
                if (Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE)) {
                    JavaMelodyMonitor.printInfo(ruleAlertLog.getRuleLevelName(), currentDate + " ruleAlertLog.getClassNames()-->>" + ruleAlertLog.getClassNames());
                    JavaMelodyMonitor.printInfo(ruleAlertLog.getRuleLevelName(), currentDate + " ruleAlertLog.getFieldNames()-->>" + ruleAlertLog.getFieldNames());
                    JavaMelodyMonitor.printInfo(ruleAlertLog.getRuleLevelName(), currentDate + " ruleAlertLog.getRuleLevelName()-->>" + ruleAlertLog.getRuleLevelName());
                    JavaMelodyMonitor.printInfo(ruleAlertLog.getRuleLevelName(), currentDate + " ruleAlertLog.getName()-->>" + ruleAlertLog.getName());
                    JavaMelodyMonitor.printInfo(ruleAlertLog.getRuleLevelName(), currentDate + " ruleAlertLog.getDescription()-->>" + ruleAlertLog.getDescription());
                    for (RuleAlertLogReceiverData ruleAlertLogReceiverData : ruleAlertLog.getRuleAlertLogReceiverData())
                        JavaMelodyMonitor.printInfo(ruleAlertLog.getRuleLevelName(), currentDate +
                                " ruleAlertLogReceiverData.getRuleReceiverId()-->>" + ruleAlertLogReceiverData.getRuleReceiverId() +
                                " ruleAlertLogReceiverData.getName()-->>" + ruleAlertLogReceiverData.getName() +
                                " ruleAlertLogReceiverData.getIsEmail()-->>" + ruleAlertLogReceiverData.getIsEmail() +
                                " ruleAlertLogReceiverData.getIsUrl()-->>" + ruleAlertLogReceiverData.getIsUrl());
                    JavaMelodyMonitor.printInfo(ruleAlertLog.getRuleLevelName(), currentDate + " ip-->>" + ruleAlertLog.getIp() + " deviceId-->>" + ruleAlertLog.getDeviceId());
                    JavaMelodyMonitor.printInfo(ruleAlertLog.getRuleLevelName(), currentDate + " ip-->>" + ruleAlertLog.getIp() + " deviceId-->>" + ruleAlertLog.getDeviceId() + " ruleId-->>" + ruleAlertLog.getRuleId());
                    //JavaMelodyMonitoring.printInfo("RuleAlertRisk",currentDate+" ruleAlertLog.getPatterns()-->>"+strings);
                }
                for (RuleAlertLogReceiverData ruleAlertLogReceiverData : ruleAlertLog.getRuleAlertLogReceiverData()) {
                    String key = ruleAlertLogReceiverData.getRuleReceiverId() + "_" + ruleAlertLog.getRuleLevelId();
                    RuleAlertSendLog ruleAlertSendLog = new RuleAlertSendLog();
                    ruleAlertSendLog.setRuleReceiverIdRuleLevelId(key);

                    ruleAlertSendLog.setIsShinhanbankAtopTerminalError(ruleAlertLog.getIsShinhanbankAtopTerminalError());
                    ruleAlertSendLog.setRuleReceiverId(ruleAlertLogReceiverData.getRuleReceiverId());
                    ruleAlertSendLog.setRuleReceiverName(ruleAlertLogReceiverData.getName());
                    ruleAlertSendLog.setRuleReceiverUniqueId(ruleAlertLogReceiverData.getUniqueId());
                    ruleAlertSendLog.setRuleReceiverIsEmail(ruleAlertLogReceiverData.getIsEmail());
                    ruleAlertSendLog.setRuleReceiverEmail(ruleAlertLogReceiverData.getEmail());
                    ruleAlertSendLog.setRuleReceiverIsUrl(ruleAlertLogReceiverData.getIsUrl());
                    ruleAlertSendLog.setRuleReceiverSenderId(ruleAlertLogReceiverData.getSenderId());
                    ruleAlertSendLog.setRuleReceiverSenderName(ruleAlertLogReceiverData.getSenderName());
                    ruleAlertSendLog.setTimeCreated(DateTimeConvertor.getTimeRegistered());
                    ruleAlertSendLogs.put(key, ruleAlertSendLog);
                }
            }
        }

        // ????????????, ??????????????? rule ??????
        for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
            if (Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) &&
                ruleAlertLog.getClassNames().equals(IntegrityLog.class.getSimpleName())) {
                // ??? ????????? ???????????? ???????????? ???????????? ?????? ????????? ?????? createAndSendShinhanbankatopIntegrityLog ??????????????? ?????? ??????.
            } else {
                for (RuleAlertLogReceiverData ruleAlertLogReceiverData : ruleAlertLog.getRuleAlertLogReceiverData()) {
                    String key = ruleAlertLogReceiverData.getRuleReceiverId()+"_"+ruleAlertLog.getRuleLevelId();
                    RuleAlertSendLog ruleAlertSendLog = ruleAlertSendLogs.get(key);
                    ruleAlertSendLog.setRuleReceiverIdRuleLevelId(key);
                    ruleAlertSendLog.setParentIds(ruleAlertSendLog.getParentIds()+","+ruleAlertLog.getDocumentId());
                    ruleAlertSendLog.setRuleLevelName(ruleAlertLog.getRuleLevelName());
                    RuleAlertSendLogRuleData ruleAlertSendLogRuleData = new RuleAlertSendLogRuleData();
                    ruleAlertSendLogRuleData.setRuleId(ruleAlertLog.getRuleId());
                    ruleAlertSendLogRuleData.setName(ruleAlertLog.getName());
                    ruleAlertSendLogRuleData.setDescription(ruleAlertLog.getDescription());
                    ruleAlertSendLogRuleData.setClassNames(ruleAlertLog.getClassNames());
                    ruleAlertSendLogRuleData.setDisplayClassNames(ruleAlertLog.getDisplayClassNames());
                    ruleAlertSendLogRuleData.setFieldNames(ruleAlertLog.getFieldNames());
                    ruleAlertSendLogRuleData.setDisplayFieldNames(ruleAlertLog.getDisplayFieldNames());
                    ruleAlertSendLogRuleData.setPattern(ruleAlertLog.getPattern());
                    ruleAlertSendLogRuleData.setPatternData(ruleAlertLog.getPatternData());
                    ruleAlertSendLogRuleData.setTimeCurrent(ruleAlertLog.getTimeCurrent());
                    ruleAlertSendLog.getRuleAlertSendLogRuleData().add(ruleAlertSendLogRuleData);
                }
            }
        }

        // title content ??????
        Enumeration<String> keys = ruleAlertSendLogs.keys();
        while(keys.hasMoreElements()) {
            RuleAlertSendLog ruleAlertSendLog = ruleAlertSendLogs.get(keys.nextElement());
            StringBuffer title = new StringBuffer("[ATOP] "+"??????("+ruleAlertSendLog.getRuleLevelName()+")??? "+ruleAlertSendLog.getRuleAlertSendLogRuleData().size()+"??? ??????????????????.");
            StringBuffer content = new StringBuffer("-------------------------------------------------"+"\n");
            StringBuffer contentSummary = new StringBuffer(title);
            for(RuleAlertSendLogRuleData ruleAlertSendLogRuleData : ruleAlertSendLog.getRuleAlertSendLogRuleData() ) {
                content.append(" ?????? ??? :"+ruleAlertSendLogRuleData.getName()+"\n");
                content.append(" ?????? ??? :"+ruleAlertSendLogRuleData.getClassNames()+"\n");
                content.append(" ?????? : " + ruleAlertSendLogRuleData.getPattern()+"\n");
                content.append(" ?????? ????????? : " + ruleAlertSendLogRuleData.getPatternData()+"\n");
                content.append(" ?????? ?????? ?????? : "+ruleAlertSendLogRuleData.getTimeCurrent()+"\n");
                content.append("-------------------------------------------------"+"\n");
            }
            ruleAlertSendLog.setSendTitle(title.toString());
            ruleAlertSendLog.setSendContentSummary(title.toString());
            ruleAlertSendLog.setSendContent(content.toString());
            ruleAlertSendLog.setTimeCurrent(DateTimeConvertor.getTimeRegistered());
        }

        return ruleAlertSendLogs;
    }

    /**
     * 2. ????????? ?????? ????????? ??????
     */
    public void sendRuleAlertSendLogs() {
        Enumeration<String> keys = ruleAlertSendLogs.keys();
        while(keys.hasMoreElements()) {
            RuleAlertSendLog ruleAlertSendLog = ruleAlertSendLogs.get(keys.nextElement());
            try {
                if( ruleAlertSendLog.getIsShinhanbankAtopTerminalError() ) {
                    // ??? ????????? ???????????? ???????????? ???????????? ?????? ????????? ?????? createAndSendShinhanbankatopIntegrityLog ??????????????? ?????? ??????.
                } else {
                    if (ruleAlertSendLog.getRuleReceiverIsEmail()) {
                        if (Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)) {
//                            ShinhanbankGoldWing shinhanbankGoldWing = new ShinhanbankGoldWing();
//                            shinhanbankGoldWing.sendMailExchangeServer(ruleAlertSendLog.getRuleReceiverEmail(), ruleAlertSendLog.getSendTitle(), ruleAlertSendLog.getSendContent());
                        } else {
                            Mailer mailer = new Mailer();
                            mailer.sendMail(ruleAlertSendLog.getSendTitle(), ruleAlertSendLog.getSendContent(), ruleAlertSendLog.getRuleReceiverEmail(), "utf-8");
                        }
                        ruleAlertSendLog.setIsEmailSendSucess(true);
                        ruleAlertSendLog.setTimeSend(DateTimeConvertor.getTimeRegistered());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(ruleAlertSendLog.getRuleReceiverIsUrl()) {
                    if (Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)) {
//                        if( ruleAlertSendLog.getIsShinhanbankAtopTerminalError() ) {
//                            // ??? ????????? ???????????? ???????????? ???????????? ?????? ????????? ?????? createAndSendShinhanbankatopIntegrityLog ??????????????? ?????? ??????.
//                        } else {
//                            String requestData="", responseData="";
//                            try {
//                                ShinhanbankGoldWing shinhanbankGoldWing = new ShinhanbankGoldWing();
//                                responseData = shinhanbankGoldWing.sendMessage(ruleAlertSendLog.getRuleReceiverUniqueId(), ruleAlertSendLog.getRuleReceiverSenderId(), ruleAlertSendLog.getRuleReceiverSenderName(), ruleAlertSendLog.getSendContentSummary());
//                                requestData = shinhanbankGoldWing.getRequestData();
//                            }catch(Exception e) {
//                                e.printStackTrace();
//                            }
//                            ruleAlertSendLog.setIsUrlSendSucess(true);
//                        }
                    } else {
                        // ?????? ??????????????? ????????? ????????? ???????????? ???. ??????
                        ruleAlertSendLog.setIsUrlSendSucess(false);
                    }
                    ruleAlertSendLog.setTimeSend(DateTimeConvertor.getTimeRegistered());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ???????????? atop integrity??? ?????? ??????
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) ) {
            initCurrentPolicyGroupErrorCount();
            createRuleAlertSendLogShinhanbankatopIntegrityLog();
            sendRuleAlertSendLogShinhanbankatopIntegrityLog();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    /**
     * 2-1. ???????????? ATOP ????????? ?????? ?????? ??? ?????? (????????????, ????????? ?????????)

     ?????? ????????? ?????? ????????? ?????? ??????
         10?????????            00:00:00 ~ 09:59:59
         11?????????            10:00:00 ~ 10:59:59
         12?????????            11:00:00 ~ 11:59:59
     ?????? ?????? ?????? : isFaultReg(UI)??? true??? ?????? ??????
     ????????? ?????????, ???????????? ?????? ????????? : groupUserErrorCount
        ???????????? ????????? ???????????? ????????? ?????? ?????? (integrity current log ??????)
        result??? ????????? ?????????  ??????
        ?????? ????????? ????????? ?????????, ??????????????? ?????? failCount(?????? ?????? ????????? UI)
     ?????????  : ????????????????????? ???????????????->?????????????????? ??? ????????????->??????????????? ???????????? ??? (groupUserErrorCount<=failCount)
        netClinet5 :
        userId : ?????????
        phoneNo : ????????????
        content : ?????? [?????????????????? ????????? (????????????)] ruleName(ruleId), ......
     ????????? : ??????????????? 1????????? ??????, ????????? ?????? ??? ??????, ??????????????? (groupUserErrorCount>failCount)
        uniqueId
        senderId
        senderName
        content
        [????????? ?????????] ruleName(?????????)
        [????????? ?????????] ruleName(?????????)
     ????????? ?????????
     ????????? ??? current????????? ?????? ??? ??????
     ruleAlertLog -->> ????????? ?????? ??????
     ruleAlertLogDaily -->> ????????????, ????????? ??????     key??? group user ????????? ???????????? ??????
     ruleAlertSendLogDaily -->> ???????????? ????????? ?????? ??????     ?????? ?????? ?????? ruleAlertLogDaily??? 1:1
     */

    private Hashtable<String,Integer> currentPolicyGroupErrorCount = null;
    /**
     * 2-0 ?????? ?????? ???????????? ???????????? ????????? ?????? ?????? ??? group??? total ???????????? ?????????.
     *     ?????? ?????? ?????? ?????? ????????? ?????? ???. (?????? ?????? + ?????? ??????)
     */
    private void initCurrentPolicyGroupErrorCount() {

        currentPolicyGroupErrorCount = new Hashtable<String, Integer>();

        Hashtable<String, RuleAlertLog> todayLastRuleAlertLogs = new Hashtable<>();
        // ?????? ?????????
        for(RuleAlertLog newRuleAlertLog : ruleAlertLogs) {
            RuleAlertLog orgRuleAlertLog = todayLastRuleAlertLogs.get(newRuleAlertLog.getDeviceId());
            if(orgRuleAlertLog==null) {
                todayLastRuleAlertLogs.put(newRuleAlertLog.getDeviceId(),newRuleAlertLog);
            } else {
                Date newDate = newRuleAlertLog.getTimeCurrent();
                Date orgDate = orgRuleAlertLog.getTimeCurrent();
                if( orgDate.compareTo(newDate)!=1 ) {
                    todayLastRuleAlertLogs.put(newRuleAlertLog.getDeviceId(),newRuleAlertLog);
                }
            }
        }
        // ?????? ???????????? ????????????
        try {
            String indexName = DateTimeConvertor.getIndexName(RuleAlertLog.class.getSimpleName().toLowerCase() + "daily", DateTimeConvertor.getTimeRegistered());
            DynamicIndexBean.setIndexName(indexName);
            ruleAlertLogRepository.setIndexName(indexName);
            Iterable<RuleAlertLog> findTodayAllRuleAlertLogs = ruleAlertLogRepository.findAll(); // distinct??? ???????????? ?????? ??? ???????????? ??????
            for (RuleAlertLog newRuleAlertLog : findTodayAllRuleAlertLogs) {
                RuleAlertLog orgRuleAlertLog = todayLastRuleAlertLogs.get(newRuleAlertLog.getDeviceId());
                if(orgRuleAlertLog==null) {
                    todayLastRuleAlertLogs.put(newRuleAlertLog.getDeviceId(),newRuleAlertLog);
                } else {
                    Date newDate = newRuleAlertLog.getTimeCurrent();
                    Date orgDate = orgRuleAlertLog.getTimeCurrent();
                    if( orgDate.compareTo(newDate)!=1 ) {
                        todayLastRuleAlertLogs.put(newRuleAlertLog.getDeviceId(),newRuleAlertLog);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Enumeration<String> deviceIds = todayLastRuleAlertLogs.keys();
        while (deviceIds.hasMoreElements()) {
            String deviceId = deviceIds.nextElement();
            RuleAlertLog ruleAlertLog = todayLastRuleAlertLogs.get(deviceId);
            if(ruleAlertLog==null) {
            } else {
                for(RuleAlertLogShinhanbankAtopTerminalError ruleAlertLogShinhanbankAtopTerminalError : ruleAlertLog.getRuleAlertLogShinhanbankAtopTerminalError()) {
                    if (ruleAlertLogShinhanbankAtopTerminalError.getFailCount()>0) {
                        Object object = currentPolicyGroupErrorCount.get(ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName());
                        if (object == null) {
                            currentPolicyGroupErrorCount.put(ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName(), 1);
                        } else {
                            Integer totalCount = (Integer) object;
                            totalCount++;
                            currentPolicyGroupErrorCount.put(ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName(), totalCount);
                        }
                    } else {
                    }
                }
            }
        }

        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "////////////////////////////////////////////////");
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "policyGroupName totalCount");
        Enumeration<String> keys = currentPolicyGroupErrorCount.keys();
        while (keys.hasMoreElements()) {
            String policyGroupName = keys.nextElement();
            Integer totalCount = (Integer) currentPolicyGroupErrorCount.get(policyGroupName);
            JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "" + policyGroupName + "\t" + totalCount);
        }
    }

    /**
     *
     */
    private void createRuleAlertSendLogShinhanbankatopIntegrityLog() {

        for(RuleAlertLog ruleAlertLog : ruleAlertLogs) {
            try {

                // ??????1. ????????? ????????? ?????? ??????.
                if( ruleAlertLog.getClassNames().equals(IntegrityLog.class.getSimpleName()) ) {

                    // ??????2. 10??? 11??? 12??? ?????? ?????????.
                    boolean isSendHour = false;
                    int currentHour = DateUtil.getHour();
                    if(currentHour==10||currentHour==11||currentHour==12)
                        isSendHour = true;
                    if(isSendHour) {

                        // ??????3. ?????? ?????? ?????? ??? ?????? ??? ????????? ??????
                        DynamicIndexBean.setIndexName(IntegrityLog.class.getSimpleName().toLowerCase());
                        integrityLogRepository.setIndexName(IntegrityLog.class.getSimpleName().toLowerCase());
                        IntegrityLog integrityLog = integrityLogRepository.findByDeviceId(ruleAlertLog.getDeviceId());
                        if(integrityLog==null) {
                        } else {
                            if ( integrityLog.getResult()==100 || integrityLog.getResult()==200 || integrityLog.getResult()==300) {
                                ruleAlertLog.setIsAutoFix(true);
                                ruleAlertLog.setTimeAutoFix(DateTimeConvertor.changeElasticsearchDate(-9, integrityLog.getTimeCurrent()));
                            } else {
                            }
                        }
                        if( ruleAlertLog.getIsAutoFix() ) {
                        } else {
                            for (RuleAlertLogShinhanbankAtopTerminalError ruleAlertLogShinhanbankAtopTerminalError : ruleAlertLog.getRuleAlertLogShinhanbankAtopTerminalError()) {

                                // ??????4. ????????????, ?????? ?????????,  ??????????? ??????????? ?????? ????????? ????????? ????????? ?????????.
                                try {
                                    String indexName = DateTimeConvertor.getIndexName(RuleAlertSendLog.class.getSimpleName().toLowerCase() + "daily", DateTimeConvertor.changeElasticsearchDate(-9,ruleAlertLog.getTimeRegistered()));
                                    DynamicIndexBean.setIndexName(indexName);
                                    ruleAlertSendLogRepository.setIndexName(indexName);
                                    List<RuleAlertSendLog> todayAlreadySentRuleAlertSendLogs = ruleAlertSendLogRepository.findByDeviceIdAndIntegrityPolicyGroupName(ruleAlertLog.getDeviceId(),ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName());
                                    if(todayAlreadySentRuleAlertSendLogs.size()>0) {
                                        ruleAlertLogShinhanbankAtopTerminalError.setIsTodayAlreadySentRuleAlertSendLog(true);
                                        ruleAlertLogShinhanbankAtopTerminalError.setTimeTodayAlreadySentRuleAlertSendLog(DateTimeConvertor.changeElasticsearchDate(-9,todayAlreadySentRuleAlertSendLogs.get(0).getTimeCurrent()));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace(); // ???????????? ?????? ?????? exception??? ????????? ??? ??????.
                                }
                                if (ruleAlertLogShinhanbankAtopTerminalError.getIsTodayAlreadySentRuleAlertSendLog()) {
                                } else {

                                    // ??????5. ????????? ????????? ?????? ????????? ???????????????????????????, ?????? ????????? ???????????? ?????????.
                                    Object obj = currentPolicyGroupErrorCount.get(ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName());
                                    Integer groupErrorCount = 0;
                                    if(obj==null) {
                                        groupErrorCount = 0;
                                    } else {
                                        groupErrorCount = (Integer) obj;
                                    }
                                    ruleAlertLogShinhanbankAtopTerminalError.setIntegrityPolicyGroupTodayTotalErrorCount(groupErrorCount);
                                    if(groupErrorCount < ruleAlertLogShinhanbankAtopTerminalError.getSendErrCnt()) { // group error??? ?????????.. ?????? ?????? ?????????
                                        try {
                                            String key = ruleAlertLog.getDeviceId() + "_" + ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName() + "_2011";

                                            RuleAlertSendLog ruleAlertSendLog = new RuleAlertSendLog();

                                            ruleAlertSendLog.setKey(key);
                                            ruleAlertSendLog.setParentIds(ruleAlertLog.getDocumentId());
                                            ruleAlertSendLog.setRuleReceiverId(2011L);

                                            ruleAlertSendLog.setIntegrityPolicyGroupName(ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName());
                                            ruleAlertSendLog.setIntegrityPolicyGroupDescription(ruleAlertLogShinhanbankAtopTerminalError.getDescription());
                                            ruleAlertSendLog.setSendErrCnt(ruleAlertLogShinhanbankAtopTerminalError.getSendErrCnt());
                                            ruleAlertSendLog.setFailCount(ruleAlertLogShinhanbankAtopTerminalError.getFailCount());
                                            ruleAlertSendLog.setFailIds(ruleAlertLogShinhanbankAtopTerminalError.getFailIds());
                                            ruleAlertSendLog.setFailNames(ruleAlertLogShinhanbankAtopTerminalError.getFailNames());
                                            ruleAlertSendLog.setFailDescriptions(ruleAlertLogShinhanbankAtopTerminalError.getFailDescriptions());
                                            ruleAlertSendLog.setIntegrityPolicyGroupTodayTotalErrorCount(groupErrorCount);
                                            ruleAlertSendLog.setIsTargetTerminalErrorSystem(true);
                                            ruleAlertSendLog.setIntegrityResult(ruleAlertLogShinhanbankAtopTerminalError.getResult());
                                            ruleAlertSendLog.setIsTargetHome(ruleAlertLogShinhanbankAtopTerminalError.getIsTargetHome());
                                            ruleAlertSendLog.setPcType(ruleAlertLogShinhanbankAtopTerminalError.getPcType());
                                            ruleAlertSendLog.setNetClient5(ruleAlertLogShinhanbankAtopTerminalError.getNetClient5());
                                            ruleAlertSendLog.setRemoteCode(ruleAlertLogShinhanbankAtopTerminalError.getRemoteCode());
                                            ruleAlertSendLog.setFaultReg(ruleAlertLogShinhanbankAtopTerminalError.getFaultReg());
                                            ruleAlertSendLog.setIsTodayAlreadySentRuleAlertSendLog(ruleAlertLogShinhanbankAtopTerminalError.getIsTodayAlreadySentRuleAlertSendLog());
                                            ruleAlertSendLog.setTimeTodayAlreadySentRuleAlertSendLog(ruleAlertLogShinhanbankAtopTerminalError.getTimeTodayAlreadySentRuleAlertSendLog());

                                            ruleAlertSendLog.setService(ruleAlertLog.getService());
                                            ruleAlertSendLog.setAppId(ruleAlertLog.getAppId());
                                            ruleAlertSendLog.setOsType(ruleAlertLog.getOsType());
                                            ruleAlertSendLog.setSource(ruleAlertLog.getSource());
                                            ruleAlertSendLog.setDeviceId(ruleAlertLog.getDeviceId());
                                            ruleAlertSendLog.setIp(ruleAlertLog.getIp());
                                            ruleAlertSendLog.setHostName(ruleAlertLog.getHostName());

                                            ruleAlertSendLog.setUserId(ruleAlertLog.getUserId());
                                            ruleAlertSendLog.setTermNo(ruleAlertLog.getTermNo());
                                            ruleAlertSendLog.setSsoBrNo(ruleAlertLog.getSsoBrNo());
                                            ruleAlertSendLog.setBrNo(ruleAlertLog.getBrNo());
                                            ruleAlertSendLog.setHwnNo(ruleAlertLog.getHwnNo());
                                            ruleAlertSendLog.setDeptName(ruleAlertLog.getDeptName());
                                            ruleAlertSendLog.setUserName(ruleAlertLog.getUserName());
                                            ruleAlertSendLog.setSsoType(ruleAlertLog.getSsoType());
                                            ruleAlertSendLog.setPcName(ruleAlertLog.getPcName());
                                            ruleAlertSendLog.setPhoneNo(ruleAlertLog.getPhoneNo());
                                            ruleAlertSendLog.setJKGP(ruleAlertLog.getJKGP());
                                            ruleAlertSendLog.setJKWI(ruleAlertLog.getJKWI());
                                            ruleAlertSendLog.setMaxAddress(ruleAlertLog.getMaxAddress());
                                            ruleAlertSendLog.setFirstWork(ruleAlertLog.getFirstWork());

                                            ruleAlertSendLog.setRuleReceiverName("???????????????????????????");
                                            ruleAlertSendLog.setRuleReceiverUniqueId("0");
                                            ruleAlertSendLog.setRuleReceiverIsEmail(false);
                                            ruleAlertSendLog.setRuleReceiverEmail("");
                                            ruleAlertSendLog.setRuleReceiverIsUrl(false);
                                            ruleAlertSendLog.setRuleReceiverSenderId("ATOP_SYSTEM_ID");
                                            ruleAlertSendLog.setRuleReceiverSenderName("ATOP_SYSTEM");
                                            ruleAlertSendLog.setRuleLevelName(ruleAlertLog.getRuleLevelName());
                                            ruleAlertSendLog.setSendTitle("????????? ?????? ?????? ?????? ????????? ??????");
                                            ruleAlertSendLog.setSendContentSummary("");
                                            ruleAlertSendLog.setTimeSend(DateTimeConvertor.getTimeRegistered());
                                            ruleAlertSendLog.setIsShinhanbankAtopTerminalError(true);

                                            RuleAlertSendLogRuleData ruleAlertSendLogRuleData = new RuleAlertSendLogRuleData();
                                            ruleAlertSendLogRuleData.setRuleId(ruleAlertLog.getRuleId());
                                            ruleAlertSendLogRuleData.setName(ruleAlertLog.getName());
                                            ruleAlertSendLogRuleData.setDescription(ruleAlertLog.getDescription());
                                            ruleAlertSendLogRuleData.setClassNames(ruleAlertLog.getClassNames());
                                            ruleAlertSendLogRuleData.setDisplayClassNames(ruleAlertLog.getDisplayClassNames());
                                            ruleAlertSendLogRuleData.setFieldNames(ruleAlertLog.getFieldNames());
                                            ruleAlertSendLogRuleData.setDisplayFieldNames(ruleAlertLog.getDisplayFieldNames());
                                            ruleAlertSendLogRuleData.setPattern(ruleAlertLog.getPattern());
                                            ruleAlertSendLogRuleData.setPatternData(ruleAlertLog.getPatternData());
                                            ruleAlertSendLog.getRuleAlertSendLogRuleData().add(ruleAlertSendLogRuleData);

                                            RuleAlertSendLog orgRuleAlertSendLog = currentRuleAlertSendLogsForShinhanbankatop.get(key);
                                            if(orgRuleAlertSendLog==null) {
                                                currentRuleAlertSendLogsForShinhanbankatop.put(key, ruleAlertSendLog);
                                            } else {
                                                Date newDate = ruleAlertLog.getTimeCurrent();
                                                Date orgDate = orgRuleAlertSendLog.getTimeCurrent();
                                                if( orgDate.compareTo(newDate)!=1 ) {
                                                    currentRuleAlertSendLogsForShinhanbankatop.put(key, ruleAlertSendLog);
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else { // group error??? ??????.. ???????????? ??????

                                        // ????????? ?????? ????????????, ???????????????, ???????????????????????? ?????? ??? ??????
                                        for (RuleAlertLogReceiverData ruleAlertLogReceiverData : ruleAlertLog.getRuleAlertLogReceiverData()) {
                                            try {
                                                // userId policyGroupName ruleReceiverId ????????? ???????????? ?????? ?????? ??? ?????? ??? ????????? ???????????? ?????????
                                                String key = ruleAlertLog.getDeviceId() + "_" + ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName() + "_" + ruleAlertLogReceiverData.getRuleReceiverId();

                                                RuleAlertSendLog ruleAlertSendLog = new RuleAlertSendLog();

                                                ruleAlertSendLog.setKey(key);
                                                ruleAlertSendLog.setParentIds(ruleAlertLog.getDocumentId());
                                                ruleAlertSendLog.setRuleReceiverId(ruleAlertLogReceiverData.getRuleReceiverId());

                                                ruleAlertSendLog.setIntegrityPolicyGroupName(ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName());
                                                ruleAlertSendLog.setIntegrityPolicyGroupDescription(ruleAlertLogShinhanbankAtopTerminalError.getDescription());
                                                ruleAlertSendLog.setSendErrCnt(ruleAlertLogShinhanbankAtopTerminalError.getSendErrCnt());
                                                ruleAlertSendLog.setFailCount(ruleAlertLogShinhanbankAtopTerminalError.getFailCount());
                                                ruleAlertSendLog.setFailIds(ruleAlertLogShinhanbankAtopTerminalError.getFailIds());
                                                ruleAlertSendLog.setFailNames(ruleAlertLogShinhanbankAtopTerminalError.getFailNames());
                                                ruleAlertSendLog.setFailDescriptions(ruleAlertLogShinhanbankAtopTerminalError.getFailDescriptions());
                                                ruleAlertSendLog.setIntegrityPolicyGroupTodayTotalErrorCount(groupErrorCount);
                                                ruleAlertSendLog.setIsTargetGolewingMessage(true);
                                                ruleAlertSendLog.setIntegrityResult(ruleAlertLogShinhanbankAtopTerminalError.getResult());
                                                ruleAlertSendLog.setIsTargetHome(ruleAlertLogShinhanbankAtopTerminalError.getIsTargetHome());
                                                ruleAlertSendLog.setPcType(ruleAlertLogShinhanbankAtopTerminalError.getPcType());
                                                ruleAlertSendLog.setNetClient5(ruleAlertLogShinhanbankAtopTerminalError.getNetClient5());
                                                ruleAlertSendLog.setRemoteCode(ruleAlertLogShinhanbankAtopTerminalError.getRemoteCode());
                                                ruleAlertSendLog.setFaultReg(ruleAlertLogShinhanbankAtopTerminalError.getFaultReg());
                                                ruleAlertSendLog.setIsTodayAlreadySentRuleAlertSendLog(ruleAlertLogShinhanbankAtopTerminalError.getIsTodayAlreadySentRuleAlertSendLog());
                                                ruleAlertSendLog.setTimeTodayAlreadySentRuleAlertSendLog(ruleAlertLogShinhanbankAtopTerminalError.getTimeTodayAlreadySentRuleAlertSendLog());

                                                ruleAlertSendLog.setService(ruleAlertLog.getService());
                                                ruleAlertSendLog.setAppId(ruleAlertLog.getAppId());
                                                ruleAlertSendLog.setOsType(ruleAlertLog.getOsType());
                                                ruleAlertSendLog.setSource(ruleAlertLog.getSource());
                                                ruleAlertSendLog.setDeviceId(ruleAlertLog.getDeviceId());
                                                ruleAlertSendLog.setIp(ruleAlertLog.getIp());
                                                ruleAlertSendLog.setHostName(ruleAlertLog.getHostName());

                                                ruleAlertSendLog.setUserId(ruleAlertLog.getUserId());
                                                ruleAlertSendLog.setTermNo(ruleAlertLog.getTermNo());
                                                ruleAlertSendLog.setSsoBrNo(ruleAlertLog.getSsoBrNo());
                                                ruleAlertSendLog.setBrNo(ruleAlertLog.getBrNo());
                                                ruleAlertSendLog.setHwnNo(ruleAlertLog.getHwnNo());
                                                ruleAlertSendLog.setDeptName(ruleAlertLog.getDeptName());
                                                ruleAlertSendLog.setUserName(ruleAlertLog.getUserName());
                                                ruleAlertSendLog.setSsoType(ruleAlertLog.getSsoType());
                                                ruleAlertSendLog.setPcName(ruleAlertLog.getPcName());
                                                ruleAlertSendLog.setPhoneNo(ruleAlertLog.getPhoneNo());
                                                ruleAlertSendLog.setJKGP(ruleAlertLog.getJKGP());
                                                ruleAlertSendLog.setJKWI(ruleAlertLog.getJKWI());
                                                ruleAlertSendLog.setMaxAddress(ruleAlertLog.getMaxAddress());
                                                ruleAlertSendLog.setFirstWork(ruleAlertLog.getFirstWork());

                                                ruleAlertSendLog.setRuleReceiverIdRuleLevelId(key);
                                                ruleAlertSendLog.setRuleReceiverId(ruleAlertLogReceiverData.getRuleReceiverId());
                                                ruleAlertSendLog.setRuleReceiverName(ruleAlertLogReceiverData.getName());
                                                ruleAlertSendLog.setRuleReceiverUniqueId(ruleAlertLogReceiverData.getUniqueId());
                                                ruleAlertSendLog.setRuleReceiverIsEmail(ruleAlertLogReceiverData.getIsEmail());
                                                ruleAlertSendLog.setRuleReceiverEmail(ruleAlertLogReceiverData.getEmail());
                                                ruleAlertSendLog.setRuleReceiverIsUrl(ruleAlertLogReceiverData.getIsUrl());
                                                ruleAlertSendLog.setRuleReceiverSenderId(ruleAlertLogReceiverData.getSenderId());
                                                ruleAlertSendLog.setRuleReceiverSenderName(ruleAlertLogReceiverData.getSenderName());
                                                ruleAlertSendLog.setRuleLevelName(ruleAlertLog.getRuleLevelName());
                                                ruleAlertSendLog.setSendTitle("????????? ?????? ????????? ??????");
                                                ruleAlertSendLog.setSendContentSummary("");
                                                ruleAlertSendLog.setTimeSend(DateTimeConvertor.getTimeRegistered());
                                                ruleAlertSendLog.setIsShinhanbankAtopTerminalError(true);

                                                RuleAlertSendLogRuleData ruleAlertSendLogRuleData = new RuleAlertSendLogRuleData();
                                                ruleAlertSendLogRuleData.setRuleId(ruleAlertLog.getRuleId());
                                                ruleAlertSendLogRuleData.setName(ruleAlertLog.getName());
                                                ruleAlertSendLogRuleData.setDescription(ruleAlertLog.getDescription());
                                                ruleAlertSendLogRuleData.setClassNames(ruleAlertLog.getClassNames());
                                                ruleAlertSendLogRuleData.setDisplayClassNames(ruleAlertLog.getDisplayClassNames());
                                                ruleAlertSendLogRuleData.setFieldNames(ruleAlertLog.getFieldNames());
                                                ruleAlertSendLogRuleData.setDisplayFieldNames(ruleAlertLog.getDisplayFieldNames());
                                                ruleAlertSendLogRuleData.setPattern(ruleAlertLog.getPattern());
                                                ruleAlertSendLogRuleData.setPatternData(ruleAlertLog.getPatternData());
                                                ruleAlertSendLog.getRuleAlertSendLogRuleData().add(ruleAlertSendLogRuleData);

                                                RuleAlertSendLog orgRuleAlertSendLog = currentRuleAlertSendLogsForShinhanbankatop.get(key);
                                                if(orgRuleAlertSendLog==null) {
                                                    currentRuleAlertSendLogsForShinhanbankatop.put(key, ruleAlertSendLog);
                                                } else {
                                                    Date newDate = ruleAlertLog.getTimeCurrent();
                                                    Date orgDate = orgRuleAlertSendLog.getTimeCurrent();
                                                    if( orgDate.compareTo(newDate)!=1 ) {
                                                        currentRuleAlertSendLogsForShinhanbankatop.put(key, ruleAlertSendLog);
                                                    }
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }// isAutoFix
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 2-2. ??????
     *      ????????????????????? :????????????, ?????????????????? ??????
     *      ????????? : ??????????????? ?????? (????????? ??????)
     */
    private void sendRuleAlertSendLogShinhanbankatopIntegrityLog() {

        StringBuffer policyGroupHeaderCountInfo = new StringBuffer();
        Enumeration<String> keys = currentPolicyGroupErrorCount.keys();
        while (keys.hasMoreElements()) {
            try {
                String policyGroupName = keys.nextElement();
                Integer policyGroupTotalCount = currentPolicyGroupErrorCount.get(policyGroupName);
                policyGroupHeaderCountInfo.append("[" + policyGroupName + " : " + policyGroupTotalCount + "???] \n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ???????????? ????????? ?????? ??????
        Hashtable<Long, RuleAlertLogReceiverData> goldWingSendRuleAlertLogReceiverData = new Hashtable<Long, RuleAlertLogReceiverData>();
        // ???????????? ????????? ????????? ??????
        Hashtable<Long,Hashtable>  shinhanbankAtopSwingSendGroupAndCount = new Hashtable<Long,Hashtable>();
        keys = currentRuleAlertSendLogsForShinhanbankatop.keys();
        while (keys.hasMoreElements()) {
            try {
                RuleAlertSendLog ruleAlertSendLog = currentRuleAlertSendLogsForShinhanbankatop.get(keys.nextElement());
                StringBuffer ruleNameDescription = new StringBuffer();
                // ?????? ?????? ?????? ?????????
                if (ruleAlertSendLog.getIsTargetTerminalErrorSystem()) {
                    ruleNameDescription.append("[" + ruleAlertSendLog.getIntegrityPolicyGroupName() + " " + ruleAlertSendLog.getFailIds().size() + "???] \n"+ruleAlertSendLog.getIntegrityPolicyGroupDescription()+"\n");
                    for (String name : ruleAlertSendLog.getFailNames())
                        ruleNameDescription.append(name+", ");
                    ruleAlertSendLog.setSendContent(ruleNameDescription.toString());

                    // ?????? ?????? ????????? ??????
                    String requestData = "", responseData = "";
                    try {
//                        ShinhanbankTerminalErrorAlertSystem shinhanbankTerminalErrorAlertSystem = new ShinhanbankTerminalErrorAlertSystem();  // netClient5???   remoteCode ????--d????????????
//                        requestData = ruleNameDescription.toString();
//                        responseData = shinhanbankTerminalErrorAlertSystem.sendIntegrityAlert(ruleAlertSendLog.getNetClient5(), ruleAlertSendLog.getUserId(), ruleAlertSendLog.getPhoneNo(), ruleNameDescription.toString());
//                        requestData = shinhanbankTerminalErrorAlertSystem.getRequestData();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ruleAlertSendLog.setIsSendException(true);
                    }
                    ruleAlertSendLog.setTimeSend(DateTimeConvertor.getTimeRegistered());
                    ruleAlertSendLog.setSendContent(requestData);
                    ruleAlertSendLog.setResponseData(responseData);

// ???????????? ruleAlertLog??? ????????? ???. ??????????????????????????????????????/
//                    for(RuleAlertLog ruleAlertLog : ruleAlertLogs) {
//                        ruleAlertLog.setIsSendTerminalErrorSystem(true);
//                        ruleAlertLog.setTimeSendTerminalErrorSystem(DateTimeConvertor.getTimeRegistered());
//                    }

                // ????????? ?????????
                } else if (ruleAlertSendLog.getIsTargetGolewingMessage()) {

                    RuleAlertLogReceiverData ruleAlertLogReceiverData = goldWingSendRuleAlertLogReceiverData.get(ruleAlertSendLog.getRuleReceiverId());
                    if (ruleAlertLogReceiverData == null) {
                        ruleAlertLogReceiverData = new RuleAlertLogReceiverData();
                        ruleAlertLogReceiverData.setRuleReceiverId(ruleAlertSendLog.getRuleReceiverId());
                        ruleAlertLogReceiverData.setName(ruleAlertSendLog.getRuleReceiverName());
                        ruleAlertLogReceiverData.setUniqueId(ruleAlertSendLog.getRuleReceiverUniqueId());
                        ruleAlertLogReceiverData.setIsEmail(ruleAlertSendLog.getRuleReceiverIsEmail());
                        ruleAlertLogReceiverData.setEmail(ruleAlertSendLog.getRuleReceiverEmail());
                        ruleAlertLogReceiverData.setIsUrl(ruleAlertSendLog.getRuleReceiverIsUrl());
                        ruleAlertLogReceiverData.setSenderId(ruleAlertSendLog.getRuleReceiverSenderId());
                        ruleAlertLogReceiverData.setSenderName(ruleAlertSendLog.getRuleReceiverSenderName());
                    }
                    StringBuffer content = new StringBuffer();
                    content.append("[ ?????????:"+ruleAlertSendLog.getIntegrityPolicyGroupName() + " ??????:" + ruleAlertSendLog.getIntegrityPolicyGroupTodayTotalErrorCount()+"]\n");
                    ruleAlertSendLog.setSendTitle("");
                    ruleAlertSendLog.setSendContent(policyGroupHeaderCountInfo.toString()+"\n\n"+content.toString());
                    ruleAlertSendLog.setResponseData("");
                    ruleAlertSendLog.setTimeSend(DateTimeConvertor.getTimeRegistered());

                    ruleAlertLogReceiverData.setContent(ruleAlertLogReceiverData.getContent() + content + "\n"); // pc??? ????????? ????????? ?????? ???????????? ?????????. ????????????????
                    goldWingSendRuleAlertLogReceiverData.put(ruleAlertSendLog.getRuleReceiverId(), ruleAlertLogReceiverData);

                    Hashtable<String,String> groupCountInfo = shinhanbankAtopSwingSendGroupAndCount.get(ruleAlertSendLog.getRuleReceiverId());
                    if(groupCountInfo==null) {
                        groupCountInfo = new Hashtable<String,String>();
                        groupCountInfo.put(ruleAlertSendLog.getIntegrityPolicyGroupName(),"[?????????:"+ruleAlertSendLog.getIntegrityPolicyGroupName()+" ??????:"+ruleAlertSendLog.getIntegrityPolicyGroupTodayTotalErrorCount()+"]+\n");
                        shinhanbankAtopSwingSendGroupAndCount.put(ruleAlertSendLog.getRuleReceiverId(),groupCountInfo);
                    } else {
                        String sendContent = groupCountInfo.get(ruleAlertSendLog.getIntegrityPolicyGroupName());
                        sendContent = "[?????????:"+ruleAlertSendLog.getIntegrityPolicyGroupName()+" ??????:"+ruleAlertSendLog.getIntegrityPolicyGroupTodayTotalErrorCount()+"]+\n";
                        groupCountInfo.put(ruleAlertSendLog.getIntegrityPolicyGroupName(),sendContent);
                        shinhanbankAtopSwingSendGroupAndCount.put(ruleAlertSendLog.getRuleReceiverId(),groupCountInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ????????? ?????????
        Enumeration<Long> keys2 = goldWingSendRuleAlertLogReceiverData.keys();
        while (keys2.hasMoreElements()) {
            try {
                Long ruleReceiverId = keys2.nextElement();
                RuleAlertLogReceiverData ruleAlertLogReceiverData = goldWingSendRuleAlertLogReceiverData.get(ruleReceiverId);
                String requestData = "", responseData = "";
                try {
                    Hashtable<String,String> groupCountInfo = shinhanbankAtopSwingSendGroupAndCount.get(ruleReceiverId);
                    Enumeration<String> key3 = groupCountInfo.keys();
                    while (key3.hasMoreElements()) {
                        try {
                            String policyGroupName = key3.nextElement();
                            String policyGroupNameCount = groupCountInfo.get(policyGroupName);
                            requestData = requestData + policyGroupNameCount;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    ShinhanbankGoldWing shinhanbankGoldWing = new ShinhanbankGoldWing();
//                    responseData = shinhanbankGoldWing.sendMessage(ruleAlertLogReceiverData.getUniqueId(), ruleAlertLogReceiverData.getSenderId(), ruleAlertLogReceiverData.getSenderName(), requestData);
//                    requestData = shinhanbankGoldWing.getRequestData();

// ???????????? ruleAlertSendLog??? ????????? ???. ??????????????????????????????????????/
//                ruleAlertSendLog.setSendContent(requestData);
//                ruleAlertSendLog.setResponseData(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                }

// ???????????? ruleAlertLog??? ????????? ???. ??????????????????????????????????????/
//                    for(RuleAlertLog ruleAlertLog : ruleAlertLogs) {
//                        ruleAlertLog.setIsSendMessage(true);
//                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 3. ?????? ?????? ??????
     */
    public void saveRuleAlertSendLog() {

        // daily save
        List<Document2> document2s = new ArrayList<>();
        HashSet<String> indexNames = new HashSet<>();

        //
        saveRuleAlertSendLogs = new ArrayList<>();

        Enumeration<String> keys = ruleAlertSendLogs.keys();
        while(keys.hasMoreElements()) {
            RuleAlertSendLog ruleAlertSendLog = ruleAlertSendLogs.get(keys.nextElement());
            ruleAlertSendLog.setIndexName(RuleAlertSendLog.class.getSimpleName().toLowerCase());
            ruleAlertSendLog.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
            String indexName = DateTimeConvertor.getIndexName(RuleAlertSendLog.class.getSimpleName().toLowerCase()+"daily", ruleAlertSendLog.getTimeRegistered());
            indexNames.add(indexName);
            ruleAlertSendLog.setIndexName(indexName);
            document2s.add(ruleAlertSendLog);
            saveRuleAlertSendLogs.add(ruleAlertSendLog);
        }

        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "////////////////////////////////////////////////");
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "indexName\tdocumentId\tdeviceId\thwpNo\ttimeCurrent\ttimeRegistered\tpolicyGroupName\tpolicyGroupTodayErrorCount\tfailCount\tsendErrCnt\tisTargetTerminalErrorSystem\tisTargetGolewingMessage");
        if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) ) {
            keys = currentRuleAlertSendLogsForShinhanbankatop.keys();
            while(keys.hasMoreElements()) {
                try{
                    RuleAlertSendLog ruleAlertSendLog = currentRuleAlertSendLogsForShinhanbankatop.get(keys.nextElement());
                    ruleAlertSendLog.setIndexName(RuleAlertSendLog.class.getSimpleName().toLowerCase());
                    ruleAlertSendLog.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
                    ruleAlertSendLog.setTimeCurrent(DateTimeConvertor.getTimeRegistered());
                    String indexName = DateTimeConvertor.getIndexName(RuleAlertSendLog.class.getSimpleName().toLowerCase()+"daily", ruleAlertSendLog.getTimeRegistered());
                    indexNames.add(indexName);
                    ruleAlertSendLog.setIndexName(indexName);
                    document2s.add(ruleAlertSendLog);

                    try{
                        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),
                                            ruleAlertSendLog.getIndexName()+"\t"+
                                                    ruleAlertSendLog.getDocumentId()+"\t"+
                                                    ruleAlertSendLog.getDeviceId()+"\t"+
                                                    ruleAlertSendLog.getHwnNo()+"\t"+
                                                    DateUtil.getDate(ruleAlertSendLog.getTimeCurrent(),Constants.TAG_DATE_PATTERN_OF_ADMIN_DISPLAY)+"\t"+
                                                    DateUtil.getDate(ruleAlertSendLog.getTimeRegistered(),Constants.TAG_DATE_PATTERN_OF_ADMIN_DISPLAY)+"\t"+
                                                    ruleAlertSendLog.getIntegrityPolicyGroupName()+"\t"+
                                                    ruleAlertSendLog.getIntegrityPolicyGroupTodayTotalErrorCount()+"\t"+
                                                    ruleAlertSendLog.getFailCount()+"\t"+
                                                    ruleAlertSendLog.getSendErrCnt()+"\t"+
                                                    ruleAlertSendLog.getIsTargetTerminalErrorSystem()+"\t"+
                                                    ruleAlertSendLog.getIsTargetGolewingMessage()+"\t"
                        );
                    } catch (Exception e) { e.printStackTrace(); }

                    saveRuleAlertSendLogs.add(ruleAlertSendLog);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(saveRuleAlertSendLogs.size()>0) {
            try {
                ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);
                BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
                if (bulkResponse.hasFailures())
                    logger.error(bulkResponse.buildFailureMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 4. ?????? ????????? ?????? ?????? ?????? ?????? ?????? ??????
     */
    public boolean completeRuleAlertLog() {

        boolean isExistSendData = false;

        // daily save
        List<Document2> document2s = new ArrayList<>();
        HashSet<String> indexNames = new HashSet<>();

        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "////////////////////////////////////////////////");
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "indexName\tdocumentId\tdeviceId\thwpNo\ttimeCurrent\ttimeRegistered\tpolicyGroupName\tpolicyGroupTodayErrorCount\tfailCount\tsendErrCnt\tisTargetTerminalErrorSystem\tisTargetGolewingMessage");

        // current delete
        for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
            boolean isMoveDaily = false;
            if (Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) &&
                ruleAlertLog.getIsShinhanbankAtopTerminalError() ) {
                // ???????????? 10??? 11??? 12????????? ??????
                int currentHour = DateUtil.getHour();
                if(currentHour>=0 && currentHour<=9) { // 0~9 ?????? ???????????? ?????????.
                    isMoveDaily = false;
                } else if(currentHour==10||currentHour==11||currentHour==12) { // 10 11 12 ?????? ???????????? ?????? ?????????. saveRuleAlertSendLogs ??? ????????? ??????
                    isMoveDaily = true;
                } else { // 12~23 ????????? ??????????????? ???????????? ?????? ????????? saveRuleAlertSendLogs ??? ????????? ??????
                    isMoveDaily = true;
                }
            } else {
                isMoveDaily = true;
            }

            JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "completeRuleAlertLog isMoveDaily->"+isMoveDaily);
            if(isMoveDaily) {
                String ruleAlertSendLogIndexName="";
                for (RuleAlertSendLog ruleAlertSendLog : saveRuleAlertSendLogs) {
                    if (ruleAlertSendLog.getParentIds().indexOf(ruleAlertLog.getDocumentId()) != -1) {
                        ruleAlertLog.getRuleAlertSendLogIds().add(ruleAlertSendLog.getDocumentId());
                        ruleAlertLog.setChildIndexName(ruleAlertSendLog.getIndexName());
                        if(ruleAlertSendLogIndexName.equals("") && !ruleAlertSendLog.getIndexName().equals("") )
                            ruleAlertSendLogIndexName=ruleAlertSendLog.getIndexName();
                    }
                }
                ruleAlertLog.setTimeCheck(DateTimeConvertor.getTimeRegistered());
                ruleAlertLog.setIsCheck(true);

                // elastic?????? ?????? ??? ??????????????? 9????????? ?????????...
                /////////////////////////////////////////////////////////////////////////////////
                ruleAlertLog.setTimeCurrent(DateTimeConvertor.changeElasticsearchDate(-9,ruleAlertLog.getTimeCurrent()));
                ruleAlertLog.setTimeCreated(DateTimeConvertor.changeElasticsearchDate(-9,ruleAlertLog.getTimeCreated()));
                ruleAlertLog.setUpdateDate(DateTimeConvertor.changeElasticsearchDate(-9,ruleAlertLog.getUpdateDate()));
                ruleAlertLog.setTimeRegistered(DateTimeConvertor.changeElasticsearchDate(-9,ruleAlertLog.getTimeRegistered()));

                ruleAlertLog.setTimeSendTerminalErrorSystem(DateTimeConvertor.changeElasticsearchDate(-9,ruleAlertLog.getTimeSendTerminalErrorSystem())); // null

                ruleAlertLog.setTimeRegisteredDaily(DateTimeConvertor.getTimeRegistered()); // ok
                /////////////////////////////////////////////////////////////////////////////////

                // elasticsearch --> log --> elasticsearch ?????? date????????? ??????.
                // String indexName = DateTimeConvertor.getIndexName(RuleAlertLog.class.getSimpleName().toLowerCase() + "daily", DateTimeConvertor.changeElasticsearchDate(-9,ruleAlertLog.getTimeCurrent()));
                String indexName = DateTimeConvertor.getIndexName2(RuleAlertLog.class.getSimpleName().toLowerCase()+"daily",DateUtil.getDate(ruleAlertLog.getTimeCurrent(),Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS));
                indexNames.add(indexName);
                ruleAlertLog.setIndexName(indexName);
                if(ruleAlertLog.getChildIndexName()==null||ruleAlertLog.getChildIndexName().equals("")) {
                    if(ruleAlertSendLogIndexName.equals("")) {
                        String tempIndexName = DateTimeConvertor.getIndexName2(RuleAlertSendLog.class.getSimpleName().toLowerCase()+"daily",DateUtil.getDate(new Date(),Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS));
                        ruleAlertLog.setChildIndexName(tempIndexName);
                    } else {
                        ruleAlertLog.setChildIndexName(ruleAlertSendLogIndexName);
                    }
                }
                if(ruleAlertLog.getIsAutoFix())
                    ;
                else
                    document2s.add(ruleAlertLog);
                try{
                    for (RuleAlertLogShinhanbankAtopTerminalError ruleAlertLogShinhanbankAtopTerminalError : ruleAlertLog.getRuleAlertLogShinhanbankAtopTerminalError()) {
                        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),
                                ruleAlertLog.getIndexName()+"\t"+
                                        ruleAlertLog.getDocumentId()+"\t"+
                                        ruleAlertLog.getDeviceId()+"\t"+
                                        ruleAlertLog.getHwnNo()+"\t"+
                                        DateUtil.getDate(ruleAlertLog.getTimeCurrent(),Constants.TAG_DATE_PATTERN_OF_ADMIN_DISPLAY)+"\t"+
                                        DateUtil.getDate(ruleAlertLog.getTimeRegistered(),Constants.TAG_DATE_PATTERN_OF_ADMIN_DISPLAY)+"\t"+
                                        ruleAlertLogShinhanbankAtopTerminalError.getInterityPolicyGroupName()+"\t"+
                                        ruleAlertLogShinhanbankAtopTerminalError.getIntegrityPolicyGroupTodayTotalErrorCount()+"\t"+
                                        ruleAlertLogShinhanbankAtopTerminalError.getFailCount()+"\t"+
                                        ruleAlertLogShinhanbankAtopTerminalError.getSendErrCnt()+"\t"
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                try {
                    // ???????????? ATOP ???????????? ??????????????? 00:00:00~09:55:59??? ?????? ???????????? 10?????? ???????????? ??????.
                    // elastic?????? ?????? ??? ??????????????? 9????????? ?????????...
                    /////////////////////////////////////////////////////////////////////////////////
                    ruleAlertLog.setTimeCurrent(DateTimeConvertor.changeElasticsearchDate(-9, ruleAlertLog.getTimeCurrent()));
                    ruleAlertLog.setTimeCreated(DateTimeConvertor.changeElasticsearchDate(-9, ruleAlertLog.getTimeCreated()));
                    ruleAlertLog.setUpdateDate(DateTimeConvertor.changeElasticsearchDate(-9, ruleAlertLog.getUpdateDate()));
                    ruleAlertLog.setTimeRegistered(DateTimeConvertor.changeElasticsearchDate(-9, ruleAlertLog.getTimeRegistered()));
                    /////////////////////////////////////////////////////////////////////////////////

                    indexNames.add(RuleAlertLog.class.getSimpleName().toLowerCase());
                    ruleAlertLog.setIndexName(RuleAlertLog.class.getSimpleName().toLowerCase());
                    if (ruleAlertLog.getIsAutoFix())
                        ;
                    else
                        document2s.add(ruleAlertLog);
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
//                MessageRuleProcessorManager.getInstance().initExistSendData(ruleAlertLog.getRuleLevelName()); // 1?????? ??? ?????? ?????? (?????? emergency ????????? ????????? ??????...
                    isExistSendData = true;
                }
            }
        }
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "completeRuleAlertLog document2s.size()->"+document2s.size());

        // daily??? ruleAlertLog ??????
        try {
            JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "writeDocument2s document2s.size()->"+document2s.size());
            ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);
            BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
            if (bulkResponse.hasFailures())
                logger.error(bulkResponse.buildFailureMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExistSendData;
    }

}