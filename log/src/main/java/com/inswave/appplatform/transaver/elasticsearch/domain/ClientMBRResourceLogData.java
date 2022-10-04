package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter
@Setter
public class ClientMBRResourceLogData {


    @Field(type = FieldType.Keyword, name = "name")
    private String name;//""name"": ""\\\\.\\PHYSICALDRIVE0"",

    @Field(type = FieldType.Keyword, name = "deviceID")
    private String deviceID;//""deviceID"": ""\\\\.\\PHYSICALDRIVE0"",

    @Field(type = FieldType.Keyword, name = "sectorMBR")
    private String sectorMBR;// MBR sector ( 512 bytes) ""sectorMBR"": ""M8CO0LwAfI7Ajti+AHy/AAa5AAL886RQaBwGy/u5BAC9vgeAfgAAfAsPhQ4Bg8UQ4vHNGIhWAFXGRhEFxkYQALRBu6pVzRNdcg+B+1WqdQn3wQEAdAP+RhBmYIB+EAB0JmZoAAAAAGb/dghoAABoAHxoAQBoEAC0QopWAIv0zROfg8QQnusUuAECuwB8ilYAinYBik4Cim4DzRNmYXMc/k4RdQyAfgCAD4SKALKA64RVMuSKVgDNE13rnoE+/n1VqnVu/3YA6I0AdRf6sNHmZOiDALDf5mDofACw/+Zk6HUA+7gAu80aZiPAdTtmgftUQ1BBdTKB+QIBcixmaAe7AABmaAACAABmaAgAAABmU2ZTZlVmaAAAAABmaAB8AABmYWgAAAfNGloy9uoAfAAAzRigtwfrCKC2B+sDoLUHMuQFAAeL8Kw8AHQJuwcAtA7NEOvy9Ov9K8nkZOsAJALg+CQCw0ludmFsaWQgcGFydGl0aW9uIHRhYmxlAEVycm9yIGxvYWRpbmcgb3BlcmF0aW5nIHN5c3RlbQBNaXNzaW5nIG9wZXJhdGluZyBzeXN0ZW0AAABje5oAAAAAAAAAAAIA7v///wEAAAD/////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAVao=""

    @Field(type = FieldType.Keyword, name = "partitionType")
    private String partitionType; // 파티션 형식

    @Field(type = FieldType.Boolean, name = "bootPartition")
    private Boolean bootPartition; // 부팅 파티션 있음 여부

    @Field(type = FieldType.Boolean, name = "bootable")
    private Boolean bootable; // 부팅 가능 여부

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;

}
