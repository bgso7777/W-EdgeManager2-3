package com.inswave.appplatform.wedgemanager.domain.terminal.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Builder
public class TerminalDistResultDetailFileVO {
    @Getter @Setter
    private String        action;    // 'A'dd, 'D'elete, 'R'eplace, 'M'odify : 실제로 R은 없음 (A, D로 동작)
    @Getter @Setter
    private String        pathSource;
    @Getter @Setter
    private String        pathDestination;
    @Getter @Setter
    private Long          pathDestinationFileSize;
    @Getter @Setter @Builder.Default
    private Boolean       success    = true;
    @Getter @Setter
    private String        desc;
    @Builder.Default
    private ZonedDateTime createDate = ZonedDateTime.now();

    public String getCreateDate() {
        return createDate != null ? createDate.toLocalDateTime().toString() : null;
    }

    public String getActionName() {
        switch (action) {
        case "A":
            return "Add";
        case "D":
            return "Delete";
        case "R":
            return "Replace";
        case "M":
            return "Modify";
        }
        return "";
    }

}