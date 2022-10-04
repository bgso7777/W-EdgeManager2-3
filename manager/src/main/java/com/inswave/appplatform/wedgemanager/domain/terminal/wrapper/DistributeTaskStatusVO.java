package com.inswave.appplatform.wedgemanager.domain.terminal.wrapper;

import com.inswave.appplatform.wedgemanager.service.scm.distribute.DistributeTask;
import com.inswave.appplatform.wedgemanager.service.scm.distribute.DistributeTaskStatusType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;

@Data
@Builder
@Slf4j
public class DistributeTaskStatusVO {

    private String                   repositoryName;
    private Boolean                  beforeClean;
    private DistributeTaskStatusType status                    = DistributeTaskStatusType.WAIT;
    private String                   statusMessage             = DistributeTaskStatusType.WAIT.toString();
    private Boolean                  executed                  = false;
    private ZonedDateTime            executeDate               = null;
    private Integer                  currentStatusTaskPosition = 0;
    private Integer                  currentStatusTaskCount    = 0;
    private Integer                  taskPosition              = 0;
    private Integer                  taskCount                 = 0;
    private Integer                  totalTaskPosition         = 0;
    private Integer                  totalTaskCount            = 0;

    public String getExecuteDate() {
        return executeDate != null ? executeDate.toLocalDateTime().toString() : "";
    }

    public static DistributeTaskStatusVO from(DistributeTask distributeTask) {
        return DistributeTaskStatusVO.builder()
                                     .repositoryName(distributeTask.getRepositoryName())
                                     .beforeClean(distributeTask.getBeforeClean())
                                     .status(distributeTask.getStatus())
                                     .statusMessage(distributeTask.getStatusMessage())
                                     .executed(distributeTask.getExecuted())
                                     .executeDate(distributeTask.getExecuteDate())
                                     .taskPosition(distributeTask.getStatus().ordinal())
                                     .taskCount(DistributeTaskStatusType.values().length)
                                     .currentStatusTaskPosition(distributeTask.getCurrentStatusTaskPosition())
                                     .currentStatusTaskCount(distributeTask.getCurrentStatusTaskCount())
                                     .totalTaskPosition(distributeTask.getTotalTaskPosition())
                                     .totalTaskCount(distributeTask.getTotalTaskCount())
                                     .build();
    }
}
