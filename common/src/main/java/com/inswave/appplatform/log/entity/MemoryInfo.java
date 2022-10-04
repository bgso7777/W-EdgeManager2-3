package com.inswave.appplatform.log.entity;

import lombok.Data;

/**
 * Created by chungheepark on 2018. 4. 5..
 */
@Data
public class MemoryInfo {
	private long total;
	private long used;
	private long free;
	private double usage;
}
