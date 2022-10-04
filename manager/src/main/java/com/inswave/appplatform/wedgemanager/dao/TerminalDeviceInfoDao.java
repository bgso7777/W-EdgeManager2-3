package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.wedgemanager.domain.device.DeviceKey;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalDeviceInfoDao extends JpaRepository<TerminalDeviceInfo, DeviceKey> {
}
