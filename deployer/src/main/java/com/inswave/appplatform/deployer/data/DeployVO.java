package com.inswave.appplatform.deployer.data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeployVO {
    private Long    deployId;            // Deploy 식별자
    private String  deployName;          // 배포명
    private Integer deployType;          // 배포유형
    private String  fileName;            // 파일명
    private Long    fileSize;            // 파일크기
    private String  createdDate;         // 생성일
    private String  lastDeployedDate;    // 최종배포일
    private Integer status;              // 상태
    private Integer statusChunkCount;    // 진행상태 (길이)
    private Integer statusChunkPos;      // 진행상태 (현위치)
}
