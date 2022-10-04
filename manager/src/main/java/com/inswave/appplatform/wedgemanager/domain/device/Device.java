package com.inswave.appplatform.wedgemanager.domain.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inswave.appplatform.wedgemanager.enums.WebsocketState;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="EM_DEVICES")
@IdClass(DeviceKey.class)
public class Device {
	@Id
	@Column(name="APPID",length=50, insertable = true, updatable = true, nullable = false)
	private String appId;

	@Id
	@Column(name="ID", length=30, insertable = true, updatable = true, nullable = false)
	private String deviceId;

	@Column(name="OSTYPE", length=30)
	private String osType;

	@Column(name="USERID")
	private String userId;

	@Column(name="IP", length=30)
    private String ip;

	@Column(name="DEPARTCODE")
	private String departCode="";

	@Column(name="STATE", length=30)
	private String state;

	// 최종로그인일자
	@Column(name = "LASTDATE")
	@UpdateTimestamp
	private Date lastDate;

	@Column(name="SESSIONSTATE")
	private WebsocketState sessionState;

	@Column(name="UPDATEVERSION", length=30)
	private String updateVersion;

	@Column(name="UPDATESTATE", length = 30)
	private String updateState;

	@Column(name="UPDATESCOPE", length=1)
	private String updateScope;

	@Column(name="UPDATEUPDATEDATE")
	private Date updateUpdateDate;

	@Column(name="DISTVERSION", length=100)
	private String distVersion;

	@Column(name="DISTSTATE", length=30)
	private String distState;

	@Column(name="DISTSCOPE", length=1)
	private String distScope;

	@Column(name="DISTUPDATEDATE")
	private Date distUpdateDate;

	@Column(name="STATEMSG", length = 512)
	private String stateMsg;

	@JsonIgnore
	@CreationTimestamp
	@Column(name = "CREATEDATE", updatable = false)
	private Date createDate;

	@JsonIgnore
	@UpdateTimestamp
	@Column(name="UPDATEDATE")
	private Date updateDate;
}
