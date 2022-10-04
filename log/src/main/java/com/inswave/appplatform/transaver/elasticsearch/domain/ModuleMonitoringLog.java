package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter
@Setter
public class ModuleMonitoringLog extends Document2 implements Cloneable {

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

    @Field(type = FieldType.Keyword, name = "key")
    private String key;

    @Field(type = FieldType.Keyword, name = "instanceId")
    private String instanceId;

    @Field(type = FieldType.Keyword, name = "topicName")
    private String topicName;

    @Field(type = FieldType.Keyword, name = "groupId")
    private String groupId;

    @Field(type = FieldType.Integer, name = "partitionId")
    private Integer partitionId;

    @Field(type = FieldType.Long, name = "offset")
    private Long offset;

    @Field(type = FieldType.Integer, name = "jsonProcessingExceptionCount")
    private Integer jsonProcessingExceptionCount=0;

    @Field(type = FieldType.Integer, name = "commitSyncExceptionCount")
    private Integer commitSyncExceptionCount=0;

    @Field(type = FieldType.Integer, name = "exceptionCount")
    private Integer exceptionCount=0;

    @Field(type = FieldType.Keyword, name = "processDateTime")
    private String processDateTime="";

    @Field(type = FieldType.Integer, name = "processCount")
    private Integer processCount=0;

    @Field(type = FieldType.Long, name = "processByte")
    private Long processByte=0L;

    @Field(type = FieldType.Long, name = "processTime")
    private Long processTime=0L;

    @Field(type = FieldType.Long, name = "bps")
    private Long bps=0L;

    @Field(type = FieldType.Long, name = "bpms")
    private Long bpms=0L;

    @Field(type = FieldType.Integer, name = "rowSize")
    private Integer rowSize=0;

    @Field(type = FieldType.Integer, name = "lastRowSize")
    private Integer lastRowSize=-1;

    @Field(type = FieldType.Integer, name = "maxRowSize")
    private Integer maxRowSize=-999999999;

    @Field(type = FieldType.Integer, name = "minRowSize")
    private Integer minRowSize=999999999;

    @Field(type = FieldType.Integer, name = "dailyRowSize")
    private Integer dailyRowSize=0;

    @Field(type = FieldType.Long, name = "runningTime")
    private Long runningTime=0L;

    @Field(type = FieldType.Long, name = "objectConvertTime")
    private Long objectConvertTime=0L;

    @Field(type = FieldType.Long, name = "elasticsearchCurrentInsert")
    private Long elasticsearchCurrentInsert=0L;

    @Field(type = FieldType.Long, name = "elasticsearchDailyInsert")
    private Long elasticsearchDailyInsert=0L;

    @Field(type = FieldType.Integer, name = "year")
    private int year = 0;

    @Field(type = FieldType.Integer, name = "month")
    private int month = 0;

    @Field(type = FieldType.Integer, name = "day")
    private int day = 0;

    @Field(type = FieldType.Integer, name = "dayOfWeek")
    private int dayOfWeek = -1;

    @Field(type = FieldType.Integer, name = "hour")
    private int hour = -1;

    @Field(type = FieldType.Integer, name = "minutely10")
    private int minutely10 = -1;

    @Field(type = FieldType.Long, name = "beginOffsets")
    private Long beginOffsets=0L;

    @Field(type = FieldType.Long, name = "endOffsets")
    private Long endOffsets=0L;

    @Field(type = FieldType.Long, name = "processOffsets")
    private Long processOffsets=1L;

    public ModuleMonitoringLog cloneModuleMonitoringLog() throws CloneNotSupportedException {
        return (ModuleMonitoringLog)this.clone();
    }
}
