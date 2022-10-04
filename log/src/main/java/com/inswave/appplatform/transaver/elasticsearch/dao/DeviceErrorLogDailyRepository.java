package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.DeviceErrorLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DeviceErrorLogDailyRepository extends ElasticsearchRepository<DeviceErrorLogDaily, String>, Document2Repository<DeviceErrorLogDaily> {

    List<DeviceErrorLogDaily> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);
    Page<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(Long from, Long to, Pageable pageable);

}