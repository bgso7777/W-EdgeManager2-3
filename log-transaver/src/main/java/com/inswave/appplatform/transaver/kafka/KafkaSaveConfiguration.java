package com.inswave.appplatform.transaver.kafka;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaSaveConfiguration {

    @Value("${kafka.servers}")
    private String kafkaServers;

    @Value("${kafka.curreny.count}")
    private int kafkaCurrenyCount=0;

    @Value("${kafka.max.poll.interval.ms}")
    private int maxPollIntervalMs=0;

    @Value("${kafka.max.poll.records}")
    private int maxPollRecords=0;

    @Value("${kafka.group.instance.id}")
    private String groupInstanceId="";

    @Value("${kafka.request.timeout.ms}")
    private int requestTimeoutMs=0;

    @Value("${kafka.windowseventsystemerroralllogsavelistener.idle.between.polls.ms}")
    private int windowsEventSystemErrorAllLogSaveListenerIdleBetweenPolls=3000;

    @Value("${kafka.clientprocesscreationlogsavelistener.idle.between.polls.ms}")
    private int clientProcessCreationLogSaveListenerIdleBetweenPolls=3000;

    @Bean
    public ProducerFactory<String, String> producerFactoryString() {
        Map<String, Object> configProps = new HashMap<>();
        Config.getInstance().getLog().setKafkaServers(kafkaServers); // AppplatformSpringApplication class의 seed데이터 보다 먼저 호출됨.
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateString() {
        return new KafkaTemplate<>(producerFactoryString());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        if(kafkaCurrenyCount>0)
            factory.setConcurrency(kafkaCurrenyCount);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        // 로그별 listener가 있으므로 무시됨.
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "cgroup1");

        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // 자동 오프셋 커밋 여부
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 자동 오프셋 커밋일 때 interval 시간
        //configProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);

        // 11분 컨슈머가 polling하고 commit 할때까지의 대기시간 -->> 최대 로그 처리 시간을 감안.
        // 이시간에 처리하지 못하면 consumer group에서 탈락하며, Listener로 메시지가 계속 들어 온다.
        // 이때 해당 컨슈머 그룹의 클라이언트들이 다 사라진다.
        if(maxPollIntervalMs>0)
            configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,maxPollIntervalMs);

        // 1 recode씩 받아 온다. (처리중 에러 시) 기본값:500
        if(maxPollRecords>0)
            configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,maxPollRecords);

        // 인스턴스 그룹 아이디 기본값 없음 (없으면 강제로 생성함)
        if(groupInstanceId.equals("")) {
            groupInstanceId = "transaver"+StringUtil.getRandomString(3);
        } else {
            if (groupInstanceId.indexOf("_000") != -1) {
                groupInstanceId = groupInstanceId.replaceAll("000", StringUtil.getRandomString(3));
            }
        }
        Config.getInstance().getLog().setInstanceId(groupInstanceId);
        configProps.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, groupInstanceId);

        // 10분
        // 요청에 대해 응답을 기다리는 최대 시간  기본값:30000
        if(requestTimeoutMs>0)
            configProps.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG,requestTimeoutMs);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

// log manager 실행 시 주석처리.
// WindowsEventSystemErrorAllLogSaveListener의 containerFactory="kafkaListenerContainerFactory" 교체
// ClientProcessCreationLogSaveListener의 containerFactory="kafkaListenerContainerFactory" 교체
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaWindowsEventSystemErrorAllLogSaveListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        if(kafkaCurrenyCount>0)
            factory.setConcurrency(kafkaCurrenyCount);
        factory.setConsumerFactory(consumerFactory());
        if(windowsEventSystemErrorAllLogSaveListenerIdleBetweenPolls>0)
            factory.getContainerProperties().setIdleBetweenPolls(windowsEventSystemErrorAllLogSaveListenerIdleBetweenPolls);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaClientProcessCreationLogSaveListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        if(kafkaCurrenyCount>0)
            factory.setConcurrency(kafkaCurrenyCount);
        factory.setConsumerFactory(consumerFactory());
        if(clientProcessCreationLogSaveListenerIdleBetweenPolls>0)
            factory.getContainerProperties().setIdleBetweenPolls(clientProcessCreationLogSaveListenerIdleBetweenPolls);
        return factory;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}