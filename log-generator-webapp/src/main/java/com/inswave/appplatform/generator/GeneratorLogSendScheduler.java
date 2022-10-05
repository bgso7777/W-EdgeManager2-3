package com.inswave.appplatform.generator;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.util.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Component
public class GeneratorLogSendScheduler {

    /**
     * 1분 주기로 전송한다.
     */
    @Scheduled(cron="0 */1 * * * *") // 초 분 시 일 월 요일
    public void sendLog() {
        try {
//            if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) &&
//                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV)) {
//
//            } else {
                long sleepTime = 3000 * Config.getInstance().getLog().getLogGeneratorInstanceIdx();
                Thread.sleep(sleepTime);
                Class<?> cls = Class.forName(ConstantsTranSaver.CLASS_OF_LOG_GENERATOR_SERVICE);
                Constructor<?> constructor = cls.getConstructor();
                Object node = constructor.newInstance();
                Method method = cls.getMethod(Constants.METHOD_OF_INTERNAL_SEND_LOG_SERVICE);
                Object object = method.invoke(node);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
