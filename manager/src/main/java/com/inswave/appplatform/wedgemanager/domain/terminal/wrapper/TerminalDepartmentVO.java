package com.inswave.appplatform.wedgemanager.domain.terminal.wrapper;

import com.inswave.appplatform.wedgemanager.domain.organization.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TerminalDepartmentVO {
    private String departCode;
    private String departName;
    private int    departLevel;
    private String upperCode;
    private Date   createDate;
    private Date   updateDate;

    public TerminalDepartmentVO bind(TerminalDepartmentVO updatedDepartment) {
        //       .departCode(updatedDepartment.getDepartCode())
        this.setDepartName(updatedDepartment.getDepartName());
        this.setDepartLevel(updatedDepartment.getDepartLevel());
        this.setUpperCode(updatedDepartment.getUpperCode());
        this.setCreateDate(updatedDepartment.getCreateDate());
        this.setUpdateDate(updatedDepartment.getUpdateDate());
        return this;
    }

    public static TerminalDepartmentVO from(Department department) {
        return TerminalDepartmentVO.builder()
                                   .departCode(department.getDepartCode())
                                   .departName(department.getDepartName())
                                   .departLevel(department.getDepartLevel())
                                   .upperCode(department.getUpperCode())
                                   .createDate(department.getCreateDate())
                                   .updateDate(department.getUpdateDate())
                                   .build();
    }
}
