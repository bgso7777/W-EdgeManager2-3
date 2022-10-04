package com.inswave.appplatform.deployer.data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeployTransferHistoryVO {
    private Long    deployTransferHistoryId;     // DeployTransferHistory 식별자
    private Long    deployTransferId;            // DeployTransfer 식별자
    private Integer repeatPos;                   // 반복순번
    private Integer seq;                         // 순번
    private String  sentDate;                    // 송신일시
    private Long    chunkSizeOrg;                // chunk 원본size
    private Long    chunkSizeSent;               // chunk 송신size
    private Boolean useCompress;                 // 압축여부
    private Integer compressRate;                // 압축율
    private Integer lossCount;                   // 손실량
}
