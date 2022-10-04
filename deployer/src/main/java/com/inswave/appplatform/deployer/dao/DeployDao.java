package com.inswave.appplatform.deployer.dao;

import com.inswave.appplatform.deployer.domain.Deploy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeployDao extends JpaRepository<Deploy, Long> {

}
