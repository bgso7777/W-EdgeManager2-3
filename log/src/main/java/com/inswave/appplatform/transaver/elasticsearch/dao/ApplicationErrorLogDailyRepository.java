package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ApplicationErrorLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ApplicationErrorLogDailyRepository extends ElasticsearchRepository<ApplicationErrorLogDaily, String>, Document2Repository<ApplicationErrorLogDaily> {

    List<ApplicationErrorLogDaily> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);
    Page<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(Long from, Long to, Pageable pageable);

}