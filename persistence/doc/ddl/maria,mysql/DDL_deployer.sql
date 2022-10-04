
CREATE TABLE IF NOT EXISTS deploy (
                                      DEPLOY_ID BIGINT(20) NOT NULL COMMENT 'Deploy 식별자',
                                      DEPLOY_NAME VARCHAR(255) NOT NULL COMMENT '배포명',
                                      DEPLOY_TYPE INT NOT NULL COMMENT '1 : 윈도우 업데이트\n2 : 오피스 업데이트\n3 : 윈도우 보안 업데이트\n4 : 일반 파일\n...',
                                      FILE_NAME VARCHAR(255) NOT NULL COMMENT '파일명',
                                      FILE_SIZE BIGINT(20) NOT NULL COMMENT '파일크기 (Byte)',
                                      CREATOR VARCHAR(255) NULL COMMENT '생성자',
                                      CREATED_DATE DATETIME NULL COMMENT '생성일',
                                      PRIMARY KEY (DEPLOY_ID))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table deploy_transfer
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS deploy_transfer (
                                               DEPLOY_TRANSFER_ID BIGINT(20) NOT NULL COMMENT 'DeployTransfer 식별자',
                                               DEPLOY_ID BIGINT(20) NOT NULL COMMENT 'Deploy 식별자',
                                               BANDWIDTH_KBPS INT NULL COMMENT '전송속도',
                                               USE_COMPRESS BIT(1) NULL COMMENT '압축여부',
                                               MULTICAST_GROUP_ADDRESS VARCHAR(15) NULL COMMENT 'Multicast Group Address',
                                               MULTICAST_GROUP_PORT INT NULL COMMENT 'Multicast Group Port',
                                               STATUS VARCHAR(10) NULL COMMENT '상태 : READY, SENDING, COMPLETE',
                                               REPEAT_COUNT INT NULL COMMENT '반복전송 횟수',
                                               TRANSFER_COUNT INT NULL COMMENT '분할 수',
                                               REACTIVE_CLIENT_COUNT INT NULL COMMENT '수신단말 수',
                                               FINISHED_DATE DATETIME NULL COMMENT '전송완료일시',
                                               CREATOR VARCHAR(255) NULL COMMENT '전송실행자',
                                               CREATED_DATE DATETIME NULL COMMENT '전송실행일시',
                                               EXECUTE_INSTALL_DATE DATETIME NULL COMMENT '설치 실행일시',
                                               RECEIVE_STATUS LONGTEXT NULL,
                                               PRIMARY KEY (DEPLOY_TRANSFER_ID),
                                               INDEX FKIDX_DEPLOY_TRANSFER1 (DEPLOY_ID ASC),
                                               INDEX IDX_DEPLOY_TRANSFER1 (DEPLOY_ID ASC, DEPLOY_TRANSFER_ID ASC),
                                               CONSTRAINT fk_DEPLOY_TRANSFER_DEPLOY
                                                   FOREIGN KEY (DEPLOY_ID)
                                                       REFERENCES deploy (DEPLOY_ID)
                                                       ON DELETE NO ACTION
                                                       ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table deploy_transfer_history
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS deploy_transfer_history (
                                                       DEPLOY_TRANSFER_HISTORY_ID BIGINT(20) NOT NULL COMMENT 'DeployTransferHistory 식별자',
                                                       DEPLOY_TRANSFER_ID BIGINT(20) NOT NULL COMMENT 'DeployTransfer 식별자',
                                                       REPEAT_POS INT NULL,
                                                       SEQ INT NULL COMMENT '순번',
                                                       SENT_DATE DATETIME NULL COMMENT '송신일시',
                                                       CHUNK_SIZE_ORG BIGINT(20) NULL COMMENT 'chunk 원본size (압축전)',
                                                       CHUNK_SIZE_SENT BIGINT(20) NULL COMMENT 'chunk 송신size (압축후) : 압축사이즈가 더 큰경우는 ORG로 전송',
                                                       USE_COMPRESS BIT(1) NULL COMMENT '압축여부',
                                                       LOSS_COUNT INT NULL COMMENT '손실량',
                                                       PRIMARY KEY (DEPLOY_TRANSFER_HISTORY_ID),
                                                       INDEX FKIDX_DEPLOY_TRANSFER_HISTORY (DEPLOY_TRANSFER_ID ASC) ,
                                                       INDEX IDX_DEPLOY_TRANSFER_HISTORY1 (DEPLOY_TRANSFER_ID ASC, DEPLOY_TRANSFER_HISTORY_ID ASC) ,
                                                       CONSTRAINT fk_DEPLOY_TRANSFER_HISTORY_DEPLOY_TRANSFER1
                                                           FOREIGN KEY (DEPLOY_TRANSFER_ID)
                                                               REFERENCES deploy_transfer (DEPLOY_TRANSFER_ID)
                                                               ON DELETE NO ACTION
                                                               ON UPDATE NO ACTION)
    ENGINE = InnoDB
COMMENT = '											';
