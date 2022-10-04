package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class RuleAlertSendLog extends Document2{

    private String indexName;
    public void setIndexName(@Param("indexName") String indexName) {
        if (indexName.equals("")) {
            indexName = this.getClass().getSimpleName().toLowerCase();
            this.indexName = indexName;
        } else {
            this.indexName = indexName;
        }
    }
    public String getIndexName() {
        return this.indexName;
    }

    /* 수신자, 레벨 */

    @Field(type = FieldType.Keyword, name = "ruleReceiverIdRuleLevelId")
    private String ruleReceiverIdRuleLevelId; /* 룰 발신 로그 아이디 */

    @Field(type = FieldType.Keyword, name = "parentIds")
    private String parentIds = ""; /* 부모 다큐먼트 아이디 */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Field(type = FieldType.Long, name = "ruleReceiverId")
    private Long ruleReceiverId; /* 룰 수신자 아이디 */

    @Field(type = FieldType.Keyword, name = "ruleReceiverName")
    private String ruleReceiverName; /* 룰 수신자명 */

    @Field(type = FieldType.Keyword, name = "ruleReceiverUniqueId")
    private String ruleReceiverUniqueId; /* 룰 수신자 유일키 아이디 */

    @Field(type = FieldType.Boolean, name = "ruleReceiverIsEmail")
    private Boolean ruleReceiverIsEmail; /* 이메일 수신여부 */

    @Field(type = FieldType.Keyword, name = "ruleReceiverEmail")
    private String ruleReceiverEmail;/* 이메일 */

    @Field(type = FieldType.Boolean, name = "ruleReceiverIsUrl")
    private Boolean ruleReceiverIsUrl;/* 메시지 수신여부 */

    @Field(type = FieldType.Keyword, name = "ruleReceiverSenderId")
    private String ruleReceiverSenderId;/* 메시지 발신자 아이디 */

    @Field(type = FieldType.Keyword, name = "ruleReceiverSenderName")
    private String ruleReceiverSenderName;/* 메시지 발신자명 */

    @Field(type = FieldType.Keyword, name = "ruleReceiverSendUrlData")
    private String ruleReceiverUrlData;/* 메시지 발송 url 데이터 */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Field(type = FieldType.Long, name = "ruleLevelId")
    private Long ruleLevelId;/* 룰 레벨 아이디 */

    @Field(type = FieldType.Keyword, name = "ruleLevelName")
    private String ruleLevelName;/* 룰 레벨명 */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Field(type = FieldType.Nested, name = "ruleAlertSendLogRuleData")
    private List<RuleAlertSendLogRuleData> ruleAlertSendLogRuleData = new ArrayList<RuleAlertSendLogRuleData>();/* 룰 레벨명 */

    @Field(type = FieldType.Keyword, name = "sendTitle")
    private String sendTitle;/* 발송 제목 */

    @Field(type = FieldType.Keyword, name = "sendContent")
    private String sendContent;/* 발송 내용 */

    @Field(type = FieldType.Keyword, name = "sendContentSummary")
    private String sendContentSummary;/* 메시지 발송 요약 내용 */

    @Field(type = FieldType.Keyword, name = "responseData")
    private String responseData;

    @Field(type = FieldType.Boolean, name = "isSendException")
    private Boolean isSendException=false;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Field(type = FieldType.Boolean, name = "isEmailSendSucess")
    private Boolean isEmailSendSucess=false; /* 이메일 전송 성공 여부 */

    @Field(type = FieldType.Boolean, name = "isUrlSendSucess")
    private Boolean isUrlSendSucess=false;/* 메시지 전송 성공 여부 */

    @Field(type = FieldType.Date, name = "timeSend", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeSend;/* 발송 일시 */

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;/* 등록 일시 */

    // 신한은행 ATOP 정합성 단말 에러 등록 (그룹명별, 사용자별)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Field(type = FieldType.Keyword, name = "key")
    private String key; /* key */

    @Field(type = FieldType.Boolean, name = "isShinhanbankAtopTerminalError")
    private Boolean isShinhanbankAtopTerminalError=false;/* 신한은행 atop */

    @Field(type = FieldType.Keyword, name = "userId")
    private String userId;

    @Field(type = FieldType.Keyword, name = "integrityPolicyGroupName")
    private String integrityPolicyGroupName;

    @Field(type = FieldType.Keyword, name = "integrityPolicyGroupDescription")
    private String integrityPolicyGroupDescription;

    @Field(type = FieldType.Integer, name = "integrityPolicyGroupTodayTotalErrorCount")
    private Integer integrityPolicyGroupTodayTotalErrorCount=0;

    @Field(type = FieldType.Keyword, name = "netClient5")
    private String netClient5; /*  */ /* 단말에서 주는 정보 */

    @Field(type = FieldType.Keyword, name = "remoteCode")
    private String remoteCode; // 원격 지원 코드

    @Field(type = FieldType.Boolean, name = "faultReg")
    private Boolean faultReg; // 단말 장애 등록 여부

    @Field(type = FieldType.Integer, name = "sendErrCnt")
    private Integer sendErrCnt=-1; // 정책으로 받은 카운트

    @Field(type = FieldType.Keyword, name = "failIds")
    private List<String> failIds = new ArrayList<String>(); // 실패 아이디

    @Field(type = FieldType.Keyword, name = "failNames")
    private List<String> failNames = new ArrayList<String>(); // 실패 아이디명

    @Field(type = FieldType.Integer, name = "failCount")
    private Integer failCount;

    // 신한은행 ATOP 정합성 메시지 전송
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Field(type = FieldType.Boolean, name = "isTargetTerminalErrorSystem")
    private Boolean isTargetTerminalErrorSystem=false; /* 단말에러시스템전송 */

    @Field(type = FieldType.Boolean, name = "isTargetGolewingMessage")
    private Boolean isTargetGolewingMessage=false; /* 골드윙 메시지 */

    @Field(type = FieldType.Keyword, name = "failDescriptions")
    private List<String> failDescriptions = new ArrayList<String>();

    @Field(type = FieldType.Integer, name = "integrityResult")
    private Integer integrityResult;

    @Field(type = FieldType.Boolean, name = "isTargetHome")
    private Boolean isTargetHome; // 재택 PC 해당 여부 (입력값과 동일)

    @Field(type = FieldType.Keyword, name = "pcType")
    private List<String> pcType = new ArrayList<String>(); // "HOME":재택  "VPC::VPC "LAUNCHER":런처PC "WORK":업무PC "NORMAL":위 4개 항목에 속하지 않는 모든 TYPE

    @Field(type = FieldType.Boolean, name = "isAutoFix")
    private Boolean isTodayAlreadySentRuleAlertSendLog=false; /* 사용자별, 정책그룹별 전송여부 */

    @Field(type = FieldType.Date, name = "timeAutoFix", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeTodayAlreadySentRuleAlertSendLog; /* 사용자별, 정책그룹별 전송 일시 */

}