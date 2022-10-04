package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter 
@Setter
public class IntegrityLogDataInfo {

    @Field(type = FieldType.Keyword, name = "NetClient5")
    private String NetClient5;

}