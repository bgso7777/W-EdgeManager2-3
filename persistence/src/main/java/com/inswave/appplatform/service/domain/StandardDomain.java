package com.inswave.appplatform.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inswave.appplatform.dao.Domain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public abstract class StandardDomain implements Domain {

    @Builder.Default
    @CreatedDate
    @Setter
    @Column(updatable = false)
    private ZonedDateTime createDate = ZonedDateTime.now();
    @CreatedBy @Setter @Getter
    @Column(updatable = false)
    private String        createUserId;
    @CreatedBy @Setter @Getter
    @Column(updatable = false)
    private String        createUserName;
    @LastModifiedDate @Setter
    private ZonedDateTime updateDate;
    @LastModifiedBy @Setter @Getter
    private String        updateUserId;
    @LastModifiedBy @Setter @Getter
    private String        updateUserName;

    public String getCreateDate() {
        return createDate != null ? createDate.toLocalDateTime().toString() : "";
    }

    public String getUpdateDate() {
        return updateDate != null ? updateDate.toLocalDateTime().toString() : "";
    }

    @JsonIgnore
    public ZonedDateTime getCreateDateOrigin() {
        return createDate;
    }

    @JsonIgnore
    public ZonedDateTime getUpdateDateOrigin() {
        return updateDate;
    }
}
