package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class ClientPerformanceResourceLogStatistics extends Document2 {

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

    @Field(type = FieldType.Integer, name = "count")
    private Integer count=0; // ???????????????????????????????????

    @Field(type = FieldType.Double, name = "cpuUsage")
    private Double cpuUsage=0.0;

    @Field(type = FieldType.Long, name = "threadCount")
    private Long threadCount=0L;

    @Field(type = FieldType.Long, name = "handleCount")
    private Long handleCount=0L;

    @Field(type = FieldType.Long, name = "memoryUsed")
    private Long memoryUsed=0L;

    @Field(type = FieldType.Long, name = "memoryTotal")
    private Long memoryTotal=0L;

    @Field(type = FieldType.Long, name = "memoryUsage")
    private Long memoryUsage=0L;

//    @Field(type = FieldType.Long, name = "poolPaged")
//    public Long poolPaged=0L;

    @Field(type = FieldType.Long, name = "poolNonpaged")
    private Long poolNonpaged=0L;

    @Field(type = FieldType.Long, name = "diskUsage")
    private Long diskUsage=0L;

//    @Field(type = FieldType.Long, name = "diskTotal")
//    private Long diskTotal=0L;

    @Field(type = FieldType.Double, name = "diskUsed")
    private Double diskUsed=0.0;

//    @Field(type = FieldType.Long, name = "diskTime")
//    private Double diskTime=0L;

    @Field(type = FieldType.Long, name = "diskRead")
    private Double diskRead=0.0;

    @Field(type = FieldType.Long, name = "diskWrite")
    private Double diskWrite=0.0;

//    @Field(type = FieldType.Long, name = "networkUsed")
//    public Long networkUsed=0L;

    @Field(type = FieldType.Double, name = "networkSent")
    private Double networkSent=0.0;

    @Field(type = FieldType.Double, name = "networkReceived")
    private Double networkReceived=0.0;

    @Field(type = FieldType.Double, name = "cpuTemperature")
    private Double cpuTemperature=0.0;

    @Field(type = FieldType.Long, name = "memoryVirtualTotal")
    public Long memoryVirtualTotal=0L;
}