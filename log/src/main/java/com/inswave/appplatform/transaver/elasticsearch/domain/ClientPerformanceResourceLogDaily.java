package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.List;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class ClientPerformanceResourceLogDaily extends Document2 {

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

    @Field(type = FieldType.Double, name = "cpuUsage")
    private Double cpuUsage;

    @Field(type = FieldType.Long, name = "threadCount")
    private Long threadCount;

    @Field(type = FieldType.Long, name = "handleCount")
    private Long handleCount;

    @Field(type = FieldType.Long, name = "memoryUsed")
    private Long memoryUsed;

    @Field(type = FieldType.Long, name = "memoryTotal")
    private Long memoryTotal;

    @Field(type = FieldType.Long, name = "memoryUsage")
    private Long memoryUsage;

    @Field(type = FieldType.Long, name = "poolPaged")
    public Long poolPaged;

    @Field(type = FieldType.Long, name = "poolNonpaged")
    private Long poolNonpaged;

    @Field(type = FieldType.Long, name = "diskUsage")
    private Long diskUsage;

    @Field(type = FieldType.Long, name = "diskTotal")
    private Long diskTotal;

    @Field(type = FieldType.Double, name = "diskUsed")
    private Double diskUsed;

    @Field(type = FieldType.Double, name = "diskTime")
    private Double diskTime;

    @Field(type = FieldType.Double, name = "diskRead")
    private Double diskRead;

    @Field(type = FieldType.Double, name = "diskWrite")
    private Double diskWrite;

    @Field(type = FieldType.Double, name = "networkSent")
    private Double networkSent;

    @Field(type = FieldType.Double, name = "networkReceived")
    private Double networkReceived;

    @Field(type = FieldType.Long, name = "networkUsed")
    public Long networkUsed;

    @Field(type = FieldType.Double, name = "cpuTemperature")
    private Double cpuTemperature;

    @Field(type = FieldType.Long, name = "memoryVirtualTotal")
    public Long memoryVirtualTotal;

    @Field(type = FieldType.Nested, name = "diskInfo")
    private List<ClientPerformanceResourceLogDataDiskInfo> diskInfo;
}