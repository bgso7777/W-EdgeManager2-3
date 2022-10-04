package com.inswave.appplatform.domain;

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
name = "COUNTRY_GENERATOR",
pkColumnValue = "COUNTRY_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class Country {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COUNTRY_GENERATOR")
    private Long countryId;
	
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String description;

}
