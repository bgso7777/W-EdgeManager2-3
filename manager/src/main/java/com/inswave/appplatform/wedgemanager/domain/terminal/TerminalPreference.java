package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableGenerator(
name = "TS_PREFERENCE_GENERATOR",
pkColumnValue = "TS_PREFERENCE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_preference")
@Entity
public class TerminalPreference extends StandardDomain {
    @Id
    private String dataKey;
    @Column
    private String dataValue;
    @Column
    private String dataDesc;
}