package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.PcOnOffEventLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PcOnOffEventLogRepository extends ElasticsearchRepository<PcOnOffEventLog, String>,Document2Repository<PcOnOffEventLog> {

    PcOnOffEventLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}