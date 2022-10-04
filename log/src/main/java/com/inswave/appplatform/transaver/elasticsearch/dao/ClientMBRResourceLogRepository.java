package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientMBRResourceLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientMBRResourceLogRepository extends ElasticsearchRepository<ClientMBRResourceLog, String>,Document2Repository<ClientMBRResourceLog> {

    ClientMBRResourceLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);

}