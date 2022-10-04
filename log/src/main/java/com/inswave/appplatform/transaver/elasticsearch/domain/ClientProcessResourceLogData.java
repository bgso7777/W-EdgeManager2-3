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
public class ClientProcessResourceLogData {

    /* ----------------------------------------------------------------------*/

    @Field(type = FieldType.Integer, name = "pid")
    private Integer pid;//프로세스 ID

    @Field(type = FieldType.Keyword, name = "procName")
    private String procName;//프로세스명

    @Field(type = FieldType.Long, name = "procCpuUsage")
    private Long procCpuUsage;//CPU 사용률 (%)

    @Field(type = FieldType.Long, name = "procThreadCount")
    private Long procThreadCount;//thread 갯수

    @Field(type = FieldType.Long, name = "procHandleCount")
    private Long procHandleCount;//핸들 갯수

    @Field(type = FieldType.Long, name = "procWorkingSet")
    private Long procWorkingSet;//메모리 양

    @Field(type = FieldType.Long, name = "procPoolPage")
    private Long procPoolPage;//메모리 페이징 풀

    @Field(type = FieldType.Long, name = "procPoolNonpaged")
    private Long procPoolNonpaged;//메모리 비페이징 풀

    @Field(type = FieldType.Long, name = "procIoRead")
    private Long procIoRead;//I/O 읽기

    @Field(type = FieldType.Long, name = "procIoWrite")
    private Long procIoWrite;//I/O 쓰기

    @Field(type = FieldType.Float, name = "procNetworkSent")
    private Float procNetworkSent;

    @Field(type = FieldType.Float, name = "procNetworkReceived")
    private Float procNetworkReceived;

    @Field(type = FieldType.Boolean, name = "procIntegrityLevel")
    private Boolean procIntegrityLevel;

    @Field(type = FieldType.Long, name = "procMemoryUsed")
    private Long procMemoryUsed;

    @Field(type = FieldType.Long, name = "memroyTotal")
    private Long memroyTotal;

    @Field(type = FieldType.Long, name = "procMemoryUsage")
    private Long procMemoryUsage;

    @Field(type = FieldType.Keyword, name = "procFileDescription")
    private String procFileDescription;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

}