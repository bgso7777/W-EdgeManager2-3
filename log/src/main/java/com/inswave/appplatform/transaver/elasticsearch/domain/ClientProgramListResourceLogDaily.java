package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.Date;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=1, replicas=2, refreshInterval="-1", createIndex=false)

@Getter
@Setter
public class ClientProgramListResourceLogDaily extends Document2 {

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

    @Field(type = FieldType.Keyword, name = "name")
    private String name;//""name"": ""W-Matrix(SHBNext Dev)"",

    @Field(type = FieldType.Keyword, name = "version")
    private String version;//""version"": ""1.100.3.0706"",

    @Field(type = FieldType.Keyword, name = "publisher")
    private String publisher;//""publisher"": ""Inswave Systems Co., Ltd."",

    @Field(type = FieldType.Integer, name = "size")
    private Integer size;// ""size"": null

    @Field(type = FieldType.Date, name = "installDate", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date installDate;
}
