package com.inswave.appplatform.log.monitor;

import com.inswave.appplatform.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMelodyMonitor {

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    public static void printInfo(String className, String content) {
        if(Config.getInstance().getLog().isJavamelodyErrorPrint())
            logger.error("[INFOMATION] "+className+" "+content+""); // 디버깅 용도로 무조건 남긴다. 일별 로그 관리 해야 함.
    }

}
