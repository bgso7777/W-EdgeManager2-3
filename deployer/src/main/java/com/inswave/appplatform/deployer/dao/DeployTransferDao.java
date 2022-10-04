package com.inswave.appplatform.deployer.dao;

import com.inswave.appplatform.deployer.domain.DeployTransfer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeployTransferDao extends JpaRepository<DeployTransfer, Long> {
    List<DeployTransfer> findByDeployId(Long deployId, Sort sort);

    int deleteByDeployId(Long deployId);
}
