package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalHelpReply;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminalHelpReplyDao extends StandardDao<TerminalHelpReply, Long> {
    @Query("   SELECT A "
           + " FROM    TerminalHelpReply A"
           + " WHERE   A.screenId = :screenId"
           + " AND     A.parentId IS NULL")
    List<TerminalHelpReply> findByScreenIdAndPIdIsNull(@Param("screenId") String screenId);
}