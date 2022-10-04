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
public class RuleAlertLog extends Document2 {

    private String indexName;
    public void setIndexName(@Param("indexName") String indexName) {
        if (indexName.equals("")) {
            indexName = this.getClass().getSimpleName().toLowerCase();
            this.indexName = indexName;
        } else {
            this.indexName = indexName;
        }
        super.setIndexName(this.indexName);
    }
    public String getIndexName() {
        return this.indexName;
    }

    @Field(type = FieldType.Keyword, name = "childIndexName")
    private String childIndexName; /*  */

    @Field(type = FieldType.Long, name = "ruleId")
    private Long ruleId; /* 알림 룰 아이디  */

    @Field(type = FieldType.Keyword, name = "name")
    private String name; /* 알림 룰명 */

    @Field(type = FieldType.Keyword, name = "description")
    private String description; /* 알림 설명 */

    @Field(type = FieldType.Boolean, name = "isSystem")
    private Boolean isSystem; /* 시스템 여부 */

    @Field(type = FieldType.Keyword, name = "classNames")
    private String classNames; /* 대상 클래스 */

    @Field(type = FieldType.Keyword, name = "displayClassNames")
    private String displayClassNames; /* 대상 클래스 표시명 */

    @Field(type = FieldType.Keyword, name = "fieldNames")
    private String fieldNames; /* 필드명 */

    @Field(type = FieldType.Keyword, name = "displayFieldNames")
    private String displayFieldNames; /* 표시 필드명 */

    @Field(type = FieldType.Keyword, name = "pattern")
    private String pattern; /* 룰 패턴 */

    @Field(type = FieldType.Keyword, name = "patternData")
    private String patternData; /* 룰 패턴 데이터 */

    @Field(type = FieldType.Long, name = "ruleLevelId")
    private Long ruleLevelId; /* 룰 레벨 */

    @Field(type = FieldType.Keyword, name = "ruleLevelName")
    private String ruleLevelName; /* 룰 레벨명 */

    @Field(type = FieldType.Keyword, name = "ruleLevelDescription")
    private String ruleLevelDescription;/* 룰 레벨 설명 */

    @Field(type = FieldType.Nested, name = "ruleAlertLogReceiverData")
    private List<RuleAlertLogReceiverData> ruleAlertLogReceiverData = new ArrayList<RuleAlertLogReceiverData>(); /* 룰 알림 수신자 데이터 */

    @Field(type = FieldType.Date, name = "updateDate", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date updateDate;/* 업데이트 일자 */

    @Field(type = FieldType.Date, name = "registrationDate", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date registrationDate; /* 등록일자 */

    @Field(type = FieldType.Nested, name = "associationAnalysisRuleIds")
    private List<Long> associationAnalysisRuleIds = new ArrayList<Long>(); /* 연관 룰 아이디 */

    @Field(type = FieldType.Date, name = "timeSend", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeAssociationAnalysis;/* 연관 룰 발생 시간 */

    @Field(type = FieldType.Nested, name = "currentAssociationAnalysisDocumentIds")
    private List<String> currentAssociationAnalysisDocumentIds = new ArrayList<String>();/* 연관 룰 발생 다큐먼트 아이디 */

    @Field(type = FieldType.Boolean, name = "isCheck")
    private Boolean isCheck=false;/* 발송 체크 여부 */

    @Field(type = FieldType.Date, name = "timeSend", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCheck;/* 발송 체크 일시 */

    @Field(type = FieldType.Nested, name = "ruleAlertSendLogIds")
    private List<String> ruleAlertSendLogIds = new ArrayList<String>();/* 룰 알림 전송 다큐먼트 아이디 */

    // 신한은행ATOP 장애단말예측 관련 데이터 (정합성 재실행 실패 시 원격제어용 데이터로 IntegrityLog 만 해당)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Field(type = FieldType.Boolean, name = "isShinhanbankAtopTerminalError")
    private Boolean isShinhanbankAtopTerminalError=false;/* 신한은행 atop */

    @Field(type = FieldType.Nested, name = "ruleAlertLogShinhanbankAtopTerminalError")
    private List<RuleAlertLogShinhanbankAtopTerminalError> ruleAlertLogShinhanbankAtopTerminalError = new ArrayList<>();

    @Field(type = FieldType.Integer, name = "failCount")
    private Integer failCount=0; // 전체 실패 수

    @Field(type = FieldType.Boolean, name = "isSendTerminalErrorSystem")
    private Boolean isSendTerminalErrorSystem=false;/* 장애단말발송 여부 */

    @Field(type = FieldType.Boolean, name = "isSendMessage")
    private Boolean isSendMessage=false;/* 관리자 메시지 전송 */

    @Field(type = FieldType.Date, name = "timeSendTerminalErrorSystem", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeSendTerminalErrorSystem;/* 장애단말예측 발송 일시 */

    @Field(type = FieldType.Boolean, name = "isFollowUp")
    private Boolean isFollowUp=false; /* 수동 조치 여부 */

    @Field(type = FieldType.Date, name = "timeFollowUp", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeFollowUp; /* 수동 조치 일시 */

    @Field(type = FieldType.Boolean, name = "isAutoFix")
    private Boolean isAutoFix=false; /* 자동 조치 */

    @Field(type = FieldType.Date, name = "timeAutoFix", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeAutoFix; /* 자동 조치 일시 */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // daily 등록 시간
    @Field(type = FieldType.Date, name = "timeRegisteredDaily", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeRegisteredDaily;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}