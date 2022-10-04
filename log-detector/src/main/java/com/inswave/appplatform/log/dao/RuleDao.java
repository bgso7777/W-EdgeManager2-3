package com.inswave.appplatform.log.dao;

import com.inswave.appplatform.log.domain.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleDao extends JpaRepository<Rule, Long > {

    List<Rule> findAllByActive(Boolean active);

}
