package com.inswave.appplatform.svn.domain;

import lombok.*;
import org.apache.subversion.javahl.callback.ListItemCallback;
import org.apache.subversion.javahl.types.DirEntry;
import org.apache.subversion.javahl.types.Lock;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListItemVO implements ListItemCallback {
    private DirEntry dirent;
    private Lock     lock;
    private String   externalParentURL;
    private String   externalTarget;

    List<ListItemVO> list = new ArrayList<>();

    @Override
    public void doEntry(DirEntry dirent, Lock lock, String externalParentURL, String externalTarget) {
        list.add(ListItemVO.builder()
                           .dirent(dirent)
                           .lock(lock)
                           .externalParentURL(externalParentURL)
                           .externalTarget(externalTarget)
                           .build());

    }
}
