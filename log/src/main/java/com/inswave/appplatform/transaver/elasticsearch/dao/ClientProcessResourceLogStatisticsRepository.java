package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProcessResourceLogStatistics;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ClientProcessResourceLogStatisticsRepository extends ElasticsearchRepository<ClientProcessResourceLogStatistics, String>, Document2Repository<ClientProcessResourceLogStatistics> {

    List<ClientProcessResourceLogStatistics> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);

    Page<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(Long from, Long to, Pageable pageable);
    Optional<ClientProcessResourceLogStatistics> findByStatisticsValueAndDeviceIdAndProcName(String statisticsValue, String deviceId, String procName);

}