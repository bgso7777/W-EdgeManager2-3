package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=1, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class GeneratorLog extends Document2 {

    private String indexName;
    public void setIndexName(@Param("indexName") String indexName) {
        if (indexName.equals("")) {
            indexName = this.getClass().getSimpleName().toLowerCase();
            this.indexName = this.getClass().getSimpleName().toLowerCase();
        } else {
            this.indexName = indexName;
        }
        super.setIndexName(indexName);
    }
    public String getIndexName() {
        return this.indexName;
    }

    @Field(type = FieldType.Keyword, name = "logName")
    private String logName;

    @Field(type = FieldType.Text, name = "data")
    private String data;

    @Field(type = FieldType.Keyword, name = "currentHHmmss")
    private String currentHHmmss;

    @Field(type = FieldType.Integer, name = "currentHH")
    private Integer currentHH=-1;

    @Field(type = FieldType.Integer, name = "currentMm")
    private Integer currentMm=-1;

    @Field(type = FieldType.Integer, name = "currentSs")
    private Integer currentSs=-1;

    @Field(type = FieldType.Boolean, name = "isBrokenKorean")
    private Boolean isBrokenKorean=false;
    /**
     * 실제 데이터가 있는 index명
     */
    @Field(type = FieldType.Keyword, name = "indexNameData")
    private String indexNameData;
    /**
     * logmanager consumer가 받은 일시  2022-08-09T17:57:22
     */
    @Field(type = FieldType.Date, name = "currentDate", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date currentDate;

//    private String id;
    private ArrayList<String> brokenKoreanIds = new ArrayList<>();

    @Field(type = FieldType.Keyword, name = "otherUserName")
    private String otherUserName;
    @Field(type = FieldType.Keyword, name = "otherDeptName")
    private String otherDeptName;
    @Field(type = FieldType.Keyword, name = "otherUserId")
    private String otherUserId;
    @Field(type = FieldType.Keyword, name = "otherHostName")
    private String otherHostName;

//    /**
//     * current index에서 실제 보내고자 할때 사용
//     * 사용하기 전에 shuffle 후 send
//     */
//    /*----------------------------------------------------------------------------------------------------------------*/
//    @Field(type = FieldType.Keyword, name = "sendService")
//    private String sendService;
//
//    @Field(type = FieldType.Keyword, name = "sendAppId")
//    private String sendAppId;
//
//    @Field(type = FieldType.Keyword, name = "sendOsType")
//    private String sendOsType;
//
//    @Field(type = FieldType.Keyword, name = "sendSource")
//    private String sendSource;
//
    @Field(type = FieldType.Keyword, name = "sendDeviceId")
    private String sendDeviceId;
//
//    @Field(type = FieldType.Keyword, name = "sendDeviceType")
//    private String sendDeviceType;
//
//    @Field(type = FieldType.Keyword, name = "sendIp")
//    private String sendIp;
//
//    @Field(type = FieldType.Keyword, name = "sendHostName")
//    private String sendHostName;
//
//    @Field(type = FieldType.Keyword, name = "sendUserId")
//    private String sendUserId;
//
//    @Field(type = FieldType.Keyword, name = "sendTermNo")
//    private String sendTermNo;
//
//    @Field(type = FieldType.Keyword, name = "sendSsoBrNo")
//    private String sendSsoBrNo;
//
//    @Field(type = FieldType.Keyword, name = "sendBrNo")
//    private String sendBrNo;
//
//    @Field(type = FieldType.Keyword, name = "sendDeptName")
//    private String sendDeptName;
//
//    @Field(type = FieldType.Keyword, name = "sendHwnNo")
//    private String sendHwnNo;
//
//    @Field(type = FieldType.Keyword, name = "sendUserName")
//    private String sendUserName;
//
//    @Field(type = FieldType.Keyword, name = "sendSsoType")
//    private String sendSsoType;
//
//    @Field(type = FieldType.Keyword, name = "sendPcName")
//    private String sendPcName;
//
//    @Field(type = FieldType.Keyword, name = "sendPhoneNo")
//    private String sendPhoneNo;
//
//    @Field(type = FieldType.Keyword, name = "sendJKGP")
//    private String sendJKGP;
//
//    @Field(type = FieldType.Keyword, name = "sendJKWI")
//    private String sendJKWI;
//
//    @Field(type = FieldType.Keyword, name = "sendMaxAddress")
//    private String sendMaxAddress;
//
//    @Field(type = FieldType.Keyword, name = "sendFirstWork")
//    private String sendFirstWork;
}