package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.Data;

import javax.persistence.*;

@Data
@TableGenerator(
name = "TS_STATEMENT_GENERATOR",
pkColumnValue = "TS_STATEMENT_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_statement")
@Entity
public class TerminalStatement extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_STATEMENT_GENERATOR")
    private         Long   id;
    @Column private String source;
    @Column private String statementId;
    @Column private String convertedId;
    @Column private String convertedDesc;
    @Column private String userData1;
}