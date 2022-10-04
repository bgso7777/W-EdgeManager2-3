package com.inswave.appplatform.log.dao;

import com.inswave.appplatform.log.domain.RuleTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleTargetDao extends JpaRepository<RuleTarget, Long > {

}
