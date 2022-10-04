package com.inswave.appplatform.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@TableGenerator(
name = "CONFIG_GENERATOR",
pkColumnValue = "CONFIG_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CONFIG_GENERATOR")
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
