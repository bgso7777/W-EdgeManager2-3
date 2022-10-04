package com.inswave.appplatform.svn.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepositoryVO {
    private String       uuid;
    private String       name;
    private String       path;
    private long         revision;
    private String       createdDate;
//    private List<LockVO> locks;
}
