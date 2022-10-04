package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class RuleAlertExclusion extends RelationDocument {

    @Field(type = FieldType.Keyword, name = "indexName")
    private String indexName;
    public void setIndexName(@Param("indexName") String indexName) {
        if (indexName.equals("")) {
            indexName = this.getClass().getSimpleName().toLowerCase();
            this.indexName = indexName;
            super.setIndexName(indexName);
        } else {
            this.indexName = indexName;
            super.setIndexName(indexName);
        }
    }
    public String getIndexName() {
        return this.indexName;
    }

    @Field(type = FieldType.Keyword, name = "name")
    private String name; /* 제외 명 */

    @Field(type = FieldType.Keyword, name = "description")
    private String description; /* 제외 설명 */

    @Field(type = FieldType.Boolean, name = "active")
    private Boolean active=false; /* 활성여부 */

    @Field(type = FieldType.Integer, name = "exclusionType")
    private Integer exclusionType=0; // 제외 타입 1:알림 등록 제외, 2:알림 전송 제외

    @Field(type = FieldType.Keyword, name = "className")
    private String className; /* 대상 클래스 */

    @Field(type = FieldType.Keyword, name = "ip")
    private String ip;

    @Field(type = FieldType.Keyword, name = "hostName")
    private String hostName;

    @Field(type = FieldType.Keyword, name = "userId")
    private String userId;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}