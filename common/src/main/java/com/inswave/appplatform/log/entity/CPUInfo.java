package com.inswave.appplatform.log.entity;

import lombok.Data;

/**
 * Created by chungheepark on 2018. 4. 5..
 */
@Data
public class CPUInfo {
	private String cpuNum;
	private double load;
	private double usage;
}
