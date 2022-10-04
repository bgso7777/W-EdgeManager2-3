package com.inswave.appplatform.log.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.log.processor.MessageRuleProcessorManager;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public final class IntegrityLogRuleListener {

    private static final Logger logger = LoggerFactory.getLogger(IntegrityLogRuleListener.class);
    private static final String topicName="IntegrityLog",groupId="IntegrityLogRealtimeGroupId";

    /**
     * groupId가 WAS별로 달라야 WAS별로 동일한 로그를 저장 한다.
     * @param consumerRecord
     * @param consumer
     */
    @KafkaListener(topics=topicName,groupId=groupId,autoStartup="${kafka.listener.integritylogrulelistener.enabled}",containerFactory="kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> consumerRecord, Consumer<String, String> consumer) {
        try{
            MessageRuleProcessorManager.getInstance().processIntegrityLog(consumerRecord.value());
            consumer.commitSync();
        } catch (
        JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}
