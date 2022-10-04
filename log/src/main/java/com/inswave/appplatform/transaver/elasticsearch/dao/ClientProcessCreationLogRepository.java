package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProcessCreationLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientProcessCreationLogRepository extends ElasticsearchRepository<ClientProcessCreationLog, String>,Document2Repository<ClientProcessCreationLog> {

    ClientProcessCreationLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);

}