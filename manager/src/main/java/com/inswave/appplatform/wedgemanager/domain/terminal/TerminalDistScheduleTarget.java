package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableGenerator(
name = "TS_DIST_SCHEDULE_TARGET_GENERATOR",
pkColumnValue = "TS_DIST_SCHEDULE_TARGET_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_dist_schedule_target")
@Entity
public class TerminalDistScheduleTarget extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_DIST_SCHEDULE_TARGET_GENERATOR")
    private Long                 id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "DIST_SCHEDULE_ID", nullable = false)
    private TerminalDistSchedule distSchedule;
    @ManyToOne(optional = false)
    @JoinColumn(name = "TARGET_DIST_ID", nullable = false)
    private TerminalDist         targetDist;
}