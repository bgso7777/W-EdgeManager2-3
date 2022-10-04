package com.inswave.appplatform.svn.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepositoryListItemCache {
    private List<SvnItemVO> cache;
    private long            revision;
    private long            accessMapVersion;
}
