package com.inswave.appplatform.log.entity;

import lombok.Data;

/**
 * Created by chungheepark on 2018. 4. 5..
 */
@Data
public class DiskInfo {
	private long free=0L;
	private long total=0L;
	private long usableSpace=0L;
	private long used=0L;	// absolute
	private double use=0.0;	// percentage
}
