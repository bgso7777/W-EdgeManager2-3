package com.inswave.appplatform.domain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@TableGenerator(
name = "MENU_ROLE_GENERATOR",
pkColumnValue = "MENU_ROLE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class MenuRole {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MENU_ROLE_GENERATOR")
    private Long menuRoleId;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long roleId;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    public static MenuRole from(JsonNode obj) {
        JsonNode menuId = obj.get("menuId");
        JsonNode roleId = obj.get("roleId");

        return builder().menuId(menuId != null ? menuId.asLong() : null)
                        .roleId(roleId != null ? roleId.asLong() : null)
                        .build();
    }

    public void bind(JsonNode obj) {
        JsonNode menuId = obj.get("menuId");
        JsonNode roleId = obj.get("roleId");

        this.menuId = menuId != null ? menuId.asLong() : null;
        this.roleId = roleId != null ? roleId.asLong() : null;
    }
}
