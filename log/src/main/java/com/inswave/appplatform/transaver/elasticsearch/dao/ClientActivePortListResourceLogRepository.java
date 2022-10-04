package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientActivePortListResourceLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientActivePortListResourceLogRepository extends ElasticsearchRepository<ClientActivePortListResourceLog, String>,Document2Repository<ClientActivePortListResourceLog> {

    ClientActivePortListResourceLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}