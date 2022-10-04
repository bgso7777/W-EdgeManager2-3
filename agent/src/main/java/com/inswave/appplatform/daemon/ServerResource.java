package com.inswave.appplatform.daemon;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.log.entity.CPUInfo;
import com.inswave.appplatform.log.entity.SystemInfo;
import com.inswave.appplatform.util.DateUtil;
import com.inswave.appplatform.util.HttpClient;
import com.inswave.appplatform.util.SystemUtil;

/**
 * 서버의 resource 로그를 취합하여 collector로 보낸다.
 */
public class ServerResource {

    //@Override
    public Object sendLog() {

        Integer result = Constants.RESULT_SUCESS;
        try {
            HttpClient httpClient = new HttpClient();
            String url = Config.getInstance().getLog().getServerResourceLogUrl();
            StringBuffer sendData = getCurrentServerResourceLogData();
            System.out.println(url);
            System.out.println(sendData.toString());
            String rcvData = httpClient.sendRequestByJSON2(url,sendData.toString(),Constants.TIME_OUT_FOR_HTTP_REQUEST,"UTF-8");
            System.out.println(rcvData);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String orgData="";

    public String getOrgData() {
        return orgData;
    }

    public StringBuffer getCurrentServerResourceLogData() {

        SystemInfo systemInfo = SystemUtil.getSystemInfo();

        String msgId = "";
        String source = Config.getInstance().getRunMode2();
        String service = Constants.TAG_SERVER_RESOURCE_LOG;
        String destination = Constants.TAG_DESTINATION_FLUME_KAFKA;
        String daoOperation = "";
        String deviceId = systemInfo.getHostName();
        String appId = "";
        String osType = systemInfo.getOsInfo().getDistro()+" "+systemInfo.getOsInfo().getRelease();
        Long siteId = 0L;
        Long installId = 0L;
        String siteName = Config.getInstance().getRunMode3();
        String userId="";
        String termNo="";
        String ssoBrNo="";
        String brNo="";
        String deptName="";
        String hwnNo="";
        String userName="";
        String ssoType="";
        String pcName="";

        Parse parse = new Parse();
        IData reqIData = parse.createHeader(msgId, source, service, destination, daoOperation, deviceId, appId, osType, siteId, installId, siteName, userId, termNo, ssoBrNo, brNo, deptName, hwnNo, userName, ssoType, pcName);

        IData body = new NodeData();

        double cpuUsage=0;
        for (CPUInfo cPUInfo : systemInfo.getCpu() ) {
            System.out.println(cPUInfo.getUsage());
            cpuUsage = cpuUsage + cPUInfo.getUsage();
        }
        try{cpuUsage=cpuUsage/systemInfo.getCpu().size();}catch(Exception e) {}

        body.put(Constants.TAG_CPU_USAGE, new SimpleData(cpuUsage));

        body.put(Constants.TAG_MEMORY_AVAILABLE, new SimpleData(systemInfo.getMem().get(0).getFree()));
        body.put(Constants.TAG_MEMORY_TOTAL, new SimpleData(systemInfo.getMem().get(0).getTotal()));
        body.put(Constants.TAG_MEMORY_USED, new SimpleData(systemInfo.getMem().get(0).getUsed()));
        body.put(Constants.TAG_MEMORY_USAGE, new SimpleData(systemInfo.getMem().get(0).getUsage()));

        body.put(Constants.TAG_DISK_TOTAL, new SimpleData(systemInfo.getDisk().get(0).getTotal()));
        body.put(Constants.TAG_DISK_USED, new SimpleData(systemInfo.getDisk().get(0).getUsed()));
        body.put(Constants.TAG_DISK_USAGE, new SimpleData(systemInfo.getDisk().get(0).getUse()));

        body.put(Constants.TAG_NETWORK_SENT, new SimpleData(systemInfo.getNetwork().get(0).getSent()));
        body.put(Constants.TAG_NETWORK_RECEIVED, new SimpleData(systemInfo.getNetwork().get(0).getReceived()));
        body.put(Constants.TAG_NETWORK_RX, new SimpleData(systemInfo.getNetwork().get(0).getRx()));
        body.put(Constants.TAG_NETWORK_TX, new SimpleData(systemInfo.getNetwork().get(0).getTx()));

        body.put(Constants.TAG_TIME_CREATED, new SimpleData( DateUtil.getCurrentDateElasticsearch(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)));

//        body.put(Constants.TAG_MODULE_NAME, new SimpleData("flume")); // manager, admin, flume, kafka, elasticsearch
//        body.put(Constants.TAG_MODULE_STATUS, new SimpleData("GREEN")); // YELLOW RED // 모듈 정보? 상태 체크?

        reqIData.put(Constants.TAG_BODY,body);

        this.orgData = reqIData.toString();
        String reqRawData = reqIData.toString();
        reqRawData = reqRawData.replaceAll("},\"body\":\\{",",");
        reqRawData = reqRawData.replaceAll("\"header\":\\{","{");
        reqRawData = reqRawData.replaceAll("\"","\\\\\"");
        reqRawData = "["+reqRawData.substring(1,reqRawData.length()-1)+"]";

        System.out.println( reqRawData.toString() );

        IData reqData = new NodeData();

        IData reqDataHeader = new NodeData();
        reqDataHeader.put(Constants.TAG_HEADER_SOURCE, new SimpleData(Config.getInstance().getRunMode2()));
        reqDataHeader.put(Constants.TAG_HEADER_DESTINATION, new SimpleData(Constants.TAG_DESTINATION_FLUME_KAFKA));
        reqDataHeader.put(Constants.TAG_HEADER_SERVICE, new SimpleData(Constants.TAG_SERVER_RESOURCE_LOG));

        //IData reqDataBody = new NodeData();

        reqData.put(Constants.TAG_HEADER,reqDataHeader);
        reqData.put(Constants.TAG_BODY,new SimpleData(reqRawData.toString()));

        StringBuffer ret = new StringBuffer("["+reqData.toString()+"]");

        return ret;
    }

    public static void main(String[] argv) {
        ServerResource serverResource = new ServerResource();
        StringBuffer sendData = serverResource.getCurrentServerResourceLogData();
        System.out.println(sendData.toString());
    }

}
