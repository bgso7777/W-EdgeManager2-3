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
public class ClientActivePortListResourceLogData {

    @Field(type = FieldType.Keyword, name = "protocalType")
    private String protocalType;

    @Field(type = FieldType.Keyword, name = "localAddress")
    private String localAddress;

    @Field(type = FieldType.Keyword, name = "localPort")
    private String localPort;

    @Field(type = FieldType.Keyword, name = "remoteAddress")
    private String remoteAddress;

    @Field(type = FieldType.Keyword, name = "remotePort")
    private String remotePort;

    @Field(type = FieldType.Keyword, name = "netState")
    private String netState;

    @Field(type = FieldType.Keyword, name = "procId")
    private String procId;

    @Field(type = FieldType.Keyword, name = "procName")
    private String procName;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

}