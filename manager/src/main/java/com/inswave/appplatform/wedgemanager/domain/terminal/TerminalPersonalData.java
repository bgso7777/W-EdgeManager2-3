package com.inswave.appplatform.wedgemanager.domain.terminal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableGenerator(
name = "TS_PERSONAL_DATA_GENERATOR",
pkColumnValue = "TS_PERSONAL_DATA_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_personal_data")
@Entity
public class TerminalPersonalData {
    @Column(nullable = false) private String        userId;
    @Column(nullable = false) private String        dataType;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_PERSONAL_DATA_GENERATOR")
    private                           Long          dataId;
    @Lob @Column private              String        dataJson;
    @Column private                   ZonedDateTime createDate;
    @Column private                   String        createUserId;
    @Column private                   String        createUserName;
    @Column private                   ZonedDateTime updateDate;
    @Column private                   String        updateUserId;
    @Column private                   String        updateUserName;
}