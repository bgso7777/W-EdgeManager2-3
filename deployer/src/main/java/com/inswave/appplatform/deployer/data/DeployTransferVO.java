package com.inswave.appplatform.deployer.data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeployTransferVO {
    private Long    deployTransferId;        // DeployTransfer 식별자
    private Long    deployId;                // Deploy 식별자
    private Integer bandwidthKbps;           // 전송속도
    private Boolean useCompress;             // 압축여부
    private String  multicastGroupAddress;   // Multicast Group Address
    private Integer multicastGroupPort;      // Multicast Group Port
    private String  status;                  // 상태
    private Integer transferCount;           // 분할 수
    private Integer transferDoneCount;       // 전송 수
    private Integer transferRate;            // 전송율

    private Integer lossCount;               // 손실 수
    private Integer lossRate;                // 손실율
    private Integer reactiveClientCount;     // 수신단말 수

    private Integer receiveDoneCount;       // 수신 수
    private Integer receiveRate;            // 수신율 = (수신/분할) * 100

    private String  executor;                // 전송실행자
    private String  executedDate;            // 전송실행일시
    private String  finishedDate;            // 전송완료일시
    private String  executeInstallDate;      // 설치실행일시
    private Integer repeatCount;             // 반복전송 수
}
