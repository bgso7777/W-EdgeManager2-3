package com.inswave.appplatform.deployer.dao;

import com.inswave.appplatform.deployer.domain.DeployTransferHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeployTransferHistoryDao extends JpaRepository<DeployTransferHistory, Long> {
    List<DeployTransferHistory> findByDeployTransferId(Long deployTransferId, Sort deployId);

    //    @Query("DELETE FROM DeployTransferHistory dth WHERE dth.deployTransferId IN ( SELECT dt.deployTransferId FROM DeployTransfer dt WHERE dt.deployId = :deployId)")
    @Transactional
    @Modifying
    @Query("DELETE FROM DeployTransferHistory dth WHERE dth.deployTransferId IN ?1")
    int deleteWithDeployIds(List<Long> ids);
}
