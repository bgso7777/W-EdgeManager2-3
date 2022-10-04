package com.inswave.appplatform.domain;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@TableGenerator(
name = "SITE_GENERATOR",
pkColumnValue = "SITE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SITE_GENERATOR")
    private Long siteId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String address1;

    @Column(length = 20, nullable = false)
    private String address2;

    @Column(length = 20, nullable = false)
    private String fax;

    @Column(length = 20, nullable = false)
    private String tel;

    @Column(length = 20, nullable = false)
    private String signKey;

    @ManyToOne
    @JoinColumn(name = "LICENSE_ID")
    private License license;

    public String getLicenseName() {
        return license.getName();
    }

    public void setLicenseName(String licenseName) {
        license.setName(licenseName);
    }

    @Column(length = 11, nullable = true)
    private Integer count=0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

}