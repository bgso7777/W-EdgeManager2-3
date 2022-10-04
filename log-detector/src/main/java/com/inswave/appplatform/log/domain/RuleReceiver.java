package com.inswave.appplatform.log.domain;

import com.inswave.appplatform.dao.Domain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@TableGenerator(
        name = "RULE_RECEIVER_TYPE_GENERATOR",
        pkColumnValue = "RULE_RECEIVER_TYPE_SEQ",
        table = "EM_SEQ_GENERATOR",
        allocationSize = 1)
public class RuleReceiver implements Domain  {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RULE_RECEIVER_TYPE_GENERATOR")
    private Long ruleReceiverId;

    @Column(nullable = false)
    private Boolean active=true;

    @Column(length = 1024, nullable = false)
    private String name; // 수신자명

    @Column(length = 1024, nullable = false)
    private String uniqueId; // 수신자 아이디

    @Column(nullable = false)
    private Boolean isEmail=false; // 수신 smtp 이메일 사용

    @Column(length = 1024, nullable = false)
    private String email; // 수신 이메일

    @Column(nullable = false)
    private Boolean isUrl=false; // 수신 http 쪽지 사용

    @Column(length = 1024, nullable = false)
    private String senderId; // 발신자 아이디

    @Column(length = 1024, nullable = false)
    private String senderName; // 발신자명

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

}