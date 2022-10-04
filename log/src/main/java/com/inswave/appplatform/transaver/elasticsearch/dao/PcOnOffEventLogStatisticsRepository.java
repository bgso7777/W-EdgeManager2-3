package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.PcOnOffEventLogStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface PcOnOffEventLogStatisticsRepository extends ElasticsearchRepository<PcOnOffEventLogStatistics, String>, Document2Repository<PcOnOffEventLogStatistics> {

    List<PcOnOffEventLogStatistics> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);

    Page<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(Long from, Long to, Pageable pageable);
    Optional<PcOnOffEventLogStatistics> findByStatisticsValueAndDeviceId(String statisticsValue, String deviceId);

}