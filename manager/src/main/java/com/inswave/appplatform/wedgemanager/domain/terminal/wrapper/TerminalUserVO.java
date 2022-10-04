package com.inswave.appplatform.wedgemanager.domain.terminal.wrapper;

import com.inswave.appplatform.wedgemanager.controller.terminal.TerminalUserController;
import com.inswave.appplatform.wedgemanager.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TerminalUserVO {
    private String  userId;
    private String  name;
    private String  department;
    private String  duty;
    private String  position;
    private String  email;
    private Date    regDate;
    private Date    applyDate;
    private String  valid;
    private Date    createDate;
    private Date    updateDate;
    //    private String pwd;
    private String  departName;
    private String  termIp;
    private boolean login;
    private Date    lastLoginDate;
    private Date    lastLogoutDate;

    public static TerminalUserVO from(User user) {
        boolean isLogin = false;
        long loginExpirationPeriod = TerminalUserController.TERMINAL_LOGIN_EXPIRATION_PERIOD;

        if (user.getLastLoginPingDate() != null) {
            if (user.getLastLogoutDate() == null) {
                isLogin = true;
            } else if (user.getLastLogoutDate() != null) {
                if (user.getLastLoginPingDate().isAfter(user.getLastLogoutDate())) {
                    isLogin = true;
                }
            }
            ZonedDateTime expireDate = user.getLastLoginPingDate().plus(loginExpirationPeriod, ChronoUnit.MILLIS);
            if (expireDate.isBefore(ZonedDateTime.now())) {
                isLogin = false;
            }
        }
        return TerminalUserVO.builder()
                             .userId(user.getUserId())
                             .name(user.getName())
                             .department(user.getDepartment())
                             .duty(user.getDuty())
                             .position(user.getPosition())
                             .email(user.getEmail())
                             .regDate(user.getRegDate())
                             .applyDate(user.getApplyDate())
                             .valid(user.getValid())
                             .createDate(user.getCreateDate())
                             .updateDate(user.getUpdateDate())
                             //                             .pwd(user.getPwd())
                             .login(isLogin)
                             .build();
    }

    public TerminalUserVO bind(TerminalUserVO updatedUser) {
        //        this.userId(updatedUser.getUserId());
        this.setName(updatedUser.getName());
        this.setDepartment(updatedUser.getDepartment());
        this.setDuty(updatedUser.getDuty());
        this.setPosition(updatedUser.getPosition());
        this.setEmail(updatedUser.getEmail());
        this.setRegDate(updatedUser.getRegDate());
        this.setApplyDate(updatedUser.getApplyDate());
        this.setValid(updatedUser.getValid());
        //        this.setPwd(updatedUser.getPwd());
        return this;
    }
}
