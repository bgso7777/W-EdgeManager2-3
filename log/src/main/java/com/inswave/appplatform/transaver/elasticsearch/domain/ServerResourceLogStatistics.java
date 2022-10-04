package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServerResourceLogStatistics extends Document2 {

    private String indexName;

    public void setIndexName(@Param("indexName") String indexName) {
        if(indexName.equals("")) {
            indexName = this.getClass().getSimpleName().toLowerCase();
            this.indexName = this.getClass().getSimpleName().toLowerCase();
        } else {
            this.indexName=indexName;
        }
        super.setIndexName(indexName);
    }
    public String getIndexName() {
        return this.indexName;
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private String serverResourceLogMinutelyId;

    /* ----------------------------------------------------------------------*/

    @Field(type = FieldType.Integer, name = "count")
    private Integer count=0;

    @Field(type = FieldType.Keyword, name = "service")
    private String service;

    @Field(type = FieldType.Keyword, name = "appId")
    private String appId;

    @Field(type = FieldType.Keyword, name = "destination")
    private String destination;

    @Field(type = FieldType.Keyword, name = "destination")
    private String osType;

    @Field(type = FieldType.Keyword, name = "destination")
    private String msgId;

    @Field(type = FieldType.Double, name = "siteId")
    private Double siteId;

    @Field(type = FieldType.Keyword, name = "siteName")
    private String siteName;

    @Field(type = FieldType.Keyword, name = "source")
    private String source;

    @Field(type = FieldType.Keyword, name = "deviceId")
    private String deviceId;

    @Field(type = FieldType.Double, name = "cpuUsage")
    private Double cpuUsage;

    @Field(type = FieldType.Double, name = "memoryAvailable")
    private Double memoryAvailable;

    @Field(type = FieldType.Double, name = "memoryTotal")
    private Double memoryTotal;

    @Field(type = FieldType.Double, name = "memoryUsed")
    private Double memoryUsed;

    @Field(type = FieldType.Double, name = "memoryUsage")
    private Double memoryUsage;

    @Field(type = FieldType.Double, name = "diskTotal")
    private Double diskTotal;

    @Field(type = FieldType.Double, name = "diskUsed")
    private Double diskUsed;

    @Field(type = FieldType.Double, name = "diskUsage")
    private Double diskUsage;

    @Field(type = FieldType.Double, name = "networkSent")
    private Double networkSent;

    @Field(type = FieldType.Double, name = "networkReceived")
    private Double networkReceived;

}