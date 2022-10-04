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
name = "CONFIG_MANAGER_GENERATOR",
pkColumnValue = "CONFIG_MANAGER_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class ConfigManager {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CONFIG_MANAGER_GENERATOR")
    private Long configId;

    @Column(length = 50, nullable = false)
    private String key_;

    @Column(length = 255, nullable = false)
    private String value;

    @Column(length = 255, nullable = false)
    private String value2;

    @Column(length = 255, nullable = false)
    private String description;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
}
