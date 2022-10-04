package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProgramListResourceLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientProgramListResourceLogRepository extends ElasticsearchRepository<ClientProgramListResourceLog, String>,Document2Repository<ClientProgramListResourceLog> {

    ClientProgramListResourceLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);

}