package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.List;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter
@Setter
public class ClientHWInfoResourceLogOriginal extends Document {

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

    @Field(type = FieldType.Nested, name = "clientHWInfoResourceData")
    private List<ClientHWInfoResourceLogData> clientHWInfoResourceData;

    /*

    apache-flume-1.9.0-bin-ClientHWInfoResourceLog	8015
    clientHWInfoResource :  {
    columns : [ osBuildNumber, osServicePackVersion, osVersion,  osName, osArchitecture, osSerialNumber, osCaption, osStatus, pcName, pcModel, pcManufacturer, pcCaption, pcSystemType,  pcStatus , cpuName,  cpuArchitecture, cpuManufacturer, cpuNumberOfCores, cpuThreadCount,   cpuMaxClockSpeed,  cpuStatus, cpuCaption, memName, memModel, memManufacturer, memBankLabel, memSerialNumber, memSpeed, memMemoryType, memStatus, memCaption, memTotalSize, memVirtualTotalSize, diskName, diskModel, diskManufacturer, diskSerialNumber, diskCaption,  diskInterfaceType, diskMediaType, diskKBSize, diskStatus],
    "# 수집 항목 (Array)
    osBuildNumber :  운영 체제의 빌드 번호,
    osServicePackVersion :  설치된 최신 서비스팩 버전,
    osVersion :  운영 체제의 버전 번호,
    osName :  이름,
            osArchitecture :  운영 체제의 아키텍쳐,
            osSerialNumber :  운영 체제 제품 일련 식별 번호,
    osCaption :  간단한 설명,
    osStatus :  상태
    pcName :  이름,
            pcModel :  제품 이름,
    pcManufacturer : 컴퓨터 제조업체의 이름,
            pcCaption :  간단한 설명,
    pcSystemType :  시스템 정보 ( ex, ""x64 기반 PC""),
    pcStatus :  상태
    cpuName :  이름
    cpuArchitecture : 프로세서 아키텍쳐,
    cpuManufacturer :  제조업체의 이름,
    cpuNumberOfCores :  코어 개수,
    cpuThreadCount :  스레드 개수,
    cpuMaxClockSpeed :  프로세서의 최대 속도(MHz),
            cpuStatus :  상태,
            cpuCaption :  간단한 설명,
    memName :  이름,
            memModel :  제품 이름,
    memManufacturer : 제조업체의 이름,
    memBankLabel : 실제로 레이블이 지정 된 bank의 메모리 위치,
            memDeviceLocator : 소켓 또는 회로 보드의 레이블,
            memSerialNumber :  일련 식별 번호,
            memSpeed :  실제 메모리의 속도(nanoseconds),
            memMemoryType :  메모리의 유형,
    memTotalSize : 실제 메모리의 총 크기 (kb),
            memVirtualTotalSize :  가상 메모리의 수(kb),
            memStatus : 상태 ,
            memCaption :  간단한 설명,
    diskName :  이름,
            diskModel :  제품 이름,
    diskManufacturer : 제조업체의 이름,
    diskSerialNumber :  일련 식별 번호,
            diskCaption :  간단한 설명,
    diskInterfaceType :  인터페이스 유형,
    diskMediaType :  미디어 유형,
    diskKBSize :  디스크 크기,
    diskStatus :  상태"

            */
}