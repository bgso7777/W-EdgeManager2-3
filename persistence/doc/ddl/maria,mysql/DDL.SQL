-- --------------------------------------------------------
-- 호스트:                          3.34.253.162
-- 서버 버전:                        10.4.19-MariaDB - MariaDB Server
-- 서버 OS:                        Linux
-- HeidiSQL 버전:                  11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE IF NOT EXISTS em_seq_generator (
                                                SEQUENCE_NAME VARCHAR(255) NOT NULL,
                                                NEXT_VAL BIGINT(20) NULL,
                                                PRIMARY KEY (SEQUENCE_NAME)
) ENGINE = InnoDB;


-- 테이블 wedgemanager.admin_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `admin_user` (
    `ADMIN_USER_ID` bigint(20) NOT NULL COMMENT '사용자 ID',
    `SITE_ID` bigint(20) NOT NULL COMMENT '사이트 ID',
    `ROLE_ID` bigint(20) NOT NULL COMMENT '권한 ID',
    `COUNTRY_ID` bigint(20) NOT NULL COMMENT '국가 ID',
    `LOGIN_ID` varchar(255) DEFAULT NULL COMMENT '로그인 아이디',
    `PASSWORD` varchar(255) DEFAULT NULL COMMENT '패스워드(암호화된 스트링)',
    `IS_FIRST_PASSWORD` bit(1) DEFAULT NULL COMMENT '최초 패스워드 여부',
    `NAME` varchar(255) DEFAULT NULL COMMENT '이름',
    `USERNAME` varchar(255) DEFAULT NULL COMMENT 'uniqueId(로그인 아이디)',
    `EMAIL` varchar(255) DEFAULT NULL COMMENT '이메일',
    `PHONE` varchar(255) DEFAULT NULL COMMENT '핸드폰번호',
    `CONNECT_IPS` varchar(255) DEFAULT NULL COMMENT '접속 IP',
    `COUNT_WRONG_PASSWORD` int(11) DEFAULT NULL COMMENT '로그인 실패 수',
    `PASSWORD_DUE_DATE` datetime DEFAULT NULL COMMENT '패스워드 기한일시',
    `UPDATE_DATE` datetime DEFAULT NULL COMMENT '수정일시',
    `LOGIN_FAIL_LOCK_DATE` datetime DEFAULT NULL COMMENT '로그인 실패 lock 일시',
    `LOGIN_DATE` datetime DEFAULT NULL COMMENT '로그인일시',
    `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    `AUTH_TOKEN` varchar(20000) DEFAULT NULL,
    PRIMARY KEY (`ADMIN_USER_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.admin_user:~3 rows (대략적) 내보내기
DELETE FROM `admin_user`;

INSERT INTO `admin_user` (`ADMIN_USER_ID`, `SITE_ID`, `ROLE_ID`, `COUNTRY_ID`, `LOGIN_ID`, `PASSWORD`, `IS_FIRST_PASSWORD`, `NAME`, `USERNAME`, `EMAIL`, `PHONE`, `CONNECT_IPS`, `COUNT_WRONG_PASSWORD`, `PASSWORD_DUE_DATE`, `UPDATE_DATE`, `LOGIN_FAIL_LOCK_DATE`, `LOGIN_DATE`, `REGISTRATION_DATE`, `AUTH_TOKEN`) VALUES
(1, 1, 1, 1, 'admin@inswave.com', '1234', b'0', 'admin1', 'admin1', 'admin@inswave.com', NULL, NULL, 0, '2020-12-07 12:18:14', '2020-12-07 12:18:21', '2021-02-17 14:14:39', '2021-08-23 17:48:42', '2020-12-07 12:18:24', NULL),
(2, 1, 2, 1, 'admin2@inswave.com', '1234', b'0', 'admin2', 'admin2', 'admin1@inswave.com', NULL, NULL, 0, NULL, NULL, NULL, '2021-07-08 21:58:12', NULL, NULL),
(3, 1, 3, 1, 'admin3@inswave.com', '1234', b'0', 'admin3', 'admin3', 'admin2@inswave.com', NULL, NULL, 0, NULL, NULL, NULL, '2021-07-09 10:20:39', NULL, NULL);
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('ADMIN_USER_SEQ' ,3);

-- 테이블 wedgemanager.config 구조 내보내기
CREATE TABLE IF NOT EXISTS `config` (
    `CONFIG_ID` bigint(20) NOT NULL COMMENT '설정 ID',
    `KEY_` varchar(255) DEFAULT NULL COMMENT '설정 키',
    `VALUE` varchar(255) DEFAULT NULL COMMENT '설정 값1',
    `VALUE2` varchar(2048) DEFAULT NULL COMMENT '설정 값2',
    `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '설정 설명',
    `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    PRIMARY KEY (`CONFIG_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 테이블 데이터 wedgemanager.config:~9 rows (대략적) 내보내기
DELETE FROM `config`;

INSERT INTO `config` (`CONFIG_ID`, `KEY_`, `VALUE`, `VALUE2`, `DESCRIPTION`, `REGISTRATION_DATE`) VALUES
(1, 'managerServerUrl', 'http://3.34.253.162:8081/api/wem/manager', NULL, 'manager 관리자 url (복수일 경우 , 로 구분 8081~8085)', '2021-04-26 14:50:00'),
(2, 'relayServerUrl', 'http://127.0.0.1:8082/api/wem/transaver', NULL, 'transaver url (웹소켓  복수일 경우 , 로 구분)', '2021-04-26 14:50:00'),
(3, 'logServerUrl', 'http://52.79.53.230:8080/api/wem/log', NULL, 'log manager url (복수일 경우 , 로 구분 8086~8090)', '2021-04-26 14:50:00'),
(4, 'adminServerUrl', 'http://3.34.253.162:8080/', NULL, 'admin 관리자 UI url (복수일 경우 , 로 구분)', '2021-04-26 14:50:00'),
(5, 'smtpHost', 'smtp.gmail.com', NULL, 'SMTP 호스트', '2021-05-16 17:04:55'),
(6, 'smtpPort', '465', NULL, 'SMTP 포트', '2021-05-16 17:08:58'),
(7, 'smtpId', 'bgso@inswave.com', NULL, 'SMTP 로그인 아이디', '2021-05-16 17:09:12'),
(8, 'smtpPassword', '12345qwer', NULL, 'SMTP 로그인 패스워드', '2021-05-16 17:09:22'),
(9, 'smtpName', 'W-EdgeManager 관리자', NULL, 'SMTP 발신자 명', '2021-05-16 17:09:30');
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('config', 9);

-- 테이블 wedgemanager.config_log 구조 내보내기
CREATE TABLE IF NOT EXISTS `config_log` (
    `CONFIG_ID` bigint(20) NOT NULL COMMENT '설정 ID',
    `KEY_` varchar(255) DEFAULT NULL COMMENT '설정 키',
    `VALUE` varchar(255) DEFAULT NULL COMMENT '설정 값1',
    `VALUE2` varchar(255) DEFAULT NULL COMMENT '설정 값2',
    `LOG_SAVED_DAY` int(11) NOT NULL DEFAULT -1 COMMENT '로그저장일자',
    `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '설정 설명',
    `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    PRIMARY KEY (`CONFIG_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 테이블 데이터 wedgemanager.config_log:~19 rows (대략적) 내보내기
DELETE FROM `config_log`;
/*!40000 ALTER TABLE `config_log` DISABLE KEYS */;
INSERT INTO `config_log` (`CONFIG_ID`, `KEY_`, `VALUE`, `VALUE2`, `LOG_SAVED_DAY`, `DESCRIPTION`, `REGISTRATION_DATE`) VALUES
(1, 'isSendLogToCollector', 'true', '0', -1, '로그 전송 여부', '2021-04-26 14:50:00'),
(2, 'realtimeDataReplaceDate', 'yyyy-MM-dd', NULL, -1, '실시간 현황 서버 수집 데이터 주기 (일자:yyyy-MM-dd 시간:yyyy-MM-dd HH 분:yyyy-MM-dd HH mm)', '2021-07-19 15:55:05'),
(3, '1. ServerResourceLog', 'http://172.31.3.88:8001/api/log/serverresourcelog', '3000', -1, '서버 시스템의 리소스 로그(내부IP)', '2021-04-23 17:30:01'),
(4, '2. ClientResourceLog', 'http://52.79.53.230:8002/api/log/clientresourcelog', '-1', -1, '시스템의 리소스 로그 ', '2021-04-26 15:12:59'),
(5, '3. WindowsEventSystemErrorAllLog', 'http://52.79.53.230:8003/api/log/windowseventsystemerroralllog', '-1', -1, '윈도우 이벤트', '2021-06-06 10:24:48'),
(6, '4. HWErrorLog', 'http://52.79.53.230:8004/api/log/hwerrorlog', '-1', -1, 'HW 장애 로그', '2021-04-26 15:12:59'),
(7, '5. DeviceErrorLog', 'http://52.79.53.230:8005/api/log/deviceerrorlog', '-1', -1, '디바이스 에러로그', '2021-04-26 15:12:59'),
(8, '6. ApplicationErrorLog', 'http://52.79.53.230:8006/api/log/applicationerrorlog', '-1', -1, '실행 프로그램 에러 로그', '2021-04-26 15:12:59'),
(9, '7. ClientPerformanceLog', 'http://52.79.53.230:8007/api/log/clientperformancelog', '-1', -1, '퍼포먼스 로그', '2021-04-26 15:12:59'),
(10, '8. WindowsBlueScreenLog', 'http://52.79.53.230:8008/api/log/windowsbluescreenlog', '-1', -1, '블루스크린', '2021-04-26 15:12:59'),
(11, '9. IntegrityLog', 'http://52.79.53.230:8009/api/log/integritylog', '-1', -1, '정합성 로그', '2021-04-26 15:12:59'),
(12, '10. PcOnOffEventLog', 'http://52.79.53.230:8010/api/log/pconoffeventlog', '-1', -1, 'PC On Off', '2021-06-25 16:19:40'),
(13, '11. ClientMBRResourceLog', 'http://52.79.53.230:8011/api/log/clientmbrresourcelog', '-1', -1, 'MBR', '2021-08-11 17:12:59'),
(14, '12. ClientDefragAnalysisResourceLog', 'http://52.79.53.230:8012/api/log/clientdefraganalysisresourcelog', '-1', -1, '조각모음', '2021-08-11 17:13:01'),
(15, '13. ClientProgramListResourceLog', 'http://52.79.53.230:8013/api/log/clientprogramlistresourcelog', '-1', -1, '프로그램 리스트', '2021-08-11 17:13:03'),
(16, '14. ClientWindowsUpdateLog', 'http://52.79.53.230:8014/api/log/clientwindowsupdatelog', '-1', -1, '윈도우 업데이트', '2021-08-11 17:13:05'),
(17, '15. ClientHWInfoResourceLog', 'http://52.79.53.230:8015/api/log/clienthwinforesourcelog', '-1', -1, 'H/W정보', '2021-08-11 17:13:07'),
(18, '16. ClientActivePortListResourceLog', 'http://52.79.53.230:8016/api/log/clientactiveportlistresourcelog', '-1', -1, '프로세스별 포트 리스트', '2021-08-11 17:13:09'),
(19, '17. ClientUserTermMonitorLog', 'http://52.79.53.230:8017/api/log/clientusertermmonitorlog', '-1', -1, '사용자 정의 로그', '2021-08-11 17:13:11'),
(20, '18. ClientProcessCreationLog', 'http://52.79.53.230:8018/api/log/clientprocesscreationlog', '-1', 10, '프로세스 실행 이력 이벤트 로그', '2021-08-11 17:13:11'),
(21, '19. ClientControlProcessResourceLog', 'http://52.79.53.230:8019/api/log/clientcontrolprocessresourcelog', 'clientcontrolprocessresourcelog', 10, '프로세스 제어 로그', '2021-11-16 20:15:11'),
(22, 'integritylogSendCheckErrorCount', '0', '', -1, '신한은행 정합성 로그 장애예측시스템 전송 에러 룰 카운트', '2022-01-07 13:20:00'),
(23, 'deleteDailyIndices', 'modulemonitoringlogdaily', '10', -1, '엘라스틱서치 인덱스 삭제 ,로 구분 value index명 value2 저장일', '2022-01-07 13:20:00');


-- 테이블 wedgemanager.config_manager 구조 내보내기
CREATE TABLE IF NOT EXISTS `config_manager` (
    `CONFIG_ID` bigint(20) NOT NULL COMMENT '설정 ID',
    `KEY_` varchar(255) DEFAULT NULL COMMENT '설정 키',
    `VALUE` varchar(255) DEFAULT NULL COMMENT '설정 값1',
    `VALUE2` varchar(255) DEFAULT NULL COMMENT '설정 값2',
    `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '설정 설명',
    `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    PRIMARY KEY (`CONFIG_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 테이블 데이터 wedgemanager.config_manager:~5 rows (대략적) 내보내기
DELETE FROM `config_manager`;

INSERT INTO `config_manager` (`CONFIG_ID`, `KEY_`, `VALUE`, `VALUE2`, `DESCRIPTION`, `REGISTRATION_DATE`) VALUES
(1, 'countLoginFailLock', '5', NULL, '관리자 로그인 실패 lock count', '2021-04-23 17:30:01'),
(2, 'minuteLoginFailLock', '5', NULL, '관리자 로그인 실패 시 lock minute (잠김)', '2021-04-23 17:30:01'),
(3, 'userPasswordDay', '60', NULL, '관리자 패스워드 사용일', '2021-04-23 17:30:01'),
(4, 'isSaveUiLog', 'false', NULL, '관리자 로그 저장 여부', '2021-04-23 17:30:01'),
(5, 'isCheckIp', 'false', NULL, '관리자 접속 IP 제한', '2021-04-23 17:30:01');
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('config_manager', 5);

-- 테이블 wedgemanager.country 구조 내보내기
CREATE TABLE IF NOT EXISTS `country` (
    `COUNTRY_ID` int(11) NOT NULL ,
    `NAME` varchar(45) NOT NULL,
    `DISPLAY_NAME` varchar(45) NOT NULL,
    `DESCRIPTION` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`COUNTRY_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 테이블 데이터 wedgemanager.country:~3 rows (대략적) 내보내기
DELETE FROM `country`;

INSERT INTO `country` (`COUNTRY_ID`, `NAME`, `DISPLAY_NAME`, `DESCRIPTION`) VALUES
(1, 'korea', 'korean', '한국'),
(2, 'usa', 'english', '미국'),
(3, 'japan', 'japanese', '일본');
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('country', 3);

-- 테이블 wedgemanager.DATABASECHANGELOGLOCK 구조 내보내기
CREATE TABLE IF NOT EXISTS `DATABASECHANGELOGLOCK` (
    `ID` int(11) NOT NULL,
    `LOCKED` bit(1) NOT NULL,
    `LOCKGRANTED` datetime DEFAULT NULL,
    `LOCKEDBY` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.DATABASECHANGELOGLOCK:~1 rows (대략적) 내보내기
DELETE FROM `DATABASECHANGELOGLOCK`;

INSERT INTO `DATABASECHANGELOGLOCK` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES
(1, b'0', NULL, NULL);
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('DATABASECHANGELOGLOCK', 1);

-- 테이블 wedgemanager.em_collection 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_collection` (
    `ID` int(11) NOT NULL,
    `GATHERTHRESTHOLDID` int(11) NOT NULL,
    `SENDOPTIONID` int(11) NOT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_collection:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_collection_info 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_collection_info` (
    `ID` int(11) NOT NULL,
    `COLLECTIONID` int(11) NOT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_collection_info:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_collection_policy 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_collection_policy` (
    `ID` int(11) NOT NULL,
    `GATHERTHRESTHOLDID` int(11) NOT NULL,
    `SENDOPTIONID` int(11) NOT NULL,
    `CONDITIONSID` int(11) NOT NULL,
    `COLLECTIONINFOID` int(11) NOT NULL,
    `URL` varchar(45) DEFAULT NULL,
    `INTERVALTYPE` varchar(45) DEFAULT NULL,
    `_INTERVAL` varchar(45) DEFAULT NULL,
    `COLUMNS` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_collection_policy:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_conditions 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_conditions` (
    `ID` int(11) NOT NULL,
    `level` varchar(45) DEFAULT NULL,
    `channel` varchar(45) DEFAULT NULL,
    `date` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_conditions:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_department 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_department` (
    `DEPARTCODE` varchar(30) NOT NULL,
    `DEPARTNAME` varchar(50) NOT NULL,
    `DEPARTLEVEL` int(10) NOT NULL,
    `UPPERCODE` varchar(30) NOT NULL,
    `CREATEDATE` timestamp(6) NULL DEFAULT NULL,
    `UPDATEDATE` timestamp(6) NULL DEFAULT NULL,
    PRIMARY KEY (`DEPARTCODE`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_department:~32 rows (대략적) 내보내기
DELETE FROM `em_department`;

INSERT INTO `em_department` (`DEPARTCODE`, `DEPARTNAME`, `DEPARTLEVEL`, `UPPERCODE`, `CREATEDATE`, `UPDATEDATE`) VALUES
('0000', '신한은행', 1, 'root', NULL, NULL),
('0061', 'ICT 운영부', 3, '200000', NULL, NULL),
('0064', '글로벌개발부', 3, '200000', NULL, NULL),
('100000', '국외', 2, '0000', NULL, NULL),
('200000', '국내', 2, '0000', NULL, NULL),
('7501', '아메리카신한은행 본점', 3, '100000', NULL, NULL),
('7531', '아메리카신한은행 동부 영업 본부', 3, '100000', NULL, NULL),
('7532', '맨하탄 지점', 3, '100000', NULL, NULL),
('7533', '플러싱 지점', 3, '100000', NULL, NULL),
('7534', '노던 150 지점', 3, '100000', NULL, NULL),
('7535', '베이사이드 지점', 3, '100000', NULL, NULL),
('7536', '리틀넥 지점', 3, '100000', NULL, NULL),
('7537', '포트리 지점', 3, '100000', NULL, NULL),
('7538', '펠리세이드 팍 지점', 3, '100000', NULL, NULL),
('7541', '뉴저지 론센터', 3, '100000', NULL, NULL),
('7542', '뉴욕 론센터', 3, '100000', NULL, NULL),
('7601', '아메리카신한은행 본점(캘리포니아)', 3, '100000', NULL, NULL),
('7602', '올림픽 지점', 3, '100000', NULL, NULL),
('7603', '뷰에나파크 지점', 3, '100000', NULL, NULL),
('7605', '얼바인 지점', 3, '100000', NULL, NULL),
('7606', '아메리카신한은행 마케팅전략팀', 3, '100000', NULL, NULL),
('7608', '토렌스 지점', 3, '100000', NULL, NULL),
('7609', '샌디에고 지점', 3, '100000', NULL, NULL),
('7610', '로스앤젤레스 론센터', 3, '100000', NULL, NULL),
('7611', '오렌지카운티 론센터', 3, '100000', NULL, NULL),
('7621', '아메리카신한은행 서부 영업본부', 3, '100000', NULL, NULL),
('7681', '아메리카신한은행 남부 본부', 3, '100000', NULL, NULL),
('7682', '들루스 지점', 3, '100000', NULL, NULL),
('7683', '달라스 지점', 3, '100000', NULL, NULL),
('7684', 'NAB', 3, '100000', NULL, NULL),
('7685', '텍사스 론센터', 3, '100000', NULL, NULL),
('7686', '조지아 론센터', 3, '100000', NULL, NULL);


-- 테이블 wedgemanager.em_devices 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_devices` (
    `OSTYPE` varchar(30) NOT NULL,
    `APPID` varchar(50) NOT NULL,
    `USERID` varchar(30) NOT NULL,
    `UNIQUEID` varchar(50) DEFAULT NULL,
    `IP` varchar(30) DEFAULT NULL,
    `SESSIONSTATE` int(10) DEFAULT NULL,
    `CPU` varchar(100) DEFAULT NULL,
    `RAM` varchar(100) DEFAULT NULL,
    `HDD` varchar(100) DEFAULT NULL,
    `VERSION` varchar(30) DEFAULT NULL,
    `WBROWSER_VERSION` varchar(30) DEFAULT NULL,
    `UPDATEVERSION` varchar(30) DEFAULT NULL,
    `UPDATESTATE` varchar(30) DEFAULT NULL,
    `UPDATESCOPE` varchar(1) DEFAULT NULL,
    `UPDATEUPDATEDATE` timestamp(6) NULL DEFAULT NULL,
    `DISTVERSION` varchar(50) DEFAULT NULL,
    `DISTSCOPE` varchar(1) DEFAULT NULL,
    `DISTSTATE` varchar(30) DEFAULT NULL,
    `DISTUPDATEDATE` timestamp(6) NULL DEFAULT NULL,
    `STATE` varchar(30) DEFAULT NULL,
    `PORT` varchar(5) DEFAULT NULL,
    `CREATEDATE` timestamp(6) NULL DEFAULT NULL,
    `UPDATEDATE` timestamp(6) NULL DEFAULT NULL,
    `LASTDATE` timestamp(6) NULL DEFAULT NULL,
    PRIMARY KEY (`OSTYPE`,`APPID`,`USERID`),
    KEY `em_devices_IDX01` (`UNIQUEID`),
    KEY `em_devices_IDX02` (`IP`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_devices:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_devicestate 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_devicestate` (
    `DEVICEID` varchar(30) NOT NULL,
    `APPID` varchar(50) NOT NULL,
    `USERID` varchar(30) NOT NULL,
    PRIMARY KEY (`DEVICEID`,`APPID`,`USERID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_devicestate:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_devicestate_test 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_devicestate_test` (
    `DEVICEID` varchar(30) NOT NULL,
    `APPID` varchar(50) NOT NULL,
    `USERID` varchar(30) NOT NULL,
    `SESSIONSATE` int(11) DEFAULT NULL,
    PRIMARY KEY (`DEVICEID`,`APPID`,`USERID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_devicestate_test:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_devices_test 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_devices_test` (
    `ID` varchar(30) NOT NULL,
    `APPID` varchar(50) NOT NULL,
    `OSTYPE` varchar(15) NOT NULL,
    `UNIQUEID` varchar(50) DEFAULT NULL,
    `IP` varchar(20) DEFAULT NULL,
    `SESSIONSATE` int(11) DEFAULT NULL,
    `UPDATEVERSION` varchar(45) DEFAULT NULL,
    `UPDATESTATE` varchar(45) DEFAULT NULL,
    `UPDATESCOPE` varchar(1) DEFAULT NULL,
    `UPDATEUPDATEDATE` datetime DEFAULT NULL,
    `DISTVERSION` varchar(45) DEFAULT NULL,
    `DISTSCOPE` varchar(45) DEFAULT NULL,
    `DISTSTATE` varchar(1) DEFAULT NULL,
    `DISTUPDATEDATE` datetime DEFAULT NULL,
    `STATE` varchar(45) DEFAULT NULL,
    `CREATEDATE` datetime DEFAULT NULL,
    `UPDATEDATE` datetime DEFAULT NULL,
    `LASTDATE` datetime DEFAULT NULL,
    `DEPARTCODE` varchar(30) NOT NULL,
    PRIMARY KEY (`ID`,`APPID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_devices_test:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_dists 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_dists` (
    `APPID` varchar(50) NOT NULL,
    `VERSION` varchar(50) NOT NULL,
    `SCOPE` varchar(1) NOT NULL,
    `EXECUTOR` varchar(30) NOT NULL,
    `EXECUTEDATE` timestamp(6) NOT NULL,
    `STATUS` varchar(1) NOT NULL,
    `LATEST` varchar(1) NOT NULL,
    `DESCRIPTION` varchar(1000) DEFAULT NULL,
    `CREATEDATE` timestamp(6) NULL DEFAULT NULL,
    `UPDATEDATE` timestamp(6) NULL DEFAULT NULL,
    PRIMARY KEY (`APPID`,`VERSION`,`SCOPE`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_dists:~6 rows (대략적) 내보내기
DELETE FROM `em_dists`;

INSERT INTO `em_dists` (`APPID`, `VERSION`, `SCOPE`, `EXECUTOR`, `EXECUTEDATE`, `STATUS`, `LATEST`, `DESCRIPTION`, `CREATEDATE`, `UPDATEDATE`) VALUES
('Windows.ATOP.Dev', '2021.07.21.11.10.38', 'F', 'admin', '2021-07-21 11:10:38.055000', 'E', 'N', NULL, '2021-07-21 11:10:38.055000', '2021-07-26 18:55:23.814000'),
('Windows.Inswave.Prod', '2021.07.21.11.10.38', 'F', 'admin', '2021-07-21 11:10:38.055000', 'E', 'N', NULL, '2021-07-21 11:10:38.055000', '2021-07-26 18:55:23.814000'),
('Windows.Inswave.Prod', '2021.07.26.18.55.13', 'F', 'admin@inswave.com', '2021-07-26 18:55:23.814000', 'E', 'N', NULL, '2021-07-26 18:55:23.814000', '2021-07-26 19:02:42.864000'),
('Windows.Inswave.Prod', '2021.07.26.19.02.42', 'F', 'admin@inswave.com', '2021-07-26 19:02:42.864000', 'E', 'N', NULL, '2021-07-26 19:02:42.864000', '2021-07-26 19:03:16.977000'),
('Windows.Inswave.Prod', '2021.07.26.19.03.16', 'F', 'admin@inswave.com', '2021-07-26 19:03:16.977000', 'E', 'N', NULL, '2021-07-26 19:03:16.977000', '2021-07-26 19:05:56.872000'),
('Windows.Inswave.Prod', '2021.07.26.19.05.56', 'F', 'admin@inswave.com', '2021-07-26 19:05:56.872000', 'S', 'Y', NULL, '2021-07-26 19:05:56.872000', '2021-07-26 19:05:56.872000');


-- 테이블 wedgemanager.em_dists_departments 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_dists_departments` (
    `APPID` varchar(50) NOT NULL,
    `VERSION` varchar(50) NOT NULL,
    `SCOPE` varchar(1) NOT NULL,
    `TARGET` varchar(30) NOT NULL,
    `CREATEDATE` timestamp(6) NULL DEFAULT NULL,
    `UPDATEDATE` timestamp(6) NULL DEFAULT NULL,
    PRIMARY KEY (`APPID`,`VERSION`,`SCOPE`,`TARGET`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_dists_departments:~6 rows (대략적) 내보내기
DELETE FROM `em_dists_departments`;

INSERT INTO `em_dists_departments` (`APPID`, `VERSION`, `SCOPE`, `TARGET`, `CREATEDATE`, `UPDATEDATE`) VALUES
('Windows.ATOP.Dev', '2021.07.21.11.10.38', 'F', '0000', '2021-07-21 11:10:38.055000', '2021-07-21 11:10:38.055000'),
('Windows.Inswave.Prod', '2021.07.21.11.10.38', 'F', '0000', '2021-07-21 11:10:38.055000', '2021-07-21 11:10:38.055000'),
('Windows.Inswave.Prod', '2021.07.26.18.55.13', 'F', '000000', '2021-07-26 18:55:23.814000', '2021-07-26 18:55:23.814000'),
('Windows.Inswave.Prod', '2021.07.26.19.02.42', 'F', '000000', '2021-07-26 19:02:42.864000', '2021-07-26 19:02:42.864000'),
('Windows.Inswave.Prod', '2021.07.26.19.03.16', 'F', '000000', '2021-07-26 19:03:16.977000', '2021-07-26 19:03:16.977000'),
('Windows.Inswave.Prod', '2021.07.26.19.05.56', 'F', '000000', '2021-07-26 19:05:56.872000', '2021-07-26 19:05:56.872000');


-- 테이블 wedgemanager.em_dists_files 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_dists_files` (
    `APPID` varchar(50) NOT NULL,
    `VERSION` varchar(50) NOT NULL,
    `SCOPE` varchar(1) NOT NULL,
    `PATH` varchar(500) NOT NULL,
    `COMPAREKEY` varchar(64) NOT NULL,
    `REEXECUTE`  varchar(10)  NULL,
    PRIMARY KEY (`APPID`,`VERSION`,`SCOPE`,`PATH`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_dists_files:~35 rows (대략적) 내보내기
DELETE FROM `em_dists_files`;

INSERT INTO `em_dists_files` (`APPID`, `VERSION`, `SCOPE`, `PATH`, `COMPAREKEY`) VALUES
('Windows.ATOP.Dev', '2021.07.21.11.10.38', 'F', 'CommonUtil.dll', '3AE33F95B9A8217CED844BDBDDE68E90EA3D9380D5B2096CFA5D9F4A82D3681E'),
('Windows.ATOP.Dev', '2021.07.21.11.10.38', 'F', 'Config.dll', '555BED3B4F150565C56C48E7624A7AFE3FB40965B63C03211F912BAD0ED48A2D'),
('Windows.ATOP.Dev', '2021.07.21.11.10.38', 'F', 'DistributionInfo.dll', '6FDAAC115DE6DE3A0A77CB08936A3A27BB459D2EE6B048AE4F509187B220B555'),
('Windows.ATOP.Dev', '2021.07.21.11.10.38', 'F', 'HandlerMain.dll', '5E21716C94334B728D07AD52FEA7EBA55CAF605B3E1ABFCB7B8E31C925C42F03'),
('Windows.ATOP.Dev', '2021.07.21.11.10.38', 'F', 'IntegrityManager.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.21.11.10.38', 'F', 'CommonUtil.dll', '3AE33F95B9A8217CED844BDBDDE68E90EA3D9380D5B2096CFA5D9F4A82D3681E'),
('Windows.Inswave.Prod', '2021.07.21.11.10.38', 'F', 'Config.dll', '555BED3B4F150565C56C48E7624A7AFE3FB40965B63C03211F912BAD0ED48A2D'),
('Windows.Inswave.Prod', '2021.07.21.11.10.38', 'F', 'DistributionInfo.dll', '6FDAAC115DE6DE3A0A77CB08936A3A27BB459D2EE6B048AE4F509187B220B555'),
('Windows.Inswave.Prod', '2021.07.21.11.10.38', 'F', 'HandlerMain.dll', '5E21716C94334B728D07AD52FEA7EBA55CAF605B3E1ABFCB7B8E31C925C42F03'),
('Windows.Inswave.Prod', '2021.07.21.11.10.38', 'F', 'IntegrityManager.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.21.11.10.38', 'F', 'test.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.26.18.55.13', 'F', 'CommonUtil.dll', '3AE33F95B9A8217CED844BDBDDE68E90EA3D9380D5B2096CFA5D9F4A82D3681E'),
('Windows.Inswave.Prod', '2021.07.26.18.55.13', 'F', 'Config.dll', '555BED3B4F150565C56C48E7624A7AFE3FB40965B63C03211F912BAD0ED48A2D'),
('Windows.Inswave.Prod', '2021.07.26.18.55.13', 'F', 'DistributionInfo.dll', '6FDAAC115DE6DE3A0A77CB08936A3A27BB459D2EE6B048AE4F509187B220B555'),
('Windows.Inswave.Prod', '2021.07.26.18.55.13', 'F', 'HandlerMain.dll', '5E21716C94334B728D07AD52FEA7EBA55CAF605B3E1ABFCB7B8E31C925C42F03'),
('Windows.Inswave.Prod', '2021.07.26.18.55.13', 'F', 'IntegrityManager.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.26.18.55.13', 'F', 'test.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.26.19.02.42', 'F', 'CommonUtil.dll', '3AE33F95B9A8217CED844BDBDDE68E90EA3D9380D5B2096CFA5D9F4A82D3681E'),
('Windows.Inswave.Prod', '2021.07.26.19.02.42', 'F', 'Config.dll', '555BED3B4F150565C56C48E7624A7AFE3FB40965B63C03211F912BAD0ED48A2D'),
('Windows.Inswave.Prod', '2021.07.26.19.02.42', 'F', 'DistributionInfo.dll', '6FDAAC115DE6DE3A0A77CB08936A3A27BB459D2EE6B048AE4F509187B220B555'),
('Windows.Inswave.Prod', '2021.07.26.19.02.42', 'F', 'HandlerMain.dll', '5E21716C94334B728D07AD52FEA7EBA55CAF605B3E1ABFCB7B8E31C925C42F03'),
('Windows.Inswave.Prod', '2021.07.26.19.02.42', 'F', 'IntegrityManager.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.26.19.02.42', 'F', 'test.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.26.19.03.16', 'F', 'CommonUtil.dll', '3AE33F95B9A8217CED844BDBDDE68E90EA3D9380D5B2096CFA5D9F4A82D3681E'),
('Windows.Inswave.Prod', '2021.07.26.19.03.16', 'F', 'Config.dll', '555BED3B4F150565C56C48E7624A7AFE3FB40965B63C03211F912BAD0ED48A2D'),
('Windows.Inswave.Prod', '2021.07.26.19.03.16', 'F', 'DistributionInfo.dll', '6FDAAC115DE6DE3A0A77CB08936A3A27BB459D2EE6B048AE4F509187B220B555'),
('Windows.Inswave.Prod', '2021.07.26.19.03.16', 'F', 'HandlerMain.dll', '5E21716C94334B728D07AD52FEA7EBA55CAF605B3E1ABFCB7B8E31C925C42F03'),
('Windows.Inswave.Prod', '2021.07.26.19.03.16', 'F', 'IntegrityManager.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.26.19.03.16', 'F', 'test.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.26.19.05.56', 'F', 'CommonUtil.dll', '3AE33F95B9A8217CED844BDBDDE68E90EA3D9380D5B2096CFA5D9F4A82D3681E'),
('Windows.Inswave.Prod', '2021.07.26.19.05.56', 'F', 'Config.dll', '555BED3B4F150565C56C48E7624A7AFE3FB40965B63C03211F912BAD0ED48A2D'),
('Windows.Inswave.Prod', '2021.07.26.19.05.56', 'F', 'DistributionInfo.dll', '6FDAAC115DE6DE3A0A77CB08936A3A27BB459D2EE6B048AE4F509187B220B555'),
('Windows.Inswave.Prod', '2021.07.26.19.05.56', 'F', 'HandlerMain.dll', '5E21716C94334B728D07AD52FEA7EBA55CAF605B3E1ABFCB7B8E31C925C42F03'),
('Windows.Inswave.Prod', '2021.07.26.19.05.56', 'F', 'IntegrityManager.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C'),
('Windows.Inswave.Prod', '2021.07.26.19.05.56', 'F', 'test.dll', '4E76BD177A66605230E97B8BBD50789225FEC84A6DCD8C9C0F94257B83EF6D4C');


-- 테이블 wedgemanager.em_dists_files_info 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_dists_files_info` (
    `APPID` varchar(50)  NOT NULL,
    `PATH` varchar(500) NOT NULL,
    `DESTPATH` varchar(500) DEFAULT NULL,
    `TYPE` varchar(15) DEFAULT NULL,
    `ORDEROFEXECUTION` int(11) DEFAULT NULL,
    PRIMARY KEY (`APPID`, `PATH`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_dists_files_info:~6 rows (대략적) 내보내기
DELETE FROM `em_dists_files_info`;

INSERT INTO `em_dists_files_info` (`PATH`, `DESTPATH`, `TYPE`, `ORDEROFEXECUTION`) VALUES
('CommonUtil.dll', 'C:\\SHBHT\\ATOP\\Application\\CommonUtil.dll', 'copy', 0),
('Config.dll', 'C:\\SHBHT\\ATOP\\Application\\Config.dll', 'copy', 0),
('DistributionInfo.dll', 'C:\\SHBHT\\ATOP\\Application\\DistributionInfo.dll', 'copy', 0),
('HandlerMain.dll', 'C:\\SHBHT\\ATOP\\Application\\HandlerMain.dll', 'copy', 0),
('IntegrityManager.dll', 'C:\\SHBHT\\ATOP\\Application\\IntegrityManager.dll', 'copy', 0),
('test.dll', 'C:\\Inswave\\Prod\\W-EdgeManager\\Update\\test.dll', 'copy', 0);

-- 테이블 wedgemanager.em_gatherthresthold 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_gatherthresthold` (
    `ID` int(11) NOT NULL,
    `CPUUASGE` varchar(45) DEFAULT NULL,
    `DISKUSAGE` varchar(45) DEFAULT NULL,
    `memoryUsage` varchar(45) DEFAULT NULL,
    `INTERVALTYPE` varchar(45) DEFAULT NULL,
    `_INTERVAL` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_gatherthresthold:~0 rows (대략적) 내보내기

-- 테이블 wedgemanager.em_group 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_group` (
    `ID` int(11) NOT NULL,
    `USERID` varchar(30) NOT NULL,
    `DEPARTCODE` varchar(30) NOT NULL,
    `POLICYGROUPID` int(11) NOT NULL,
    `TYPE` varchar(45) DEFAULT NULL,
    `TYPEVALUE` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`ID`,`USERID`,`DEPARTCODE`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_group:~0 rows (대략적) 내보내기

-- 테이블 wedgemanager.em_integrity 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_integrity` (
    `ID` int(11) NOT NULL,
    `INTEGRITYPOLICYID` int(11) NOT NULL,
    `URL` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_integrity:~0 rows (대략적) 내보내기

-- 테이블 wedgemanager.em_integrity_policy 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_integrity_policy` (
    `ID` int(11) NOT NULL,
    `ISTARGETHOME` varchar(45) DEFAULT NULL,
    `ISALWAYS` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_integrity_policy:~0 rows (대략적) 내보내기

-- 테이블 wedgemanager.em_integrity_role 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_integrity_role` (
    `_INDEX` int(11) NOT NULL,
    `INTEGRITYPOLICYID` int(11) NOT NULL,
    `TYPE` varchar(45) DEFAULT NULL,
    `PARAMETER` varchar(45) DEFAULT NULL,
    `SCRIPT` varchar(45) DEFAULT NULL,
    `WAIT` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`_INDEX`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_integrity_role:~0 rows (대략적) 내보내기

-- 테이블 wedgemanager.em_organizations 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_organizations` (
    `ID` int(10) NOT NULL,
    `ORG` longtext DEFAULT NULL,
    `CREATEDAT` timestamp(6) NULL DEFAULT NULL,
    `UPDATEDAT` timestamp(6) NULL DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_organizations:~1 rows (대략적) 내보내기
DELETE FROM `em_organizations`;

INSERT INTO `em_organizations` (`ID`, `ORG`, `CREATEDAT`, `UPDATEDAT`) VALUES
(0, '{"dlt_orgList":[{"ORGCD":"000000","LEVEL":1,"ORGNM":"국가","UPPERCD":"root"},{"ORGCD":"0001","LEVEL":2,"ORGNM":"국가기관","UPPERCD":"000000"},{"ORGCD":"0019","LEVEL":2,"ORGNM":"공공기관","UPPERCD":"000000"}]}', '2021-06-06 20:39:38.000000', '2021-06-21 07:44:24.262000');

-- 테이블 wedgemanager.em_policy_group 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_policy_group` (
    `ID` int(11) NOT NULL,
    `COLLECTIONID` int(11) NOT NULL,
    `INTEGRITYID` int(11) NOT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_policy_group:~0 rows (대략적) 내보내기

-- 테이블 wedgemanager.em_sendoption 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_sendoption` (
    `ID` int(11) NOT NULL,
    `sentToCollector` varchar(45) DEFAULT NULL,
    `INTERVALTYPE` varchar(45) DEFAULT NULL,
    `_INTERVAL` varchar(45) DEFAULT NULL,
    `MAXROW` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_sendoption:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_users 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_users`
(
    `ID`                   varchar(30)  NOT NULL,
    `NAME`                 varchar(50)  NOT NULL,
    `DEPARTCODE`           varchar(30)  NOT NULL,
    `DUTY`                 varchar(30)       DEFAULT NULL,
    `EMAIL`                varchar(128)      DEFAULT NULL,
    `PHONE_NO`             varchar(64)       DEFAULT NULL,
    `PWD`                  varchar(256)      DEFAULT NULL,
    `POSITION`             varchar(30)       DEFAULT NULL,
    `REGDATE`              timestamp(6) NULL DEFAULT NULL,
    `APPLYDATE`            timestamp(6) NULL DEFAULT NULL,
    `VALID`                varchar(1)        DEFAULT NULL,
    `CREATEDATE`           timestamp(6) NULL DEFAULT NULL,
    `UPDATEDATE`           timestamp(6) NULL DEFAULT NULL,
    `LAST_LOGIN_DATE`      timestamp(6) NULL DEFAULT NULL,
    `LAST_LOGIN_PING_DATE` timestamp(6) NULL DEFAULT NULL,
    `LAST_LOGOUT_DATE`     timestamp(6) NULL DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- 테이블 데이터 wedgemanager.em_users:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.em_users_test 구조 내보내기
CREATE TABLE IF NOT EXISTS `em_users_test` (
    `ID` varchar(30) NOT NULL,
    `NAME` varchar(45) DEFAULT NULL,
    `DUTY` varchar(45) DEFAULT NULL,
    `POSITION` varchar(45) DEFAULT NULL,
    `REGDATE` varchar(45) DEFAULT NULL,
    `APPLYDATE` varchar(45) DEFAULT NULL,
    `VALID` varchar(45) DEFAULT NULL,
    `CREATEDATE` datetime DEFAULT NULL,
    `UPDATEDATE` datetime DEFAULT NULL,
    `DEPARTCODE` varchar(30) DEFAULT NULL,
    PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.em_users_test:~0 rows (대략적) 내보내기


-- 테이블 wedgemanager.language 구조 내보내기
CREATE TABLE IF NOT EXISTS `language` (
    `LANGUAGE_ID` bigint(20) NOT NULL COMMENT '설정 ID',
    `COUNTRY_ID` bigint(20) NOT NULL COMMENT '국가 ID',
    `TYPE` varchar(255) DEFAULT NULL COMMENT '설정 값1',
    `KEY_` varchar(255) DEFAULT NULL COMMENT '설정 값2',
    `VALUE` varchar(255) DEFAULT NULL COMMENT '설정 값2',
    `UPDATE_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    PRIMARY KEY (`LANGUAGE_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 테이블 데이터 wedgemanager.language:~24 rows (대략적) 내보내기
DELETE FROM `language`;

INSERT INTO `language` (`LANGUAGE_ID`, `COUNTRY_ID`, `TYPE`, `KEY_`, `VALUE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES
(1, 1, 'message', 'productName', 'W-EdgeManager', '2021-05-17 14:35:55', '2021-05-17 14:35:56'),
(2, 2, 'message', 'productName', 'W-EdgeManager', '2021-05-17 14:35:55', '2021-05-17 14:35:56'),
(3, 3, 'message', 'productName', 'W-EdgeManager', '2021-05-17 14:35:55', '2021-05-17 14:35:56'),
(4, 1, 'message', 'errorHeader', 'header 오류입니다.', '2021-05-17 15:39:14', '2021-05-17 15:39:15'),
(5, 2, 'message', 'errorHeader', 'header error.', '2021-05-17 15:44:38', '2021-05-17 15:44:40'),
(6, 3, 'message', 'errorHeader', 'headerエラーです。', '2021-05-17 15:44:41', '2021-05-17 15:44:42'),
(7, 1, 'message', 'errorServiceProcess', 'service 처리 에러입니다.', '2021-05-17 16:20:50', '2021-05-17 16:20:51'),
(8, 2, 'message', 'errorServiceProcess', 'Service processing error.', '2021-05-17 16:20:50', '2021-05-17 16:20:51'),
(9, 3, 'message', 'errorServiceProcess', 'service処理エラーです。', '2021-05-17 16:20:50', '2021-05-17 16:20:51'),
(10, 1, 'message', 'errorServiceNotallowd', '해당 서비스는 사용할 수 없습니다.', '2021-05-17 16:34:26', '2021-05-17 16:34:26'),
(11, 2, 'message', 'errorServiceNotallowd', 'This service is not available.', '2021-05-17 16:36:39', '2021-05-17 16:36:41'),
(12, 3, 'message', 'errorServiceNotallowd', 'このサービスは、利用できません。', '2021-05-17 16:36:40', '2021-05-17 16:36:41'),
(13, 1, 'message', 'errorIdNotFound', 'ID를 찾을 수 없습니다.', '2021-05-17 16:44:50', '2021-05-17 16:44:51'),
(14, 2, 'message', 'errorIdNotFound', 'The ID could not be found.', '2021-05-17 16:44:50', '2021-05-17 16:44:51'),
(15, 3, 'message', 'errorIdNotFound', 'IDを見つけることができません。', '2021-05-17 16:44:50', '2021-05-17 16:44:51'),
(16, 1, 'message', 'errorDataNotFound', '데이터를 찾을 수 없습니다.', '2021-05-17 16:54:55', '2021-05-17 16:54:58'),
(17, 2, 'message', 'errorDataNotFound', 'No data was found.\r\n', '2021-05-17 16:54:58', '2021-05-17 16:54:57'),
(18, 3, 'message', 'errorDataNotFound', 'データを見つけることができません。', '2021-05-17 16:54:59', '2021-05-17 16:55:00'),
(19, 1, 'message', 'errorDomainFormat', 'json데이터와 domain 포멧이 일치하지 않습니다.', '2021-05-17 17:06:36', '2021-05-17 17:06:44'),
(20, 2, 'message', 'errorDomainFormat', 'The json data and domain format do not match.', '2021-05-17 17:06:41', '2021-05-17 17:06:44'),
(21, 3, 'message', 'errorDomainFormat', 'jsonデータとdomainフォーマットが一致しません。', '2021-05-17 17:06:43', '2021-05-17 17:06:45'),
(22, 1, 'message', 'errorRdbQuery', '쿼리중 에러가 발생했습니다.', '2021-05-17 17:14:44', '2021-05-17 17:14:46'),
(23, 2, 'message', 'errorRdbQuery', 'An error occurred during the query.\r\n', '2021-05-17 17:14:50', '2021-05-17 17:14:47'),
(24, 3, 'message', 'errorRdbQuery', 'クエリ中にエラーが発生しました。', '2021-05-17 17:14:50', '2021-05-17 17:14:48');
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('language', 24);

-- 테이블 wedgemanager.license 구조 내보내기
CREATE TABLE IF NOT EXISTS `license` (
    `LICENSE_ID` int(11) NOT NULL COMMENT '라이선스 ID',
    `NAME` varchar(45) NOT NULL COMMENT '라이선스 명',
    `DESCRIPTION` varchar(50) DEFAULT NULL COMMENT '라이선스 설명',
    PRIMARY KEY (`LICENSE_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.license:~5 rows (대략적) 내보내기
DELETE FROM `license`;

INSERT INTO `license` (`LICENSE_ID`, `NAME`, `DESCRIPTION`) VALUES
(1, 'LITE', '라이트'),
(2, 'BASIC', '기본'),
(3, 'STANDARD', '표준'),
(4, 'PREMIUM', '프리미엄'),
(5, 'ENTERPRISE', '엔터프라이즈 (모든 메뉴 활성화)');
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('license', 5);

-- 테이블 wedgemanager.role 구조 내보내기
CREATE TABLE IF NOT EXISTS `role` (
    `ROLE_ID` int(11) NOT NULL ,
    `NAME` varchar(45) NOT NULL,
    `DESCRIPTION` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`ROLE_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 테이블 데이터 wedgemanager.role:~3 rows (대략적) 내보내기
DELETE FROM `role`;

INSERT INTO `role` (`ROLE_ID`, `NAME`, `DESCRIPTION`) VALUES
(1, 'ADMIN', '시스템 관리자 (모든 권한)'),
(2, 'MANAGEMENT', '관리자 (관리 제외 모든 권한.)'),
(3, 'USER', '조회 관리자 (조회 권한)');
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('role', 3);

CREATE TABLE IF NOT EXISTS `menu` (
    `MENU_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '메뉴 ID',
    `ACTIVE` BIT(1) NULL DEFAULT NULL COMMENT '활성화 여부',
    `PARENT_MENU_ID` bigint(20) DEFAULT NULL COMMENT '부모 메뉴 ID',
    `LEVEL` int(11) DEFAULT NULL COMMENT '레벨',
    `NAME` varchar(20) NOT NULL COMMENT '이름',
    `PATH` VARCHAR(100) NULL COMMENT '경로',
    `DESCRIPTION` varchar(20) NOT NULL COMMENT '설명',
    `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    `ICON_IMAGE` VARCHAR(200) DEFAULT NULL COMMENT 'icon Image',
    PRIMARY KEY (`MENU_ID`) USING BTREE,
    KEY `INDEX_OF_MENU_ID` (`MENU_ID`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (1, true, null, 1, '실시간현황', null, '실시간현황', null, '/images/contents/ico_mn01.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (2, true, 1, 2, '단말 모니터링', '/realtime/rt_device_monitoring.xml', '단말 모니터링', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (3, true, 1, 2, '단말 통계', '/realtime/rt_device_statistics.xml', '단말 통계', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (4, true, 1, 2, '시스템 현황', '/realtime/rt_server_current.xml', '시스템 현황', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (5, true, null, 1, '배포', null, '배포', null, '/images/contents/ico_mn02.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (6, true, 5, 2, '에이전트', null, '에이전트', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (7, true, 6, 3, '현황', '/distribute/agent/current.xml', '현황', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (8, true, 6, 3, '등록', '/distribute/agent/register.xml', '등록', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (9, true, 6, 3, '대용량', '/deployer/deployer.xml', '대용량', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (10, true, 5, 2, '단말', null, '단말', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (11, true, 10, 3, '배포', '/distribute/terminal/distribution_mgmt.xml', '배포', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (12, true, 10, 3, '내역', '/distribute/terminal/distribution_history.xml', '내역', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (13, true, 10, 3, '설정', '/distribute/terminal/distribution_config.xml', '설정', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (14, true, null, 1, '단말지원', null, '단말지원', null, '/images/contents/ico_mn02.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (15, true, 14, 2, '공지사항', '/terminal_support/notice_management.xml', '공지사항', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (16, true, 14, 2, '메뉴관리', '/terminal_support/menu_management.xml', '메뉴관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (17, true, 14, 2, '코드관리', '/terminal_support/code_management.xml', '코드관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (18, true, 14, 2, '화면관리', '/terminal_support/screen_management.xml', '화면관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (19, true, 14, 2, '이미지관리', '/terminal_support/img_management.xml', '이미지관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (20, true, 14, 2, '도움말관리', '/terminal_support/reply_management.xml', '도움말관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (21, true, 14, 2, '전문공유내역', '/terminal_support/stmt_management.xml', '전문공유내역', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (22, true, 14, 2, '메신저관리', '/단말지원/메신저관리', '메신저관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (23, true, 14, 2, '사용자관리', '/terminal_support/user_management.xml', '사용자관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (24, true, 14, 2, '부서관리', '/terminal_support/dept_management.xml', '부서관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (25, true, 14, 2, '권한관리', '/terminal_support/auth_management.xml', '권한관리', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (26, true, null, 1, '알림', null, '알림', null, '/images/contents/ico_mn05.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (27, true, 26, 2, '공지', '/notify/notice.xml', '공지', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (28, true, 26, 2, '시스템', '/notify/nt_server.xml', '시스템', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (29, true, 26, 2, '단말', '/notify/resource.xml', '단말', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (30, true, null, 1, '조회', null, '조회', null, '/images/contents/ico_mn03.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (31, true, 30, 2, '자원사용량', '/inquiry/client_resource.xml', '자원사용량', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (32, true, 30, 2, '프로세스 자원 사용량', '/inquiry/client_processResource.xml', '프로세스 자원 사용량', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (33, true, 30, 2, 'MBR 정보', '/inquiry/client_mbr.xml', 'MBR 정보', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (34, true, 30, 2, '디스크 조각 분석 정보', '/inquiry/client_defrag.xml', '디스크 조각 분석 정보', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (35, true, 30, 2, '프로그램', '/inquiry/client_programList.xml', '프로그램', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (36, true, 30, 2, '윈도우업데이트 목록', '/inquiry/client_windowUpdateListResource.xml', '윈도우업데이트 목록', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (37, true, 30, 2, 'H/W정보', '/inquiry/client_hwInfo.xml', 'H/W정보', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (38, true, 30, 2, 'H/W에러', '/inquiry/client_hwError.xml', 'H/W에러', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (39, true, 30, 2, 'Device에러', '/inquiry/client_deviceError.xml', 'Device에러', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (40, true, 30, 2, '블루스크린', '/inquiry/client_blueScreen.xml', '블루스크린', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (41, true, 30, 2, '어플리케이션에러', '/inquiry/client_applicationError.xml', '어플리케이션에러', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (42, true, 30, 2, 'PC ON/Off 기록', '/inquiry/client_pconoff.xml', 'PC ON/Off 기록', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (43, true, 30, 2, '윈도우이벤트로그', '/inquiry/client_windowEvent.xml', '윈도우이벤트로그', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (44, true, 30, 2, '정합성', '/inquiry/client_integrity.xml', '정합성', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (45, true, 30, 2, '시스템리소스사용량', '/inquiry/client_serverResource.xml', '시스템리소스사용량', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (46, true, 30, 2, 'Listen Port 목록', '/inquiry/client_listenPortList.xml', 'Listen Port 목록', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (47, true, 30, 2, '사용자정의', '/inquiry/client_userTermMonitorLog.xml', '사용자정의', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (48, true, 30, 2, '알림', '/inquiry/ruleAlertLog.xml', '알림', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (49, true, null, 1, '분석', null, '분석', null, '/images/contents/ico_mn03.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (50, true, 49, 2, 'PC모델별 자원 사용률', '/statistics/device_model.xml', 'PC모델별 자원 사용률', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (51, true, null, 1, '설정', null, '설정', null, '/images/contents/ico_mn04.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (52, true, 51, 2, '계정', '/setting/account.xml', '계정', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (53, true, 51, 2, '권한', '/setting/power.xml', '권한', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (54, true, 51, 2, '언어', '/setting/lang.xml', '언어', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (55, true, 51, 2, '메뉴설정', '/setting/menuManage.xml', '메뉴설정', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (56, true, null, 1, '정책', null, '정책', null, '/images/contents/ico_mn05.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (57, true, 56, 2, '수집', '/policy/gather.xml', '수집', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (58, true, 56, 2, '정합성', '/policy/suitability.xml', '정합성', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (59, true, null, 1, '그룹', null, '그룹', null, '/images/contents/ico_mn05.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (60, true, 59, 2, '등록', '/band/reg.xml', '등록', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (61, true, 59, 2, '조회', null, '조회', null, null);
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (62, true, null, 1, '형상', null, '형상', null, '/images/contents/ico_mn05.png');
INSERT INTO menu (MENU_ID, ACTIVE, PARENT_MENU_ID, LEVEL, NAME, PATH, DESCRIPTION, REGISTRATION_DATE, ICON_IMAGE) VALUES (63, true, 62, 2, 'Repository', '/scm/repository.xml', 'Repository', null, null);

CREATE TABLE `menu_role` (
                             `MENU_ROLE_ID` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '메뉴 권한 ID',
                             `MENU_ID` BIGINT(20) NULL COMMENT '메뉴 ID',
                             `ROLE_ID` BIGINT(20) NULL COMMENT '권한 ID',
                             `ACTIVE` BIT(1) NULL DEFAULT NULL COMMENT '활성화 여부',
                             `UPDATE_DATE` DATETIME NULL DEFAULT NULL COMMENT '수정일시',
                             `REGISTRATION_DATE` DATETIME NULL DEFAULT NULL COMMENT '등록일시',
                             PRIMARY KEY (`MENU_ROLE_ID`) USING BTREE,
                             INDEX `INDEX_OF_MENU_ROLE_ID` (`MENU_ROLE_ID`) USING BTREE
)
    COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1;

INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,1 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,2 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,3 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,4 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,5 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,6 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,7 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,8 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,9 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,10 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,11 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,12 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,13 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,14 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,15 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,16 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,17 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,18 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,19 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,20 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,21 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,22 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,23 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,24 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,25 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,26 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,27 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,28 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,29 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,30 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,31 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,32 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,33 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,34 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,35 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,36 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,37 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,38 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,39 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,40 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,41 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,42 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,43 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,44 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,45 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,46 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,47 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,48 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,49 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,50 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,51 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,52 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,53 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,54 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,55 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,56 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,57 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (1,58 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,1 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,2 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,3 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,4 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,5 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,6 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,7 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,8 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,9 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,10 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,11 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,12 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,13 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,14 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,15 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,16 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,17 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,18 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,19 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,20 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,21 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,22 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,23 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,24 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,25 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,26 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,27 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,28 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,29 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,30 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,31 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,32 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,33 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,34 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,35 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,36 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,37 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,38 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,39 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,40 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,41 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,42 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,43 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,44 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,45 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,46 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,47 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,48 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,49 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,50 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,51 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,52 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,53 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,54 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,55 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,56 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,57 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (2,58 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,1 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,2 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,3 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,4 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,5 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,6 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,7 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,8 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,9 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,10 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,11 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,12 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,13 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,14 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,15 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,16 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,17 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,18 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,19 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,20 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,21 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,22 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,23 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,24 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,25 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,26 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,27 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,28 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,29 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,30 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,31 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,32 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,33 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,34 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,35 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,36 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,37 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,38 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,39 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,40 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,41 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,42 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,43 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,44 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,45 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,46 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,47 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,48 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,49 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,50 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,51 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,52 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,53 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,54 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,55 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,56 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,57 , b'1', NULL, NULL);
INSERT INTO `menu_role` (`ROLE_ID`, `MENU_ID`, `ACTIVE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (3,58 , b'1', NULL, NULL);

-- 테이블 wedgemanager.site 구조 내보내기
CREATE TABLE IF NOT EXISTS `site` (
    `SITE_ID` bigint(20) NOT NULL COMMENT '회사 ID',
    `NAME` varchar(255) DEFAULT NULL COMMENT '고객사명',
    `ADDRESS1` varchar(255) DEFAULT NULL COMMENT '주소1',
    `ADDRESS2` varchar(255) DEFAULT NULL COMMENT '주소1',
    `FAX` varchar(255) DEFAULT NULL COMMENT '팩스',
    `TEL` varchar(255) DEFAULT NULL COMMENT '전화번호',
    `SIGN_KEY` varchar(255) DEFAULT NULL COMMENT '인증키',
    `LICENSE_ID` bigint(20) DEFAULT NULL COMMENT '라이선스 ID',
    `COUNT` int(11) DEFAULT NULL COMMENT '수량',
    `DELETE_DATE` datetime DEFAULT NULL COMMENT '삭제일시',
    `DUE_DATE` datetime DEFAULT NULL COMMENT '기한일시',
    `UPDATE_DATE` datetime DEFAULT NULL COMMENT '수정일시',
    `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    PRIMARY KEY (`SITE_ID`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 테이블 데이터 wedgemanager.site:~2 rows (대략적) 내보내기
DELETE FROM `site`;

INSERT INTO `site` (`SITE_ID`, `NAME`, `ADDRESS1`, `ADDRESS2`, `FAX`, `TEL`, `SIGN_KEY`, `LICENSE_ID`, `COUNT`, `DELETE_DATE`, `DUE_DATE`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES
(1, '인스웨이브', '서울시 구로구', '대륭2차', NULL, NULL, NULL, 5, 999999, '9999-12-31 23:59:59', '9999-12-31 23:59:59', '2021-04-23 14:21:26', '2020-12-14 15:06:33'),
(2, '신한은행 NEXT', '서울시 중구', '부영태평빌딩', NULL, NULL, NULL, 5, 999999, '9999-12-31 23:59:59', '9999-12-31 23:59:59', '2021-06-05 17:09:52', '2021-06-05 17:08:49');
INSERT INTO em_seq_generator (SEQUENCE_NAME, NEXT_VAL) VALUES ('site', 2);

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;


CREATE TABLE `rule` (
                        `RULE_ID` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '룰 ID',
                        `ACTIVE` BIT(1) NULL DEFAULT NULL COMMENT '활성화 여부',
                        `IS_SYSTEM` BIT(1) NULL DEFAULT NULL COMMENT '시스템 여부',
                        `NAME` VARCHAR(1024) NOT NULL COMMENT '이름',
                        `DESCRIPTION` VARCHAR(1024) NULL DEFAULT NULL COMMENT '설명',
                        `RULE_LEVEL_ID` BIGINT(20) NULL COMMENT '룰 레벨ID',
                        `RULE_RECEIVER_ID1` BIGINT(20) NULL COMMENT '룰 수신ID 1',
                        `RULE_RECEIVER_ID2` BIGINT(20) NULL COMMENT '룰 수신ID 2',
                        `RULE_RECEIVER_ID3` BIGINT(20) NULL COMMENT '룰 수신ID 3',
                        `PATTERN` VARCHAR(2048) NULL DEFAULT NULL COMMENT '패턴',
                        `CLASS_NAMES` VARCHAR(1024) NULL DEFAULT NULL COMMENT '클래스명',
                        `DISPLAY_CLASS_NAMES` VARCHAR(1024) NULL DEFAULT NULL COMMENT '디스플레이클래스명',
                        `FIELD_NAMES` VARCHAR(1024) NULL DEFAULT NULL COMMENT '필드명',
                        `DISPLAY_FIELD_NAMES` VARCHAR(1024) NULL DEFAULT NULL COMMENT '디스플레이필드명',
                        `UPDATE_DATE` DATETIME NULL DEFAULT NULL COMMENT '수정일시',
                        `REGISTRATION_DATE` DATETIME NULL DEFAULT NULL COMMENT '등록일시',
                        PRIMARY KEY (`RULE_ID`) USING BTREE,
                        INDEX `INDEX_OF_RULE_ID` (`RULE_ID`) USING BTREE
)
    COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1;

INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'1', '시스템 CPU 알림', '시스템 CPU 사용률을 체크하여 90% 이상 시 알림니다.', 2, NULL, NULL, NULL, 'cpuUsage>90', 'ServerResourceLog', '시스템 자원', 'cpuUsage', 'CPU 사용률', '2021-10-06 15:05:21', '2021-10-06 15:05:19');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'1', '시스템 MEMORY 알림', '시스템 MEMORY 사용률을 체크하여 90% 이상 시 알림니다.', 2, NULL, NULL, NULL, 'memoryUsage>90', 'ServerResourceLog', '시스템 자원', 'memoryUsage', 'MEMORY 사용률', '2021-10-14 09:24:47', '2021-10-14 09:24:49');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'1', '시스템 DISK 알림', '시스템 DISK 사용률을 체크하여 90% 이상 시 알림니다.', 2, NULL, NULL, NULL, 'diskUsage>90', 'ServerResourceLog', '시스템 자원', 'diskUsage', 'DISK 사용률', '2021-10-14 09:26:37', '2021-10-14 09:26:38');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'1', '시스템 CPU MEMORY DISK 알림', '시스템 CPU MEMORY DISK 사용률을 체크하여 알림니다.', 2, NULL, NULL, NULL, 'cpuUsage>90 && memoryUsage>90 && diskUsage>90', 'ServerResourceLog', '시스템 자원', 'cpuUsage,memoryUsage,diskUsage', 'CPU 사용률, MEMORY 사용률, DISK 사용률', '2021-10-14 09:26:37', '2021-10-14 09:26:38');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'0', '단말 CPU 알림', '단말 CPU 사용률을 체크하여 90% 이상 시 알림니다.', 2, NULL, NULL, NULL, 'cpuUsage>90', 'ClientPerformanceResourceLog', '단말 자원', 'cpuUsage', 'CPU 사용률', '2021-10-06 15:05:21', '2021-10-06 15:05:19');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'0', '단말 MEMORY 알림', '단말 MEMORY 사용률을 체크하여 90% 이상 시 알림니다.', 2, NULL, NULL, NULL, 'memoryUsage>90', 'ClientPerformanceResourceLog', '단말 자원', 'memoryUsage', 'MEMORY 사용률', '2021-10-14 09:24:47', '2021-10-14 09:24:49');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'0', '단말 DISK 알림', '단말 DISK 사용률을 체크하여 90% 이상 시 알림니다.', 2, NULL, NULL, NULL, 'diskUsage>90', 'ClientPerformanceResourceLog', '단말 자원', 'diskUsage', 'DISK 사용률', '2021-10-14 09:26:37', '2021-10-14 09:26:38');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'0', '단말 CPU MEMORY DISK 알림', '단말 CPU MEMORY DISK 사용률을 체크하여 알림니다.', 2, NULL, NULL, NULL, 'cpuUsage>90 && memoryUsage>90 && diskUsage>90', 'ServerResourceLog', '단말 자원', 'cpuUsage,memoryUsage,diskUsage', 'CPU 사용률, MEMORY 사용률, DISK 사용률', '2021-10-14 09:26:37', '2021-10-14 09:26:38');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'0', '윈도우 이벤트 알림', '정보성 데이터를 제외하여 알림니다.', 2, NULL, NULL, NULL, ' level!=2 || eventId!=455 || channel!=\'Application\' ', 'WindowsEventSystemErrorAllLog', '단말 자원', 'level,eventId,channel', '레벨,아이디,채널', '2021-10-14 09:26:37', '2021-10-14 09:26:38');
INSERT INTO `rule` (`ACTIVE`, `IS_SYSTEM`, `NAME`, `DESCRIPTION`, `RULE_LEVEL_ID`, `RULE_RECEIVER_ID1`, `RULE_RECEIVER_ID2`, `RULE_RECEIVER_ID3`, `PATTERN`, `CLASS_NAMES`, `DISPLAY_CLASS_NAMES`, `FIELD_NAMES`, `DISPLAY_FIELD_NAMES`, `UPDATE_DATE`, `REGISTRATION_DATE`) VALUES (b'0', b'0', '정합성 결과 알림', '정합성 재실행 실패 시 결과를 알림니다.', 2, NULL, NULL, NULL, ' result!=100 && result!=200 && result!=300 ', 'IntegrityLog', '정합성로그', 'result', '결과', '2021-11-28 17:02:37', '2021-11-28 17:02:38');

CREATE TABLE `rule_receiver` (
                                 `RULE_RECEIVER_ID` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '수신자  ID',
                                 `ACTIVE` BIT(1) NULL DEFAULT NULL COMMENT '활성화 여부',
                                 `NAME` VARCHAR(20) NOT NULL COMMENT '이름',
                                 `UNIQUE_ID` VARCHAR(20) NOT NULL COMMENT '수신자아이디',
                                 `IS_EMAIL` BIT(1) NULL DEFAULT NULL COMMENT '메일사용여부',
                                 `EMAIL` VARCHAR(1024) NULL DEFAULT NULL COMMENT '이메일',
                                 `IS_URL` BIT(1) NULL DEFAULT NULL COMMENT 'URL사용여부',
                                 `SENDER_ID` VARCHAR(20) NOT NULL COMMENT '발신자아이디',
                                 `SENDER_NAME` VARCHAR(20) NOT NULL COMMENT '발신자명',
                                 `REGISTRATION_DATE` DATETIME NULL DEFAULT NULL COMMENT '등록일시',
                                 PRIMARY KEY (`RULE_RECEIVER_ID`) USING BTREE,
                                 INDEX `INDEX_OF_RULE_RECEIVER_ID` (`RULE_RECEIVER_ID`) USING BTREE
)
    COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1;

CREATE TABLE `rule_target` (
                               `RULE_TARGET_ID` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '룰 타겟 ID',
                               `IS_SYSTEM` BIT(1) NULL DEFAULT NULL COMMENT '시스템  여부',
                               `CLASS_NAME` VARCHAR(20) NOT NULL COMMENT '클래스명',
                               `DISPLAY_CLASS_NAME` VARCHAR(20) NULL DEFAULT NULL COMMENT '표시클래스명',
                               `FIELDS` VARCHAR(2048) NULL DEFAULT NULL COMMENT '필드명(,로구분)',
                               `DISPLAY_FIELDS` VARCHAR(2048) NULL DEFAULT NULL COMMENT '표시 필드명(,로구분)',
                               `REGISTRATION_DATE` DATETIME NULL DEFAULT NULL COMMENT '등록일시',
                               PRIMARY KEY (`RULE_TARGET_ID`) USING BTREE,
                               INDEX `INDEX_OF_RULE_TARGET_ID` (`RULE_TARGET_ID`) USING BTREE
)
    COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `rule_level` (
    `RULE_LEVEL_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `NAME` VARCHAR(45) NOT NULL,
    `DESCRIPTION` VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (`RULE_LEVEL_ID`) USING BTREE,
    INDEX `INDEX_OF_RULE_LEVEL_ID` (`RULE_LEVEL_ID`) USING BTREE
    )
    COLLATE='utf8_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=1;

INSERT INTO `rule_level` (`NAME`, `DESCRIPTION`) VALUES
('EMERGENCY', '비상 (발생 즉시 알림)'),
('RISK', '위험 (10분간 누적 후 알림)'),
('HIGH', '높음 (30분간 누적 후 알림'),
('MEDIUM', '중간 (1시간 누적 후 알림)'),
('LOW', '낮음 (1일간 누적 후 알림)');

CREATE TABLE IF NOT EXISTS `rule_target` (
    `RULE_TARGET_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '룰 타겟 ID',
    `IS_SYSTEM` bit(1) DEFAULT NULL COMMENT '시스템 여부',
    `CLASS_NAME` VARCHAR(50) NOT NULL COMMENT '클래스명',
    `DISPLAY_CLASS_NAME` varchar(20) DEFAULT NULL COMMENT '표시클래스명',
    `FIELDS` VARCHAR(50) DEFAULT NULL COMMENT '필드명(,로구분)',
    `DISPLAY_FIELDS` VARCHAR(20) DEFAULT NULL COMMENT '표시 필드명(,로구분)',
    `FIELDS_TYPE` varchar(20) DEFAULT NULL COMMENT '표시클래스명',
    `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '등록일시',
    PRIMARY KEY (`RULE_TARGET_ID`) USING BTREE,
    KEY `INDEX_OF_RULE_TARGET_ID` (`RULE_TARGET_ID`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 테이블 데이터 wedgemanager.rule_target:~3 rows (대략적) 내보내기
DELETE FROM `rule_target`;
/*!40000 ALTER TABLE `rule_target` DISABLE KEYS */;
INSERT INTO `rule_target` (`RULE_TARGET_ID`, `IS_SYSTEM`, `CLASS_NAME`, `DISPLAY_CLASS_NAME`, `FIELDS`, `DISPLAY_FIELDS`, `FIELDS_TYPE` ,`REGISTRATION_DATE`) VALUES
(1, b'1', 'ServerResourceLog', '서버 시스템 자원', 'cpuUsage', 'CPU 사용률', 'Integer', '2021-10-06 15:00:12'),
(2, b'1', 'ServerResourceLog', '서버 시스템 자원', 'memoryUsage', 'MEMORY 사용률', 'Integer', '2021-10-06 15:00:12'),
(3, b'1', 'ServerResourceLog', '서버 시스템 자원', 'diskUsage', 'DISK 사용률', 'Integer', '2021-10-06 15:00:12'),
(4, b'1', 'ClientPerformanceResourceLog', '단말자원', 'cpuUsage', 'CPU 사용률', 'Integer', '2021-11-09 19:58:12'),
(5, b'1', 'ClientPerformanceResourceLog', '단말자원', 'memoryUsage', 'MEMORY 사용률', 'Integer', '2021-11-09 19:58:12'),
(6, b'1', 'ClientPerformanceResourceLog', '단말자원', 'diskUsage', 'DISK 사용률', 'Integer', '2021-11-09 19:58:12'),
(7, b'1', 'ClientProcessResourceLog', '프로세스자원', 'procName', '이름', 'String', '2021-11-09 19:58:12'),
(8, b'1', 'ClientProcessResourceLog', '프로세스자원', 'procCpuUsage', 'CPU 사용률', 'Integer', '2021-11-09 19:58:12'),
(9, b'1', 'ClientProcessResourceLog', '프로세스자원', 'procMemoryUsage', 'MEMORY 사용률', 'Integer', '2021-11-09 19:58:12'),
(10, b'1', 'ClientProcessResourceLog', '프로세스자원', 'procThreadCount', '스레드 카운트', 'Integer', '2021-11-09 19:58:12'),
(11, b'1', 'ClientProcessResourceLog', '프로세스자원', 'procHandleCount', '핸들 카운트', 'Integer', '2021-11-09 19:58:12'),
(12, b'1', 'ClientActivePortListResourceLog', 'Listen Port', 'procName', '프로세스명', 'String', '2021-11-09 19:58:12'),
(13, b'1', 'ClientActivePortListResourceLog', 'Listen Port', 'netState', '넷스테이트', 'String', '2021-11-09 19:58:12'),
(14, b'1', 'ClientActivePortListResourceLog', 'Listen Port', 'protocalType', '프로토콜 타입', 'String', '2021-11-09 19:58:12'),
(15, b'1', 'ClientActivePortListResourceLog', 'Listen Port', 'localAddress', '로컬 주소', 'String', '2021-11-09 19:58:12'),
(16, b'1', 'ClientActivePortListResourceLog', 'Listen Port', 'localPort', '로컬 포트', 'String', '2021-11-09 19:58:12'),
(17, b'1', 'ClientActivePortListResourceLog', 'Listen Port', 'remoteAddress', '원격 주소', 'String', '2021-11-09 19:58:12'),
(18, b'1', 'ClientActivePortListResourceLog', 'Listen Port', 'remotePort', '원격포트', 'String', '2021-11-09 19:58:12'),
(19, b'1', 'ClientDefragAnalysisResourceLog', '디스크조각', 'averageFileSize', '파일의 평균 크기', 'Integer', '2021-11-09 19:58:12'),
(20, b'1', 'ClientDefragAnalysisResourceLog', '디스크조각', 'filePercentFragmentation', '조각화 된 파일의 백분율', 'Integer', '2021-11-09 19:58:12'),
(21, b'1', 'ClientDefragAnalysisResourceLog', '디스크조각', 'freeSpace', '사용 가능 공간', 'Integer', '2021-11-09 19:58:12'),
(22, b'1', 'ClientDefragAnalysisResourceLog', '디스크조각', 'freeSpacePercent', '여유 공간 백분율', 'Integer', '2021-11-09 19:58:12'),
(23, b'1', 'ClientHWInfoResourceLog', 'H/W정보', 'osCaption', '운영체제', 'String', '2021-11-09 19:58:12'),
(24, b'1', 'ClientHWInfoResourceLog', 'H/W정보', 'osStatus', '상태', 'String', '2021-11-09 19:58:12'),
(25, b'1', 'ClientHWInfoResourceLog', 'H/W정보', 'pcModel', '제품 이름', 'String', '2021-11-09 19:58:12'),
(26, b'1', 'ClientMBRResourceLog', 'MBR정보', 'name', '이름', 'String', '2021-11-09 19:58:12'),
(27, b'1', 'ClientMBRResourceLog', 'MBR정보', 'deviceID', 'device ID', 'String', '2021-11-09 19:58:12'),
(28, b'1', 'ClientMBRResourceLog', 'MBR정보', 'sectorMBR', 'MBR sector', 'String', '2021-11-09 19:58:12'),
(29, b'1', 'ClientProgramListResourceLog', '프로그램', 'name', '이름', 'String', '2021-11-09 19:58:12'),
(30, b'1', 'ClientProgramListResourceLog', '프로그램', 'version', '버젼', 'String', '2021-11-09 19:58:12'),
(31, b'1', 'ClientProgramListResourceLog', '프로그램', 'publisher', '제작자', 'String', '2021-11-09 19:58:12'),
(32, b'1', 'ClientProgramListResourceLog', '프로그램', 'size', '크기', 'Integer', '2021-11-09 19:58:12'),
(33, b'1', 'ClientWindowsUpdateListResourceLog', '업데이트', 'title', '제목', 'String', '2021-11-09 19:58:12'),
(34, b'1', 'WindowsEventSystemErrorAllLog', '윈도우이벤트', 'level', '레벨', 'String', '2021-10-06 15:00:12'),
(35, b'1', 'WindowsEventSystemErrorAllLog', '윈도우이벤트', 'eventId', '아이디', 'String', '2021-10-06 15:00:12'),
(36, b'1', 'WindowsEventSystemErrorAllLog', '윈도우이벤트', 'channel', '채널', 'String', '2021-10-06 15:00:12'),
(37, b'1', 'WindowsEventSystemErrorAllLog', '윈도우이벤트', 'activityId', '액티비티아이디', 'String', '2021-10-06 15:00:12'),
(38, b'1', 'PcOnOffEventLog', 'PC On/Off', 'level', '레벨', 'String', '2021-10-06 15:00:12'),
(39, b'1', 'PcOnOffEventLog', 'PC On/Off', 'eventId', '아이디', 'String', '2021-10-06 15:00:12'),
(40, b'1', 'PcOnOffEventLog', 'PC On/Off', 'channel', '채널', 'String', '2021-10-06 15:00:12'),
(41, b'1', 'PcOnOffEventLog', 'PC On/Off', 'activityId', '액티비티아이디', 'String', '2021-10-06 15:00:12'),
(42, b'1', 'ClientUserTermMonitorLog', '사용자정의', 'userEventID', '정의ID', 'String', '2021-10-06 15:00:12'),
(43, b'1', 'ClientUserTermMonitorLog', '사용자정의', 'userEventData', '정의데이터', 'String', '2021-10-06 15:00:12'),
(44, b'1', 'DeviceErrorLog', '디바이스에러', 'level', '레벨', 'String', '2021-10-06 15:00:12'),
(45, b'1', 'DeviceErrorLog', '디바이스에러', 'eventId', '아이디', 'String', '2021-10-06 15:00:12'),
(46, b'1', 'DeviceErrorLog', '디바이스에러', 'channel', '채널', 'String', '2021-10-06 15:00:12'),
(47, b'1', 'DeviceErrorLog', '디바이스에러', 'activityId', '액티비티아이디', 'String', '2021-10-06 15:00:12'),
(48, b'1', 'ApplicationErrorLog', '어플리케이션에러', 'level', '레벨', 'String', '2021-10-06 15:00:12'),
(49, b'1', 'ApplicationErrorLog', '어플리케이션에러', 'eventId', '아이디', 'String', '2021-10-06 15:00:12'),
(50, b'1', 'ApplicationErrorLog', '어플리케이션에러', 'channel', '채널', 'String', '2021-10-06 15:00:12'),
(51, b'1', 'ApplicationErrorLog', '어플리케이션에러', 'activityId', '액티비티아이디', 'String', '2021-10-06 15:00:12'),
(52, b'1', 'HWErrorLog', 'HW에러', 'level', '레벨', 'String', '2021-10-06 15:00:12'),
(53, b'1', 'HWErrorLog', 'HW에러', 'eventId', '아이디', 'String', '2021-10-06 15:00:12'),
(54, b'1', 'HWErrorLog', 'HW에러', 'channel', '채널', 'String', '2021-10-06 15:00:12'),
(55, b'1', 'HWErrorLog', 'HW에러', 'activityId', '액티비티아이디', 'String', '2021-10-06 15:00:12'),
(56, b'1', 'WindowsBlueScreenLog', '블루스크린', 'level', '레벨', 'String', '2021-10-06 15:00:12'),
(57, b'1', 'WindowsBlueScreenLog', '블루스크린', 'eventId', '아이디', 'String', '2021-10-06 15:00:12'),
(58, b'1', 'WindowsBlueScreenLog', '블루스크린', 'channel', '채널', 'String', '2021-10-06 15:00:12'),
(59, b'1', 'WindowsBlueScreenLog', '블루스크린', 'activityId', '액티비티아이디', 'String', '2021-10-06 15:00:12'),
(60, b'1', 'IntegrityLog', '정합성', 'result', '결과', 'Integer', '2021-11-09 19:58:12');

create table if not exists file_attachement
(
    ID bigint not null
        primary key,
    ORG_FILE_NAME varchar(256) not null,
    FILE_TYPE varchar(3) not null comment '001:normal, 002:image',
    FILE_SIZE bigint null,
    SAVE_PATH varchar(512) not null,
    CREATE_DATE timestamp not null,
    CREATE_USER_ID varchar(30) null,
    CREATE_USER_NAME varchar(45) null,
    UPDATE_DATE timestamp null,
    UPDATE_USER_ID varchar(30) null,
    UPDATE_USER_NAME varchar(45) null
)
charset=utf8;

create table if not exists file_attachement_map
(
    ID bigint not null
        primary key,
    SOURCE_TYPE varchar(64) not null,
    SOURCE_CODE varchar(64) not null,
    FILE_ID bigint not null,
    CREATE_DATE timestamp not null,
    CREATE_USER_ID varchar(30) null,
    CREATE_USER_NAME varchar(45) null,
    UPDATE_DATE timestamp null,
    UPDATE_USER_ID varchar(30) null,
    UPDATE_USER_NAME varchar(45) null,
    constraint fk_file_attachement_map_file_attachement1
        foreign key (FILE_ID) references file_attachement (ID)
)
charset=utf8;

create index file_attachement_map_index1
    on file_attachement_map (SOURCE_TYPE, SOURCE_CODE, FILE_ID);

create index fk_file_attachement_map_file_attachement1_idx
    on file_attachement_map (FILE_ID);


CREATE TABLE IF NOT EXISTS common_code
(
    ID               BIGINT(20)    NOT NULL,
    CODE_TYPE        VARCHAR(512)  NOT NULL,
    CODE_CAPTION     VARCHAR(1024) NOT NULL,
    CODE_VALUE       VARCHAR(1024) NOT NULL,
    ORD              INT           NOT NULL,
    USE_YN           BIT(1)        NULL,
    CREATE_DATE      DATETIME      NOT NULL,
    CREATE_USER_ID   VARCHAR(30),
    CREATE_USER_NAME VARCHAR(45),
    UPDATE_DATE      DATETIME,
    UPDATE_USER_ID   VARCHAR(30),
    UPDATE_USER_NAME VARCHAR(45),

    PRIMARY KEY (ID)
)
    ENGINE = INNODB;


CREATE INDEX COMMON_CODE_IDX1 ON common_code (CODE_TYPE, ORD, USE_YN);