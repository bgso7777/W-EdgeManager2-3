package com.inswave.appplatform.svn.domain;

import com.inswave.appplatform.util.DateUtil;
import lombok.*;
import org.tigris.subversion.javahl.DirEntry;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DirectoryVO {
    private String path;
    private String lastChanged;
    private long   lastChangedRevision;
    private String lastAuthor;
    private long   size;

    public static DirectoryVO from(DirEntry dirent) {
        return DirectoryVO.builder()
                          .path(dirent.getPath())
                          .lastChanged(DateUtil.getDate(dirent.getLastChanged(), "yyyy-MM-dd hh:mm:ss"))
                          .lastChangedRevision(dirent.getLastChangedRevisionNumber())
                          .lastAuthor(dirent.getLastAuthor())
                          .size(dirent.getSize())
                          .build();
    }
}
