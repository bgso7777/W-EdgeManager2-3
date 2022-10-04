package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientDefragAnalysisResourceLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientDefragAnalysisResourceLogRepository extends ElasticsearchRepository<ClientDefragAnalysisResourceLog, String>,Document2Repository<ClientDefragAnalysisResourceLog> {

    ClientDefragAnalysisResourceLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}