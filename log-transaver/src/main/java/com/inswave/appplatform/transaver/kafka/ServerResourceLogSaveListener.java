package com.inswave.appplatform.transaver.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.manager.ModuleMonitoringManager;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.monitor.KafkaListenerMonitor;
import com.inswave.appplatform.log.monitor.LoggerMonitor;
import com.inswave.appplatform.transaver.saver.ServerResourceLogger;
import com.inswave.appplatform.util.DateUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public final class ServerResourceLogSaveListener extends KafkaListenerMonitor {

    @Autowired
    ServerResourceLogger serverResourceLogger;

    public static final String topicName = "ServerResourceLog";
    public static final String groupId = "ServerResourceLogSaverGroup1";
    public static Long idleBetweenPollsMs = 0L;

    @KafkaListener(topics=topicName,groupId=groupId,autoStartup="${kafka.listener.serverresourcelogsavelistener.enabled}",containerFactory="kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> consumerRecord, Consumer<String, String> consumer) {

        super.begin();
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"begin");

        try {
            super.setTimeCurrent(DateUtil.getCurrentDateElasticsearch(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z));
            super.setYear(DateUtil.getYear());
            super.setMonth(DateUtil.getMonth());
            super.setDay(DateUtil.getDay());
            super.setDayOfWeek(DateUtil.getDayOfWeek());
            super.setHour(DateUtil.getHour());
            super.setMinutely10(DateUtil.getMinute10());

            super.setInstanceId(Config.getInstance().getLog().getInstanceId());
            super.setTopicName(topicName);
            super.setGroupId(groupId);
            super.setPartitionId(consumerRecord.partition());
            super.setOffset(consumerRecord.offset());

            serverResourceLogger.insert("ServerResourceLog", consumerRecord.value());

            super.setProcessByte((long) consumerRecord.value().getBytes().length);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            serverResourceLogger.setJsonProcessingException(true);
        } catch (Exception e) {
            e.printStackTrace();
            serverResourceLogger.setException(true);
        } finally {
            try {
                consumer.commitSync();
            } catch (Exception e) {
                e.printStackTrace();
                serverResourceLogger.setCommitSyncException(true);
            } finally {
            }
        }
        super.end();
        JavaMelodyMonitor.printInfo( this.getClass().getSimpleName(),"end runningTime-->"+super.getRunningTime()+ " processCount->"+super.getProcessCount()+" bps-->"+super.getBps()+" bpms-->"+super.getBpms()+" processByte->"+super.getProcessByte());

        if(Config.getInstance().getLog().getIsUseModuleMonitoring()) {
            try {
                LoggerMonitor loggerMonitor = serverResourceLogger.cloneLoggerMonitor();
                loggerMonitor.setInstanceId(Config.getInstance().getLog().getInstanceId());
                loggerMonitor.setTopicName(topicName);
                loggerMonitor.setGroupId(groupId);
                ModuleMonitoringManager.getInstance().addLoggerMonitor(loggerMonitor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Map<TopicPartition, Long> beginningOffsets = consumer.beginningOffsets(consumer.assignment());
                Map<TopicPartition, Long> endOffsets = consumer.endOffsets(consumer.assignment());
                Set<TopicPartition> assignment = consumer.assignment();
                List<PartitionInfo> partitionsFor = consumer.partitionsFor(groupId);

                Long tempEndOffsets = 0L;
                for (TopicPartition topicPartition : endOffsets.keySet()) {
                    tempEndOffsets += endOffsets.get(topicPartition);
                }
                super.setEndOffsets(tempEndOffsets);

                ModuleMonitoringManager.getInstance().addKafkaListenerMonitor(super.cloneKafkaListenerMonitor());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}