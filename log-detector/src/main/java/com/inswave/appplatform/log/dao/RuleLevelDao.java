package com.inswave.appplatform.log.dao;

import com.inswave.appplatform.log.domain.RuleLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleLevelDao extends JpaRepository<RuleLevel, Long > {
}
