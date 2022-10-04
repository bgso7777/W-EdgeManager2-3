package com.inswave.appplatform.log.monitor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class KafkaListenerMonitor implements Cloneable {

    private String processDateTime = "";
    private Integer processCount=1;
    private Long processByte=0L;
    private Long processTime=0L;

    private long runningTime=0L;
    private Date beginTime = null;
    private Date endTime = null;
    private Long bps=-1L;
    private Long bpms=-1L;

    private String key;
    private String instanceId;
    private String topicName;
    private String groupId;
    private Integer partitionId;
    private Long offset=0L;
    private Long endOffsets=0L;

    private String timeCurrent = "";
    private int year = -1;
    private int month = -1;
    private int day = -1;
    private int dayOfWeek = -1;
    private int hour = -1;
    private int minutely10 = -1;

    public void begin() {
        beginTime = new Date();

        processDateTime = "";
        processCount=1;
        processByte=0L;
        processTime=0L;

        runningTime=0L;
        endTime = null;
        bps=-1L;
        bpms=-1L;

        key="";
        instanceId="";
        topicName="";
        groupId="";
        partitionId=-1;
        offset=0L;
        endOffsets=0L;

        timeCurrent = "";
        year = -1;
        month = -1;
        day = -1;
        dayOfWeek = -1;
        hour = -1;
        minutely10 = -1;
    }

    public void end() {
        endTime = new Date();
        runningTime = endTime.getTime() - beginTime.getTime();
        processTime = runningTime;
        processDateTime = DateUtil.getCurrentDate(Config.getInstance().getLog().getLogFileDatePattern());

        try{bpms=processByte/processTime;} catch(Exception e) {}
        try{bps=processByte/(processTime/1000);} catch(Exception e) {}
    }

    public KafkaListenerMonitor cloneKafkaListenerMonitor() throws CloneNotSupportedException {
        return (KafkaListenerMonitor)this.clone();
    }
}
