package com.inswave.appplatform.log.entity;

import lombok.Data;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chungheepark on 2018. 4. 5..
 * Modify by bgso on 2021-05-18
 */
@Data
public class SystemInfo extends oshi.SystemInfo {
    private HardwareAbstractionLayer hardware     = super.getHardware();
    public String                   hostName     = "";
    public OSInfo                   osInfo       = new OSInfo();
    public List<CPUInfo>            cpu;
    public List<MemoryInfo>         mem;
    public List<DiskInfo>           disk;
    public List<NetworkInfo>        network      = new ArrayList<>();
    public List<NetworkInfo>        networkAfter = new ArrayList<>();
    public long                     runTime;

    @Override
    public HardwareAbstractionLayer getHardware() {
        return this.hardware;
    }
}