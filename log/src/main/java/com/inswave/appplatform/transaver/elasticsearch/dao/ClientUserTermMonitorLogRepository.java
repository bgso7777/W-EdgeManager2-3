package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientUserTermMonitorLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientUserTermMonitorLogRepository extends ElasticsearchRepository<ClientUserTermMonitorLog, String>,Document2Repository<ClientUserTermMonitorLog> {

    ClientUserTermMonitorLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}