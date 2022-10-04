package com.inswave.appplatform.svn.domain;

import lombok.*;
import org.tigris.subversion.javahl.ChangePath;
import org.tigris.subversion.javahl.NodeKind;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LogMessageFileVO {
    private String path;
    private long   copySrcRevision;
    private String copySrcPath;
    private char   action;
    /**
     * 'A'dd, 'D'elete, 'R'eplace, 'M'odify
     */
    private String nodeKind;

    public static List<LogMessageFileVO> from(ChangePath[] changePaths) {
        return Arrays.stream(changePaths).map(LogMessageFileVO::from).collect(Collectors.toList());
    }

    public static LogMessageFileVO from(ChangePath changePath) {
        return LogMessageFileVO.builder()
                               .path(changePath.getPath())
                               .copySrcRevision(changePath.getCopySrcRevision())
                               .copySrcPath(changePath.getCopySrcPath())
                               .action(changePath.getAction())
                               .nodeKind(NodeKind.getNodeKindName(changePath.getNodeKind()))
                               .build();
    }
}
