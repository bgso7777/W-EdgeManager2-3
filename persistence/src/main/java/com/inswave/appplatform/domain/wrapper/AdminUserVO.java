package com.inswave.appplatform.domain.wrapper;

import com.inswave.appplatform.domain.AdminUser;
import com.inswave.appplatform.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserVO {
    private Long   adminUserId;
    private Long   siteId;
    private Long   countryId;
    private String loginId;
    //    private String password; // UserDetails
    //    private Boolean isFirstPassword;
    private String username; // UserDetails
    private String email;
    private String phone;
    //    private String connectIps;
    //    private Date passwordDueDate;
    //    private Date    updateDate;
    //    private Date    registrationDate;
    //    private Integer countWrongPassword = 0;
    private Role   role;

    public static AdminUserVO from(AdminUser adminUser) {
        return AdminUserVO.builder()
                          .adminUserId(adminUser.getAdminUserId())
                          .siteId(adminUser.getSiteId())
                          .countryId(adminUser.getCountryId())
                          .loginId(adminUser.getLoginId())
                          .username(adminUser.getName())
                          .email(adminUser.getEmail())
                          .phone(adminUser.getPhone())
                          .role(adminUser.getRole())
                          .build();
    }
}
