package com.inswave.appplatform.wedgemanager.domain.terminal.wrapper;

import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalNotice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TerminalNoticeVO {
    private Long    id;
    private String  title;
    private String  contents;
    private String  exposeTarget;
    private Boolean exposeOn;
    private int     fileCount;
    private String  createDate;
    private String  createUserId;
    private String  createUserName;
    private String  updateDate;
    private String  updateUserId;
    private String  updateUserName;

    public static TerminalNoticeVO from(TerminalNotice terminalNotice) {
        return TerminalNoticeVO.builder()
                               .id(terminalNotice.getId())
                               .title(terminalNotice.getTitle())
                               .contents(terminalNotice.getContents())
                               .exposeTarget(terminalNotice.getExposeTarget())
                               .exposeOn(terminalNotice.getExposeOn())
                               .createDate(terminalNotice.getCreateDate())
                               .createUserId(terminalNotice.getCreateUserId())
                               .createUserName(terminalNotice.getCreateUserName())
                               .updateDate(terminalNotice.getUpdateDate())
                               .updateUserId(terminalNotice.getUpdateUserId())
                               .updateUserName(terminalNotice.getUpdateUserName())
                               .build();
    }
}
