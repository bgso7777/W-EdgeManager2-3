package com.inswave.appplatform.core;

import com.inswave.appplatform.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@SpringBootApplication
@EnableScheduling
@Component
public class AppplatformSpringApplication {

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

    public static void main(String[] args) {
        SpringApplication.run(AppplatformSpringApplication.class, args);
    }

    @PostConstruct
    public void started() {
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //        return super.configure(builder);
        return builder.sources(AppplatformSpringApplication.class);
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler(TaskSchedulerBuilder builder) {
        return builder.build();
    }

    @EventListener
    public void setSeedData(ContextRefreshedEvent event) {
        Config.getInstance().setRunMode1(runMode1);
        Config.getInstance().setRunMode2(runMode2);
        Config.getInstance().setRunMode3(runMode3);
        Config.getInstance().setConfigInitialServiceSleepTime(configInitialServiceSleepTime);
        Config.getInstance().setCountry(country);
    }
}
