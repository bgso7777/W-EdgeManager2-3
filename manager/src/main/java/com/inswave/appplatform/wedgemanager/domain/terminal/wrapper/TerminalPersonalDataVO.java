package com.inswave.appplatform.wedgemanager.domain.terminal.wrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalPersonalData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalPersonalDataVO {
    private String   userId;
    private String   dataType;
    private Long     dataId;
    private JsonNode dataJson;
    private String   createDate;
    private String   updateDate;

    public static TerminalPersonalDataVO from(TerminalPersonalData terminalPersonalData) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode dataJson = null;
        try {
            dataJson = mapper.readTree(terminalPersonalData.getDataJson());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return TerminalPersonalDataVO.builder()
                                     .userId(terminalPersonalData.getUserId())
                                     .dataType(terminalPersonalData.getDataType())
                                     .dataId(terminalPersonalData.getDataId())
                                     .dataJson(dataJson)
                                     .createDate(terminalPersonalData.getCreateDate() != null ? terminalPersonalData.getCreateDate().toLocalDateTime().toString() : "")
                                     .updateDate(terminalPersonalData.getUpdateDate() != null ? terminalPersonalData.getUpdateDate().toLocalDateTime().toString() : "")
                                     .build();
    }
}