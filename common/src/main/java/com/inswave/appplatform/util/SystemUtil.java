package com.inswave.appplatform.util;

import com.inswave.appplatform.log.entity.*;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.management.OperatingSystemMXBean;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import zutil.net.ThroughputCalculator;

/**
 * Created by chungheepark on 2018. 4. 5..
 * Modify by bgso on 2021-05-18
 * Modify by bgso on 2021-12-28
 */
public class SystemUtil {

    public static void initOSInfo(SystemInfo systemInfo) {
        OperatingSystem os = systemInfo.getOperatingSystem();
        OperatingSystem.OSVersionInfo oSVersionInfo = os.getVersionInfo();
        String osFamily = os.getFamily();
        OSInfo osInfo = new OSInfo();
        osInfo.setDistro(osFamily.substring(0, 1).toUpperCase() + osFamily.substring(1));
        osInfo.setRelease(oSVersionInfo.getVersion());
        systemInfo.setOsInfo(osInfo);
    }

    public static void initHostName(SystemInfo systemInfo) {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        systemInfo.setHostName(hostName);
    }

//    private static void initCpuInfo(SystemInfo systemInfo) {
//        List<CPUInfo> cpuInfos = new ArrayList<CPUInfo>();
//        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
//        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
//        long[] lods = centralProcessor.getSystemCpuLoadTicks();
//        for(int idx=0; idx<lods.length; idx++) {
//            CPUInfo cpuInfo = new CPUInfo();
//            cpuInfo.setCpuNum("cpu" + (idx));
//            try{ cpuInfo.setLoad(Double.parseDouble(String.format("%.2f", lods[idx] * 100))); } catch(Exception e) {};
//            cpuInfo.setUsage(getCPUUsage(systemInfo));
//            cpuInfos.add(cpuInfo);
//        }
//        systemInfo.setCpu(cpuInfos);
//    }
//
//    private static double getCPUUsage(SystemInfo systemInfo) {
//        HardwareAbstractionLayer hal = systemInfo.getHardware();
//        CentralProcessor cpu = hal.getProcessor();
//        long[] prevTicks = new long[CentralProcessor.TickType.values().length];
//        double cpuLoad = cpu.getSystemCpuLoadBetweenTicks( prevTicks ) * 100;
//        return cpuLoad;
//    }

    // 초기엔 정확하나 시간이 지남에 따라 사용률이 누적되는 것으로 보임.
    public static void initCpuInfo(SystemInfo systemInfo) {
        List<CPUInfo> cpuInfos = new ArrayList<CPUInfo>();
        CPUInfo cPUInfo = new CPUInfo();
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        cPUInfo.setUsage( (osBean.getSystemCpuLoad() * 100) );
        cpuInfos.add(cPUInfo);
        systemInfo.setCpu(cpuInfos);
    }

    public static void initMemoryInfo(SystemInfo systemInfo) {
        List<MemoryInfo> memoryInfos = new ArrayList<MemoryInfo>();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        GlobalMemory memory = hal.getMemory();
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setTotal(memory.getTotal());
        memoryInfo.setFree(memory.getAvailable());
        memoryInfo.setUsed(memoryInfo.getTotal() - memoryInfo.getFree());
        memoryInfo.setUsage((memoryInfo.getUsed() / (double) memory.getTotal()) * 100);
        memoryInfos.add(memoryInfo);
        systemInfo.setMem(memoryInfos);
    }

    public static void initDiskInfo(SystemInfo systemInfo) {
        List<DiskInfo> diskInfos = new ArrayList<DiskInfo>();
        OperatingSystem os = systemInfo.getOperatingSystem();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        DiskInfo diskInfo = new DiskInfo();
        for(OSFileStore fs : fsArray) {
            diskInfo.setTotal(diskInfo.getTotal()+fs.getTotalSpace());
            diskInfo.setFree(diskInfo.getFree()+fs.getUsableSpace());
            diskInfo.setUsableSpace(diskInfo.getUsableSpace()+fs.getUsableSpace());
            diskInfo.setUsed(diskInfo.getTotal()-diskInfo.getFree()); // 사용한 량..
        }
        double use = 100.0 * diskInfo.getUsed() / diskInfo.getTotal();
        diskInfo.setUse(use);
        diskInfos.add(diskInfo);
        systemInfo.setDisk(diskInfos);
    }

//    private static void initDiskInfo(SystemInfo systemInfo) {
//
//        List<DiskInfo> diskInfos = new ArrayList<DiskInfo>();
//
//        DiskInfo diskInfo = new DiskInfo();
//
//        File root = new File("/");
////        System.out.println(String.format("Total space: %.2f GB", (double)root.getTotalSpace() /1073741824));
////        System.out.println(String.format("Free space: %.2f GB", (double)root.getFreeSpace() /1073741824));
////        System.out.println(String.format("Usable space: %.2f GB", (double)root.getUsableSpace() /1073741824));
//        diskInfo.setTotal((long)root.getTotalSpace()/1073741824);
//        diskInfo.setFree((long)root.getFreeSpace()/1073741824);
//        diskInfo.setUsableSpace((long)root.getUsableSpace()/1073741824);
//        diskInfo.setUsed(diskInfo.getTotal()-diskInfo.getFree()); // 사용한 량..
//
//        double use = 100.0 * diskInfo.getUsed() / diskInfo.getTotal();
//        diskInfo.setUse(use);
//        diskInfos.add(diskInfo);
//        systemInfo.setDisk(diskInfos);
//
//    }

    public static void initNetworkInfo(SystemInfo systemInfo) {
        List<NetworkInfo> networkInfos = new ArrayList<NetworkInfo>();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        //NetworkIF[] networkIFS = hal.getNetworkIFs();
        List<NetworkIF> networkIFS = hal.getNetworkIFs();
        double recvBytesTotal = 0;
        double sentBytesTotal = 0;
        for(NetworkIF net : networkIFS) {
            recvBytesTotal += net.getBytesRecv();
            sentBytesTotal += net.getBytesSent();
        }
        NetworkInfo networkInfo = new NetworkInfo();
        //networkInfo.setRx(recvBytesTotal / 1000 / 1000); // MiB
        //networkInfo.setTx(sentBytesTotal / 1000 / 1000); // MiB
        networkInfo.setReceived(recvBytesTotal); // MiB
        networkInfo.setSent(sentBytesTotal); // MiB

        initNetworkInfo2(systemInfo,networkInfos);

        networkInfos.add(networkInfo);
        systemInfo.setNetwork(networkInfos);
    }

    private static HashMap<String,ThroughputCalculator> txMap = new HashMap<String,ThroughputCalculator>();
    private static HashMap<String,ThroughputCalculator> rxMap = new HashMap<String,ThroughputCalculator>();

    public static void initNetworkInfo2(SystemInfo systemInfo, List<NetworkInfo> networkInfos) {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        double tx = 0;
        double rx = 0;
        for (NetworkIF intf : hal.getNetworkIFs()) {
            intf.updateAttributes();
            ThroughputCalculator txThroughput = txMap.get(intf);
            if(txThroughput == null) {
                txThroughput = new ThroughputCalculator();
                txMap.put(intf.getName(), txThroughput);
            }
            txThroughput.setTotalHandledData(intf.getBytesSent());
            tx=tx+txThroughput.getBitThroughput();

            ThroughputCalculator rxThroughput = rxMap.get(intf);
            if(rxThroughput == null) {
                rxThroughput = new ThroughputCalculator();
                rxMap.put(intf.getName(), rxThroughput);
            }
            rxThroughput.setTotalHandledData(intf.getBytesRecv());
            rx=rx+rxThroughput.getBitThroughput();
        }

        NetworkInfo networkInfo = new NetworkInfo();
        //networkInfo.setRx(recvBytesTotal / 1000 / 1000); // MiB
        //networkInfo.setTx(sentBytesTotal / 1000 / 1000); // MiB
        networkInfo.setRx(rx);
        networkInfo.setTx(tx);

        networkInfo.setSent(rx);
        networkInfo.setReceived(tx);

        networkInfos.add(networkInfo);
        systemInfo.setNetwork(networkInfos);
    }

    public static void initNetworkInfo3(List<NetworkIF> networkIFS, SystemInfo systemInfo, int intervalMillis) {
        setNetworkInfo3(systemInfo.getNetwork(), networkIFS);
        try {
            Thread.sleep(intervalMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setNetworkInfo3(systemInfo.getNetworkAfter(), networkIFS);
    }

    private static void setNetworkInfo3(List<NetworkInfo> networkInfos, List<NetworkIF> networkIFS) {
        for (NetworkIF net : networkIFS) {
            net.updateAttributes();
//            NetworkInterface itfc = net.queryNetworkInterface();
            NetworkInfo networkInfo = new NetworkInfo();
            networkInfo.setInterfaceName(net.getDisplayName());
            networkInfo.setReceived(net.getBytesRecv());
            networkInfo.setSent(net.getBytesSent());
            networkInfo.setSpeed(net.getSpeed());
//            networkInfo.setVirtual(itfc.isVirtual());
//            try {
//                networkInfo.setLoopback(itfc.isLoopback());
//                networkInfo.setUp(itfc.isUp());
//                networkInfo.setPointToPoint(itfc.isPointToPoint());
//            } catch (SocketException e) {
//                e.printStackTrace();
//            }
            networkInfo.setIpv4addr(net.getIPv4addr());
            networkInfo.setIpv6addr(net.getIPv6addr());
            networkInfo.setMacaddr(net.getMacaddr());
            networkInfos.add(networkInfo);
        }
    }

    public static SystemInfo getSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        initHostName(systemInfo);
        initOSInfo(systemInfo);
        initCpuInfo(systemInfo);
        initMemoryInfo(systemInfo);
        initDiskInfo(systemInfo);
        initNetworkInfo(systemInfo);
        systemInfo.setRunTime(System.currentTimeMillis());
        return systemInfo;
    }

}