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
public final class ClientUserTermMonitorLogRuleListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientUserTermMonitorLogRuleListener.class);
    private static final String topicName="ClientUserTermMonitorLog",groupId="ClientUserTermMonitorLogRealtimeGroupId";

    /**
     * groupId가 WAS별로 달라야 WAS별로 동일한 로그를 저장 한다.
     * @param consumerRecord
     * @param consumer
     */
    @KafkaListener(topics=topicName,groupId=groupId,autoStartup="${kafka.listener.clientusertermmonitorlogrulelistener.enabled}",containerFactory="kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> consumerRecord, Consumer<String, String> consumer) {
        try{
            MessageRuleProcessorManager.getInstance().processClientUserTermMonitorLog(consumerRecord.value());
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