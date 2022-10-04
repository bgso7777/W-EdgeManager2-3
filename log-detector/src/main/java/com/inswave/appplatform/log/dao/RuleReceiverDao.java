package com.inswave.appplatform.log.dao;

import com.inswave.appplatform.log.domain.RuleReceiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleReceiverDao extends JpaRepository<RuleReceiver, Long > {

}
