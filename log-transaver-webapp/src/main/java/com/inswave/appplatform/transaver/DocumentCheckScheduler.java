package com.inswave.appplatform.transaver;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Component
public class DocumentCheckScheduler {

    /**
     * 매일 0시 1분에 elasticsearch document를 체크한다.
     */
    @Scheduled(cron="0 1 0 * * *") // 초 분 시 일 월 요일
    public void runScheduler() {
        if( Config.getInstance().getRunMode2().equals(Constants.RUN_MODE2_TRANSAVER) ) {
            try {
                Class<?> cls = Class.forName(ConstantsTranSaver.CLASS_OF_DOCUMENT_CHECK_BATCH_SERVICE);
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
