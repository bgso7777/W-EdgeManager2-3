package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class RuleAlertLogShinhanbankAtopTerminalError {

    @Field(type = FieldType.Keyword, name = "userId")
    private String userId;

    @Field(type = FieldType.Keyword, name = "interityPolicyGroupName")
    private String interityPolicyGroupName;

    @Field(type = FieldType.Keyword, name = "description")
    private String description;

    @Field(type = FieldType.Integer, name = "integrityPolicyGroupTodayTotalErrorCount")
    private Integer integrityPolicyGroupTodayTotalErrorCount=0;

    @Field(type = FieldType.Keyword, name = "userName")
    private String userName;

    @Field(type = FieldType.Integer, name = "result")
    private Integer result;

    @Field(type = FieldType.Keyword, name = "netClient5")
    private String netClient5; /*  */ /* 단말에서 주는 정보 */

    @Field(type = FieldType.Boolean, name = "isTargetHome")
    private Boolean isTargetHome; // 재택 PC 해당 여부 (입력값과 동일)

    @Field(type = FieldType.Keyword, name = "pcType")
    private List<String> pcType = new ArrayList<String>(); // "HOME":재택  "VPC::VPC "LAUNCHER":런처PC "WORK":업무PC "NORMAL":위 4개 항목에 속하지 않는 모든 TYPE

    @Field(type = FieldType.Keyword, name = "remoteCode")
    private String remoteCode; // 원격 지원 코드

    @Field(type = FieldType.Boolean, name = "faultReg")
    private Boolean faultReg; // 단말 장애 등록 여부

    @Field(type = FieldType.Integer, name = "sendErrCnt")
    private Integer sendErrCnt=-1; // 정책으로 받은 카운트

    @Field(type = FieldType.Integer, name = "failCount")
    private Integer failCount;

    @Field(type = FieldType.Keyword, name = "failIds")
    private List<String> failIds = new ArrayList<String>();

    @Field(type = FieldType.Keyword, name = "failNames")
    private List<String> failNames = new ArrayList<String>();

    @Field(type = FieldType.Keyword, name = "failDescriptions")
    private List<String> failDescriptions = new ArrayList<String>();

    @Field(type = FieldType.Boolean, name = "isAutoFix")
    private Boolean isTodayAlreadySentRuleAlertSendLog=false; /* 사용자별, 정책그룹별 전송여부 */

    @Field(type = FieldType.Date, name = "timeAutoFix", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeTodayAlreadySentRuleAlertSendLog; /* 사용자별, 정책그룹별 전송 일시 */
}