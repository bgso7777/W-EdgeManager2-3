package com.inswave.appplatform.log.domain;

import com.inswave.appplatform.dao.Domain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@TableGenerator(
                name = "RULE_GENERATOR",
                pkColumnValue = "RULE_SEQ",
                table = "EM_SEQ_GENERATOR",
                allocationSize = 1)
public class Rule implements Domain  {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RULE_GENERATOR")
    private Long ruleId;

    @Column(nullable = false)
    private Boolean active=true;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String description;

    @Column(nullable = true)
    private Boolean isSystem=false;

    @Column(length = 20, nullable = true)
    private String classNames;

    @Column(length = 20, nullable = true)
    private String displayClassNames;

    @Column(length = 1024, nullable = true)
    private String fieldNames;

    @Column(length = 1024, nullable = true)
    private String displayFieldNames;

    @Column(length = 1024, nullable = false)
    private String pattern;

//    @ManyToOne
//    @JoinColumn(name = "RULE_LEVEL_ID")
//    private RuleLevel ruleLevel;
////    public Long getRuleLevelId() { return ruleLevel.getRuleLevelId(); }
////    public void setRuleLevelId(Long ruleLevelId) {
////        ruleLevel.setRuleLevelId(ruleLevelId);
////    }
//
//    /* 알림 수신자 최대 3개까지 등록 */
//    @ManyToOne
//    @JoinColumn(name = "RULE_RECEIVER_ID1", nullable = true)
//    private RuleReceiver ruleReceiver1;
//
//    @ManyToOne
//    @JoinColumn(name = "RULE_RECEIVER_ID2", nullable = true)
//    private RuleReceiver ruleReceiver2;
//
//    @ManyToOne
//    @JoinColumn(name = "RULE_RECEIVER_ID3", nullable = true)
//    private RuleReceiver ruleReceiver3;

    @Column(name="RULE_LEVEL_ID", nullable=false)
    private Long ruleLevelId;

    @Column(name="RULE_RECEIVER_ID1", nullable=true)
    private Long ruleReceiverId1;

    @Column(name="RULE_RECEIVER_ID2", nullable=true)
    private Long ruleReceiverId2;

    @Column(name="RULE_RECEIVER_ID3", nullable=true)
    private Long ruleReceiverId3;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
}
