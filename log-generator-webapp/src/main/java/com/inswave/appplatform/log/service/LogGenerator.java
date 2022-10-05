package com.inswave.appplatform.log.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.log.ConstantsLog;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.service.ExternalService;
import com.inswave.appplatform.service.InternalService;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.GenratorLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean;
import com.inswave.appplatform.transaver.elasticsearch.domain.GeneratorLog;
import com.inswave.appplatform.transaver.saver.*;
import com.inswave.appplatform.transaver.util.BeanUtils;
import com.inswave.appplatform.util.*;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class LogGenerator implements InternalService, ExternalService {

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    private static DecimalFormat decimalFormat = new DecimalFormat("00");

    private static List<GeneratorLog> allDevice = new ArrayList<>();
    private static List<GeneratorLog> currentDevice = new ArrayList<>();

    private static boolean isStartedSenderThread = false;

    private static int generatorSenderStatus = 0; // 0 중지, 1 실행
    private static int currentDeviceCount=0; // 장치 수
    private static int instanceIdx=1;

    private static boolean replaceTodaySendDeviceId = false; // 시작 시, 1일 1회 특정 deviceId로 바꾼다.


    @Override
    public IData excuteGet(HashMap<String, Object> params) {
        return null;
    }

    @Override
    public IData excutePost(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData excutePost(IData reqIData, IData resIData, Object object) {
        return null;
    }

    @Override
    public Object excute() {
        return null;
    }

    @Override
    public Object sendLog() {

        currentDeviceCount = Config.getInstance().getLog().getLogGeneratorCurrentDeviceCount();
        instanceIdx = Config.getInstance().getLog().getLogGeneratorInstanceIdx();

        if ( generatorSenderStatus==0 ) {
            initAllDevice();
            initCurrentDevice(currentDeviceCount, instanceIdx);
            generatorSenderStatus = 1;
        }

        int hour = DateUtil.getHour();
        int minute = DateUtil.getMinute();
        int second = DateUtil.getSecond();
        run(hour,minute,second);

        return null;
    }

    public IData initLogGenerator(IData reqIData, IData resIData) {
        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        generatorSenderStatus = (Integer)reqIData.getBodyValue(ConstantsLog.TAG_GENERATOR_SENDER_STATUS);
        currentDeviceCount = (Integer)reqIData.getBodyValue(ConstantsLog.TAG_CURRENT_DEVICE_COUNT);
        instanceIdx  = (Integer)reqIData.getBodyValue(ConstantsLog.TAG_INSTANCE_IDX);

        initLogGenerator(generatorSenderStatus, currentDeviceCount, instanceIdx);
        process();

        body.put(ConstantsLog.TAG_IS_STARTED_SENDER_THREAD, new SimpleData(isStartedSenderThread));
        body.put(ConstantsLog.TAG_GENERATOR_SENDER_STATUS, new SimpleData(generatorSenderStatus));
        body.put(ConstantsLog.TAG_CURRENT_DEVICE_COUNT, new SimpleData(currentDeviceCount));
        body.put(ConstantsLog.TAG_INSTANCE_IDX, new SimpleData(instanceIdx));

        body.put(ConstantsLog.TAG_ALL_DEVICE_SIZE, new SimpleData(allDevice.size()));
        body.put(ConstantsLog.TAG_CURRENT_DEVICE_SIZE, new SimpleData(currentDevice.size()));

        resIData.put(Constants.TAG_BODY, body);

        return resIData;
    }

    public void initLogGenerator(int generatorSenderStatus, int currentDeviceCount, int instanceIdx) {
        this.generatorSenderStatus = generatorSenderStatus;
        this.currentDeviceCount = currentDeviceCount;
        this.instanceIdx = instanceIdx;
    }

    public void process() {

        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"process generatorSenderStatus-->"+generatorSenderStatus);
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"process currentDeviceCount-->"+currentDeviceCount);

        if(generatorSenderStatus==0) {
            currentDeviceCount=0;
        } else {
            if (currentDeviceCount<=0) {
                generatorSenderStatus=0;
            } else {
                initAllDevice();
                initCurrentDevice(currentDeviceCount,instanceIdx);
                if(!isStartedSenderThread) {
                    Thread runThread = new Thread() {
                        public void run() {
                            int startMinute = -1;
                            do {
                                try {
                                    int hour = DateUtil.getHour();
                                    int minute = DateUtil.getMinute();
                                    int second = DateUtil.getSecond();
                                    if ( generatorSenderStatus==1 && startMinute!=minute ) {
//                                        LogSender logSender = new LogSender(hour, minute, second);
//                                        logSender.start();
//                                        startMinute = minute;
//                                        Thread.sleep(500);
                                    }
                                    Thread.sleep(500); // 1분에 한번 씩 send thread를 생성하여 보낸다.

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } while (true);
                        }
                    };
                    runThread.start();
                    isStartedSenderThread = true;
                }
            }
        }
    }

    /**
     *
     */
    private void initAllDevice() {
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"initAllDevice");
        allDevice = new ArrayList<>();
        GenratorLogRepository genratorLogRepository = (GenratorLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(GeneratorLog.class.getSimpleName());
        genratorLogRepository.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());
        DynamicIndexBean.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());

        allDevice = genratorLogRepository.findAggsTermsDeviceId(30000);

        Comparator<GeneratorLog> comparator = new Comparator<GeneratorLog>() {
            @Override
            public int compare(GeneratorLog a, GeneratorLog b) {
                String obj1 = a.getDeviceId();
                String obj2 = b.getDeviceId();
                if (obj1 == obj2)
                    return 0;
                if (obj1 == null)
                    return -1;
                if (obj2 == null)
                    return 1;
                return obj1.compareTo(obj2);
            }
        };
        Collections.sort(allDevice, comparator);
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"initAllDevice allDevice.size() --> "+allDevice.size());
    }

    private void initCurrentDevice(int deviceCount, int instanceIdx) {
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"initCurrentDevice deviceCount-->>"+deviceCount);
        currentDevice = new ArrayList<>();
        //for (int i = 0; i < deviceCount; i++)
        int i=0;
        if(instanceIdx==1)
            i = 0;
        else
            i = (instanceIdx-1)*deviceCount;

        for (; i < (deviceCount*instanceIdx); i++)
            currentDevice.add(allDevice.get(i));

JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"initCurrentDevice allDevice.size() --> "+allDevice.size());
        for (GeneratorLog generatorLog : allDevice) {
JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"generatorLog.getDeviceId() --> "+generatorLog.getDeviceId());
        }

JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"initCurrentDevice currentDevice.size() --> "+currentDevice.size());
        for (GeneratorLog generatorLog : currentDevice) {
JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"generatorLog.getDeviceId() --> "+generatorLog.getDeviceId());
        }
    }

    public IData statusLogGenerator(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        body.put(ConstantsLog.TAG_IS_STARTED_SENDER_THREAD, new SimpleData(isStartedSenderThread));
        body.put(ConstantsLog.TAG_GENERATOR_SENDER_STATUS, new SimpleData(generatorSenderStatus));
        body.put(ConstantsLog.TAG_CURRENT_DEVICE_COUNT, new SimpleData(currentDeviceCount));
        body.put(ConstantsLog.TAG_ALL_DEVICE_SIZE, new SimpleData(allDevice.size()));
        body.put(ConstantsLog.TAG_CURRENT_DEVICE_SIZE, new SimpleData(currentDevice.size()));

        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    private String name="";
    private void setName(String name) {
        this.name=name;
    }
    private String getName() {
        return this.name;
    }

    public void run(int hour, int minute, int second) {

        List<GeneratorLog> sendDeviceGeneratorLogs = new ArrayList<>();

if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) &&
    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV)) {
    hour = 9;
    minute = 31;
}
        setName("LogSender "+decimalFormat.format(hour)+":"+decimalFormat.format(minute)+":"+decimalFormat.format(second));
JavaMelodyMonitor.printInfo(getName(),"begin");
JavaMelodyMonitor.printInfo(getName(),"currentDevice.size() --> " + currentDevice.size());
        GenratorLogRepository genratorLogRepository = (GenratorLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(GeneratorLog.class.getSimpleName());
        for (GeneratorLog iGeneratorLog : currentDevice) {
JavaMelodyMonitor.printInfo(getName(),"iGeneratorLog.getDeviceId()-->" + iGeneratorLog.getDeviceId() + " hour-->" + hour + " minute-->" + minute + " second-->" + second);
            try{
                String strHour = decimalFormat.format(hour);
                String strMinute = decimalFormat.format(minute);
                String indexNameData = GeneratorLog.class.getSimpleName().toLowerCase() + "_" +strHour+strMinute.substring(0,1);
                genratorLogRepository.setIndexName(indexNameData);
                DynamicIndexBean.setIndexName(indexNameData);
                List<GeneratorLog> sendGeneratorLogs = genratorLogRepository.findByDeviceIdAndCurrentHHAndCurrentMm(iGeneratorLog.getDeviceId(),hour,minute);
JavaMelodyMonitor.printInfo(getName(),"find indexNameData-->"+indexNameData+" iGeneratorLog.getDeviceId()-->"+iGeneratorLog.getDeviceId()+" hour-->"+hour+" minute-->"+minute+" sendGeneratorLogs"+sendGeneratorLogs.size());
                for (GeneratorLog sendGeneratorLog : sendGeneratorLogs)
                    sendDeviceGeneratorLogs.add(sendGeneratorLog);
                sendGeneratorLogs=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(sendDeviceGeneratorLogs.size()<=0) {
JavaMelodyMonitor.printInfo(getName(),"sendDevice not found!");
        } else {
            int sendCount=0;
JavaMelodyMonitor.printInfo(getName(),"logging begin currentDeviceCount-->"+currentDeviceCount+" sendDeviceGeneratorLogs"+sendDeviceGeneratorLogs.size());
            for (GeneratorLog sendGeneratorLog : sendDeviceGeneratorLogs) {
                try {
                    if( sendGeneratorLog.getUserName()==null || sendGeneratorLog.getUserName().equals("") || (sendGeneratorLog.getUserName().indexOf("?")!=-1) ||
                        sendGeneratorLog.getDeptName()==null || sendGeneratorLog.getDeptName().equals("") || (sendGeneratorLog.getDeptName().indexOf("?")!=-1) ||
                        sendGeneratorLog.getUserId()==null || sendGeneratorLog.getUserId().equals("") ) {

                        // 어떻게 일별로 스위치 시킬지 깨진한글 어떻게 복구할지 ??????????

                    } else {
                        sendGeneratorLog.setData(new String(Crypto.base64Decode(sendGeneratorLog.getData())));
                        replaceDate(sendGeneratorLog);
                        hiddenShinhhanInfo(sendGeneratorLog);

JavaMelodyMonitor.printInfo(getName(), sendCount + "/" + sendDeviceGeneratorLogs.size() + " " + "sendGeneratorLog.getIndexNameData()-->>" + sendGeneratorLog.getIndexNameData() + " sendGeneratorLog.getDeviceId() --> " + sendGeneratorLog.getDeviceId());
//JavaMelodyMonitor.printInfo(getName(),"sendGeneratorLog.getData()-->>" + sendGeneratorLog.getData());

                        if (sendGeneratorLog.getLogName().equals("ApplicationErrorLog")) {

                            ApplicationErrorLogger applicationErrorLogger = new ApplicationErrorLogger();
                            applicationErrorLogger.insert("ApplicationErrorLog", sendGeneratorLog.getData());
                            applicationErrorLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientActivePortListResourceLog")) {

                            ClientActivePortListResourceLogger clientActivePortListResourceLogger = new ClientActivePortListResourceLogger();
                            clientActivePortListResourceLogger.insert("ClientActivePortListResourceLog", sendGeneratorLog.getData());
                            clientActivePortListResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientControlProcessResourceLog")) {

                            ClientControlProcessResourceLogger clientControlProcessResourceLogger = new ClientControlProcessResourceLogger();
                            clientControlProcessResourceLogger.insert("ClientControlProcessResourceLog", sendGeneratorLog.getData());
                            clientControlProcessResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientDefragAnalysisResourceLog")) {

                            ClientDefragAnalysisResourceLogger clientDefragAnalysisResourceLogger = new ClientDefragAnalysisResourceLogger();
                            clientDefragAnalysisResourceLogger.insert("ClientDefragAnalysisResourceLog", sendGeneratorLog.getData());
                            clientDefragAnalysisResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientHWInfoResourceLog")) {

                            ClientHWInfoResourceLogger clientHWInfoResourceLogger = new ClientHWInfoResourceLogger();
                            clientHWInfoResourceLogger.insert("ClientHWInfoResourceLog", sendGeneratorLog.getData());
                            clientHWInfoResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientMBRResourceLog")) {

                            ClientMBRResourceLogger clientMBRResourceLogger = new ClientMBRResourceLogger();
                            clientMBRResourceLogger.insert("ClientMBRResourceLog", sendGeneratorLog.getData());
                            clientMBRResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientPerformanceLog")) {

                            ClientPerformanceResourceLogger clientPerformanceResourceLogger = new ClientPerformanceResourceLogger();
                            clientPerformanceResourceLogger.insert("ClientPerformanceLog", sendGeneratorLog.getData());
                            clientPerformanceResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientProcessCreationLog")) {

                            ClientProcessCreationLogger clientProcessCreationLogger = new ClientProcessCreationLogger();
                            clientProcessCreationLogger.insert("ClientProcessCreationLog", sendGeneratorLog.getData());
                            clientProcessCreationLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientResourceLog")) {

                            ClientProcessResourceLogger clientProcessResourceLogger = new ClientProcessResourceLogger();
                            clientProcessResourceLogger.insert("ClientResourceLog", sendGeneratorLog.getData());
                            clientProcessResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientProgramListResourceLog")) {

                            ClientProgramListResourceLogger clientProgramListResourceLogger = new ClientProgramListResourceLogger();
                            clientProgramListResourceLogger.insert("ClientProgramListResourceLog", sendGeneratorLog.getData());
                            clientProgramListResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientUserTermMonitorLog")) {

                            ClientUserTermMonitorLogger clientUserTermMonitorLogger = new ClientUserTermMonitorLogger();
                            clientUserTermMonitorLogger.insert("ClientUserTermMonitorLog", sendGeneratorLog.getData());
                            clientUserTermMonitorLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ClientWindowsUpdateListResourceLog")) {

                            ClientWindowsUpdateListResourceLogger clientWindowsUpdateListResourceLogger = new ClientWindowsUpdateListResourceLogger();
                            clientWindowsUpdateListResourceLogger.insert("ClientWindowsUpdateListResourceLog", sendGeneratorLog.getData());
                            clientWindowsUpdateListResourceLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("DeviceErrorLog")) {

                            DeviceErrorLogger deviceErrorLogger = new DeviceErrorLogger();
                            deviceErrorLogger.insert("DeviceErrorLog", sendGeneratorLog.getData());
                            deviceErrorLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("HWErrorLog")) {

                            HWErrorLogger hWErrorLogger = new HWErrorLogger();
                            hWErrorLogger.insert("HWErrorLog", sendGeneratorLog.getData());
                            hWErrorLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("IntegrityLog")) {

                            IntegrityLogger integrityLogger = new IntegrityLogger();
                            integrityLogger.insert("IntegrityLog", sendGeneratorLog.getData());
                            integrityLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("PcOnOffEventLog")) {

                            PcOnOffEventLogger pcOnOffEventLogger = new PcOnOffEventLogger();
                            pcOnOffEventLogger.insert("PcOnOffEventLog", sendGeneratorLog.getData());
                            pcOnOffEventLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("WindowsBlueScreenLog")) {

                            WindowsBlueScreenLogger windowsBlueScreenLogger = new WindowsBlueScreenLogger();
                            windowsBlueScreenLogger.insert("WindowsBlueScreenLog", sendGeneratorLog.getData());
                            windowsBlueScreenLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("WindowsEventSystemErrorAllLog")) {

                            WindowsEventSystemErrorAllLogger windowsEventSystemErrorAllLogger = new WindowsEventSystemErrorAllLogger();
                            windowsEventSystemErrorAllLogger.insert("WindowsEventSystemErrorAllLog", sendGeneratorLog.getData());
                            windowsEventSystemErrorAllLogger=null;

                        } else if (sendGeneratorLog.getLogName().equals("ServerResourceLog")) {

                            ServerResourceLogger serverResourceLogger = new ServerResourceLogger();
                            serverResourceLogger.insert("ServerResourceLog", sendGeneratorLog.getData());
                            serverResourceLogger=null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendCount++;
            }
            sendDeviceGeneratorLogs.clear();
            sendDeviceGeneratorLogs=null;
JavaMelodyMonitor.printInfo(getName(),"logging end currentDeviceCount-->"+currentDeviceCount+" sendDeviceGeneratorLogs "+sendDeviceGeneratorLogs.size());
        }
    }

    /**
     * 일자 보정
     */
    private void replaceDate(GeneratorLog generatorLog) {
        String data = generatorLog.getData();
        String currentDate = DateUtil.getCurrentDateElasticsearch("yyyy-MM-dd'T'");
        String[] replaceStr = { "2022-08-07T","2022-08-08T","2022-08-09T","2022-08-10T","2022-08-11T","2022-08-12T","2022-08-09T","2022-08-13T","2022-08-14T",
                                "2022-08-15T","2022-08-16T","2022-08-17T","2022-08-18T","2022-08-19T"};
        for (int i = 0; i < replaceStr.length; i++)
            data = data.replaceAll(replaceStr[i],currentDate);
        generatorLog.setData(data);
    }

    /**
     * 한글 깨짐 보정, 이름, 그룹명(지점명), hostName, shinhan->inswave, 프로그램명 shb->isw
     */
    private void hiddenShinhhanInfo(GeneratorLog generatorLog) {
        replaceUserName(generatorLog);
        replaceUserId(generatorLog);
        replaceDeptName(generatorLog);
        replaceHostName(generatorLog);
        replaceData(generatorLog);
    }

    private void replaceData(GeneratorLog generatorLog) {
        try {
            String data = generatorLog.getData();
            data = data.replaceAll("신한은행", "인스웨이브");
            data = data.replaceAll("신한은행", "인스웨이브");
            data = data.replaceAll("신한", "인스웨이브");
            data = data.replaceAll("ShinHan", "INSWAVE");
            data = data.replaceAll("shinhan", "INSWAVE");
            data = data.replaceAll("shb", "INSWAVE");
            data = data.replaceAll("Shb", "INSWAVE");
            data = data.replaceAll("SHB", "INSWAVE");
            data = data.replaceAll("THE NEXT", "");
            data = data.replaceAll("QueryOne", "query");
            generatorLog.setData(data);
        } catch(Exception e) {
        }
    }

    private void replaceHostName(GeneratorLog generatorLog) {
        String hostName = generatorLog.getHostName();
        if(hostName==null||hostName.equals(""))
            generatorLog.setDeptName("UNKNOWN-HOSTNAME");
        String[] hostNames = hostName.split("-");
        if(hostNames.length>=3) {
            try{
                int i = -1;
                try {
                    i = Integer.parseInt(hostNames[1]);
                    i = i * 2;
                } catch (Exception e) {
                }
                if(i>0) {
                    DecimalFormat decimalFormat = new DecimalFormat("70000");
                    hostName = decimalFormat.format(i)+"-"+hostNames[2];
                    generatorLog.setOtherHostName(hostName);
                }
            } catch (Exception e) {
            }
        }
        try {
            String data = generatorLog.getData().replaceAll(generatorLog.getHostName(), generatorLog.getOtherHostName());
            generatorLog.setData(data);
        } catch(Exception e) {
        }
    }

    private void replaceDeptName(GeneratorLog generatorLog) {
        if(generatorLog.getDeptName()==null||generatorLog.getDeptName().equals(""))
            generatorLog.setDeptName("본점");
        generatorLog.setOtherDeptName( maskString(generatorLog.getDeptName(), 3) );
        try {
            String data = generatorLog.getData().replaceAll(generatorLog.getDeptName(), generatorLog.getOtherDeptName());
            generatorLog.setData(data);
        } catch(Exception e) {
        }
    }

    private String maskString(String s, int x) {
        int n = s.length()/x;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (n >= 1 && (i < n || i >= (s.length() - n))) {
                sb.append(s.charAt(i));
            }
            else {
                sb.append("*");
            }
        }
        return sb.toString();
    }

    private void replaceUserId(GeneratorLog generatorLog) {
        if(generatorLog.getUserId()==null||generatorLog.getUserId().equals(""))
            generatorLog.setUserId("00000");
        else
            try{
                int i = Integer.parseInt(generatorLog.getUserId())/2;
                generatorLog.setOtherUserId(Integer.toString(i));
            } catch (Exception e) {
            }
        try{
            String data = generatorLog.getData().replaceAll(generatorLog.getUserId(),generatorLog.getOtherUserId());
            generatorLog.setData(data);
        } catch(Exception e) {
        }
//        if(ret.equals(""))
//            generatorLog.setOtherUserId( generatorLog.getUserId().replaceAll("\\b(\\d{2})\\d+(\\d)", "$1*******$2") );
    }

    /**
     * 가명 처리.
     */
    private void replaceUserName(GeneratorLog generatorLog) {

        //StringUtil.replaceLastName(String inLastName);
//            "userName":"이희중","JKGP":

//            String data = "........\"userName\":\"이희중\",\"JKGP\":\"........";
//            String userNameBeginStr = "\"userName\":\"";
//            String userNameEndStr = "\",\"JKGP\":\"";
//            String userName = data.substring(data.indexOf(userNameBeginStr)+userNameBeginStr.length(),data.indexOf(userNameEndStr));
//            String replaceName = StringUtil.replaceLastName(userName);
//System.out.println( userName+"-->>"+replaceName );
//
//            data.replaceAll(userName,replaceName);

        String otherUserName = generatorLog.getUserName();

        otherUserName = otherUserName.replaceAll("NEXT","김");
        otherUserName = otherUserName.replaceAll("RPA","최");
        otherUserName = otherUserName.replaceAll("알파봇","이");
        otherUserName = otherUserName.replaceAll("상담원","박");

        otherUserName = StringUtil.replaceLastName(otherUserName);
        otherUserName = StringUtil.replaceFirstName(otherUserName);

        generatorLog.setOtherUserName(otherUserName);
        try {
            generatorLog.setData(generatorLog.getData().replaceAll(generatorLog.getUserName(), generatorLog.getOtherUserName()));
        } catch(Exception e) {
        }
    }

    public IData replaceName(IData reqIData, IData resIData) {
        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        if(elasticsearchHighLevelClient==null)
            elasticsearchHighLevelClient = new ElasticsearchHighLevelClient();

//        GenratorLogRepository genratorLogRepository = (GenratorLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(GeneratorLog.class.getSimpleName());

//        int count=1;
//        int page=0, size=1000;
//        do {
//            genratorLogRepository.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());
//            DynamicIndexBean.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());

////            Page<GeneratorLog> generatorLogPage = genratorLogRepository.findAll(PageRequest.of(page, size));
//
////            Pageable pageable = PageRequest.of(page, size);
////            Page<GeneratorLog> generatorLogPage = genratorLogRepository.findAll(pageable);
////            pageable.next();
//
//            List<GeneratorLog> generatorLogs = genratorLogRepository.findPageble(page,size);
//
////            for (GeneratorLog generatorLog : generatorLogPage.getContent()) {
//            for (GeneratorLog generatorLog : generatorLogs) {
//                try {
//                    generatorLog.setOtherUserName(StringUtil.replaceLastName(generatorLog.getUserName()));
//                    generatorLog.setOtherUserName(StringUtil.replaceFirstName(generatorLog.getOtherUserName()));
//logger.debug("page->"+page+" generatorLogs.size()"+generatorLogs.size()+" count->"+count+" "+"generatorLog.getUserName()-->>"+generatorLog.getUserName()+" generatorLog.getOtherUserName()-->>"+generatorLog.getOtherUserName());
//                    if( generatorLog.getUserName()==null || generatorLog.getUserName().indexOf("?")!=-1 ) {
//
//                    } else {
////                        elasticsearchHighLevelClient.updateDocument();
//                    }
//                } catch(Exception e) {
////                    e.printStackTrace();
//                }
//                count++;
//            }
//            if( generatorLogs.size() < size )
//                break;
//            page++;
//        }while(true);

        Hashtable<String,GeneratorLog> generatorLogs = new Hashtable<>();
        SearchResponse searchResponse = null;
        Object[] sortValues = null;
        int from=0,size=10000,count=0;
        String sortField = "deviceId.keyword";
        do {
            try {
                searchResponse = elasticsearchHighLevelClient.getDocuments(GeneratorLog.class.getSimpleName().toLowerCase(), sortField, sortValues, size);
                SearchHits searchHits = searchResponse.getHits();
                SearchHit[] hits = searchHits.getHits();

                if (hits.length == 0) {
                    break;
                } else {
                    for (SearchHit searchHit : hits) {
                        count++;
                        ObjectMapper objectMapper = new ObjectMapper();
                        GeneratorLog generatorLog = objectMapper.readValue(searchHit.getSourceAsString(), GeneratorLog.class);
                        generatorLog.setOtherUserName(StringUtil.replaceLastName(generatorLog.getUserName()));
                        generatorLog.setOtherUserName(StringUtil.replaceFirstName(generatorLog.getOtherUserName()));
//                        generatorLog.setId(searchHit.getId());
System.out.println(generatorLog.getIndexName()+ " count-->>" + count + " id-->>"+searchHit.getId() + " generatorLog.getDeviceId()-->>"+generatorLog.getDeviceId() + " generatorLog.getUserName()-->>"+generatorLog.getUserName() + " generatorLog.getOtherUserName()-->>"+generatorLog.getOtherUserName() );

                        if( (generatorLog.getUserName()!=null) && (!generatorLog.getUserName().equals("")) && !(generatorLog.getUserName().indexOf("?")!=-1) ) {
                            GeneratorLog tempGeneratorLog = generatorLogs.get(generatorLog.getDeviceId());
                            if( tempGeneratorLog == null ) {
                                generatorLogs.put(generatorLog.getDeviceId(), generatorLog);
                            } else {
//                                if( (tempGeneratorLog.getUserName()!=null) && (!tempGeneratorLog.getUserName().equals("")) && !(tempGeneratorLog.getUserName().indexOf("?")!=-1) ) {
//                                    ;
//                                } else {
//                                    generatorLog.setBrokenKoreanIds(tempGeneratorLog.getBrokenKoreanIds());
//                                    generatorLogs.put(generatorLog.getDeviceId(),generatorLog);
//                                }
                            }
                        } else { // 깨진것
//                            GeneratorLog tempGeneratorLog = generatorLogs.get(generatorLog.getId());
//                            if( tempGeneratorLog == null ) {
//                                generatorLog.getBrokenKoreanIds().add(generatorLog.getId());
//                                generatorLogs.put(generatorLog.getDeviceId(), generatorLog);
//                            } else {
//                                tempGeneratorLog.getBrokenKoreanIds().add(generatorLog.getId());
//                            }
                        }
                    }
                    SearchHit lastHitDocument = hits[hits.length - 1];
                    sortValues = lastHitDocument.getSortValues();
System.out.println();
                }
                from++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while(true);
        System.out.println();
        Enumeration enumeration = generatorLogs.keys();
        while(enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            GeneratorLog generatorLog = generatorLogs.get(key);
System.out.println("generatorLog.getDeviceId()-->>"+generatorLog.getDeviceId() + " generatorLog.getUserName()-->>"+generatorLog.getUserName() + " generatorLog.getOtherUserName()-->>"+generatorLog.getOtherUserName() + " generatorLog.getBrokenKoreanIds().size()-->>"+generatorLog.getBrokenKoreanIds().size()  );
        }
System.out.println("generatorLogs.size()-->>"+generatorLogs.size());


        //??




        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    public IData replaceName_new(IData reqIData, IData resIData) {
        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        GenratorLogRepository genratorLogRepository = (GenratorLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(GeneratorLog.class.getSimpleName());

        if(elasticsearchHighLevelClient==null)
            elasticsearchHighLevelClient = new ElasticsearchHighLevelClient();

        int count=1;
        int page=0, size=1000;
        do {
            genratorLogRepository.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());
            DynamicIndexBean.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());

            Pageable pageable = PageRequest.of(page, size);

            Page<GeneratorLog> generatorLogPage = genratorLogRepository.findByUserNameNameUsingCustomQuery("RPA2외환A7",PageRequest.of(page,size));

            if( generatorLogPage.getContent().size() < size )
                break;
            page++;
        }while(true);

        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    private ElasticsearchHighLevelClient elasticsearchHighLevelClient = null;

    public void insertLogFileToElasticsearch(String logDataRootDir) {

        logger.error("begin!");
        logger.error("insertLogFileToElasticsearch logDataRootDir-->"+logDataRootDir);

        try {
            elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);
        } catch(Exception e) {
            //e.printStackTrace();
        }

        if(elasticsearchHighLevelClient==null)
            elasticsearchHighLevelClient = new ElasticsearchHighLevelClient();

        int totalCount=0;
        int errorCount=0;

        File rootFolder= new File(logDataRootDir);
        String[] yyyyMMddhhmSubFolders = rootFolder.list();
        for (int i = 0; i < yyyyMMddhhmSubFolders.length; i++) {
            String dir = yyyyMMddhhmSubFolders[i];
            File file = new File( rootFolder.getAbsolutePath() +"/"+ yyyyMMddhhmSubFolders[i] );
            logger.error("process dir -->> "+rootFolder.getAbsolutePath() +"/"+ yyyyMMddhhmSubFolders[i]); // 20220809200 까지 진행 함 20220809201부터 시작
            List<Document2> document2s = new ArrayList<>();
            for (int j = 0; j < file.list().length; j++) {
//logger.error("process file -->> "+file.getAbsolutePath() + "/" + file.list()[j]);
                if(file.list()[j].equals("ApplicationErrorLog_logmanager_FZ1_20220817000146"))
                    System.out.println();
                String[] contents = new String[0];
                try{
                    StringBuffer content = FileUtil.getFileToStringBuffer(file.getAbsolutePath() + "/" + file.list()[j]);
                    content = new StringBuffer(content.toString().replaceAll("}]\n","#REQUEST_LAST_CHAR#"));
                    contents = content.toString().split("#REQUEST_LAST_CHAR#"); // client에서 요청한 message 수
                    for (int k = 0; k < contents.length; k++)
                        contents[k] = contents[k]+"}]";
                } catch(Exception e) {
                    e.printStackTrace();
                }
                totalCount += contents.length;
                for (int k = 0; k < contents.length; k++) { // line 수대로 document 생성
                    String data = contents[k].trim();
                    try {
                        if(data.equals("")) {

                        } else {
                            GeneratorLog generatorLog = new GeneratorLog(); // 요청한 수만큼 document를 만든다.
                            String[] logNames = file.list()[j].split("_");
                            generatorLog.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase() + "_" + dir.substring(dir.length() - 3, dir.length()));
                            generatorLog.setData(data);
                            generatorLog.setData(generatorLog.getData().replaceAll("\uFEFF", ""));
                            //                        generatorLog.setData(generatorLog.getData().replaceAll("/n", ""));
                            //                        generatorLog.setData(generatorLog.getData().replaceAll("/", ""));
                            //                        generatorLog.setData(generatorLog.getData().replaceAll("'", "\""));

                            //                        if (file.list()[j].indexOf(ApplicationErrorLog.class.getSimpleName()) != -1) {
                            //                            if(generatorLog.getData().indexOf("clientDefragAnalysisResourceData")!=-1)
                            //                                generatorLog.setLogName("ClientDefragAnalysisResourceLog");
                            //                            else
                            //                                generatorLog.setLogName(logNames[0]);
                            //                        } else {
                            //                            generatorLog.setLogName(logNames[0]);
                            //                        }
                            if (generatorLog.getData().indexOf("applicationErrorData") != -1)
                                generatorLog.setLogName("ApplicationErrorLog");
                            else if (generatorLog.getData().indexOf("clientActivePortListResourceData") != -1)
                                generatorLog.setLogName("ClientActivePortListResourceLog");
                            else if (generatorLog.getData().indexOf("clientControlProcessResourceData") != -1)
                                generatorLog.setLogName("ClientControlProcessResourceLog");
                            else if (generatorLog.getData().indexOf("clientDefragAnalysisResourceData") != -1)
                                generatorLog.setLogName("ClientDefragAnalysisResourceLog");
                            else if (generatorLog.getData().indexOf("clientHWInfoResourceData") != -1)
                                generatorLog.setLogName("ClientHWInfoResourceLog");
                            else if (generatorLog.getData().indexOf("clientMBRResourceData") != -1)
                                generatorLog.setLogName("ClientMBRResourceLog");
                            else if (generatorLog.getData().indexOf("clientPerfResourceData") != -1)
                                generatorLog.setLogName("ClientPerformanceLog");
                            else if (generatorLog.getData().indexOf("processCreationLogData") != -1)
                                generatorLog.setLogName("ClientProcessCreationLog");
                            else if (generatorLog.getData().indexOf("clientProcessResourceData") != -1)
                                generatorLog.setLogName("ClientResourceLog");
                            else if (generatorLog.getData().indexOf("clientProgramListResourceData") != -1)
                                generatorLog.setLogName("ClientProgramListResourceLog");
                            else if (generatorLog.getData().indexOf("userTermMonitorLogData") != -1)
                                generatorLog.setLogName("ClientUserTermMonitorLog");
                            else if (generatorLog.getData().indexOf("clientWindowsUpdateListResourceData") != -1)
                                generatorLog.setLogName("ClientWindowsUpdateListResourceLog");
                            else if (generatorLog.getData().indexOf("deviceDriverErrorData") != -1)
                                generatorLog.setLogName("DeviceErrorLog");
                            else if (generatorLog.getData().indexOf("hardwareErrorData") != -1)
                                generatorLog.setLogName("HWErrorLog");
                            else if (generatorLog.getData().indexOf("integrity") != -1)
                                generatorLog.setLogName("IntegrityLog");
                            else if (generatorLog.getData().indexOf("pcOnOffEventLogData") != -1)
                                generatorLog.setLogName("PcOnOffEventLog");
                            else if (generatorLog.getData().indexOf("blueScreenData") != -1)
                                generatorLog.setLogName("WindowsBlueScreenLog");
                            else if (generatorLog.getData().indexOf("collectWindowsEventLogData") != -1)
                                generatorLog.setLogName("WindowsEventSystemErrorAllLog");
                            else
                                generatorLog.setLogName(logNames[0]); // serverresourclog or 기타 ...

                            generatorLog.setCurrentHHmmss(logNames[3].substring(8, logNames[3].length()));
                            generatorLog.setCurrentHH(Integer.parseInt(generatorLog.getCurrentHHmmss().substring(0, 2)));
                            generatorLog.setCurrentMm(Integer.parseInt(generatorLog.getCurrentHHmmss().substring(2, 4)));
                            generatorLog.setCurrentSs(Integer.parseInt(generatorLog.getCurrentHHmmss().substring(4, 6)));
                            generatorLog.setCurrentDate(DateTimeConvertor.getDate(ConstantsTranSaver.TAG_DATE_PATTERN_YYYYMMDDHHMMSS, logNames[3]));

                            data = data.replaceAll("\uFEFF", "");

                            Parse parse = new Parse();
                            JSONArray jSONArray = new JSONArray();
                            try {
                                jSONArray = parse.getJSONArray(new StringBuffer(data));
                            } catch(Exception e) {
//                                e.printStackTrace();
                                errorCount++;
                                logger.error(errorCount+"/"+totalCount+" error file -->> "+file.getAbsolutePath() + "/" + file.list()[j]);
                            }

                            // header 정보 적용을 위한
                            for (Object object : jSONArray) {

                                LinkedHashMap linkedHashMap = (LinkedHashMap) object;
                                generatorLog.setService((String) linkedHashMap.get("service"));
                                generatorLog.setAppId((String) linkedHashMap.get("appId"));
                                generatorLog.setOsType((String) linkedHashMap.get("osType"));
                                generatorLog.setSource((String) linkedHashMap.get("source"));
                                generatorLog.setDeviceId((String) linkedHashMap.get("deviceId"));
                                generatorLog.setDeviceType((String) linkedHashMap.get("deviceType"));
                                generatorLog.setIp((String) linkedHashMap.get("ip"));
                                generatorLog.setHostName((String) linkedHashMap.get("hostName"));
                                generatorLog.setUserId((String) linkedHashMap.get("userId"));
                                generatorLog.setTermNo((String) linkedHashMap.get("termNo"));
                                generatorLog.setSsoBrNo((String) linkedHashMap.get("ssoBrNo"));
                                generatorLog.setBrNo((String) linkedHashMap.get("brNo"));
                                generatorLog.setDeptName((String) linkedHashMap.get("deptName"));
                                generatorLog.setHwnNo((String) linkedHashMap.get("hwnNo"));
                                generatorLog.setUserName((String) linkedHashMap.get("userName"));
                                generatorLog.setSsoType((String) linkedHashMap.get("ssoType"));
                                generatorLog.setPcName((String) linkedHashMap.get("pcName"));
                                generatorLog.setPhoneNo((String) linkedHashMap.get("phoneNo"));
                                generatorLog.setMaxAddress((String) linkedHashMap.get("maxAddress"));
                                generatorLog.setFirstWork((String) linkedHashMap.get("firstWork"));
                                generatorLog.setJKGP((String) linkedHashMap.get("JKGP"));
                                generatorLog.setJKWI((String) linkedHashMap.get("JKWI"));
                                generatorLog.setData(Crypto.base64Encode(generatorLog.getData().getBytes()));
                                try {
                                    if ((generatorLog.getDeptName().indexOf("?")!=-1) || (generatorLog.getUserName().indexOf("?")!=-1) || (generatorLog.getJKWI().indexOf("?")!=-1))
                                        generatorLog.setIsBrokenKorean(true);
                                } catch(Exception e) {
//                                    e.printStackTrace();
                                }
                                document2s.add(generatorLog);

                                break; // header 정보 적용 후 break;

                            } // log array header 정보 적용
                        }
                    } catch (Exception e) {
                        logger.error(data);
                        e.printStackTrace();
                    }
                } // end content array

                // generatorlog_hhM index name 으로 저장
                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                try {
                    if(document2s.size()>0) {
                        BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
                        if (bulkResponse.hasFailures())
                            logger.error(bulkResponse.buildFailureMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // current index device Id ?
                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                for(Document2 document2 : document2s) {
                    GeneratorLog generatorLog = (GeneratorLog) document2;
                    generatorLog.setIndexNameData(generatorLog.getIndexName());
                    generatorLog.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());
                    generatorLog.setData("");
                    generatorLog.setSendDeviceId("");
                }
                try {
                    if(document2s.size()>0) {
                        BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
                        if (bulkResponse.hasFailures())
                            logger.error(bulkResponse.buildFailureMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 초기화
                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                document2s = new ArrayList<>();

            } // end file list
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        } // end date folder
        logger.error("end!");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    // 로그 파일 읽기 -- 시간이 상당히 소요됨.
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    private ArrayList<GeneratorLog> getGeneratorLogs(String rootDir, int hour, int minute, int second) {
//        DecimalFormat decimalFormat = new DecimalFormat("00");
//        ArrayList<GeneratorLog> generatorLogs = new ArrayList<>();
//        File rootFolder= new File(rootDir);
//        String[] yyyyMMddhhmSubFolders = rootFolder.list();
//        for (int i = 0; i < yyyyMMddhhmSubFolders.length; i++) {
//            try {
//                String dir = yyyyMMddhhmSubFolders[i];
//                String checkTime = decimalFormat.format(hour) + decimalFormat.format(minute).substring(0, 1);
//                if( checkTime.equals(dir.substring(dir.length()-3,dir.length())) ) {
//                    File file = new File(rootFolder.getAbsolutePath() + "/" + yyyyMMddhhmSubFolders[i]);
//                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"process dir -->> " + rootFolder.getAbsolutePath() + "/" + yyyyMMddhhmSubFolders[i]);
//                    for (int j = 0; j < file.list().length; j++) {
//                        try{
//                            checkTime = decimalFormat.format(hour)+decimalFormat.format(minute)+decimalFormat.format(second);
//                            if( checkTime.equals(file.list()[j].substring(file.list()[j].length()-6,file.list()[j].length())) ) {
//                                ArrayList<GeneratorLog> tempGeneratorLogs = getGeneratorLogs(file, j);
//                                for ( GeneratorLog generatorLog : tempGeneratorLogs )
//                                    generatorLogs.add(generatorLog);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return generatorLogs;
//    }
    private ArrayList<GeneratorLog> getGeneratorLogs(String rootDir, int hour, int minute) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        ArrayList<GeneratorLog> generatorLogs = new ArrayList<>();
        File rootFolder= new File(rootDir);
        String[] yyyyMMddhhmSubFolders = rootFolder.list();
        for (int i = 0; i < yyyyMMddhhmSubFolders.length; i++) {
            try {
                String dir = yyyyMMddhhmSubFolders[i];
                String checkTime = decimalFormat.format(hour) + decimalFormat.format(minute).substring(0, 1);
                if( checkTime.equals(dir.substring(dir.length()-3,dir.length())) ) {
                    File file = new File(rootFolder.getAbsolutePath() + "/" + yyyyMMddhhmSubFolders[i]);
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"process dir -->> " + rootFolder.getAbsolutePath() + "/" + yyyyMMddhhmSubFolders[i]);
                    for (int j = 0; j < file.list().length; j++) {
                        try{
                            ArrayList<GeneratorLog> tempGeneratorLogs = getGeneratorLogs(file, j);
                            for ( GeneratorLog generatorLog : tempGeneratorLogs )
                                generatorLogs.add(generatorLog);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return generatorLogs;
    }

    private ArrayList<GeneratorLog> getGeneratorLogs(File file, int idx) {

        ArrayList<GeneratorLog> generatorLogs = new ArrayList<>();

        // C:\work\W-EdgeManager\3.development\shinhanbankatop_log_20220812\20220810\prod2_20220810.tar\20220810151/ApplicationErrorLog_logmanager_CVH_20220810151926
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"process file -->> " + file.getAbsolutePath() + "/" + file.list()[idx]);
        String[] contents = new String[0];
        try{
            StringBuffer content = FileUtil.getFileToStringBuffer(file.getAbsolutePath() + "/" + file.list()[idx]);
            content = new StringBuffer(content.toString().replaceAll("}]\n","#REQUEST_LAST_CHAR#"));
            contents = content.toString().split("#REQUEST_LAST_CHAR#"); // client에서 요청한 message 수
            for (int k = 0; k < contents.length; k++)
                contents[k] = contents[k]+"}]";
        } catch(Exception e) {
            e.printStackTrace();
        }
        for (int k = 0; k < contents.length; k++) { // line 수대로 document 생성
            try {
                GeneratorLog generatorLog = new GeneratorLog(); // 요청한 수만큼 document를 만든다.
                String[] logNames = file.list()[idx].split("_");
                generatorLog.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());
                generatorLog.setLogName(logNames[0]);
                generatorLog.setCurrentHHmmss(logNames[3].substring(8,logNames[3].length()));
                generatorLog.setCurrentHH(Integer.parseInt(generatorLog.getCurrentHHmmss().substring(0,2)));
                generatorLog.setCurrentMm(Integer.parseInt(generatorLog.getCurrentHHmmss().substring(2,4)));
                generatorLog.setCurrentSs(Integer.parseInt(generatorLog.getCurrentHHmmss().substring(4,6)));
                generatorLog.setCurrentDate(DateTimeConvertor.getDate(ConstantsTranSaver.TAG_DATE_PATTERN_YYYYMMDDHHMMSS,logNames[3]));
                generatorLog.setData(contents[k].replaceAll("\uFEFF", ""));

                Parse parse = new Parse();
                JSONArray jSONArray = parse.getJSONArray(new StringBuffer(generatorLog.getData()));

                // header 정보 적용을 위한
                for (Object object : jSONArray) {

                    LinkedHashMap linkedHashMap = (LinkedHashMap) object;
                    generatorLog.setService((String) linkedHashMap.get("service"));
                    generatorLog.setAppId((String) linkedHashMap.get("appId"));
                    generatorLog.setOsType((String) linkedHashMap.get("osType"));
                    generatorLog.setSource((String) linkedHashMap.get("source"));
                    generatorLog.setDeviceId((String) linkedHashMap.get("deviceId"));
                    generatorLog.setDeviceType((String) linkedHashMap.get("deviceType"));
                    generatorLog.setIp((String) linkedHashMap.get("ip"));
                    generatorLog.setHostName((String) linkedHashMap.get("hostName"));
                    generatorLog.setUserId((String) linkedHashMap.get("userId"));
                    generatorLog.setTermNo((String) linkedHashMap.get("termNo"));
                    generatorLog.setSsoBrNo((String) linkedHashMap.get("ssoBrNo"));
                    generatorLog.setBrNo((String) linkedHashMap.get("brNo"));
                    generatorLog.setDeptName((String) linkedHashMap.get("deptName"));
                    generatorLog.setHwnNo((String) linkedHashMap.get( "hwnNo"));
                    generatorLog.setUserName((String) linkedHashMap.get("userName"));
                    generatorLog.setSsoType((String) linkedHashMap.get("ssoType"));
                    generatorLog.setPcName((String) linkedHashMap.get("pcName"));
                    generatorLog.setPhoneNo((String) linkedHashMap.get("phoneNo"));
                    generatorLog.setMaxAddress((String) linkedHashMap.get("maxAddress"));
                    generatorLog.setFirstWork((String) linkedHashMap.get("firstWork"));
                    generatorLog.setJKGP((String) linkedHashMap.get("JKGP"));
                    generatorLog.setJKWI((String) linkedHashMap.get("JKWI"));
                    generatorLog.setData(Crypto.base64Encode(generatorLog.getData().getBytes()));

//                            if (file.list()[j].indexOf(ApplicationErrorLog.class.getSimpleName()) != -1) {
//                                ApplicationError applicationError = new ApplicationError();
//                                ApplicationErrorLogOriginal applicationErrorLogOriginal = applicationError.getOriginalObject((LinkedHashMap) object);
//                            } else if (file.list()[j].indexOf(ClientActivePortListResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientControlProcessResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientDefragAnalysisResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientHWInfoResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientMBRResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientPerformanceResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientProcessCreationLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientProcessResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientProgramListResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientUserTermMonitorLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ClientWindowsUpdateListResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(DeviceErrorLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(HWErrorLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(IntegrityLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(PcOnOffEventLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(ServerResourceLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(WindowsBlueScreenLog.class.getSimpleName()) != -1) {
//
//                            } else if (file.list()[j].indexOf(WindowsEventSystemErrorAllLog.class.getSimpleName()) != -1) {
//
//                            } else {
//
//                            }
                    generatorLogs.add(generatorLog);
                    break; // header 정보 적용 후 break;
                } // log array header 정보 적용
            } catch (Exception e) {
                e.printStackTrace();
            }
        } // end content array
        return generatorLogs;
    }

    // 로그 전송
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void sendLog(ArrayList<GeneratorLog> generatorLogs) {
        for(GeneratorLog generatorLog : generatorLogs) {
            String sendUrl = "";
            try {
                if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlApplicationErrorLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlApplicationErrorLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientActivePortListResourcelog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientActivePortListResourcelog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientHWInfoResourceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientHWInfoResourceLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientMBRResourceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientMBRResourceLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientPerformanceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientPerformanceLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientProcessCreationLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientProcessCreationLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientProgramListResourceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientProgramListResourceLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientResourceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientResourceLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientUserTermMonitorLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientUserTermMonitorLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlIntegrityLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlIntegrityLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlPcOnOffEventLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlPcOnOffEventLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlServerResourceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlServerResourceLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlWindowsEventSystemErrorAllLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlWindowsEventSystemErrorAllLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlHWErrorLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlHWErrorLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlDevicEerrorLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlDevicEerrorLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlWindowsBlueScreenLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlWindowsBlueScreenLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientDefraganAlysisResourceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientDefraganAlysisResourceLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientWindowsUpdateListResourceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientWindowsUpdateListResourceLog();
                } else if (generatorLog.getLogName().indexOf(getLogName(Config.getInstance().getLog().getRequestLogUrlClientControlProcessResourceLog())) != -1) {
                    sendUrl = Config.getInstance().getLog().getRequestLogUrlClientControlProcessResourceLog();
                }
                if(sendUrl.equals("")) {
                    logger.error("not found url  generatorLog.getLogName() -->>"+generatorLog.getLogName());
                } else {
                    HttpClient httpClient = new HttpClient();
                    httpClient.sendRequestByJSON2(sendUrl, generatorLog.getData(), 3000, "application/json;charset=utf-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getLogName(String url) {
        String ret="";
        try {
            ret = url.substring(url.lastIndexOf("/") + 1, url.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }







    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public LogGenerator() {
        if(elasticsearchHighLevelClient==null) {
            if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) &&
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) )
                elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("192.168.79.100", 9203);
            else if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) &&
                    Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) )
                elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("3.38.208.73", 9202);
        }
    }

    public static void main(String[] argv) {

        LogGenerator logGenerator = new LogGenerator();
//        logGenerator.replaceName(new NodeData(),new NodeData());

        GeneratorLog generatorLog = new GeneratorLog();

        generatorLog.setUserName("RPA2여신E2");

        generatorLog.setDeptName(null);
        generatorLog.setDeptName("HR부소속(노동조합)");
//        logGenerator.replaceDeptName(generatorLog);

        generatorLog.setUserId("");
        generatorLog.setUserId(null);
        generatorLog.setUserId("99119");
//        logGenerator.replaceUserId(generatorLog);

        generatorLog.setHostName("H6A-0018-4144B");

        generatorLog.setData(" ShinHan Report Designer 5.0 EXE/OCX Viewer(5,0,0,522) ");
        logGenerator.hiddenShinhhanInfo(generatorLog);

System.out.println(generatorLog.getOtherUserName());
System.out.println(generatorLog.getOtherDeptName());
System.out.println(generatorLog.getOtherUserId());
System.out.println(generatorLog.getOtherHostName());
System.out.println(generatorLog.getData());

    }

    public static void main3(String argv[]) {
        LogGenerator logGenerator = new LogGenerator();
        String dir = "C:\\work\\W-EdgeManager\\3.development\\shinhanbankatop_log_20220825_1\\";
        int hour = DateUtil.getHour();
        int minute = DateUtil.getMinute();
        int second = DateUtil.getSecond();
        ArrayList<GeneratorLog> generatorLogs = logGenerator.getGeneratorLogs(dir,hour,minute);
        System.out.println("generatorLogs.size()-->>"+generatorLogs.size());
    }
    public static void main2(String argv[]) {

        String logDataRootDir = "C:\\work\\W-EdgeManager\\3.development\\shinhanbankatop_log_20220825_1\\";
        LogGenerator logGenerator = new LogGenerator();

        IData resIData = new NodeData();
        IData reqIData = new NodeData();

        IData header = new NodeData();
        header.put(Constants.TAG_HEADER_DEVICEID, new SimpleData("asfjio332ojf91"));

        IData body = new NodeData();
        body.put(ConstantsLog.TAG_CURRENT_DEVICE_COUNT, new SimpleData(1));

        reqIData.put(Constants.TAG_HEADER, header);
        reqIData.put(Constants.TAG_BODY, body);

//        logGenerator.startLogSender(reqIData, resIData);

        try{ Thread.sleep(10000); } catch(Exception e) {}

    }
    public static void main_4(String argv[]) {
//        String logDataRootDir = "C:\\work\\W-EdgeManager\\3.development\\shinhanbankatop_log_20220828_5";
//        LogGenerator_20220831 logGenerator = new LogGenerator_20220831();
//        logGenerator.insertLogFileToElasticsearch(logDataRootDir);
    }

}
