package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableGenerator(
name = "TS_STATEMENT_GENERATOR",
pkColumnValue = "TS_STATEMENT_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_statement")
@Entity
public class TerminalStatementDetail extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_STATEMENT_GENERATOR")
    private              Long   id;
    @Column private      String source;
    @Column private      String statementId;
    @Lob @Column private String contents;
    @Column private      String convertedId;
    @Column private      String convertedDesc;
    @Column private      String userData1;
    @Lob @Column private String convertedContents;
}