package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "ts_menu")
@Entity
public class TerminalMenuFlat extends StandardDomain {
    @Id
    @Column(updatable = false)
    private String  id;
    @Column
    private String  parentId;
    @Column
    private Integer ord;
    @Column
    private Integer depth;
    @Column
    private String  menuName;
    @Column
    private String  screenId;
    @Column
    private String  keyword;
    @Column
    private String  keywordPast4;
    @Column
    private String  screenIdPast;
    @Column
    private String  chargeEmpPhone;
    @Column
    private String  chargeEmpName;
    @Column
    private String  chargeDeptName;
    @Column
    private String  menuType;

}