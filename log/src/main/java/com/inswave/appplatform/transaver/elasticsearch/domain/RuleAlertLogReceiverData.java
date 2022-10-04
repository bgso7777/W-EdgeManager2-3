package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
public class RuleAlertLogReceiverData {

    @Field(type = FieldType.Long, name = "ruleReceiverId")
    private Long ruleReceiverId; /* 수신자 아이디 */

    @Field(type = FieldType.Keyword, name = "name")
    private String name; /* 수신자명 */

    @Field(type = FieldType.Keyword, name = "uniqueId")
    private String uniqueId;/* 유일키 아이디 */

    @Field(type = FieldType.Boolean, name = "isEmail")
    private Boolean isEmail; /* 이메일 수신여부 */

    @Field(type = FieldType.Keyword, name = "email")
    private String email; /* 이메일 */

    @Field(type = FieldType.Boolean, name = "isUrl")
    private Boolean isUrl;/* 메시지 수신여부 */

    @Field(type = FieldType.Keyword, name = "senderId")
    private String senderId;/* 메시지 발신자 아이디 */

    @Field(type = FieldType.Keyword, name = "senderName")
    private String senderName;/* 메시지 발신자명 */

    @Field(type = FieldType.Keyword, name = "content")
    private String content;/* ATOP 메시지 내용 */
}