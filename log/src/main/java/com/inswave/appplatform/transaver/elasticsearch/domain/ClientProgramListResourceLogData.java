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
public class ClientProgramListResourceLogData {

    @Field(type = FieldType.Keyword, name = "name")
    private String name;//""name"": ""W-Matrix(SHBNext Dev)"",

    @Field(type = FieldType.Keyword, name = "version")
    private String version;//""version"": ""1.100.3.0706"",

    @Field(type = FieldType.Keyword, name = "publisher")
    private String publisher;//""publisher"": ""Inswave Systems Co., Ltd."",

    @Field(type = FieldType.Integer, name = "size")
    private Integer size;// ""size"": null

    @Field(type = FieldType.Keyword, name = "installDate")
    private String installDate;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

}
