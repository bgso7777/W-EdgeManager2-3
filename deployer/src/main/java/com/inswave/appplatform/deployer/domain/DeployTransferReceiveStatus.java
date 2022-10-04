package com.inswave.appplatform.deployer.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@TableGenerator(
name = "DEPLOY_TRANSFER_GENERATOR",
pkColumnValue = "DEPLOY_TRANSFER_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "deploy_transfer")
public class DeployTransferReceiveStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DEPLOY_TRANSFER_GENERATOR")
    private Long    deployTransferId;        // DeployTransfer 식별자
    @Column
    private Long    deployId;                // Deploy 식별자
    @Column
    private Integer bandwidthKbps;           // 전송속도
    @Column
    private Boolean useCompress;             // 압축여부
    @Column
    private String  multicastGroupAddress;   // Multicast Group Address
    @Column
    private Integer multicastGroupPort;      // Multicast Group Port
    @Column
    private String  status;                  // 상태
    @Column
    private Integer repeatCount;             // 반복전송 수
    @Column
    private Integer transferCount;           // 분할 수
    @Column
    private Integer reactiveClientCount;     // 수신단말 수
    @Column
    private Date    finishedDate;            // 전송완료일시
    @CreatedBy
    private String  creator;                 // 전송실행자
    @CreatedDate
    //    @Temporal(TemporalType.TIMESTAMP)
    private Date    createdDate;             // 전송실행일시
    @Column
    private Date    executeInstallDate;      // 배포파일 설치실행 일시
    @Lob @Column
    private String  receiveStatus;         // 클라이언트 수신상태 json

}
