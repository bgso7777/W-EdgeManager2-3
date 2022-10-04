package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class ClientActivePortListResourceLog extends Document2 {

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

    @Field(type = FieldType.Keyword, name = "protocalType")
    private String protocalType;

    @Field(type = FieldType.Keyword, name = "localAddress")
    private String localAddress;

    @Field(type = FieldType.Keyword, name = "localPort")
    private String localPort;

    @Field(type = FieldType.Keyword, name = "remoteAddress")
    private String remoteAddress;

    @Field(type = FieldType.Keyword, name = "remotePort")
    private String remotePort;

    @Field(type = FieldType.Keyword, name = "netState")
    private String netState;

    @Field(type = FieldType.Keyword, name = "procId")
    private String procId;

    @Field(type = FieldType.Keyword, name = "procName")
    private String procName;

}