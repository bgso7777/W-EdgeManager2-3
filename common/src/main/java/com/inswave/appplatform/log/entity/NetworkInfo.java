package com.inswave.appplatform.log.entity;

import lombok.Data;

/**
 * Created by chungheepark on 2018. 4. 5..
 */
@Data
public class NetworkInfo {
    private String   interfaceName;
    private double   received;
    private double   sent;
    private long     speed;
    private double   tx = 0;
    private double   rx = 0;
    private boolean  loopback;
    private boolean  virtual;
    private boolean  up;
    private boolean  pointToPoint;
    private String[] ipv4addr;
    private String[] ipv6addr;
    private String   macaddr;
}
