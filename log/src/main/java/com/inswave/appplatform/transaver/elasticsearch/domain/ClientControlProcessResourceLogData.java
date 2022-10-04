package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter 
@Setter
public class ClientControlProcessResourceLogData {

    /**

    "columns" : [ pid, procName, procParentPid, procSessionID, procSid, timeCreatedSystemTime, timeCreated, execResult],

     "# 수집 항목 (Array)
     pid : Process Id
     procName : Process name
     procParentPid : Parent process pid
     procSessionId : Session id
     procSid : security id
     execResult : 실행 결과 (killedProcess, changedAC )
     timeCreatedSystemTime : 이벤트 발생 시간 ( UTC )
     timeCreated : 실행 시간"

     */

    @Field(type = FieldType.Keyword, name = "pid")
    private String pid;

    @Field(type = FieldType.Keyword, name = "procName")
    private String procName;

    @Field(type = FieldType.Keyword, name = "procParentPid")
    private String procParentPid;

    @Field(type = FieldType.Keyword, name = "procSessionID")
    private String procSessionID;

    @Field(type = FieldType.Keyword, name = "execResult")
    private String execResult;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

}