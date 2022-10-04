package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter
@Setter
public abstract class RelationDocument {

    @Field(type = FieldType.Keyword, name = "indexName")
    private String indexName="";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Field(type = FieldType.Keyword, name = "documentId")
    private String documentId;

    @Field(type = FieldType.Date, name = "timeUpdated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeUpdated;

    // 등록 시간
    @Field(type = FieldType.Date, name = "timeRegistered", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeRegistered;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}