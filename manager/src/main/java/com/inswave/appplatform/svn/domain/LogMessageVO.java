package com.inswave.appplatform.svn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inswave.appplatform.util.DateUtil;
import lombok.*;
import org.tigris.subversion.javahl.ChangePath;
import org.tigris.subversion.javahl.LogMessageCallback;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LogMessageVO implements LogMessageCallback {
    private String                 commitAuthor;
    private String                 commitAuthorName;
    private String                 commitAuthorEmail;
    private String                 commitAuthorRole;
    private Date                   commitDate;
    private String                 commitComments;
    private Long                   revision;
    private Boolean                hasChildren;
    private List<LogMessageFileVO> changedFiles;

    @Builder.Default
    @JsonIgnore
    private boolean            useChangedFiles = false;
    @JsonIgnore
    private List<LogMessageVO> list            = new ArrayList<>();

    @Override
    public void singleMessage(ChangePath[] changedPaths, long revision, Map revprops, boolean hasChildren) {
        Date commitDate = null;
        try {
            commitDate = DateUtil.svnDateToDate((String) revprops.get("svn:date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        list.add(LogMessageVO.builder()
                             .commitAuthor((String) revprops.get("svn:author"))
                             .commitDate(commitDate)
                             .commitComments((String) revprops.get("svn:log"))
                             .revision(revision)
                             .hasChildren(hasChildren)
                             .changedFiles(useChangedFiles ? LogMessageFileVO.from(changedPaths) : null)
                             .build());
    }

    public List<LogMessageVO> getList() {
        return list.stream().sorted(Comparator.comparing(LogMessageVO::getRevision).reversed()).collect(Collectors.toList());
    }
}
