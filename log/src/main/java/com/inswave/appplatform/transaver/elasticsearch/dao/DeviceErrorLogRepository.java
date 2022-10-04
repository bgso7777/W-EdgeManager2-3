package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.DeviceErrorLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DeviceErrorLogRepository extends ElasticsearchRepository<DeviceErrorLog, String>,Document2Repository<DeviceErrorLog> {

    DeviceErrorLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}