package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.WindowsEventSystemErrorAllLogStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface WindowsEventSystemErrorAllLogStatisticsRepository extends ElasticsearchRepository<WindowsEventSystemErrorAllLogStatistics, String>, Document2Repository<WindowsEventSystemErrorAllLogStatistics> {

    List<WindowsEventSystemErrorAllLogStatistics> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);
    Page<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(Long from, Long to, Pageable pageable);
    Optional<WindowsEventSystemErrorAllLogStatistics> findByStatisticsValueAndDeviceId(String statisticsValue, String deviceId);

}