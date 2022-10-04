package com.inswave.appplatform.dao;

import com.inswave.appplatform.domain.ConfigManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigManagerDao extends JpaRepository<ConfigManager, Long> {

}
