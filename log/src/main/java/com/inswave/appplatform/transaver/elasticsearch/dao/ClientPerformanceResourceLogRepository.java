package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientPerformanceResourceLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientPerformanceResourceLogRepository extends ElasticsearchRepository<ClientPerformanceResourceLog, String>,Document2Repository<ClientPerformanceResourceLog> {

//    Page<Document> findByTimeCreatedGreaterThanAndTimeCreatedLessThan(Date from, Date to, Pageable pageable);
//    Page<Document> findByTimeCreatedGreaterThanAndTimeCreatedLessThanAndIp(Date from, Date to, String ip, Pageable pageable);

//    List<ClientPerformanceResourceLog> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);
//
//    Page<Document> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to, Pageable pageable);
//    Page<Document> findByTimeCurrentGreaterThanAndTimeCurrentLessThanAndIp(String from, String to, String ip, Pageable pageable);

    ClientPerformanceResourceLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);

//    Page<Document> findByTimeCreatedGreaterThanAndTimeCreatedLessThan(long from, long to, Pageable pageable);
//    Page<Document> findByTimeCreatedGreaterThanAndTimeCreatedLessThanAndIp(long from, long to, String ip, Pageable pageable);
}