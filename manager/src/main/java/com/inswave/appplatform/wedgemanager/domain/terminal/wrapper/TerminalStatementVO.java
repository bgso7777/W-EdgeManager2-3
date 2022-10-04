package com.inswave.appplatform.wedgemanager.domain.terminal.wrapper;

import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalStatement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TerminalStatementVO {
    private String id;
    private String name;
    private String userData1;

    public static TerminalStatementVO from(TerminalStatement terminalStatement) {
        return TerminalStatementVO.builder()
                                  .id(terminalStatement.getConvertedId())
                                  .name(terminalStatement.getConvertedDesc())
                                  .userData1(terminalStatement.getUserData1())
                                  .build();
    }
}
