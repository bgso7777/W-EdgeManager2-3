package com.inswave.appplatform.wedgemanager.dao;

import com.inswave.appplatform.service.dao.StandardDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalMenu;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminalMenuDao extends StandardDao<TerminalMenu, Long> {

    List<TerminalMenu> findByParentIdIsNull(Sort sort);

    TerminalMenu findByScreenId(String screenId);
}
