package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.HWErrorLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface HWErrorLogRepository extends ElasticsearchRepository<HWErrorLog, String>,Document2Repository<HWErrorLog> {

    HWErrorLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);

}