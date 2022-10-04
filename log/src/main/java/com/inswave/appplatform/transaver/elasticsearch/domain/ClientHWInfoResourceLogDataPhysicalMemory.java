package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
public class ClientHWInfoResourceLogDataPhysicalMemory {

    @Field(type = FieldType.Keyword, name = "memName")
    private String memName;

    @Field(type = FieldType.Keyword, name = "memModel")
    private String memModel;

    @Field(type = FieldType.Keyword, name = "memManufacturer")
    private String memManufacturer;

    @Field(type = FieldType.Keyword, name = "memBankLabel")
    private String memBankLabel;

    @Field(type = FieldType.Keyword, name = "memSerialNumber")
    private String memSerialNumber;

    @Field(type = FieldType.Integer, name = "memSpeed")
    private Integer memSpeed;

    @Field(type = FieldType.Integer, name = "memMemoryType")
    private Integer memMemoryType;

    @Field(type = FieldType.Keyword, name = "memStatus")
    private String memStatus;

    @Field(type = FieldType.Keyword, name = "memCaption")
    private String memCaption;

    /*

		"physicalMemory": [{
			"memName": "실제 메모리",
			"memModel": null,
			"memManufacturer": "Samsung",
			"memBankLabel": "BANK 0",
			"memSerialNumber": "39F89EAB",
			"memSpeed": 3200,
			"memMemoryType": 0,
			"memStatus": null,
			"memCaption": "실제 메모리"
		}, {
			"memName": "실제 메모리",
			"memModel": null,
			"memManufacturer": "Samsung",
			"memBankLabel": "BANK 2",
			"memSerialNumber": "00000000",
			"memSpeed": 3200,
			"memMemoryType": 0,
			"memStatus": null,
			"memCaption": "실제 메모리"
		}],

    */
}
