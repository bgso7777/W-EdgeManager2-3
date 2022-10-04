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
public final class ClientProgramListResourceLogRuleListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientProgramListResourceLogRuleListener.class);
    private static final String topicName="ClientProgramListResourceLog",groupId="ClientProgramListResourceLogRealtimeGroupId";

    /**
     * groupId가 WAS별로 달라야 WAS별로 동일한 로그를 저장 한다.
     * @param consumerRecord
     * @param consumer
     */
    @KafkaListener(topics=topicName,groupId=groupId,autoStartup="${kafka.listener.clientprogramlistresourcelogrulelistener.enabled}",containerFactory="kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> consumerRecord, Consumer<String, String> consumer) {
        try{
            MessageRuleProcessorManager.getInstance().processClientProgramListResourceLog(consumerRecord.value());
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
