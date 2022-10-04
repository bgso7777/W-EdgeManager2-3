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
public class ClientWindowsUpdateListResourceLogData {

    @Field(type = FieldType.Keyword, name = "title")
    private String title;

    @Field(type = FieldType.Date, name = "date", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date date;

    @Field(type = FieldType.Keyword, name = "clientApplicationID", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private String clientApplicationID;

    @Field(type = FieldType.Keyword, name = "serverSelection", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private String serverSelection;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;
}