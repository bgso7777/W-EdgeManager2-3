package com.inswave.appplatform.log.service;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.service.ExternalService;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.ServerResourceLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLog;
import com.inswave.appplatform.transaver.kafka.ClientProcessCreationLogSaveListener;
import com.inswave.appplatform.transaver.kafka.ServerResourceLogSaveListener;
import com.inswave.appplatform.transaver.util.BeanUtils;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KafkaListenerController implements ExternalService {

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

    public void init() {
    }

    public IData start(IData reqIData, IData resIData) {

        int result = Constants.RESULT_SUCESS;

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String groupId = (String) reqIData.getBodyValue(Constants.TAG_GROUP_ID);

        try{
            result = start(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        body.put(Constants.TAG_RESULT, new SimpleData(result));

        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    public int start(String groupId) {

        JavaMelodyMonitor.printInfo("KafkaListenerController", "start" );
        JavaMelodyMonitor.printInfo("KafkaListenerController", "groupId-->>"+groupId );

        AtomicInteger result = new AtomicInteger(Constants.RESULT_ID_NOT_FOUND);

        init();

        KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry = BeanUtils.getBean(KafkaListenerEndpointRegistry.class);
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(messageListenerContainer -> {
            if(groupId.equals(messageListenerContainer.getGroupId())) {
                messageListenerContainer.start();
                result.set(Constants.RESULT_SUCESS);
            }
        });

        return result.get();
    }


    public IData stop(IData reqIData, IData resIData) {

        int result = Constants.RESULT_SUCESS;

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String groupId = (String) reqIData.getBodyValue(Constants.TAG_GROUP_ID);

        try{
            result = stop(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        body.put(Constants.TAG_RESULT, new SimpleData(result));

        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    public int stop(String groupId) {

        JavaMelodyMonitor.printInfo("KafkaListenerController", "stop" );
        JavaMelodyMonitor.printInfo("KafkaListenerController", "groupId-->>"+groupId );

        AtomicInteger result = new AtomicInteger(Constants.RESULT_ID_NOT_FOUND);

        init();

        KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry = BeanUtils.getBean(KafkaListenerEndpointRegistry.class);
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(messageListenerContainer -> {
            if(groupId.equals(messageListenerContainer.getGroupId())) {
                messageListenerContainer.stop();
                result.set(Constants.RESULT_SUCESS);
            }
        });

        return result.get();
    }

    public IData set(IData reqIData, IData resIData) {

        int result = Constants.RESULT_SUCESS;

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String groupId = (String) reqIData.getBodyValue(Constants.TAG_GROUP_ID);
        String methodName = (String) reqIData.getBodyValue(Constants.TAG_METHOD_NAME);
        Object value = (Object) reqIData.getBodyValue(Constants.TAG_VALUE);

        try{
            result = set(groupId,methodName,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        body.put(Constants.TAG_RESULT, new SimpleData(result));

        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    public int set(String groupId, String methodName, Object value) {

        JavaMelodyMonitor.printInfo("KafkaListenerController", "set" );
        JavaMelodyMonitor.printInfo("KafkaListenerController", "groupId-->>"+groupId );

        AtomicInteger result = new AtomicInteger(Constants.RESULT_ID_NOT_FOUND);

        KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry = BeanUtils.getBean(KafkaListenerEndpointRegistry.class);
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(messageListenerContainer -> {
            if(groupId.equals(messageListenerContainer.getGroupId())) {
                if(methodName.equals(Constants.TAG_SET_IDLE_BETWEEN_POLLS)) {
                    JavaMelodyMonitor.printInfo("KafkaListenerController", "value-->>"+(Long)value);
                    messageListenerContainer.stop();
                    messageListenerContainer.getContainerProperties().setIdleBetweenPolls((Long)value);
                    messageListenerContainer.start();
                    result.set(Constants.RESULT_SUCESS);
                }
            }
        });
        return result.get();
    }

    public Object excute() {
        int result = Constants.RESULT_SUCESS;
        return result;
    }

    /**
     * 7:00 ~ 18:00  3분마다 프로세스 실행 이력의 ms을 조절한다. (전체 시스템의 상태(???)를 체크하여 ....)
     */
    public Object checkShinhanbankAtopCreationLogIdleBetweenPollsMs() {

        int result = Constants.RESULT_ID_NOT_FOUND;
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "excute");
        init();

        // 시간, elasticsearch cpu사용률, 실시간성 로그의 lag값(windowevent, performancelog, processresourclog)에 따른 idleBetweenPollsMs을 계산 한다.
        //idleBetweenPollsMs=??

        String deviceId1 = "localhost.localdomain";
        String deviceId2 = "localhost.localdomain";

        if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)) {
            if( Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) ) {
                deviceId1 = "atopdb1p"; // ??
                deviceId2 = "atopdb2p"; // ??
            } else if( Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_PRODUCT) ) {
                deviceId1 = "atopdb1p";
                deviceId2 = "atopdb2p";
            }
        } else if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE)) {
            if( Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) ) {
                deviceId1 = "localhost.localdomain";
                deviceId2 = "localhost.localdomain";
            } else if( Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) ) {
                deviceId1 = "atopdb1p";
                deviceId2 = "atopdb2p";
            }
        }

        ServerResourceLogRepository serverResourceLogRepository = (ServerResourceLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(ServerResourceLog.class.getSimpleName());
        ServerResourceLog serverResourceLog1 = serverResourceLogRepository.findByDeviceId(deviceId1);
        ServerResourceLog serverResourceLog2 = serverResourceLogRepository.findByDeviceId(deviceId2);

        Long cpuThreshold = Config.getInstance().getLog().getKafkaListenerControllerDecreseCpuThreshold();
        Double cpuUsage1 = serverResourceLog1.getCpuUsage();
        Double cpuUsage2 = serverResourceLog2.getCpuUsage();

        Long min = Config.getInstance().getLog().getClientProcessCreationLogSaveListenerMinIdleBetweenPollsMs();
        Long max = Config.getInstance().getLog().getClientProcessCreationLogSaveListenerMaxIdleBetweenPollsMs();
        Long pollsMs = Config.getInstance().getLog().getClientProcessCreationLogSaveListenerIdleBetweenPollsMs();

        // process creation
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "ClientProcessCreationLog");
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "min-->>"+min);
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "max-->>"+max);
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "pollsMs-->>"+pollsMs);
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "cpuThreshold-->>"+cpuThreshold);
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "cpuUsage1-->>"+cpuUsage1);
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "cpuUsage2-->>"+cpuUsage2);

        Config.getInstance().getLog().getWindowsEventSystemErrorAllLogSaveListenerIdleBetweenPollsMs();
        Config.getInstance().getLog().getWindowsEventSystemErrorAllLogSaveListenerMinIdleBetweenPollsMs();
        Config.getInstance().getLog().getWindowsEventSystemErrorAllLogSaveListenerMaxIdleBetweenPollsMs();
        Config.getInstance().getLog().getWindowsEventSystemErrorAllLogSaveListenerDecreseIdleBetweenPollsMs();

        // ?????????????
//        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "groupId -->> " + ClientProcessCreationLogSaveListener.groupId);
//        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "idleBetweenPollsMs -->> " + idleBetweenPollsMs);
//        String groupId = ClientProcessCreationLogSaveListener.groupId;
//        if( Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) )
//            groupId = ServerResourceLogSaveListener.groupId;
//        set(ClientProcessCreationLogSaveListener.groupId, Constants.TAG_IDLE_BETWEEN_POLLS, idleBetweenPollsMs);

        return result;
    }

    public Object startShinhanbankAtopCreationLogIdleBetweenPollsMs() {

        JavaMelodyMonitor.printInfo("KafkaListenerController", "startShinhanbankAtopCreationLogIdleBetweenPollsMs" );

        int result = Constants.RESULT_ID_NOT_FOUND;
        init();

        JavaMelodyMonitor.printInfo("KafkaListenerController", "clientProcessCreationLogSaveListenerIdleBetweenPolls-->>"+ Config.getInstance().getLog().getClientProcessCreationLogSaveListenerIdleBetweenPollsMs() );
        Config.getInstance().getLog().setClientProcessCreationLogSaveListenerIdleBetweenPollsMs(  Config.getInstance().getLog().getClientProcessCreationLogSaveListenerMinIdleBetweenPollsMs()  );
        JavaMelodyMonitor.printInfo("KafkaListenerController", "clientProcessCreationLogSaveListenerIdleBetweenPolls-->>"+ Config.getInstance().getLog().getClientProcessCreationLogSaveListenerIdleBetweenPollsMs() );
        String groupId = ClientProcessCreationLogSaveListener.groupId;
        String methodName = Constants.TAG_SET_IDLE_BETWEEN_POLLS;

        if( Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) &&
            Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) )
            groupId = ServerResourceLogSaveListener.groupId;

        result = set(groupId,methodName,Config.getInstance().getLog().getClientProcessCreationLogSaveListenerIdleBetweenPollsMs());

        JavaMelodyMonitor.printInfo("KafkaListenerController", "result-->>"+ result );

        return result;
    }

    public Object stopShinhanbankAtopCreationLogIdleBetweenPollsMs() {

        JavaMelodyMonitor.printInfo("KafkaListenerController", "stopShinhanbankAtopCreationLogIdleBetweenPollsMs" );

        int result = Constants.RESULT_ID_NOT_FOUND;
        init();

        JavaMelodyMonitor.printInfo("KafkaListenerController", "clientProcessCreationLogSaveListenerIdleBetweenPollsMs-->>"+ Config.getInstance().getLog().getClientProcessCreationLogSaveListenerIdleBetweenPollsMs() );
        Config.getInstance().getLog().setClientProcessCreationLogSaveListenerIdleBetweenPollsMs(  Config.getInstance().getLog().getClientProcessCreationLogSaveListenerMaxIdleBetweenPollsMs()  );
        JavaMelodyMonitor.printInfo("KafkaListenerController", "clientProcessCreationLogSaveListenerIdleBetweenPollsMs-->>"+ Config.getInstance().getLog().getClientProcessCreationLogSaveListenerIdleBetweenPollsMs() );
        String groupId = ClientProcessCreationLogSaveListener.groupId;
        String methodName = Constants.TAG_SET_IDLE_BETWEEN_POLLS;

        if( Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) &&
                Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) )
            groupId = ServerResourceLogSaveListener.groupId;

        result = set(groupId,methodName,Config.getInstance().getLog().getClientProcessCreationLogSaveListenerIdleBetweenPollsMs());

        JavaMelodyMonitor.printInfo("KafkaListenerController", "result-->>"+ result );

        return result;
    }
}
