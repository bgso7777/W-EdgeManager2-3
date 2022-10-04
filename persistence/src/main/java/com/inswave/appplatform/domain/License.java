package com.inswave.appplatform.domain;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@TableGenerator(
name = "LICENSE_GENERATOR",
pkColumnValue = "LICENSE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LICENSE_GENERATOR")
    private Long licenseId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String description;

}
