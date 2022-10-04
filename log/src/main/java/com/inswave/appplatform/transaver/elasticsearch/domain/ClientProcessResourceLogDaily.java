package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=2, refreshInterval="-1", createIndex=false)

@Getter 
@Setter
public class ClientProcessResourceLogDaily extends Document2 {

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

    @Field(type = FieldType.Double, name = "pid")
    private Integer pid;

    // 동일한 이름의 프로세스가 여러개인 경우 procName이 프로세스명#1, 프로세스명#2
    @Field(type = FieldType.Keyword, name = "procName")
    private String procName;

    @Field(type = FieldType.Long, name = "procCpuUsage")
    private Long procCpuUsage;

    @Field(type = FieldType.Long, name = "procThreadCount")
    private Long procThreadCount;

    @Field(type = FieldType.Long, name = "procHandleCount")
    private Long procHandleCount;

    @Field(type = FieldType.Long, name = "procWorkingSet")
    private Long procWorkingSet;//메모리 양

    @Field(type = FieldType.Long, name = "procPoolPage")
    private Long procPoolPage;

    @Field(type = FieldType.Long, name = "procPoolNonpaged")
    private Long procPoolNonpaged;

    @Field(type = FieldType.Long, name = "procIoRead")
    private Long procIoRead;

    @Field(type = FieldType.Long, name = "procIoWrite")
    private Long procIoWrite;

    @Field(type = FieldType.Long, name = "procMemoryUsed")
    private Long procMemoryUsed;

    @Field(type = FieldType.Long, name = "memroyTotal")
    private Long memroyTotal;

    @Field(type = FieldType.Long, name = "procMemoryUsage")
    private Long procMemoryUsage;

    @Field(type = FieldType.Float, name = "procNetworkSent")
    private Float procNetworkSent;

    @Field(type = FieldType.Float, name = "procNetworkReceived")
    private Float procNetworkReceived;

    @Field(type = FieldType.Keyword, name = "procFileDescription")
    private String procFileDescription;

    @Field(type = FieldType.Boolean, name = "procIntegrityLevel")
    private Boolean procIntegrityLevel;
}