package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;


/**

 @Document(indexName = "test-index-configure-dynamic-mapping")
 @DynamicMapping(DynamicMappingValue.False)

 class ConfigureDynamicMappingEntity {

 @Nullable
 @DynamicMapping(DynamicMappingValue.Strict)
 @Field(type = FieldType.Object)
 private Author author;

 @Nullable
 public Author getAuthor() {
 return author;
 }
 public void setAuthor(Author author) {
 this.author = author;
 }
 }

 ------------------------------------------------
 @Document(indexName="store_#{department.name()}", indexStoreType="invoice")
 @Document(indexName = "my_data-#{elasticIndex.getIndexDate()}", type = "DataDoc")
 @Document(indexName="#{@indexName}",type = "syslog_watcher")

 elasticsearchTemplate.createIndex(SingleChat.class);
 elasticsearchTemplate.putMapping(SingleChat.class);
 elasticsearchTemplate.refresh(SingleChat.class, true);

 https://pythonq.com/so/java/583980
 @Document(indexName="singlemsgtemp_#{jobParameters['MONTH']}",type="singlechat")
 public class SingleChat {
 @org.springframework.data.annotation.Id
 String Id;
 @Field(type = FieldType.String)
 String conservationId;
 @Field(type = FieldType.String)
 String from;
 @Field(type = FieldType.String)
 String to;
 @Field(type = FieldType.String)
 String msgContent;
 @Field(type = FieldType.String)
 String sessionId;
 @Field(type = FieldType.Date, index = FieldIndex.not_analyzed, store = true, format = DateFormat.date_hour_minute_second_millis)
 Date postedDate;
 @Field(type = FieldType.Date, index = FieldIndex.not_analyzed, store = true, format = DateFormat.date_hour_minute_second_millis)
 Date expireDate;
 }

 esTemplate.createIndex(newIndexName, loadfromFromFile(settingsFileName));
 esTemplate.putMapping(newIndexName, "MYTYPE", loadfromFromFile(mappingFileName));

 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2_#{@serverResourceLog2.getIndexName_()}", createIndex = false)
 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2_#{@logIndexNameProvider__.timeSuffix(super.timeCreated)}", createIndex = false)
 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2_#{@logIndexNameProvider__.timeSuffix(this.timeCreated)}", createIndex = false)
 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2_#{@logIndexNameProvider__.timeSuffix(timeCreated)}", createIndex = false)
 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2_#{@logIndexNameProvider__.timeSuffix(timeCreated)}", createIndex = false)


 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2", createIndex = false)
 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{@logIndexNameProvider.daySuffix()}", createIndex = false)
 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2_#{@logIndexNameProvider_.daySuffix()}", createIndex = false)
 //@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{@indexName}", createIndex = false) /* setIndexName(@Param("indexName") String indexName) 와 같이 사용,  */

//@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(DynamicIndexBean).getIndexName()}", shards=1, replicas=2, refreshInterval="-1", createIndex=false)

//@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2#{@indexName}", shards=2, replicas=1, refreshInterval="-1", createIndex=false)
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "serverresourcelog2", shards=2, replicas=1, refreshInterval="-1", createIndex=false)

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter
public class ServerResourceLog extends Document2 implements Cloneable {

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

    @Field(type = FieldType.Double, name = "networkTx")
    private Double networkTx;

    @Field(type = FieldType.Double, name = "networkRx")
    private Double networkRx;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}