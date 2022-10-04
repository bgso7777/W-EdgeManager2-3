package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.IntegrityLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IntegrityLogRepository extends ElasticsearchRepository<IntegrityLog, String>,Document2Repository<IntegrityLog> {

    IntegrityLog findByUserId(String userId);
    IntegrityLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);

}