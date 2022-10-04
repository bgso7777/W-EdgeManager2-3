package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import com.inswave.appplatform.wedgemanager.domain.device.DeviceKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "em_device_info")
@IdClass(DeviceKey.class)
public class TerminalDeviceInfo extends StandardDomain {
    @Id private     String appId;
    @Id private     String deviceId;
    @Column private String deviceInfoUser;  // 초기 단말 정보
}