package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDeviceError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminalDeviceErrorDao extends JpaRepository<TerminalDeviceError, Long> {
    List<TerminalDeviceError> findByAppIdAndDeviceIdOrderByCreateDateDesc(String appId, String deviceId);
}
