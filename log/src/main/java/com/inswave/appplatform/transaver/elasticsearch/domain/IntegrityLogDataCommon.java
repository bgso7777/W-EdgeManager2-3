package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter 
@Setter
public class IntegrityLogDataCommon {

    @Field(type = FieldType.Keyword, name = "name")
    private String name;

    @Field(type = FieldType.Keyword, name = "description")
    private String description;

    @Field(type = FieldType.Date, name = "startTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date startTime;

    @Field(type = FieldType.Date, name = "endTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date endTime;

    @Field(type = FieldType.Keyword, name = "elapsedTime")
    private String elapsedTime;

    @Field(type = FieldType.Integer, name = "result")
    private Integer result;

    @Field(type = FieldType.Integer, name = "resultScript")
    private Integer resultScript;

    @Field(type = FieldType.Keyword, name = "message")
    private String message;

    @Field(type = FieldType.Boolean, name = "isAlways")
    private Boolean isAlways;

    @Field(type = FieldType.Boolean, name = "isTargetHome")
    private Boolean isTargetHome; // 재택 PC 해당 여부 (입력값과 동일)

    @Field(type = FieldType.Keyword, name = "pcType")
    private List<String> pcType;

    @Field(type = FieldType.Keyword, name = "remoteCode")
    private String remoteCode; // 원격 지원 코드

    @Field(type = FieldType.Boolean, name = "faultReg")
    private Boolean faultReg; // 장애등록여부

    @Field(type = FieldType.Integer, name = "sendErrCnt")
    private Integer sendErrCnt=-1;

    @Field(type = FieldType.Nested, name = "rules")
    private List<IntegrityLogDataRule> rules;

}