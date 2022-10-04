package com.inswave.appplatform.log.domain;

import com.inswave.appplatform.dao.Domain;
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
name = "RULE_TARGET_GENERATOR",
pkColumnValue = "RULE_TARGET_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class RuleTarget implements Domain  {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RULE_TARGET_GENERATOR")
    private Long ruleTargetId;

    @Column(nullable = true)
    private Boolean isSystem=false;

    @Column(length = 20, nullable = false)
    private String className;

    @Column(length = 20, nullable = false)
    private String displayClassName;

    @Column(length = 20, nullable = false)
    private String fields;

    @Column(length = 20, nullable = false)
    private String displayFields;

    @Column(length = 20, nullable = false)
    private String fieldsType;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
}
