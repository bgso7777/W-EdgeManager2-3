package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
public class ClientPerformanceResourceLogDataDiskInfo {

    @Field(type = FieldType.Keyword, name = "instanceName")
    private String instanceName;

    @Field(type = FieldType.Boolean, name = "isSystemDrive")
    public Boolean isSystemDrive;

    @Field(type = FieldType.Long, name = "diskReadIndividual")
    public Long diskReadIndividual;

    @Field(type = FieldType.Long, name = "diskWriteIndividual")
    public Long diskWriteIndividual;

    @Field(type = FieldType.Long, name = "diskTimeIndividual")
    public Long diskTimeIndividual;

}
