package com.inswave.appplatform.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@TableGenerator(
name = "CONFIG_LOG_GENERATOR",
pkColumnValue = "CONFIG_LOG_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class ConfigLog {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CONFIG_LOG_GENERATOR")
    private Long configId;

    @Column(length = 50, nullable = false)
    private String key_;

    @Column(length = 255, nullable = false)
    private String value;

    @Column(length = 255, nullable = false)
    private String value2;

    @Column(nullable = false)
    private Integer logSavedDay=-1;

    @Column(length = 255, nullable = false)
    private String description;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
}
