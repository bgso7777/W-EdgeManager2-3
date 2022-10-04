package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ClientHWInfoResourceLogData {

    @Field(type = FieldType.Keyword, name = "osBuildNumber")
    private String osBuildNumber; // 운영 체제의 빌드 번호, ""osBuildNumber"": ""19042"",

    @Field(type = FieldType.Keyword, name = "osServicePackVersion")
    private String osServicePackVersion; // 설치된 최신 서비스팩 버전,""osServicePackVersion"": null,

    @Field(type = FieldType.Keyword, name = "osVersion")
    private String osVersion; // 운영 체제의 버전 번호, ""osVersion"": ""10.0.19042"",

    @Field(type = FieldType.Keyword, name = "osName")
    private String osName; // 이름, ""osName"": ""Microsoft Windows 10 Pro|C:\\WINDOWS|\\Device\\Harddisk0\\Partition3"",

    @Field(type = FieldType.Keyword, name = "osArchitecture")
    private String osArchitecture; // 운영 체제의 아키텍쳐,""osArchitecture"": ""64비트"",

    @Field(type = FieldType.Keyword, name = "osSerialNumber")
    private String osSerialNumber; // 운영 체제 제품 일련 식별 번호,""osSerialNumber"": ""00330-80000-00000-AA248"",

    @Field(type = FieldType.Keyword, name = "osCaption")
    private String osCaption; // 간단한 설명,""osCaption"": ""Microsoft Windows 10 Pro"",

    @Field(type = FieldType.Keyword, name = "osStatus")
    private String osStatus; // 상태""osStatus"": ""OK"",

    @Field(type = FieldType.Keyword, name = "pcName")
    private String pcName; // 이름,""pcName"": ""DESKTOP-J879OFU"",

    @Field(type = FieldType.Keyword, name = "pcModel")
    private String pcModel; // 제품 이름,""pcModel"": ""15ZD90N-VX70K"",

    @Field(type = FieldType.Keyword, name = "pcManufacturer")
    private String pcManufacturer; // 컴퓨터 제조업체의 이름, ""pcManufacturer"": ""LG Electronics"",

    @Field(type = FieldType.Keyword, name = "pcCaption")
    private String pcCaption; // 간단한 설명,""pcCaption"": ""DESKTOP-J879OFU"",

    @Field(type = FieldType.Keyword, name = "pcSystemType")
    private String pcSystemType; // 시스템 정보 ( ex, ""x64 기반 PC""),""pcSystemType"": ""x64-based PC"",

    @Field(type = FieldType.Keyword, name = "pcStatus")
    private String pcStatus; // 상태" "pcStatus"": ""OK"",

    @Field(type = FieldType.Keyword, name = "cpuName")
    private String cpuName; // 이름 ""cpuName"": ""Intel(R) Core(TM) i7-1065G7 CPU @ 1.30GHz"",

    @Field(type = FieldType.Keyword, name = "cpuArchitecture")
    private String cpuArchitecture; // 프로세서 아키텍쳐,

    @Field(type = FieldType.Keyword, name = "cpuManufacturer")
    private String cpuManufacturer; // 제조업체의 이름,

    @Field(type = FieldType.Integer, name = "cpuNumberOfCores")
    private Integer cpuNumberOfCores; // 코어 개수,""cpuNumberOfCores"": 4,

    @Field(type = FieldType.Integer, name = "cpuThreadCount")
    private Integer cpuThreadCount; // 스레드 개수,""cpuThreadCount"": 8,

    @Field(type = FieldType.Integer, name = "cpuMaxClockSpeed")
    private Integer cpuMaxClockSpeed; // 프로세서의 최대 속도(MHz),""cpuMaxClockSpeed"": 1498,

    @Field(type = FieldType.Keyword, name = "cpuStatus")
    private String cpuStatus; // 상태,""cpuStatus"": ""OK"",

    @Field(type = FieldType.Keyword, name = "cpuCaption")
    private String cpuCaption; // 간단한 설명,""cpuCaption"": ""Intel64 Family 6 Model 126 Stepping 5"",

    @Field(type = FieldType.Keyword, name = "memName")
    private String memName; // 이름,""memName"": ""실제 메모리"",

    @Field(type = FieldType.Keyword, name = "memModel")
    private String memModel; // 제품 이름,""memModel"": null,

    @Field(type = FieldType.Keyword, name = "memManufacturer")
    private String memManufacturer; // 제조업체의 이름,""memManufacturer"": ""Samsung"",

    @Field(type = FieldType.Keyword, name = "memBankLabel")
    private String memBankLabel; // 실제로 레이블이 지정 된 bank의 메모리 위치,""memBankLabel"": ""BANK 0"",

    @Field(type = FieldType.Keyword, name = "memDeviceLocator")
    private String memDeviceLocator; // 소켓 또는 회로 보드의 레이블,""memDeviceLocator"": ""ChannelA-DIMM0"",

    @Field(type = FieldType.Keyword, name = "memSerialNumber")
    private String memSerialNumber; // 일련 식별 번호,""memSerialNumber"": ""39F89EAB"",

    @Field(type = FieldType.Integer, name = "memSpeed")
    private Integer memSpeed; // 실제 메모리의 속도(nanoseconds),""memSpeed"": 3200,

    @Field(type = FieldType.Integer, name = "memMemoryType")
    private Integer memMemoryType; // 메모리의 유형,""memMemoryType"": 0,

    @Field(type = FieldType.Long, name = "memTotalSize")
    private Long memTotalSize; // 실제 메모리의 총 크기 (kb),

    @Field(type = FieldType.Long, name = "memVirtualTotalSize")
    private Long memVirtualTotalSize; // 가상 메모리의 수(kb), ""memVirtualTotalSize"": 20699992,

    @Field(type = FieldType.Keyword, name = "memStatus")
    private String memStatus; // 상태 ,""memStatus"": null,

    @Field(type = FieldType.Keyword, name = "memCaption")
    private String memCaption; // 간단한 설명,""memCaption"": ""실제 메모리"",

    @Field(type = FieldType.Keyword, name = "memName_2")
    private String memName_2;//""memName_2"": ""실제 메모리"",

    @Field(type = FieldType.Keyword, name = "memModel_2")
    private String memModel_2;//""memModel_2"": null,

    @Field(type = FieldType.Keyword, name = "memManufacturer_2")
    private String memManufacturer_2;//""memManufacturer_2"": ""Samsung"",

    @Field(type = FieldType.Keyword, name = "memBankLabel_2")
    private String memBankLabel_2;//""memBankLabel_2"": ""BANK 2"",

    @Field(type = FieldType.Keyword, name = "memDeviceLocator_2")
    private String memDeviceLocator_2;//""memDeviceLocator_2"": ""ChannelB-DIMM0"",

    @Field(type = FieldType.Keyword, name = "memSerialNumber_2")
    private String memSerialNumber_2;//""memSerialNumber_2"": ""00000000"",

    @Field(type = FieldType.Integer, name = "memSpeed_2")
    private Integer memSpeed_2;//""memSpeed_2"": 3200,

    @Field(type = FieldType.Integer, name = "memMemoryType_2")
    private Integer memMemoryType_2;//""memMemoryType_2"": 0,

    @Field(type = FieldType.Keyword, name = "memStatus_2")
    private String memStatus_2;//  ""memStatus_2"": null,

    @Field(type = FieldType.Keyword, name = "memCaption_2")
    private String memCaption_2;//  ""memCaption_2"": ""실제 메모리"",

    @Field(type = FieldType.Keyword, name = "diskName")
    private String diskName; // 이름,""diskName"": ""\\\\.\\PHYSICALDRIVE0"",

    @Field(type = FieldType.Keyword, name = "diskModel")
    private String diskModel; // 제품 이름,""diskModel"": ""TS1TMTE110S"",

    @Field(type = FieldType.Keyword, name = "diskManufacturer")
    private String diskManufacturer; // 제조업체의 이름,""diskManufacturer"": ""(표준 디스크 드라이브)"",

    @Field(type = FieldType.Keyword, name = "diskSerialNumber")
    private String diskSerialNumber; // 일련 식별 번호,""diskSerialNumber"": ""G293010121"",

    @Field(type = FieldType.Keyword, name = "diskCaption")
    private String diskCaption; // 간단한 설명,""diskCaption"": ""TS1TMTE110S"",

    @Field(type = FieldType.Keyword, name = "diskInterfaceType")
    private String diskInterfaceType; // 인터페이스 유형,""diskInterfaceType"": ""SCSI"",

    @Field(type = FieldType.Keyword, name = "diskMediaType")
    private String diskMediaType; // 미디어 유형,""diskMediaType"": ""Fixed hard disk media"",

    @Field(type = FieldType.Long, name = "diskKBSize")
    private Long diskKBSize; // 디스크 크기,""diskKBSize"": 1024203640320,

    @Field(type = FieldType.Keyword, name = "diskStatus")
    private String diskStatus; // 상태"""diskStatus"": ""OK""

    @Field(type = FieldType.Keyword, name = "diskName_2")
    private String diskName_2; // 이름,""diskName"": ""\\\\.\\PHYSICALDRIVE0"",

    @Field(type = FieldType.Keyword, name = "diskModel_2")
    private String diskModel_2; // 제품 이름,""diskModel"": ""TS1TMTE110S"",

    @Field(type = FieldType.Keyword, name = "diskManufacturer_2")
    private String diskManufacturer_2; // 제조업체의 이름,""diskManufacturer"": ""(표준 디스크 드라이브)"",

    @Field(type = FieldType.Keyword, name = "diskSerialNumber_2")
    private String diskSerialNumber_2; // 일련 식별 번호,""diskSerialNumber"": ""G293010121"",

    @Field(type = FieldType.Keyword, name = "diskCaption_2")
    private String diskCaption_2; // 간단한 설명,""diskCaption"": ""TS1TMTE110S"",

    @Field(type = FieldType.Keyword, name = "diskInterfaceType_2")
    private String diskInterfaceType_2; // 인터페이스 유형,""diskInterfaceType"": ""SCSI"",

    @Field(type = FieldType.Keyword, name = "diskMediaType_2")
    private String diskMediaType_2; // 미디어 유형,""diskMediaType"": ""Fixed hard disk media"",

    @Field(type = FieldType.Long, name = "diskKBSize_2")
    private Long diskKBSize_2; // 디스크 크기,""diskKBSize"": 1024203640320,

    @Field(type = FieldType.Keyword, name = "diskStatus_2")
    private String diskStatus_2; // 상태"""diskStatus"": ""OK""

    @Field(type = FieldType.Nested, name = "physicalMemory")
    private List<ClientHWInfoResourceLogDataPhysicalMemory> physicalMemory;

    @Field(type = FieldType.Nested, name = "diskDrive")
    private List<ClientHWInfoResourceLogDataDiskDrive> diskDrive;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;
}