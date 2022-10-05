package com.inswave.appplatform.log;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Component
public class RuleInitialScheduler {

    /**
     * 10분마다 rule 정보를 db에서 읽어 온다.
     */
    @Scheduled(cron="0 8,18,28,38,48,58 * * * *") // 초 분 시간 일 월 요일
    public void initRule() {
        if( Config.getInstance().getRunMode2().equals(Constants.RUN_MODE2_LOG) ) {
            try {
                Class<?> cls = Class.forName(ConstantsLog.CLASS_OF_RULE_INITIAL_BATCH_SERVICE);
                Constructor<?> constructor = cls.getConstructor();
                Object node = constructor.newInstance();
                Method method = cls.getMethod(ConstantsLog.METHOD_OF_INTERNAL_EXCUTE_SERVICE);
                Object object = method.invoke(node);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
