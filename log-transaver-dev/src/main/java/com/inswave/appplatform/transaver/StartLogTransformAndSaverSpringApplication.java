package com.inswave.appplatform.transaver;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.entity.LogInfomation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.TimeZone;


// dev 환경에서 logmanager 실행 시 주석 해지
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//public class StartLogTransformAndSaverSpringApplication {


//// transaver 실행 시
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//@SpringBootApplication(exclude={net.bull.javamelody.JavaMelodyAutoConfiguration.class,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
//@EntityScan(basePackages={"com.inswave.appplatform"})
//@EnableScheduling
//@Async
//@MonitoredWithSpring
//@Component
//public class StartLogTransformAndSaverSpringApplication extends SpringBootServletInitializer {
//    @Bean
//    public HttpSessionListener javaMelodyListener(){
//        return new SessionListener();
//    }
//    @Bean
//    public Filter javaMelodyFilter(){
//        return new MonitoringFilter();
//    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootApplication(exclude={org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
@EntityScan(basePackages={"com.inswave.appplatform"})
@EnableScheduling
@Async
@Component
public class StartLogTransformAndSaverSpringApplication extends SpringBootServletInitializer {
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Value("${runMode1}")
    private String runMode1;
    @Value("${runMode2}")
    private String runMode2;
    @Value("${runMode3}")
    private String runMode3;

    @Value("${elasticsearch.search.server}")
    private String elasticsearchSearchServer;
    @Value("${elasticsearch.data.server}")
    private String elasticsearchDataServer;
    @Value("${index.number.of.shards}")
    private int indexNumberOfShards;
    @Value("${index.number.of.replicas}")
    private int indexNumberOfReplicas;

    @Value("${index.statistics.value}")
    private String statisticsValue;

    @Value("${javamelody.error.print}")
    private boolean isJavamelodyErrorPrint=true;

    @Value("${save.request.log.data}")
    private boolean isSaveRequestLogData=true;
    @Value("${save.request.log.data.dir}")
    private String saveRequestLogDataDir = "/home/inswave/message/";
    @Value("${log.file.date.pattern}")
    private String logFileDatePattern = "yyyyMMddHH";

    @Value("${save.exception.log.data}")
    private boolean isSaveExceptionLogData=true;
    @Value("${save.exception.log.dir}")
    private String saveExceptionLogDir = "/home/inswave/message/exception/";

    @Value("${index.dailylog.applicationerrorlog.saved.day}")
    private int indexDailylogApplicationErrorLogSavedDay=10;
    @Value("${index.dailylog.clientactiveportlistresourcelog.saved.day}")
    private int indexDailylogClientActiveportListResourceLogSavedDay=10;
    @Value("${index.dailylog.clientcontrolprocessresourcelog.saved.day}")
    private int indexDailylogClientControlProcessResourceLogSavedDay=10;
    @Value("${index.dailylog.clientdefraganalysisresourcelog.saved.day}")
    private int indexDailylogClientDefraganAlysisResourceLogSavedDay=10;
    @Value("${index.dailylog.clienthwinforesourcelog.saved.day}")
    private int indexDailylogClientHwInfoResourceLogSavedDay=10;
    @Value("${index.dailylog.clientmbrresourcelog.saved.day}")
    private int indexDailylogClientMbrResourceLogSavedDay=10;
    @Value("${index.dailylog.clientperformancelog.saved.day}")
    private int indexDailylogClientPerformanceLogSavedDay=10;
    @Value("${index.dailylog.clientprocesscreationlog.saved.day}")
    private int indexDailylogClientProcessCreationLogSavedDay=10;
    @Value("${index.dailylog.clientprocessresourcelog.saved.day}")
    private int indexDailylogClientProcessResourceLogSavedDay=10;
    @Value("${index.dailylog.clientprogramlistresourcelog.saved.day}")
    private int indexDailylogClientProgramListResourceLogSavedDay=10;
    @Value("${index.dailylog.clientusertermmonitorlog.saved.day}")
    private int indexDailylogClientUserTermMonitorLogSavedDay=10;
    @Value("${index.dailylog.clientwindowsupdatelistresourcelog.saved.day}")
    private int indexDailylogClientwindowsUpdateListResourceLogSavedDay=10;
    @Value("${index.dailylog.deviceerrorlog.saved.day}")
    private int indexDailylogDeviceerrorLogSavedDay=10;
    @Value("${index.dailylog.hwerrorlog.saved.day}")
    private int indexDailylogHwErrorLogSavedDay=10;
    @Value("${index.dailylog.integritylog.saved.day}")
    private int indexDailylogIntegrityLogSavedDay=10;
    @Value("${index.dailylog.pconoffeventlog.saved.day}")
    private int indexDailylogPcOnOffEventLogSavedDay=10;
    @Value("${index.dailylog.serverresourcelog.saved.day}")
    private int indexDailylogServerResourceLogSavedDay=10;
    @Value("${index.dailylog.modulemonitoringlog.saved.day}")
    private int indexDailylogModuleMonitoringLogSavedDay=10;
    @Value("${index.dailylog.windowsbluescreenlog.saved.day}")
    private int indexDailylogWindowsBlueScreenLogSavedDay=10;
    @Value("${index.dailylog.windowseventsystemerroralllog.saved.day}")
    private int indexDailylogWindowsEventSystemErrorAllLogSavedDay=10;
    @Value("${index.dailylog.rulealertlog.saved.day}")
    private int indexDailylogRuleAlertLogSavedDay=10;
    @Value("${index.dailylog.rulealertsendlog.saved.day}")
    private int indexDailylogRuleAlertSendLogSavedDay=10;

    @Value("${index.dailylog.applicationerrorlog.index.name}")
    private String indexDailylogApplicationErrorLogIndexName="";
    @Value("${index.dailylog.clientactiveportlistresourcelog.index.name}")
    private String indexDailylogClientActiveportListResourceLogIndexName="";
    @Value("${index.dailylog.clientcontrolprocessresourcelog.index.name}")
    private String indexDailylogClientControlProcessResourceLogIndexName="";
    @Value("${index.dailylog.clientdefraganalysisresourcelog.index.name}")
    private String indexDailylogClientDefraganAlysisResourceLogIndexName="";
    @Value("${index.dailylog.clienthwinforesourcelog.index.name}")
    private String indexDailylogClientHwInfoResourceLogIndexName="";
    @Value("${index.dailylog.clientmbrresourcelog.index.name}")
    private String indexDailylogClientMbrResourceLogIndexName="";
    @Value("${index.dailylog.clientperformancelog.index.name}")
    private String indexDailylogClientPerformanceLogIndexName="";
    @Value("${index.dailylog.clientprocesscreationlog.index.name}")
    private String indexDailylogClientProcessCreationLogIndexName="";
    @Value("${index.dailylog.clientprocessresourcelog.index.name}")
    private String indexDailylogClientProcessResourceLogIndexName="";
    @Value("${index.dailylog.clientprogramlistresourcelog.index.name}")
    private String indexDailylogClientProgramListResourceLogIndexName="";
    @Value("${index.dailylog.clientusertermmonitorlog.index.name}")
    private String indexDailylogClientUserTermMonitorLogIndexName="";
    @Value("${index.dailylog.clientwindowsupdatelistresourcelog.index.name}")
    private String indexDailylogClientwindowsUpdateListResourceLogIndexName="";
    @Value("${index.dailylog.deviceerrorlog.index.name}")
    private String indexDailylogDeviceerrorLogIndexName="";
    @Value("${index.dailylog.hwerrorlog.index.name}")
    private String indexDailylogHwErrorLogIndexName="";
    @Value("${index.dailylog.integritylog.index.name}")
    private String indexDailylogIntegrityLogIndexName="";
    @Value("${index.dailylog.pconoffeventlog.index.name}")
    private String indexDailylogPcOnOffEventLogIndexName="";
    @Value("${index.dailylog.serverresourcelog.index.name}")
    private String indexDailylogServerResourceLogIndexName="";
    @Value("${index.dailylog.modulemonitoringlog.index.name}")
    private String indexDailylogModuleMonitoringLogIndexName="";
    @Value("${index.dailylog.windowsbluescreenlog.index.name}")
    private String indexDailylogWindowsBlueScreenLogIndexName="";
    @Value("${index.dailylog.windowseventsystemerroralllog.index.name}")
    private String indexDailylogWindowsEventSystemErrorAllLogIndexName="";
    @Value("${index.dailylog.rulealertlog.index.name}")
    private String indexDailylogRuleAlertLogIndexName="";
    @Value("${index.dailylog.rulealertsendlog.index.name}")
    private String indexDailylogRuleAlertSendLogIndexName="";

    @Value("${is.use.module.monitoring}")
    private Boolean isUseModuleMonitoring=false;

    @Value("${kafka.clientprocesscreationlogsavelistener.idle.between.polls.ms}")
    private Long clientProcessCreationLogSaveListenerIdleBetweenPollsMs=3000L;
    @Value("${kafka.clientprocesscreationlogsavelistener.max.idle.between.polls.ms}")
    private Long clientProcessCreationLogSaveListenerMaxIdleBetweenPollsMs=3000L;
    @Value("${kafka.clientprocesscreationlogsavelistener.min.idle.between.polls.ms}")
    private Long clientProcessCreationLogSaveListenerMinIdleBetweenPollsMs=0L;
    @Value("${kafka.clientprocesscreationlogsavelistener.decrese.idle.between.polls.ms}")
    private Long clientProcessCreationLogSaveListenerDecreseIdleBetweenPollsMs=100L;

    public static void main(String[] args) {
        SpringApplication.run(StartLogTransformAndSaverSpringApplication.class, args);
    }

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")); // WAS JAVA_OPTS="$JAVA_OPTS -Duser.timezone=Asia/Seoul"
        initSeedData();
    }

    @EventListener
    public void setSeedData(ContextRefreshedEvent event) {
        initSeedData();
    }

    private void initSeedData() {

        Config.getInstance().setRunMode1(runMode1);
        Config.getInstance().setRunMode2(runMode2);
        Config.getInstance().setRunMode3(runMode3);

        Config.getInstance().getLog().setElasticsearchSearchServer(elasticsearchSearchServer);
        Config.getInstance().getLog().setElasticsearchDataServer(elasticsearchDataServer);
        Config.getInstance().getLog().setIndexNumberOfReplicas(indexNumberOfReplicas);
        Config.getInstance().getLog().setIndexNumberOfShards(indexNumberOfShards);

        Config.getInstance().getLog().setStatisticsValue(statisticsValue);

        Config.getInstance().getLog().setJavamelodyErrorPrint(isJavamelodyErrorPrint);
        Config.getInstance().getLog().setSaveRequestLogData(isSaveRequestLogData);
        Config.getInstance().getLog().setSaveRequestLogDataDir(saveRequestLogDataDir);
        Config.getInstance().getLog().setLogFileDatePattern(logFileDatePattern);

        Config.getInstance().getLog().setSaveExceptionLogData(isSaveExceptionLogData);
        Config.getInstance().getLog().setSaveExceptionLogDir(saveExceptionLogDir);

        Config.getInstance().getLog().setClientProcessCreationLogSaveListenerIdleBetweenPollsMs(clientProcessCreationLogSaveListenerIdleBetweenPollsMs);
        Config.getInstance().getLog().setClientProcessCreationLogSaveListenerMaxIdleBetweenPollsMs(clientProcessCreationLogSaveListenerMaxIdleBetweenPollsMs);
        Config.getInstance().getLog().setClientProcessCreationLogSaveListenerMinIdleBetweenPollsMs(clientProcessCreationLogSaveListenerMinIdleBetweenPollsMs);
        Config.getInstance().getLog().setClientProcessCreationLogSaveListenerDecreseIdleBetweenPollsMs(clientProcessCreationLogSaveListenerDecreseIdleBetweenPollsMs);

        Config.getInstance().getLog().setIsUseModuleMonitoring(isUseModuleMonitoring);

        initLogInfomation();

        // 변경될 때마다 생성함.
//        initElasticsearchTemplate();

    }

    private void initElasticsearchTemplate() {
        try {
            Class<?> cls = Class.forName(ConstantsTranSaver.CLASS_OF_ELASTICSEARCH_TEMPLATE_CREATE_SERVICE);
            Constructor<?> constructor = cls.getConstructor();
            Object node = constructor.newInstance();
            Method method = cls.getMethod(Constants.METHOD_OF_INTERNAL_EXCUTE_SERVICE);
            Object object = method.invoke(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLogInfomation() {

        Config.getInstance().getLog().setIndexDailylogApplicationErrorLogSavedDay(indexDailylogApplicationErrorLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientActiveportListResourceLogSavedDay(indexDailylogClientActiveportListResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientControlProcessResourceLogSavedDay(indexDailylogClientControlProcessResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientDefraganAlysisResourceLogSavedDay(indexDailylogClientDefraganAlysisResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientHwInfoResourceLogSavedDay(indexDailylogClientHwInfoResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientMbrResourceLogSavedDay(indexDailylogClientMbrResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientPerformanceLogSavedDay(indexDailylogClientPerformanceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientProcessCreationLogSavedDay(indexDailylogClientProcessCreationLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientProcessResourceLogSavedDay(indexDailylogClientProcessResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientProgramListResourceLogSavedDay(indexDailylogClientProgramListResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientUserTermMonitorLogSavedDay(indexDailylogClientUserTermMonitorLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogClientwindowsUpdateListResourceLogSavedDay(indexDailylogClientwindowsUpdateListResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogDeviceerrorLogSavedDay(indexDailylogDeviceerrorLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogHwErrorLogSavedDay(indexDailylogHwErrorLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogIntegrityLogSavedDay(indexDailylogIntegrityLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogPcOnOffEventLogSavedDay(indexDailylogPcOnOffEventLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogServerResourceLogSavedDay(indexDailylogServerResourceLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogModuleMonitoringLogSavedDay(indexDailylogModuleMonitoringLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogWindowsBlueScreenLogSavedDay(indexDailylogWindowsBlueScreenLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogWindowsEventSystemErrorAllLogSavedDay(indexDailylogWindowsEventSystemErrorAllLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogRuleAlertLogSavedDay(indexDailylogRuleAlertLogSavedDay);
        Config.getInstance().getLog().setIndexDailylogRuleAlertSendLogSavedDay(indexDailylogRuleAlertSendLogSavedDay);

        Config.getInstance().getLog().setIndexDailylogApplicationErrorLogIndexName(indexDailylogApplicationErrorLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientActiveportListResourceLogIndexName(indexDailylogClientActiveportListResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientControlProcessResourceLogIndexName(indexDailylogClientControlProcessResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientDefraganAlysisResourceLogIndexName(indexDailylogClientDefraganAlysisResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientHwInfoResourceLogIndexName(indexDailylogClientHwInfoResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientMbrResourceLogIndexName(indexDailylogClientMbrResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientPerformanceLogIndexName(indexDailylogClientPerformanceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientProcessCreationLogIndexName(indexDailylogClientProcessCreationLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientProcessResourceLogIndexName(indexDailylogClientProcessResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientProgramListResourceLogIndexName(indexDailylogClientProgramListResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientUserTermMonitorLogIndexName(indexDailylogClientUserTermMonitorLogIndexName);
        Config.getInstance().getLog().setIndexDailylogClientwindowsUpdateListResourceLogIndexName(indexDailylogClientwindowsUpdateListResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogDeviceerrorLogIndexName(indexDailylogDeviceerrorLogIndexName);
        Config.getInstance().getLog().setIndexDailylogHwErrorLogIndexName(indexDailylogHwErrorLogIndexName);
        Config.getInstance().getLog().setIndexDailylogIntegrityLogIndexName(indexDailylogIntegrityLogIndexName);
        Config.getInstance().getLog().setIndexDailylogPcOnOffEventLogIndexName(indexDailylogPcOnOffEventLogIndexName);
        Config.getInstance().getLog().setIndexDailylogServerResourceLogIndexName(indexDailylogServerResourceLogIndexName);
        Config.getInstance().getLog().setIndexDailylogModuleMonitoringLogIndexName(indexDailylogModuleMonitoringLogIndexName);
        Config.getInstance().getLog().setIndexDailylogWindowsBlueScreenLogIndexName(indexDailylogWindowsBlueScreenLogIndexName);
        Config.getInstance().getLog().setIndexDailylogWindowsEventSystemErrorAllLogIndexName(indexDailylogWindowsEventSystemErrorAllLogIndexName);
        Config.getInstance().getLog().setIndexDailylogRuleAlertLogIndexName(indexDailylogRuleAlertLogIndexName);
        Config.getInstance().getLog().setIndexDailylogRuleAlertSendLogIndexName(indexDailylogRuleAlertSendLogIndexName);

        LogInfomation logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogApplicationErrorLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogApplicationErrorLogSavedDay);
        logInfomation.setIndexNames(indexDailylogApplicationErrorLogIndexName);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);


        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogApplicationErrorLogIndexName);
        logInfomation.setIndexNames(indexDailylogApplicationErrorLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogApplicationErrorLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientActiveportListResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientActiveportListResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientActiveportListResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientControlProcessResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientControlProcessResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientControlProcessResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientDefraganAlysisResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientDefraganAlysisResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientDefraganAlysisResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientHwInfoResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientHwInfoResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientHwInfoResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientMbrResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientMbrResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientMbrResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientPerformanceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientPerformanceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientPerformanceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientProcessCreationLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientProcessCreationLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientProcessCreationLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientProcessResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientProcessResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientProcessResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientProgramListResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientProgramListResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientProgramListResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientUserTermMonitorLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientUserTermMonitorLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientUserTermMonitorLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogClientwindowsUpdateListResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogClientwindowsUpdateListResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogClientwindowsUpdateListResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogDeviceerrorLogIndexName);
        logInfomation.setIndexNames(indexDailylogDeviceerrorLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogDeviceerrorLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogHwErrorLogIndexName);
        logInfomation.setIndexNames(indexDailylogHwErrorLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogHwErrorLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogIntegrityLogIndexName);
        logInfomation.setIndexNames(indexDailylogIntegrityLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogIntegrityLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogPcOnOffEventLogIndexName);
        logInfomation.setIndexNames(indexDailylogPcOnOffEventLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogPcOnOffEventLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogServerResourceLogIndexName);
        logInfomation.setIndexNames(indexDailylogServerResourceLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogServerResourceLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogModuleMonitoringLogIndexName);
        logInfomation.setIndexNames(indexDailylogModuleMonitoringLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogModuleMonitoringLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogWindowsBlueScreenLogIndexName);
        logInfomation.setIndexNames(indexDailylogWindowsBlueScreenLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogWindowsBlueScreenLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogWindowsEventSystemErrorAllLogIndexName);
        logInfomation.setIndexNames(indexDailylogWindowsEventSystemErrorAllLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogWindowsEventSystemErrorAllLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogRuleAlertLogIndexName);
        logInfomation.setIndexNames(indexDailylogRuleAlertLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogRuleAlertLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

        logInfomation = new LogInfomation();
        logInfomation.setKey(indexDailylogRuleAlertSendLogIndexName);
        logInfomation.setIndexNames(indexDailylogRuleAlertSendLogIndexName);
        logInfomation.setLogSavedDay(indexDailylogRuleAlertSendLogSavedDay);
        Config.getInstance().getLog().getLogInfomation().put(logInfomation.getKey(),logInfomation);

    }

}