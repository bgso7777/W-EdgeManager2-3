package com.inswave.appplatform.dao;

import com.inswave.appplatform.domain.ConfigLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigLogDao extends JpaRepository<ConfigLog, Long> {

}
