package com.inswave.appplatform.svn.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatusVO {
    private String         path;
    private long           revision;
    private String         createdDate;
    private Boolean        isLocked;
    private String         lockOwner;
    private String         lockComment;
    private List<StatusVO> locks;
}
