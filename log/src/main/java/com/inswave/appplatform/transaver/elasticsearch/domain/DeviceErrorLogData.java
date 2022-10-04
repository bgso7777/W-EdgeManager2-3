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
public class DeviceErrorLogData {

    //"level":2
    // "level",
    @Field(type = FieldType.Long, name = "level")
    private Long level;

    //"id":455
    // "id",
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    // ??????????????????????????????? 안보임.
    // "channel",
    @Field(type = FieldType.Keyword, name = "channel")
    private String channel;

    //"activityId":""
    // "activityId",
    @Field(type = FieldType.Keyword, name = "activityId")
    private String activityId;

    //"bookmark":""
    // "bookmark",
    @Field(type = FieldType.Keyword, name = "bookmark")
    private String bookmark;

    //"keywords":36028797018963968
    // "keywords",
    @Field(type = FieldType.Keyword, name = "keywords")
    private String keywords;

    //"keywordsDisplayNames":""
    // "keywordsDisplayNames",
    @Field(type = FieldType.Keyword, name = "keywordsDisplayNames")
    private String keywordsDisplayNames;

    //"levelDisplayName":"오류"
    // "levelDisplayName",
    @Field(type = FieldType.Keyword, name = "levelDisplayName")
    private String levelDisplayName;

    //"logName":"Application"
    // "logName",
    @Field(type = FieldType.Keyword, name = "logName")
    private String logName;

    //"machineName":"DESKTOP-J879OFU"
    // "machineName",
    @Field(type = FieldType.Keyword, name = "machineName")
    private String machineName;

    //"opcode":0
    // "opcode",
    @Field(type = FieldType.Short, name = "opcode")
    private Short opcode;

    //"opcodeDisplayName":"정보"
    // "opcodeDisplayName",
    @Field(type = FieldType.Keyword, name = "opcodeDisplayName")
    private String opcodeDisplayName;

    //"processId":0
    // "processId",
    @Field(type = FieldType.Integer, name = "processId")
    private Integer processId;

    //"properties":""
    // "properties",
    @Field(type = FieldType.Keyword, name = "properties")
    private String properties;

    //"providerId":""
    // "providerId",
    @Field(type = FieldType.Keyword, name = "providerId")
    private String providerId;

    //"providerName":"ESENT"
    // "providerName",
    @Field(type = FieldType.Keyword, name = "providerName")
    private String providerName;

    //"qualifiers":0
    // "qualifiers",
    @Field(type = FieldType.Integer, name = "qualifiers")
    private Integer qualifiers;

    //"recordId":21258
    // "recordId",
    @Field(type = FieldType.Long, name = "recordId")
    private Long recordId;

    //"relatedActivityId":""
    //"relatedActivityId",
    @Field(type = FieldType.Keyword, name = "relatedActivityId")
    private String relatedActivityId;

    //"task":3
    // "task",
    @Field(type = FieldType.Integer, name = "task")
    private Integer task;

    //"taskDisplayName":"로깅/복구"
    // "taskDisplayName",
    @Field(type = FieldType.Keyword, name = "taskDisplayName")
    private String taskDisplayName;

    //"threadId":0
    // "threadId",
    @Field(type = FieldType.Integer, name = "threadId")
    private Integer threadId;

    //"userId":""
    // "userId",
    @Field(type = FieldType.Keyword, name = "securityUserId")
    private String securityUserId;

    //"version":0
    // "version",
    @Field(type = FieldType.Keyword, name = "version")
    private String version;

    //"message":"svchost (11228R98) TILEREPOSITORYS-1-5-18: 로그 파일 C:\\WINDOWS\\system32\\config\\systemprofile\\AppData\\Local\\TileDataLayer\\Database\\EDB.log을(를) 여는 동안 -1023 (0xfffffc01) 오류가 발생했습니다. "}
    // "message"
    @Field(type = FieldType.Keyword, name = "message")
    private String message;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

    @Field(type = FieldType.Date, name = "timeCreatedSystemTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreatedSystemTime;
}