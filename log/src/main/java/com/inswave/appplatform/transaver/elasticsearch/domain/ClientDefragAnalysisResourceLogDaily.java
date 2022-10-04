package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter
@Setter
public class ClientDefragAnalysisResourceLogDaily extends Document2 {

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

    /*
            "driveLetter": "C:",
            "averageFileSize": 48,
            "averageFragmentsPerFile": 1.04,
            "averageFreeSpacePerExtent": 2912256,
            "clusterSize": 4096,
            "excessFolderFragments": 1739,
            "filePercentFragmentation": 7,
            "fragmentedFolders": 591,
            "freeSpace": 215867305984,
            "freeSpacePercent": 67,
            "largestFreeSpaceExtent": 170572492800,
            "MFTPercentInUse": 100,
            "MFTRecordCount": 937727,
            "totalExcessFragments": 25419,
            "totalFiles": 562424,
            "totalFolders": 34346,
            "totalFragmentedFiles": 8510,
            "totalFreeSpaceExtents": 74032,
            "totalMFTFragments": 7,
            "usedSpace": 105651486720,
            "volumeName": null,
            "volumeSize": 321518792704
    */
    @Field(type = FieldType.Keyword, name = "driveLetter")
    private String driveLetter; //	 할당된 드라이브 문자

    @Field(type = FieldType.Long, name = "averageFileSize")
    private Long averageFileSize; // 파일의 평균 크기(Byte)

    @Field(type = FieldType.Float, name = "averageFragmentsPerFile")
    private Float averageFragmentsPerFile; // 파일 당 평균 조각 수

    @Field(type = FieldType.Long, name = "averageFreeSpacePerExtent")
    private Long averageFreeSpacePerExtent; // 여유 공간 범위의 평균 크기,

    @Field(type = FieldType.Long, name = "clusterSize")
    private Long clusterSize; // 파일 시스템 할당 단위의 크기(Byte),

    @Field(type = FieldType.Integer, name = "excessFolderFragments")
    private Integer excessFolderFragments; // 초과 폴더 조각 수,

    @Field(type = FieldType.Integer, name = "filePercentFragmentation")
    private Integer filePercentFragmentation; // 조각화 된 파일의 백분율,

    @Field(type = FieldType.Integer, name = "fragmentedFolders")
    private Integer fragmentedFolders; // 조각난 폴더 수,

    @Field(type = FieldType.Double, name = "freeSpace")
    private Double freeSpace; // 사용 가능 공간,

    @Field(type = FieldType.Integer, name = "freeSpacePercent")
    private Integer freeSpacePercent; // 여유 공간 백분율,

    @Field(type = FieldType.Double, name = "largestFreeSpaceExtent")
    private Double largestFreeSpaceExtent; // 가장 큰 여유 공간의 범위 크기,

    @Field(type = FieldType.Long, name = "mFTPercentInUse")
    private Long mFTPercentInUse; // 마스터 파일 테이블 백분율,

    @Field(type = FieldType.Long, name = "mFTRecordCount")
    private Long mFTRecordCount; // 마스터 파일 테이블 레코드 수,

    @Field(type = FieldType.Integer, name = "totalExcessFragments")
    private Integer totalExcessFragments; // 볼륨 초과 파일 조각 수,

    @Field(type = FieldType.Integer, name = "totalFiles")
    private Integer totalFiles; // 파일 수,

    @Field(type = FieldType.Integer, name = "totalFolders")
    private Integer totalFolders; // 디렉토리 수,

    @Field(type = FieldType.Integer, name = "totalFragmentedFiles")
    private Integer totalFragmentedFiles; // 조각난 파일 수,

    @Field(type = FieldType.Double, name = "totalFreeSpaceExtents")
    private Double totalFreeSpaceExtents; // 여유 공간 익스텐트 수,

    @Field(type = FieldType.Integer, name = "totalMFTFragments")
    private Integer totalMFTFragments; // 마스터 파일 테이블 조각 수,

    @Field(type = FieldType.Double, name = "usedSpace")
    private Double usedSpace; // 사용 바이트 수,

    @Field(type = FieldType.Keyword, name = "volumeName")
    private String volumeName; // 이름,

    @Field(type = FieldType.Long, name = "volumeSize")
    private Long volumeSize; // 크기

}
