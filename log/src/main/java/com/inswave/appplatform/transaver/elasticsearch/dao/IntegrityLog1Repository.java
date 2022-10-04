package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.IntegrityLog1;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IntegrityLog1Repository extends ElasticsearchRepository<IntegrityLog1, String>,Document2Repository<IntegrityLog1> {

    IntegrityLog1 findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);

}