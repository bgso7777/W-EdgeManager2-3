package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@TableGenerator(
name = "EM_DEVICE_ERROR_GENERATOR",
pkColumnValue = "EM_DEVICE_ERROR_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "em_device_error")
public class TerminalDeviceError extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "EM_DEVICE_ERROR_GENERATOR")
    private                         Long          id;
    @Setter @Getter @Column private String        appId;
    @Setter @Getter @Column private String        deviceId;
    @Setter @Getter @Column private String        errorType;         // 에러유형
    @Setter @Getter @Column private String        errorDesc;         // 에러내용
    @Setter @Column private         ZonedDateTime errorTime;         // 에러발생일시

    public String getErrorTime() {
        return errorTime != null ? errorTime.toLocalDateTime().toString() : "";
    }

    @JsonIgnore
    public ZonedDateTime getErrorTimeOrigin() {
        return errorTime;
    }
}