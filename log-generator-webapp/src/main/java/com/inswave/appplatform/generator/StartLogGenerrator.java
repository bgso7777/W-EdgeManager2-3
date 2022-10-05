package com.inswave.appplatform.generator;

import com.inswave.appplatform.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


// dev 환경에서 transaver 실행 시 주석
//public class StartLogRuleSpringApplication {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@SpringBootApplication(scanBasePackages={"com.inswave.appplatform"},exclude={org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
//@SpringBootApplication(exclude={org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class}) // 403 에러 발생
@EntityScan(basePackages = { "com.inswave.appplatform" })
@EnableScheduling
@Async
@Configuration
@Component
public class StartLogGenerrator extends SpringBootServletInitializer {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Value("${runMode1}")
    private String runMode1;
    @Value("${runMode2}")
    private String runMode2;
    @Value("${runMode3}")
    private String runMode3;
    @Value("${configInitialServiceSleepTime}")
    private Long   configInitialServiceSleepTime;
    @Value("${country}")
    private String country;

    @Value("${elasticsearch.search.server}")
    private String elasticsearchSearchServer;
    @Value("${elasticsearch.data.server}")
    private String elasticsearchDataServer;
    @Value("${index.number.of.shards}")
    private int indexNumberOfShards;
    @Value("${index.number.of.replicas}")
    private int indexNumberOfReplicas;

    @Value("${javamelody.error.print}")
    private boolean isJavamelodyErrorPrint=true;

    @Value("${is.run.serverresourcelog.observer}")
    private boolean isRunServerresourcelogObserver = false;
    @Value("${serverresourcelog.observer.capacity.elasticsearch.data.disk}")
    private int serverresourcelogObserverCapacityElasticsearchDataDisk = -1;
    @Value("${serverresourcelog.observer.check.source.name}")
    private String serverresourcelogObserverCheckSourceName = "";
    @Value("${serverresourcelog.observer.delete.index.names}")
    private String serverresourcelogObserverDeleteIndexNames = "";

    @Value("${is.master.rule.processor}")
    private boolean isMasterRuleProcessor=true;
    @Value("${sleep.slave.rule.processor.ms}")
    private Long sleepSlaveRuleProcessorMs=120000L;


    @Value("${request.log.url.applicationerrorlog}")
    private String requestLogUrlApplicationErrorLog;
    @Value("${request.log.url.clientactiveportlistresourcelog}")
    private String requestLogUrlClientActivePortListResourcelog;
    @Value("${request.log.url.clienthwinforesourcelog}")
    private String requestLogUrlClientHWInfoResourceLog;
    @Value("${request.log.url.clientmbrresourcelog}")
    private String requestLogUrlClientMBRResourceLog;
    @Value("${request.log.url.clientperformancelog}")
    private String requestLogUrlClientPerformanceLog;
    @Value("${request.log.url.clientprocesscreationlog}")
    private String requestLogUrlClientProcessCreationLog;
    @Value("${request.log.url.clientprogramlistresourcelog}")
    private String requestLogUrlClientProgramListResourceLog;
    @Value("${request.log.url.clientresourcelog}")
    private String requestLogUrlClientResourceLog;
    @Value("${request.log.url.clientusertermmonitorlog}")
    private String requestLogUrlClientUserTermMonitorLog;
    @Value("${request.log.url.integritylog}")
    private String requestLogUrlIntegrityLog;
    @Value("${request.log.url.pconoffeventlog}")
    private String requestLogUrlPcOnOffEventLog;
    @Value("${request.log.url.serverresourcelog}")
    private String requestLogUrlServerResourceLog;
    @Value("${request.log.url.windowseventsystemerroralllog}")
    private String requestLogUrlWindowsEventSystemErrorAllLog;
    @Value("${request.log.url.hwerrorlog}")
    private String requestLogUrlHWErrorLog;
    @Value("${request.log.url.deviceerrorlog}")
    private String requestLogUrlDevicEerrorLog;
    @Value("${request.log.url.windowsbluescreenlog}")
    private String requestLogUrlWindowsBlueScreenLog;
    @Value("${request.log.url.clientdefraganalysisresourcelog}")
    private String requestLogUrlClientDefraganAlysisResourceLog;
    @Value("${request.log.url.clientwindowsupdatelistresourcelog}")
    private String requestLogUrlClientWindowsUpdateListResourceLog;
    @Value("${request.log.url.clientcontrolprocessresourcelog}")
    private String requestLogUrlClientControlProcessResourceLog;

    @Value("${log.generator.data.root.dirs}")
    private String logGeneratorDataRootDirs;

    @Value("${log.generator.current.device.count}")
    private int logGeneratorCurrentDeviceCount;
    @Value("${log.generator.instance.idx}")
    private int logGeneratorInstanceIdx;

    public static void main(String[] args) {
        //AppplatformSpringApplication.main(args);
        SpringApplication.run(StartLogGenerrator.class,args);
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

        Config.getInstance().setConfigInitialServiceSleepTime(configInitialServiceSleepTime);
        Config.getInstance().setCountry(country);

        Config.getInstance().getLog().setElasticsearchSearchServer(elasticsearchSearchServer);
        Config.getInstance().getLog().setElasticsearchDataServer(elasticsearchDataServer);
        Config.getInstance().getLog().setIndexNumberOfReplicas(indexNumberOfReplicas);
        Config.getInstance().getLog().setIndexNumberOfShards(indexNumberOfShards);

        Config.getInstance().getLog().setIsRunServerresourcelogObserver(isRunServerresourcelogObserver);
        Config.getInstance().getLog().setServerresourcelogObserverCapacityElasticsearchDataDisk(serverresourcelogObserverCapacityElasticsearchDataDisk);
        Config.getInstance().getLog().setServerresourcelogObserverCheckSourceName(serverresourcelogObserverCheckSourceName);
        Config.getInstance().getLog().setServerresourcelogObserverDeleteIndexNames(serverresourcelogObserverDeleteIndexNames);

        Config.getInstance().getLog().setJavamelodyErrorPrint(isJavamelodyErrorPrint);

        Config.getInstance().getLog().setIsMasterRuleProcessor(isMasterRuleProcessor);
        Config.getInstance().getLog().setSleepSlaveRuleProcessorMs(sleepSlaveRuleProcessorMs);

        Config.getInstance().getLog().setLogGeneratorCurrentDeviceCount(logGeneratorCurrentDeviceCount);
        Config.getInstance().getLog().setLogGeneratorInstanceIdx(logGeneratorInstanceIdx);
    }

}