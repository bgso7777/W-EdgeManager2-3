package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientWindowsUpdateListResourceLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientWindowsUpdateListResourceLogRepository extends ElasticsearchRepository<ClientWindowsUpdateListResourceLog, String>,Document2Repository<ClientWindowsUpdateListResourceLog> {

    ClientWindowsUpdateListResourceLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}