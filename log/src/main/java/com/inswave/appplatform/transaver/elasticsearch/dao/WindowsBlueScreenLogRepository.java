package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.WindowsBlueScreenLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WindowsBlueScreenLogRepository extends ElasticsearchRepository<WindowsBlueScreenLog, String>,Document2Repository<WindowsBlueScreenLog> {

    WindowsBlueScreenLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}