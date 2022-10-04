package com.inswave.appplatform.transaver;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.util.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Component
public class KafkaListenerControllerScheduler {

    /**
     * 3분 주기로 체크함
     */
    @Scheduled(cron="0 */3 * * * *") // 초 분 시 일 월 요일
    public void checkShinhanbankAtopCreationLogIdleBetweenPollsMs() {

        if( Config.getInstance().getRunMode2().equals(Constants.RUN_MODE2_TRANSAVER) &&
            (Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) ||
            Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ) ) {
            try {
                if( DateUtil.getHour()>=7 && DateUtil.getHour()<18 ) {
                    Class<?> cls = Class.forName(ConstantsTranSaver.CLASS_OF_KAFKA_LISTENER_CONTROLLER_SERVICE);
                    Constructor<?> constructor = cls.getConstructor();
                    Object node = constructor.newInstance();
                    Method method = cls.getMethod(Constants.METHOD_OF_CHECK_SHINHANBANKATOP_PROCESSCREATIONLOG_IDLEBETWEENPOLLSMS_SERVICE);
                    Object object = method.invoke(node);
                } else {
                    // 일괄 처리 시간(07~18)에는 체크하지 않는다.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 매일 18시 0분 0초에 시작함.
     */
    @Scheduled(cron="0 0 18 * * *") // 초 분 시 일 월 요일
    public void startShinhanbankAtopCreationLogIdleBetweenPollsMs() {

        if( Config.getInstance().getRunMode2().equals(Constants.RUN_MODE2_TRANSAVER) &&
            (Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) ||
            Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ) ) {
            try {
                Class<?> cls = Class.forName(ConstantsTranSaver.CLASS_OF_KAFKA_LISTENER_CONTROLLER_SERVICE);
                Constructor<?> constructor = cls.getConstructor();
                Object node = constructor.newInstance();
                Method method = cls.getMethod(Constants.METHOD_OF_START_SHINHANBANKATOP_PROCESSCREATIONLOG_IDLEBETWEENPOLLSMS_SERVICE);
                Object object = method.invoke(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 매일 7시 0분 0초에 중지함.
     */
    @Scheduled(cron="0 0 7 * * *") // 초 분 시 일 월 요일
    public void stopShinhanbankAtopCreationLogIdleBetweenPollsMs() {

        if( Config.getInstance().getRunMode2().equals(Constants.RUN_MODE2_TRANSAVER) &&
            (Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) ||
            Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ) ) {
            try {
                Class<?> cls = Class.forName(ConstantsTranSaver.CLASS_OF_KAFKA_LISTENER_CONTROLLER_SERVICE);
                Constructor<?> constructor = cls.getConstructor();
                Object node = constructor.newInstance();
                Method method = cls.getMethod(Constants.METHOD_OF_STOP_SHINHANBANKATOP_PROCESSCREATIONLOG_IDLEBETWEENPOLLSMS_SERVICE);
                Object object = method.invoke(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
