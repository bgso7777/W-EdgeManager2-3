package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class ClientControlProcessResourceLogDaily extends Document2 {

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

    @Field(type = FieldType.Keyword, name = "pid")
    private String pid;

    @Field(type = FieldType.Keyword, name = "procName")
    private String procName;

    @Field(type = FieldType.Keyword, name = "procParentPid")
    private String procParentPid;

    @Field(type = FieldType.Keyword, name = "procSessionID")
    private String procSessionID;

    @Field(type = FieldType.Keyword, name = "execResult")
    private String execResult;

}