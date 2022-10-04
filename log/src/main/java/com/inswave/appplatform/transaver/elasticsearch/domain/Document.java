package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

/**
 *   Log의 Original Document extends함.
 */
@Getter
@Setter
public abstract class Document {

    @Field(type = FieldType.Keyword, name = "indexName")
    private String indexName="";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Field(type = FieldType.Keyword, name = "documentId")
    private String documentId;

    /* ----------------------------------------------------------------------*/
    @Field(type = FieldType.Keyword, name = "service")
    private String service;

    @Field(type = FieldType.Keyword, name = "appId")
    private String appId;

    @Field(type = FieldType.Keyword, name = "osType")
    private String osType;

    @Field(type = FieldType.Keyword, name = "source")
    private String source;

    @Field(type = FieldType.Keyword, name = "deviceId")
    private String deviceId;

    @Field(type = FieldType.Keyword, name = "deviceType")
    private String deviceType;

    @Field(type = FieldType.Keyword, name = "ip")
    private String ip;

    @Field(type = FieldType.Keyword, name = "hostName")
    private String hostName;

    @Field(type = FieldType.Keyword, name = "userId")
    private String userId;

    @Field(type = FieldType.Keyword, name = "termNo")
    private String termNo;

    @Field(type = FieldType.Keyword, name = "ssoBrNo")
    private String ssoBrNo;

    @Field(type = FieldType.Keyword, name = "brNo")
    private String brNo;

    @Field(type = FieldType.Keyword, name = "deptName")
    private String deptName;

    @Field(type = FieldType.Keyword, name = "hwnNo")
    private String hwnNo;

    @Field(type = FieldType.Keyword, name = "userName")
    private String userName;

    @Field(type = FieldType.Keyword, name = "ssoType")
    private String ssoType;

    @Field(type = FieldType.Keyword, name = "pcName")
    private String pcName;

    @Field(type = FieldType.Keyword, name = "phoneNo")
    private String phoneNo;

    @Field(type = FieldType.Keyword, name = "JKGP")
    private String JKGP;

    @Field(type = FieldType.Keyword, name = "JKWI")
    private String JKWI;

    @Field(type = FieldType.Keyword, name = "maxAddress")
    private String maxAddress;

    @Field(type = FieldType.Keyword, name = "firstWork")
    private String firstWork;

    /* ----------------------------------------------------------------------*/

    // client handler 수집 시간
    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

    // 등록 시간
    @Field(type = FieldType.Date, name = "timeRegistered", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeRegistered;
}