package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
public class ClientHWInfoResourceLogDataDiskDrive {

    @Field(type = FieldType.Keyword, name = "diskName")
    private String diskName;

    @Field(type = FieldType.Keyword, name = "diskModel")
    private String diskModel;

    @Field(type = FieldType.Keyword, name = "diskManufacturer")
    private String diskManufacturer;

    @Field(type = FieldType.Keyword, name = "diskSerialNumber")
    private String diskSerialNumber;

    @Field(type = FieldType.Keyword, name = "diskCaption")
    private String diskCaption;

    @Field(type = FieldType.Keyword, name = "diskInterfaceType")
    private String diskInterfaceType;

    @Field(type = FieldType.Keyword, name = "diskMediaType")
    private String diskMediaType;

    @Field(type = FieldType.Long, name = "diskKBSize")
    private Long diskKBSize;

    @Field(type = FieldType.Keyword, name = "diskStatus")
    private String diskStatus;

    /*

		"diskDrive": [{
			"diskName": "\\\\.\\PHYSICALDRIVE0",
			"diskModel": "TS1TMTE110S",
			"diskManufacturer": "(표준 디스크 드라이브)",
			"diskSerialNumber": "G293010121",
			"diskCaption": "TS1TMTE110S",
			"diskInterfaceType": "SCSI",
			"diskMediaType": "Fixed hard disk media",
			"diskKBSize": 1024203640320,
			"diskStatus": "OK"
		}],

    */
}
