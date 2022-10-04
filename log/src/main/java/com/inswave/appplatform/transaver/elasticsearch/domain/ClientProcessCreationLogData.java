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
public class ClientProcessCreationLogData {

    @Field(type = FieldType.Long, name = "level")
    private Long level;

    @Field(type = FieldType.Long, name = "eventId")
    private Long eventId;

    @Field(type = FieldType.Keyword, name = "channel")
    private String channel;

    @Field(type = FieldType.Keyword, name = "keywords")
    private String keywords;

    @Field(type = FieldType.Keyword, name = "message")
    private String message;

    @Field(type = FieldType.Keyword, name = "newProcessId")
    private String newProcessId;

    @Field(type = FieldType.Keyword, name = "newProcessName")
    private String newProcessName;

    @Field(type = FieldType.Keyword, name = "parentProcessId")
    private String parentProcessId;

    @Field(type = FieldType.Keyword, name = "parentProcessName")
    private String parentProcessName;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

    @Field(type = FieldType.Date, name = "timeCreatedSystemTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreatedSystemTime;

}
