package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter
@Setter
public class RuleAlertSendLogRuleData {

    @Field(type = FieldType.Long, name = "ruleId")
    private Long ruleId; /* 룰 아이디 */

    @Field(type = FieldType.Keyword, name = "name")
    private String name; /* 룰명 */

    @Field(type = FieldType.Keyword, name = "description")
    private String description; /* 룰 설명 */

    @Field(type = FieldType.Keyword, name = "classNames")
    private String classNames;/* 클래스명 */

    @Field(type = FieldType.Keyword, name = "displayClassNames")
    private String displayClassNames;/* 클래스 표시명 */

    @Field(type = FieldType.Keyword, name = "fieldNames")
    private String fieldNames;/* 필드명 */

    @Field(type = FieldType.Keyword, name = "displayFieldNames")
    private String displayFieldNames;/* 필드 표시명 */

    @Field(type = FieldType.Keyword, name = "pattern")
    private String pattern; /* 패턴 */

    @Field(type = FieldType.Keyword, name = "patternData")
    private String patternData; /* 패턴 데이터 */

    @Field(type = FieldType.Date, name = "timeCurrent", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCurrent; /* 발생시간 */
}