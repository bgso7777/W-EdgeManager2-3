package com.inswave.appplatform.deployer.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inswave.appplatform.util.ByteUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeployTransferReceiveStatusVO {
    private String  deviceid;
    private String  appid;
    private String  ip;
    private String  receiveStatus;
    @JsonIgnore
    private Integer receiveCount;

    public void updateReceiveStatus() {
        try {
            if (receiveStatus != null) {
                receiveStatus = ByteUtil.base64ToBinaryString(receiveStatus);
            }
        } catch (IOException e) {
        }
        receiveCount = StringUtils.countMatches(receiveStatus, "1");
        receiveStatus = "";
    }
}
