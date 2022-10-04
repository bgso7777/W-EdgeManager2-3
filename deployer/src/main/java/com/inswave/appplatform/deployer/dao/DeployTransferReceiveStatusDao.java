package com.inswave.appplatform.deployer.dao;

import com.inswave.appplatform.deployer.domain.DeployTransferReceiveStatus;
import com.inswave.appplatform.service.dao.StandardDao;
import org.springframework.stereotype.Repository;

@Repository
public interface DeployTransferReceiveStatusDao extends StandardDao<DeployTransferReceiveStatus, Long> {
}
