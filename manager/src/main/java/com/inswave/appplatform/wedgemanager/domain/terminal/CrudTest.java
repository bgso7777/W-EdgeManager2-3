package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.Data;

import javax.persistence.*;

@Data
@TableGenerator(
name = "CRUD_TEST_GENERATOR",
pkColumnValue = "CRUD_TEST_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Entity
@Table(name = "crud_test")
public class CrudTest extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CRUD_TEST_GENERATOR")
    private Long   id;
    @Column
    private String name;
    @Column
    private String descstr;
    @Column
    private String longdesc;
}
