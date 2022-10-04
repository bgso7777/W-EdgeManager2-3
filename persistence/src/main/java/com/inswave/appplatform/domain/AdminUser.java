package com.inswave.appplatform.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.dao.Domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@TableGenerator(
name = "ADMIN_USER_GENERATOR",
pkColumnValue = "ADMIN_USER_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class AdminUser implements Domain, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ADMIN_USER_GENERATOR")
    private Long adminUserId;

    @Column
    @Builder.Default
    private Long siteId = 0L;

    @Column
    @Builder.Default
    private Long countryId = 0L;

    @Column(length = 20, nullable = false)
    private String loginId;

    @Column(length = 20, nullable = false)
    private String password; // UserDetails

    @Column
    @Builder.Default
    private Boolean isFirstPassword = true;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String username; // UserDetails 

    @Column
    private String email;

    @Column(length = 20, nullable = true)
    private String phone;

    @Column(length = 255, nullable = true)
    private String connectIps;

    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordDueDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(length = 11, nullable = true)
    @Builder.Default
    private Integer countWrongPassword = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loginFailLockDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loginDate;

    @Column(length = 10000, nullable = true)
    private String authToken;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(role.getName()));
        return auth;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("adminUserId", adminUserId);                  // Long adminUserId;
        map.put("loginId", loginId);                          // String loginId;
        map.put("name", name);                                // String name;
        map.put("username", username);                        // String username;
//        map.put("email", email);                              // String email;
        map.put("phone", phone);                              // String phone;
//        map.put("connectIps", connectIps);                    // String connectIps;
        map.put("roleName", role.getName());
        map.put("roleId", role.getRoleId());

        map.put("updateDate", updateDate != null ? updateDate.getTime() : null);                    // Date updateDate;
        map.put("registrationDate", registrationDate != null ? registrationDate.getTime() : null);        // Date registrationDate;
        map.put("loginDate", loginDate != null ? loginDate.getTime() : null);                      // Date loginDate;
        return map;
    }

    public static AdminUser fromMap(JsonNode node) {
        JsonNode loginId = node.get("loginId");
        JsonNode password = node.get("password");
        JsonNode name = node.get("name");
        JsonNode username = node.get("username");
        JsonNode phone = node.get("phone");

        AdminUser adminUser = builder()
        .loginId(loginId != null ? loginId.asText() : null)
        .name(name != null ? name.asText() : null)
        .username(username != null ? username.asText() : null)
        .phone(phone != null ? phone.asText() : null)
        .build();
        if (password != null) {
            Base64.Decoder decoder = Base64.getDecoder();
            String pwd = new String(decoder.decode(decoder.decode(password.asText().getBytes())));
            adminUser.setPassword(pwd);
        }
        return adminUser;
    }

    public void bind(JsonNode node) {
        JsonNode loginId = node.get("loginId");
        JsonNode password = node.get("password");
        JsonNode name = node.get("name");
        JsonNode username = node.get("username");
        JsonNode phone = node.get("phone");

        this.loginId = loginId != null ? loginId.asText() : null;
        this.name = name != null ? name.asText() : null;
        this.username = username != null ? username.asText() : null;
        this.phone = phone != null ? phone.asText() : null;

        if (password != null) {
            Base64.Decoder decoder = Base64.getDecoder();
            String pwd = new String(decoder.decode(decoder.decode(password.asText().getBytes())));
            this.password = pwd;
        }
    }
}