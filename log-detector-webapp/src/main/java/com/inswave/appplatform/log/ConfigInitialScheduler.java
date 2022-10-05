package com.inswave.appplatform.log;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Component
public class ConfigInitialScheduler {

    /**
     * 기본 환경설정 값 설정 10분마다 service class call
     */
    @Scheduled(cron="0 5,15,25,35,45,55 * * * *") // 초 분 시간 일 월 요일
    public void run() {
        if( Config.getInstance().getRunMode2().equals(Constants.RUN_MODE2_LOG) ) {
            try {
                Class<?> cls = Class.forName(Constants.CLASS_OF_CONFIG_INITIAL_BATCH_SERVICE);
                Constructor<?> constructor = cls.getConstructor();
                Object node = constructor.newInstance();
                Method method = cls.getMethod(Constants.METHOD_OF_INTERNAL_EXCUTE_SERVICE);
                Object object = method.invoke(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
