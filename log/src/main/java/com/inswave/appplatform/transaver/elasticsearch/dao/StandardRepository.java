package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface StandardRepository extends ElasticsearchRepository<Document2, String>,Document2Repository<Document2> {

    List<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);

}
