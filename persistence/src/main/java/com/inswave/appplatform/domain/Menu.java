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
name = "MENU_GENERATOR",
pkColumnValue = "MENU_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class Menu {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MENU_GENERATOR")
    private Long menuId;

    @Column
    private Long parentMenuId;

    @Column(name="LEVEL_")
    private Integer level;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String description;

    @Column(length = 100)
    private String path;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(length = 200)
    private String iconImage;

    @Column(nullable = false)
    private Boolean active;
}