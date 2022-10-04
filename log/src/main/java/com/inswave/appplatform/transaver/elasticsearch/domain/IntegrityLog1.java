package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.HashMap;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class IntegrityLog1 extends Document2 {

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


    /*@Field(type = FieldType.Object, name = "integrity")
    private IntegrityLogData integrity;*/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Field(type = FieldType.Date, name = "startTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date startTime;

    @Field(type = FieldType.Date, name = "endTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date endTime;

    @Field(type = FieldType.Keyword, name = "elapsedTime")
    private String elapsedTime;

    @Field(type = FieldType.Integer, name = "result")
    private Integer result; // -200 부

    @Field(type = FieldType.Object, name = "info")
    private HashMap<String, String> info = new HashMap<>(); // "info" : { "netclient5": { "UID": {x86:"" x64: "" }

    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*@Field(type = FieldType.Nested, name = "integrityData")
    private IntegrityLogDataCommon[] integrityData;*/

    @Field(type = FieldType.Object, name = "integrityData")
    private HashMap<String,IntegrityLogDataCommon> integrityData = new HashMap<>();

    /*@Field(type = FieldType.Keyword, name = "name")
    private String name;

    @Field(type = FieldType.Date, name = "startTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date startTime;

    @Field(type = FieldType.Date, name = "endTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date endTime;

    @Field(type = FieldType.Keyword, name = "elapsedTime")
    private String elapsedTime;

    @Field(type = FieldType.Integer, name = "result")
    private Integer result;

    @Field(type = FieldType.Boolean, name = "isTargetHome")
    private Boolean isTargetHome;

    @Field(type = FieldType.Boolean, name = "isAlways")
    private Boolean isAlways;

    @Field(type = FieldType.Nested, name = "rules")
    private List<IntegrityLogDataRule> rules;*/

}
