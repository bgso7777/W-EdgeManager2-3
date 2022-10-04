package com.inswave.appplatform.deployer.domain;

import com.inswave.appplatform.dao.Domain;
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
name = "DEPLOY_GENERATOR",
pkColumnValue = "DEPLOY_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class Deploy implements Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DEPLOY_GENERATOR")
    private Long    deployId;            // Deploy 식별자
    @Column
    private String  deployName;          // 배포명
    @Column
    private Integer deployType;          // 배포유형 (001 : 윈도우 업데이트, 002 : 오피스 업데이트, 003 : 윈도우 보안 업데이트, 004 : 일반파일)
    @Column
    private String  fileName;            // 파일명
    @Column
    private Long    fileSize;            // 파일크기 (Byte)
    @CreatedBy
    private String  creator;             // 생성자
    @CreatedDate
    //    @Temporal(TemporalType.TIMESTAMP)
    private Date    createdDate;         // 생성일
}
