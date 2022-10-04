package com.inswave.appplatform.log.domain;

public enum RuleLevelType {
    EMERGENCY,  // 비상-발생즉시
    RISK,       // 위험-10분
    HIGH,       // 높음-30분
    MEDIUM,     // 중간-1시간
    LOW         // 낮음-1일
}