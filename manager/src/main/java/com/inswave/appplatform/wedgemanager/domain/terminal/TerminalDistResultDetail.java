package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.service.domain.StandardDomain;
import com.inswave.appplatform.util.JsonUtil;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalDistResultDetailFileVO;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableGenerator(
name = "TS_DIST_RESULT_GENERATOR",
pkColumnValue = "TS_DIST_RESULT_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_dist_result")
@Entity
public class TerminalDistResultDetail extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_DIST_RESULT_GENERATOR")
    @Getter @Setter
    private Long          id;
    @Column @Getter @Setter
    private String        repositoryName;
    @Column @Getter @Setter
    private Long          distId;
    @Column @Getter @Setter
    private String        distType;
    @Builder.Default
    @Column @Getter @Setter
    private String        taskType         = "normal";  // normal(일반), schedule(스케쥴)
    @Column @Getter @Setter
    private String        distName;
    @Column @Getter @Setter
    private String        pathSource;
    @Column @Getter @Setter
    private String        pathDestination;
    @Column @Getter @Setter
    private Boolean       includeSubdir    = false;
    @Column @Setter
    private ZonedDateTime executeStartDate;
    @Column @Setter
    private ZonedDateTime executeEndDate;
    @Builder.Default
    @Column @Getter @Setter
    private Boolean       executeSuccessYn = false;
    @Column @Setter
    private ZonedDateTime distVersion;
    @Column @Getter @Setter
    private Integer       distResultTotalFileCount;
    @Column @Getter @Setter
    private Long          distResultTotalFileSize;
    @Column @Setter
    private String        distResultDetail;

    public JsonNode getDistResultDetail() {
        if (distVersion != null) {
            return JsonUtil.toJsonNode(distResultDetail);
        }
        return null;
    }

    @JsonIgnore
    public String getDistResultDetailOrigin() {
        return distResultDetail;
    }

    public void setDistResultDetailOrigin(List<TerminalDistResultDetailFileVO> resultDetails) {
        this.distResultDetail = JsonUtil.toString(Collections.singletonList(resultDetails));
    }

    public String getExecuteStartDate() {
        return executeStartDate != null ? executeStartDate.toLocalDateTime().toString() : "";
    }

    public String getExecuteEndDate() {
        return executeEndDate != null ? executeEndDate.toLocalDateTime().toString() : "";
    }

    public String getDistVersion() {
        return distVersion != null ? distVersion.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS")) : "";
    }

    @JsonIgnore
    public ZonedDateTime getDistVersionOrigin() {
        return distVersion;
    }

    public static TerminalDistResultDetail from(TerminalDist terminalDist) {
        return TerminalDistResultDetail.builder()
                                       .distId(terminalDist.getId())
                                       .distType(terminalDist.getDistType())
                                       .distName(terminalDist.getDistName())
                                       .pathSource(terminalDist.getPathSource())
                                       .pathDestination(terminalDist.getPathDestination())
                                       .includeSubdir(terminalDist.getIncludeSubdir())
                                       .build();

    }
}