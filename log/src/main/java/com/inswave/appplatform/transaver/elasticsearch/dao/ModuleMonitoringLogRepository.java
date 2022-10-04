package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ModuleMonitoringLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ModuleMonitoringLogRepository extends ElasticsearchRepository<ModuleMonitoringLog, String>,Document2Repository<ModuleMonitoringLog> {

    List<ModuleMonitoringLog> findByKey(String Key);
    void deleteByKey(String key);
    void deleteByTopicNameAndGroupId(String topicName, String groupId);
}