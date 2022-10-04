package com.inswave.appplatform.deployer.domain;

import com.inswave.appplatform.dao.Domain;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@TableGenerator(
name = "DEPLOY_TRANSFER_HISTORY_GENERATOR",
pkColumnValue = "DEPLOY_TRANSFER_HISTORY_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "deploy_transfer_history")
public class DeployTransferHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DEPLOY_TRANSFER_HISTORY_GENERATOR")
    @Column
    private Long    deployTransferHistoryId;     // DeployTransferHistory 식별자
    @Column
    private Long    deployTransferId;            // DeployTransfer 식별자
    @Column
    private Integer repeatPos;                   // 반복순번
    @Column
    private Integer seq;                         // 순번
    @Column
    private Date    sentDate;                    // 송신일시
    @Column
    private Long    chunkSizeOrg;                // chunk 원본size (압축전)
    @Column
    private Long    chunkSizeSent;               // chunk 송신size (압축후) : 압축사이즈가 더 큰경우는 ORG로 전송
    @Column
    private Boolean useCompress;                 // 압축여부
    @Column
    private Integer lossCount;                   // 손실량
}
