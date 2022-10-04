package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@TableGenerator(
name = "TS_HELP_REPLY_GENERATOR",
pkColumnValue = "TS_HELP_REPLY_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_help_reply")
@Entity
public class TerminalHelpReplyFlat extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_HELP_REPLY_GENERATOR")
    private Long   id;
    @Column
    private Long   parentId;
    @Column
    private String screenId;
    @Column
    private String screenName;
    @Column
    private String contents;
    @Column
    private String inprogress;
}