package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.Date;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter
public class ServerResourceLogDaily extends Document2 {

    private String indexName;
    public void setIndexName(@Param("indexName") String indexName) {
        if (indexName.equals("")) {
            indexName = this.getClass().getSimpleName().toLowerCase();
            this.indexName = indexName;
        } else {
            this.indexName = indexName;
        }
        super.setIndexName(this.indexName);
    }
    public String getIndexName() {
        return this.indexName;
    }

    /* ----------------------------------------------------------------------*/
    @Field(type = FieldType.Keyword, name = "service")
    private String service;

    @Field(type = FieldType.Keyword, name = "appId")
    private String appId;

    @Field(type = FieldType.Keyword, name = "destination")
    private String destination;

    @Field(type = FieldType.Keyword, name = "osType")
    private String osType;

    @Field(type = FieldType.Keyword, name = "msgId")
    private String msgId;

    @Field(type = FieldType.Double, name = "siteId")
    private Double siteId;

    @Field(type = FieldType.Keyword, name = "siteName")
    private String siteName;

    @Field(type = FieldType.Keyword, name = "source")
    private String source;

    @Field(type = FieldType.Keyword, name = "deviceId")
    private String deviceId;

    /*@Field(type = FieldType.Double, name = "deviceId")
    private Double installId;*/

    /* ----------------------------------------------------------------------*/
    // "name", "pid", "cpuUsage", "memoryTotal", "memoryFree", "memoryUsed", "memoryUsage", "diskTotal", "diskFree", "diskUsed", "diskUsage", "networkBandWidth", "networkReceived", "networkSent", "networkUsed", "timeCreated"
    // "channel", "level", "id", "activityId", "bookmark", "keywords", "keywordsDisplayNames", "levelDisplayName", "logName", "machineName", "opcode", "opcodeDisplayName", "processId", "properties", "providerId", "providerName", "qualifiers", "recordId", "relatedActivityId", "task", "taskDisplayName", "threadId", "timeCreated", "userId", "version", "message"

    @Field(type = FieldType.Double, name = "cpuUsage")
    public Double cpuUsage;

    @Field(type = FieldType.Long, name = "memoryAvailable")
    private Long memoryAvailable;

    @Field(type = FieldType.Long, name = "memoryTotal")
    private Long memoryTotal;

    @Field(type = FieldType.Long, name = "memoryUsed")
    private Long memoryUsed;

    @Field(type = FieldType.Double, name = "memoryUsage")
    private Double memoryUsage;

    @Field(type = FieldType.Long, name = "diskTotal")
    private Long diskTotal;

    @Field(type = FieldType.Long, name = "diskUsed")
    private Long diskUsed;

    @Field(type = FieldType.Double, name = "diskUsage")
    private Double diskUsage;

    @Field(type = FieldType.Double, name = "networkSent")
    private Double networkSent;

    @Field(type = FieldType.Double, name = "networkReceived")
    private Double networkReceived;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

}