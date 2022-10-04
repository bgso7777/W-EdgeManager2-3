package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.Document;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;

public interface ServerResourceLogRepository extends ElasticsearchRepository<ServerResourceLog, String> ,Document2Repository<ServerResourceLog> {

    Page<ServerResourceLog> findByTimeCreated(Pageable pageable);

    List<ServerResourceLog> findBySource(String source);
    List<ServerResourceLog> findTopBySource(String source);

    List<ServerResourceLog> queryTop10BySource(String source, Sort.Direction desc);

    //List<ServerResourceLog> findTop10BySourceOrderByTimeCreateDesc(String source); x

    //List<ServerResourceLog> findTop10BySourceOrderByTimeCreate(String source); x

    //List<ServerResourceLog> findByTimeCreate(Date date);

    List<ServerResourceLog> findByTimeCreatedBetweenAndTimeCreated(String from, String to);
    //List<ServerResourceLog> findByTimeCreatedGreaterThanAndTimeCreatedLessThan
    //Page<ServerResourceLog> findByTimeCreatedBetweenAndTimeCreated(Date from, Date to, Pageable pageable);
    //Page<ServerResourceLog> findByTimeCreatedBetweenAndTimeCreated(String from, String to, Pageable pageable);

    Page<Document2> findByTimeCreatedGreaterThanAndTimeCreatedLessThan(Date from, Date to, Pageable pageable);
    Page<Document> findByTimeCreatedGreaterThanAndTimeCreatedLessThan(String from, String to, Pageable pageable);
    //Page<Document> findByTimeCreatedGreaterThanAndTimeCreatedLessThan(long from, long to, Pageable pageable);

//    public interface CustomServerResourceLogRepository<T> {
//        <S extends T> S save(S entity);
//    }

    ServerResourceLog findByDeviceId(String deviceId);

    void deleteByDeviceId(String deviceId);
}