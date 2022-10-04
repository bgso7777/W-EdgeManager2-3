package com.inswave.appplatform.svn.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.subversion.javahl.types.DirEntry;
import org.apache.subversion.javahl.types.Lock;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SvnItemVO {
    private String absPath;
    private String path;
    private String menuPath;
    private String nodeKind;
    private String lastAuthor;
    private int    depth;

    private String lastAuthorName;                 // 매핑필드
    private String lastAuthorEmail;                // 매핑필드
    private String lastAuthorRole;                 // 매핑필드

    private String business;                       // 매핑필드
    private String businessName;                   // 매핑필드
    private String businessAuth;                   // 매핑필드
    private String businessAuthName;               // 매핑필드

    private long    lastChangedRevisionNumber;
    private Date    lastChangedDate;
    private long    size;
    private boolean hasProps;

    private boolean lock;
    private String  lockPath;
    private String  lockOwner;
    private String  lockToken;
    private String  lockComment;
    private Date    lockCreationDate;
    private Date    lockExpirationDate;

    private String externalTarget;
    private String externalParentURL;

    private Integer commitCount;

    public static List<SvnItemVO> from(List<ListItemVO> list) {
        return list.stream().map(SvnItemVO::from).collect(Collectors.toList());
    }

    public static SvnItemVO from(ListItemVO listItem) {
        return from(
        listItem.getDirent()
        , listItem.getLock()
        , listItem.getExternalTarget()
        , listItem.getExternalParentURL()
        );
    }

    public static SvnItemVO from(DirEntry entry, Lock lock, String externalTarget, String externalParentURL) {
        return SvnItemVO.builder()
                        .lastChangedDate(entry.getLastChanged())
                        .path(entry.getPath())
                        .lastAuthor(entry.getLastAuthor())
                        .depth(StringUtils.isEmpty(entry.getPath()) ? 0 : entry.getPath().split("/").length)
                        .nodeKind(entry.getNodeKind().name())
                        .absPath(entry.getAbsPath())
                        .hasProps(entry.getHasProps())
                        .lastChangedRevisionNumber(entry.getLastChangedRevisionNumber())
                        .size(entry.getSize())
                        .lock(lock != null)
                        .lockOwner(lock != null ? lock.getOwner() : null)
                        .lockPath(lock != null ? lock.getPath() : null)
                        .lockToken(lock != null ? lock.getToken() : null)
                        .lockComment(lock != null ? lock.getComment() : null)
                        .lockCreationDate(lock != null ? lock.getCreationDate() : null)
                        .lockExpirationDate(lock != null ? lock.getExpirationDate() : null)
                        .externalTarget(externalTarget)
                        .externalParentURL(externalParentURL)
                        .build();
    }

}
