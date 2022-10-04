package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientControlProcessResourceLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientControlProcessResourceLogRepository extends ElasticsearchRepository<ClientControlProcessResourceLog, String>,Document2Repository<ClientControlProcessResourceLog> {

    ClientControlProcessResourceLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}