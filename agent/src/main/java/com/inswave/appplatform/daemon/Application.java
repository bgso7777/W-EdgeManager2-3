package com.inswave.appplatform.daemon;

import com.inswave.appplatform.Constants;
import com.inswave.appplatform.util.DateUtil;

public class Application {

    public static void main(String argv[]) {

        System.out.println("=================================================");
        System.out.println("===========   start Daemon Application   ===========");
        System.out.println("=================================================");
        System.out.println(DateUtil.getCurrentDateElasticsearch(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z));

        String mode="";
        String sendUrl="";
        Long sleepTime=10000L;
        String source="";
        String moduleName="";
        String moduleIp="";
        int modulePort=0;

        try{mode=argv[0];}catch(Exception e) {}
        try{sendUrl=argv[1];}catch(Exception e) {}
        try{sleepTime=Long.parseLong(argv[2]);}catch(Exception e) {}
        try{source=argv[3];}catch(Exception e) {}
        try{moduleName=argv[4];}catch(Exception e) {}
        try{moduleIp=argv[5];}catch(Exception e) {}
        try{modulePort=Integer.parseInt(argv[6]);}catch(Exception e) {}

        if(mode.equals(""))
            mode = "SeverResourceLog";

        System.out.println("mode "+mode); // 로그 구분 SeverResourceLog
        System.out.println("sendUrl "+sendUrl); // 전송 url
        System.out.println("sleepTime "+sleepTime); // 전송 주기 sleep time
        System.out.println("source "+source); // 소스명 (manager1..., kafka1... ,collector1..., elasticsearch1... )
        System.out.println("moduleName "+moduleName); // module name
        System.out.println("moduleIp "+moduleIp); // module ip
        System.out.println("modulePort "+modulePort); // module port

        if(mode.equals("SeverResourceLog")) {
            Runnable severResourceLogger = new SeverResourceLogger(sendUrl, sleepTime, source, moduleName, moduleIp, modulePort);
            Thread severResourceLoggerThread = new Thread(severResourceLogger);
            severResourceLoggerThread.start();
            do {
                if (!severResourceLoggerThread.isAlive())
                    break;
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            } while (true);
            severResourceLoggerThread.stop();
        }

    }
}
