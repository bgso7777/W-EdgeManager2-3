package com.inswave.appplatform.log;

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


// logmanager로 실행 시
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// jeus용 core websocket확인.
// build 시 ***** transaver 프로젝트의 StartTransformSaverSpringApplication logmanager용으로 수정 kafkalisteler를 주석 처리한다.
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//@SpringBootApplication(scanBasePackages={"com.inswave.appplatform"})
//@EntityScan(basePackages = { "com.inswave.appplatform" })
//@EnableScheduling
//@Async
//@Configuration
//@Component
//public class StartLogRuleSpringApplication extends SpringBootServletInitializer {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// tomcat용
// build 시 ***** transaver 프로젝트의 StartTransformSaverSpringApplication logmanager용으로 수정 kafkalisteler를 주석 처리한다.
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//import net.bull.javamelody.MonitoredWithSpring;
//import net.bull.javamelody.MonitoringFilter;
//import net.bull.javamelody.SessionListener;
//@SpringBootApplication(scanBasePackages={"com.inswave.appplatform"})
//@EntityScan(basePackages = { "com.inswave.appplatform" })
//@EnableScheduling
//@Async
//@Configuration
//@Component
//@MonitoredWithSpring
//public class StartLogRuleSpringApplication extends SpringBootServletInitializer {
//    @Bean
//    public HttpSessionListener javaMelodyListener(){
//        return new SessionListener();
//    }
//    @Bean
//    public Filter javaMelodyFilter(){
//        return new MonitoringFilter();
//    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// javamelody제거
    @SpringBootApplication(scanBasePackages={"com.inswave.appplatform"})
    @EntityScan(basePackages = { "com.inswave.appplatform" })
    @EnableScheduling
    @Async
    @Configuration
    @Component
    public class StartLogRuleSpringApplication extends SpringBootServletInitializer {
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

    public static void main(String[] args) {
        //AppplatformSpringApplication.main(args);
        SpringApplication.run(StartLogRuleSpringApplication.class,args);
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
    }

}