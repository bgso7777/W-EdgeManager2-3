package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.Data;

import javax.persistence.*;

@Data
@TableGenerator(
name = "TS_NOTICE_GENERATOR",
pkColumnValue = "TS_NOTICE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_notice")
@Entity
public class TerminalNotice extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_NOTICE_GENERATOR")
    private         Long          id;
    @Column private String        title;
    @Column private String        contents;
    @Column private String        exposeTarget;
    @Column private Boolean       exposeOn;
}