package com.inswave.appplatform.service.dao;

import com.inswave.appplatform.service.domain.FileAttachement;
import com.inswave.appplatform.service.domain.FileAttachementMap;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAttachementMapDao extends StandardDao<FileAttachementMap, Long> {

    List<FileAttachementMap> findBySourceTypeAndSourceCode(String sourceType, String sourceCode);

    int countBySourceTypeAndSourceCode(String sourceType, String sourceCode);

    List<FileAttachementMap> findByFile(FileAttachement fileAttachement);

}