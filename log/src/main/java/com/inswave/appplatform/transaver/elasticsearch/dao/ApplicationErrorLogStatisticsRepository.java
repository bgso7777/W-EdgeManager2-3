package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ApplicationErrorLogStatistics;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationErrorLogStatisticsRepository extends ElasticsearchRepository<ApplicationErrorLogStatistics, String>, Document2Repository<ApplicationErrorLogStatistics> {

    List<ApplicationErrorLogStatistics> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);
    Page<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(Long from, Long to, Pageable pageable);
    Optional<ApplicationErrorLogStatistics> findByStatisticsValueAndDeviceId(String statisticsValue, String deviceId);

}