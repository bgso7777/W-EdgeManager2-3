package com.inswave.appplatform.svn.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusinessVO {
    private String code;
    private String name;
    private long   revision;
    private String lastChanged;
    private String lastAuthor;

    public static BusinessVO from(DirectoryVO vo) {
        return BusinessVO.builder()
                         .code(vo.getPath())
                         .name(vo.getPath())
                         .revision(vo.getLastChangedRevision())
                         .lastChanged(vo.getLastChanged())
                         .lastAuthor(vo.getLastAuthor())
                         .build();
    }
}
