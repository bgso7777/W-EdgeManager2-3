package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertExclusion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RuleAlertExclusionRepository extends ElasticsearchRepository<RuleAlertExclusion, String> {
    List<RuleAlertExclusion> findAll();
}