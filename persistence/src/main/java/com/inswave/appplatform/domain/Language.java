package com.inswave.appplatform.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@TableGenerator(
name = "LANGUAGE_GENERATOR",
pkColumnValue = "LANGUAGE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LANGUAGE_GENERATOR")
    private Long languageId;

    @Column(length = 20, nullable = false)
    private Long countryId;

    @Column(length = 20, nullable = false)
    private String type;//"MESSAGE" "EMAILTEMPLATE"

    @Column(length = 20, nullable = false)
    private String key_;

    @Column(length = 20, nullable = false)
    private String value;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
}
