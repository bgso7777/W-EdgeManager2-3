package com.inswave.appplatform.daemon;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.util.HttpClient;

public class SeverResourceLogger implements Runnable {

    private String source;
    private String sendUrl;
    private Long sleepTime;

    private String moduleName; // manager, admin, flume, kafka, elasticsearch
    private String moduleIp;
    private int modulePort;

    public SeverResourceLogger(String sendUrl, Long sleepTime, String source,String moduleName, String moduleIp, int modulePort) {
        this.sendUrl = sendUrl;
        this.sleepTime = sleepTime;
        this.source = source;

        this.moduleName = moduleName;
        this.moduleIp = moduleIp;
        this.modulePort = modulePort;
    }

    public void run() {

        Config.getInstance().setRunMode2(source);

        do{
            try{Thread.sleep(sleepTime);}catch(Exception e) {}

            try{
                ServerResource serverResource = new ServerResource();

                HttpClient httpClient = new HttpClient();

                StringBuffer sendData = serverResource.getCurrentServerResourceLogData();

                System.out.println(sendUrl);
                System.out.println( sendData.toString() );
                String rcvData = httpClient.sendRequestByJSON2(sendUrl,sendData.toString(), Constants.TIME_OUT_FOR_HTTP_REQUEST,"UTF-8");
                System.out.println(rcvData);

                System.out.println(sendUrl);

            }catch(Exception e) {
                System.out.println(e.toString());
            }

        }while(true);

    }
}
