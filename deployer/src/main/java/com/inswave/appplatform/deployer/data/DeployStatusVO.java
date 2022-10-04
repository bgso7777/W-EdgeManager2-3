package com.inswave.appplatform.deployer.data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeployStatusVO {
    private Long    deployId;               // Deploy 식별자
    private Long    deployTransferId;       // DeployTransfer 식별자
    private Boolean isStop;                 // 정지여부
    private Integer repeatCount;            // 반복 길이
    private Integer repeatPos;              // 반복 위치
    private Integer chunkCount;             // 길이
    private Integer chunkPos;               // 현위치
}
