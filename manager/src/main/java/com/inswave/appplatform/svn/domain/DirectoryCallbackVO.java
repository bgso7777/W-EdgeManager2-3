package com.inswave.appplatform.svn.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.tigris.subversion.javahl.DirEntry;
import org.tigris.subversion.javahl.ListCallback;
import org.tigris.subversion.javahl.Lock;
import org.tigris.subversion.javahl.NodeKind;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DirectoryCallbackVO implements ListCallback {
    private List<DirectoryVO> directoryVOs = new ArrayList<>();

    @Override
    public void doEntry(DirEntry dirent, Lock lock) {
        if (dirent.getNodeKind() == NodeKind.dir
            && !dirent.getAbsPath().equals(dirent.getPath())
            && !dirent.getPath().isEmpty()) {
            directoryVOs.add(DirectoryVO.from(dirent));
        }
    }
}
