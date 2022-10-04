package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientUserTermMonitorLogStatistics;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ClientUserTermMonitorLogStatisticsRepository extends ElasticsearchRepository<ClientUserTermMonitorLogStatistics, String>, Document2Repository<ClientUserTermMonitorLogStatistics> {

    List<ClientUserTermMonitorLogStatistics> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);
    Page<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(Long from, Long to, Pageable pageable);
    Optional<ClientUserTermMonitorLogStatistics> findByStatisticsValueAndDeviceId(String statisticsValue, String deviceId);

}