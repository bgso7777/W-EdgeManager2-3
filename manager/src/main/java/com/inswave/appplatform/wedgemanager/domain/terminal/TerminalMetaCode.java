package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.Data;

import javax.persistence.*;

@Data
@TableGenerator(
name = "TS_META_CODE_GENERATOR",
pkColumnValue = "TS_META_CODE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_meta_code")
@Entity
public class TerminalMetaCode extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_META_CODE_GENERATOR")
    private         Long          id;
    @Column private String        codeType;
    @Column private String        codeValue;
    @Column private String        codeTypeName;
    @Column private String        codeValueName;
    @Column private String        remarks;
    @Column private Integer       ord;
    @Column private Boolean       useYn;
}