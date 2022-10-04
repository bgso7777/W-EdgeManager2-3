package com.inswave.appplatform.log.domain;

import com.inswave.appplatform.dao.Domain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@TableGenerator(
name = "RULE_LEVEL_GENERATOR",
pkColumnValue = "RULE_LEVEL_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class RuleLevel implements Domain {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RULE_LEVEL_GENERATOR")
    private Long ruleLevelId;
	
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String description;

}
