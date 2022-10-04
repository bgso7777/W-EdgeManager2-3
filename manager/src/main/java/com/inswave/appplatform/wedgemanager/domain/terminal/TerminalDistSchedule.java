package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.inswave.appplatform.service.domain.StandardDomain;
import com.inswave.appplatform.util.ZonedDateTimeDeserializer;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableGenerator(
name = "TS_DIST_SCHEDULE_GENERATOR",
pkColumnValue = "TS_DIST_SCHEDULE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_dist_schedule")
@Entity
public class TerminalDistSchedule extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_DIST_SCHEDULE_GENERATOR")
    @Getter @Setter
    private Long          id;
    @Column @Getter @Setter
    private String        schedule;
    @Column @Getter @Setter
    private String        scheduleType;
    @Column @Getter @Setter
    private Boolean       isActive;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @Column @Setter
    private ZonedDateTime lastExecuteDate;
    @Column @Getter @Setter
    private String        repositoryName;

    public String getLastExecuteDate() {
        return lastExecuteDate != null ? lastExecuteDate.toLocalDateTime().toString() : "";
//        return lastExecuteDate != null ? lastExecuteDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")) : "";
//        return lastExecuteDate != null ? lastExecuteDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")) : "";
    }

    @JsonIgnore
    public ZonedDateTime getLastExecuteDateOrigin() {
        return lastExecuteDate;
    }
}