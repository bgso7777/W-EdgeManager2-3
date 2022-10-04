package com.inswave.appplatform.domain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@TableGenerator(
name = "ROLE_GENERATOR",
pkColumnValue = "ROLE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ROLE_GENERATOR")
    private Long roleId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String description;

    public static Role from(JsonNode obj) {
        JsonNode name = obj.get("name");
        JsonNode description = obj.get("description");

        return builder().name(name != null ? name.asText() : null)
                        .description(description != null ? description.asText() : null)
                        .build();
    }

    public void bind(JsonNode obj) {
        JsonNode name = obj.get("name");
        JsonNode description = obj.get("description");

        this.name = name != null ? name.asText() : null;
        this.description = description != null ? description.asText() : null;
    }
}
