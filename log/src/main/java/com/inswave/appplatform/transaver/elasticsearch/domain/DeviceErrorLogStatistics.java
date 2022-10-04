package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class DeviceErrorLogStatistics extends Document2 {

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

    // 발생 카운트
    @Field(type = FieldType.Integer, name = "count")
    private Integer count=0;

    //"level":2
    // "level",
    @Field(type = FieldType.Nested, name = "levels")
    //private Long level;
    private List<Long> levels = new ArrayList<>();

    //"id":455
    // "id",
    @Field(type = FieldType.Nested, name = "ids")
    //private Long id;
    private List<Long> ids = new ArrayList<Long>();

    // "channel",
    @Field(type = FieldType.Nested, name = "channels")
    //private String channel;
    private List<String> channels = new ArrayList<String>();

    //"activityId":""
    // "activityId",
    @Field(type = FieldType.Nested, name = "activityIds")
    //private String activityId;
    private List<String> activityIds = new ArrayList<String>();

//    //"bookmark":""
//    // "bookmark",
////    @Field(type = FieldType.Keyword, name = "bookmark")
////    private String bookmark;
//
//    //"keywords":36028797018963968
//    // "keywords",
////    @Field(type = FieldType.Long, name = "keywords")
////    private Long keywords;
//
//    //"keywordsDisplayNames":""
//    // "keywordsDisplayNames",
////    @Field(type = FieldType.Keyword, name = "keywordsDisplayNames")
////    private String keywordsDisplayNames;
//
//    //"levelDisplayName":"오류"
//    // "levelDisplayName",
//    @Field(type = FieldType.Keyword, name = "levelDisplayName")
//    private String levelDisplayName;
//
//    //"logName":"Application"
//    // "logName",
////    @Field(type = FieldType.Keyword, name = "logName")
////    private String logName;
//
//    //"machineName":"DESKTOP-J879OFU"
//    // "machineName",
////    @Field(type = FieldType.Keyword, name = "machineName")
////    private String machineName;
//
//    //"opcode":0
//    // "opcode",
////    @Field(type = FieldType.Short, name = "opcode")
////    private Short opcode;
//
//    //"opcodeDisplayName":"정보"
//    // "opcodeDisplayName",
//    @Field(type = FieldType.Keyword, name = "opcodeDisplayName")
//    private String opcodeDisplayName;
    @Field(type = FieldType.Nested, name = "opcodeDisplayNames")
    private List<String> opcodeDisplayNames = new ArrayList<String>();

//    //"processId":0
//    // "processId",
////    @Field(type = FieldType.Integer, name = "processId")
////    private Integer processId;
//
//    //"properties":""
//    // "properties",
////    @Field(type = FieldType.Keyword, name = "properties")
////    private String properties;
//
//    //"providerId":""
//    // "providerId",
//    @Field(type = FieldType.Keyword, name = "providerId")
//    private String providerId;
//
//    //"providerName":"ESENT"
//    // "providerName",
//    @Field(type = FieldType.Keyword, name = "providerName")
//    private String providerName;
//
//    //"qualifiers":0
//    // "qualifiers",
//    @Field(type = FieldType.Integer, name = "qualifiers")
//    private Integer qualifiers;
//
//    //"recordId":21258
//    // "recordId",
//    @Field(type = FieldType.Long, name = "recordId")
//    private Long recordId;
//
//    //"relatedActivityId":""
//    //"relatedActivityId",
//    @Field(type = FieldType.Keyword, name = "relatedActivityId")
//    private String relatedActivityId;
//
//    //"task":3
//    // "task",
//    @Field(type = FieldType.Integer, name = "task")
//    private Integer task;
//
//    //"taskDisplayName":"로깅/복구"
//    // "taskDisplayName",
//    @Field(type = FieldType.Keyword, name = "taskDisplayName")
//    private String taskDisplayName;
    @Field(type = FieldType.Nested, name = "taskDisplayNames")
    private List<String> taskDisplayNames = new ArrayList<String>();

//    //???????
//
//    //"threadId":0
//    // "threadId",
////    @Field(type = FieldType.Integer, name = "threadId")
////    private Integer threadId;
//
//    //"userId":""
//    // "userId",
////    @Field(type = FieldType.Keyword, name = "userId")
////    private String userId;
//
//    //"version":0
//    // "version",
////    @Field(type = FieldType.Keyword, name = "version")
////    private String version;
//
//    //"message":"svchost (11228R98) TILEREPOSITORYS-1-5-18: 로그 파일 C:\\WINDOWS\\system32\\config\\systemprofile\\AppData\\Local\\TileDataLayer\\Database\\EDB.log을(를) 여는 동안 -1023 (0xfffffc01) 오류가 발생했습니다. "}
//    // "message"
////    @Field(type = FieldType.Keyword, name = "message")
////    private String message;

}