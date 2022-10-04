package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableGenerator(
name = "TS_DIST_GENERATOR",
pkColumnValue = "TS_DIST_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_dist")
@Entity
public class TerminalDist extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_DIST_GENERATOR")
    @Getter @Setter
    private Long          id;
    @Column @Getter @Setter
    private String        repositoryName;
    @Column @Getter @Setter
    private String        distName      = "business";
    @Column @Getter @Setter
    private String        distType;
    @Column @Getter @Setter
    private String        pathSource;
    @Column @Getter @Setter
    private String        pathDestination;
    @Column @Getter @Setter
    private Boolean       includeSubdir = false;
    @Column @Getter @Setter
    private ZonedDateTime lastDistVersion;

    public String getLastDistVersion() {
        return lastDistVersion != null ? lastDistVersion.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS")) : "";
    }

    @JsonIgnore
    public ZonedDateTime getLastDistVersionOrigin() {
        return lastDistVersion;
    }
}