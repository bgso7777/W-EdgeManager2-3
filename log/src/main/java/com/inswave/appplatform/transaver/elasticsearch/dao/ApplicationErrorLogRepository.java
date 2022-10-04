package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ApplicationErrorLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ApplicationErrorLogRepository extends ElasticsearchRepository<ApplicationErrorLog, String>,Document2Repository<ApplicationErrorLog> {

    ApplicationErrorLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}