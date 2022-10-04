package com.inswave.appplatform.wedgemanager.domain.terminal.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TerminalMenuVO {
    @JsonProperty("menu_id")
    private String  id;
    @JsonProperty("hrnk_menu_id")   //    @JsonIgnore
    private String  parentId;
    @JsonProperty("seq_no")
    private Integer ord;
    @JsonProperty("menu_lev_no")
    private Integer depth;
    @JsonProperty("menu_nm")
    private String  menuName;
    @JsonProperty("scrn_id")
    private String  screenId;
    @JsonProperty("scrn_path")
    private String  screenSolutionPath;
    @JsonProperty("scrn_svn_path")
    private String  screenSolutionSvnPath;
    @JsonProperty("menu_navi")
    private String  menuFullPath;
    @JsonProperty("TOBE6")
    private String  keyword;
    @JsonProperty("AS4")
    private String  keywordPast4;
    @JsonProperty("scrn_id_past")
    private String  screenIdPast;
    private String  chargeEmpPhone;
    private String  chargeEmpName;
    private String  chargeDeptName;
    private String  menuType;

    //    private Integer menuCnt;
    //    private String  menuType;   // dir, screen 으로 사용할까?, 지우자 -> screenId 기준 ..있으면 화면, 없으면 dir !
    //    private String  scncall1;
    //    private String  scncall2;
    //    private String  scncall3;
    //    private String  auth;
    //    private String  role;
    //    private Boolean defaultYn;

    public static TerminalMenuVO from(TerminalMenu terminalMenu,
                                      int depth, String screenSolutionPath,
                                      String screenSolutionSvnPath,
                                      List<String> menuFullPath) {
        return TerminalMenuVO.builder()
                             .id(terminalMenu.getId())
                             .parentId(String.valueOf(StringUtils.isEmpty(terminalMenu.getParentId()) ? "ROOT" : terminalMenu.getParentId()))
                             .ord(terminalMenu.getOrd())
                             .depth(depth)
                             .menuName(terminalMenu.getMenuName())
                             .screenId(terminalMenu.getScreenId())
                             .screenSolutionPath(screenSolutionPath)
                             .screenSolutionSvnPath(screenSolutionSvnPath)
                             .menuFullPath(String.join(">", menuFullPath))
                             .keyword(terminalMenu.getKeyword())
                             .keywordPast4(terminalMenu.getKeywordPast4())
                             .screenIdPast(terminalMenu.getScreenIdPast())
                             .chargeEmpPhone(terminalMenu.getChargeEmpPhone())
                             .chargeEmpName(terminalMenu.getChargeEmpName())
                             .chargeDeptName(terminalMenu.getChargeDeptName())
                             .menuType(terminalMenu.getMenuType())
                             .build();
    }
}