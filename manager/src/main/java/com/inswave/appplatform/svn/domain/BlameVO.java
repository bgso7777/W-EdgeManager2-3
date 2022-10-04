package com.inswave.appplatform.svn.domain;

import lombok.*;
import org.tigris.subversion.javahl.BlameCallback3;
import org.tigris.subversion.javahl.ClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlameVO implements BlameCallback3 {
    long    lineNum;
    long    revision;
    Map     revProps;
    long    mergedRevision;
    Map     mergedRevProps;
    String  mergedPath;
    String  line;
    boolean localChange;

    List<BlameVO> list = new ArrayList<>();

    @Override
    public void singleLine(long lineNum, long revision, Map revProps, long mergedRevision, Map mergedRevProps, String mergedPath, String line, boolean localChange) throws ClientException {
        list.add(BlameVO.builder()
                        .lineNum(lineNum)
                        .revision(revision)
                        .revProps(revProps)
                        .mergedRevision(mergedRevision)
                        .mergedRevProps(mergedRevProps) //mergedRevProps == null ? null : new String((byte[]) mergedRevProps.get("svn:author")))
                        .mergedPath(mergedPath)
                        .line(line)
                        .localChange(localChange)
                        .build());
    }
}
