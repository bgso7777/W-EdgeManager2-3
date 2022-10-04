package com.inswave.appplatform.deployer;

import com.inswave.appplatform.Constants;
import com.inswave.appplatform.deployer.cache.DeploySegmentCache;
import com.inswave.appplatform.deployer.dao.DeployTransferDao;
import com.inswave.appplatform.deployer.dao.DeployTransferHistoryDao;
import com.inswave.appplatform.deployer.domain.Deploy;
import com.inswave.appplatform.deployer.domain.DeployTransfer;
import com.inswave.appplatform.deployer.domain.DeployTransferHistory;
import com.inswave.appplatform.util.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class MulticastSender extends Thread {

    private DeployManager            deployManager;
    private DeployTransferDao        deployTransferDao;
    private DeployTransferHistoryDao deployTransferHistoryDao;
    private DeploySegmentCache       deploySegmentCache;

    private       List<DeployTransferHistory> deployTransferHistoryList;
    private       Deploy                      deploy;
    private       DeployTransfer              deployTransfer;
    private       Integer                     chunkCount           = 0;
    private       Integer                     chunkPos             = 0;
    private       Integer                     repeatCount          = 0;
    private       Integer                     repeatPos            = 0;
    private final int                         datagramMaxSize      = 65527; // The field size sets a theoretical limit of 65,535 bytes (8-byte header + 65,527 bytes of data) for a UDP datagram.
    private final int                         datagramReservedSize = 128;
    private       boolean                     stop;
    private       MulticastSocket             multicastSocket;
    private       DatagramSocket              broadcastSocket;
    private       InetAddress                 inetAddress;

    private String multicastGroupAddress;
    private int    multicastGroupPort;
    private int    dataByteSize;
    private int    bandwidthKbps;
    private Path   targetPath;
    private File   targetFile;
    private long   targetFileSize;
    private int    sendByteCountPerSec;
    private double sendChunkCountPerSec;
    private double goalInterval;

    private byte[] reservedFileChecksum;
    private byte[] reservedFileName;

    public MulticastSender(DeployManager deployManager,
                           Deploy deploy,
                           DeployTransfer deployTransfer,
                           DeploySegmentCache deploySegmentCache) throws IOException {
        this.deployManager = deployManager;
        this.deploy = deploy;
        this.deployTransfer = deployTransfer;
        this.deploySegmentCache = deploySegmentCache;

        // Configured value.
        multicastGroupAddress = deployTransfer.getMulticastGroupAddress();
        multicastGroupPort = deployTransfer.getMulticastGroupPort();
        inetAddress = InetAddress.getByName(multicastGroupAddress);
        bandwidthKbps = deployTransfer.getBandwidthKbps();              // User configured 10MBps of Bandwidth : 10Mbps = 1.192092895507813 MB/s
        dataByteSize = Constants.TAG_DEPLOYER_CHUNK_BYTE_SIZE; // 60KB로 고정, datagramMaxSize - datagramReservedSize;          // Reserved bytes first.
        targetPath = deployManager.getFile(deploy.getDeployId());

        // Calculated value.
        sendByteCountPerSec = (bandwidthKbps * 1000) / 8;        // 1 MB => 8.3388608 Mbps
        sendChunkCountPerSec = sendByteCountPerSec / dataByteSize;      // Send chunk count per seconds.
        goalInterval = (int) 1000 / sendChunkCountPerSec;                     // Send chunk goal interval. (milliseconds)

        targetFile = targetPath.toFile();                               // Send File.
        targetFileSize = targetFile.length();                           // Send File Size (Byte Count).
        //        chunkCount = (int) Math.ceil(targetFileSize / datagramMaxSize);
        chunkCount = (int) Math.ceil((double) targetFileSize / (double) dataByteSize);
        multicastSocket = new MulticastSocket();
        broadcastSocket = new DatagramSocket();
        //        broadcastSocket.setBroadcast(true);

        //        reservedFileChecksum = ChecksumUtil.md5(Files.readAllBytes(targetFile.toPath()));
    }

    @SneakyThrows public void run() {
        StopWatch watchPerRepeat = new StopWatch("watchPerRepeat");
        watchPerRepeat.start("watchPerRepeat start");

        reservedFileChecksum = ChecksumUtil.md5(targetFile.toPath());
        reservedFileName = deploy.getFileName().getBytes("UTF-8");

        deployTransfer.setTransferCount(chunkCount);
        deployTransferDao.save(deployTransfer);

        repeatCount = deployTransfer.getRepeatCount();
        repeatPos = 0;
        for (int i = 0, iL = repeatCount; i < iL; i++) {
            deployTransferHistoryList = new ArrayList<>();
            if (stop) {
                break;
            }
            try {
                for (int c = 0, cL = chunkCount; c < cL; c++) {
                    chunkPos = c;
                    StopWatch watchPerPacket = new StopWatch("watchPerPacket");
                    watchPerPacket.start("watchPerPacket start");
                    if (stop) {
                        break;
                    } else {
                        sendData(c, true);
                    }
                    watchPerPacket.stop();
                    double loadTime = watchPerPacket.getLastTaskTimeMillis();
                    if (goalInterval > loadTime) {
                        Thread.sleep((long) (goalInterval - loadTime));
                    }
                    log.info("repeatPos : {}/{}, seq : {}/{}, isMulticast : {}", repeatPos + 1, repeatCount, c + 1, chunkCount, deployTransfer.getMulticastGroupAddress().equals(deployManager.getMulticastIp()));
                }
                deployTransferHistoryDao.saveAll(deployTransferHistoryList);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            repeatPos++;
            log.debug("repeatPos : {}, loadtime : {}", i, watchPerRepeat.prettyPrint());
        }

        broadcastSocket.close();
        multicastSocket.close();

        watchPerRepeat.stop();
        log.debug("packetPerSend : {}", watchPerRepeat.prettyPrint());

        if (!stop) {
            deployTransfer.setFinishedDate(new Date());
            deployTransferDao.save(deployTransfer);
        }

        deployManager.remove(deployTransfer.getDeployTransferId());
    }

    private void sendBegin(int tryCnt) {

        // TODO. 시작패킷 3번? 송신
    }

    public byte[] getData(int pos) throws IOException {
        return FileUtil.getBytes(targetFile, (long) pos * dataByteSize, dataByteSize);
    }

    public void sendData(int pos, boolean makeHistory) throws IOException, InterruptedException, NoSuchAlgorithmException {
        byte[] fileData = new byte[dataByteSize];
        byte[] fileDataCompressed;
        byte[] fileDataTarget;
        boolean useCompress = false;
        int fileDataLength = FileUtil.getBytes(targetFile, (long) pos * dataByteSize, dataByteSize, fileData);

        try {
            deploySegmentCache.put(deployTransfer.getDeployId(), pos, Crypto.base64Encode(fileData));    // 캐시 추가
        } catch (IOException e) {
            log.info("add cache failed : convert base64... ");
        }

        fileDataTarget = fileData;
        if (deployTransfer.getUseCompress()) {
            fileDataCompressed = DeflateUtil.compress(fileData);
            if (fileDataCompressed.length < fileData.length) {
                fileDataTarget = fileDataCompressed;
                useCompress = true;
            }
        }

        byte[] reserved = concat(
        getSignal(),                                                                    //.  1  signal          0,1,2
        intToBytes(repeatPos),                                                          //.  4  repeatPos
        intToBytes(repeatCount),                                                        //  4  repeatCount
        new byte[] { (byte) 1 },                                                        //  1  timeoutType
        intToBytes(5),                                                            //  4  timeoutValue
        longToBytes(deployTransfer.getDeployId()),                                      //  8  deployId
        longToBytes(deployTransfer.getDeployTransferId()),                              //  8  deployTransferId
        new byte[] { (byte) (0) },                                                      //  1  hashType : MD5 (0), Sha256 (1).. etc
        reservedFileChecksum,                                                           // 16  orgHash
        intToBytes(reservedFileName.length),                                            //  4  orgFileNameLength : 00 00 00 01 (hex)
        reservedFileName,                                                               //  N  orgFileName
        intToBytes(deploy.getDeployType()),                                             //  4  deployType
        intToBytes(chunkCount),                                                         //  4  totalCount
        intToBytes(pos),                                                                //.  4  segmentIndex
        ChecksumUtil.md5(Arrays.copyOf(fileDataTarget, useCompress ? fileDataTarget.length : fileDataLength)),  // 16  segmentHash
        intToBytes(useCompress ? fileDataTarget.length : fileDataLength),               //.  4  segmentDataLength
        new byte[] { (byte) (useCompress ? 1 : 0) },                                    //.  1  segmentCompress
        DateUtil.getDate(deployTransfer.getCreatedDate(), "yyyyMMddHHmmss")
                .getBytes(StandardCharsets.UTF_8),                                      // 14  deployTime (yyyyMMddHHmmss)
        DateUtil.getDate(deployTransfer.getExecuteInstallDate(), "yyyyMMddHHmmss")
                .getBytes(StandardCharsets.UTF_8)                                       // 14  installTime (yyyyMMddHHmmss)
        );

        log.debug("reserved hex : {}", bytesToHex(reserved));

        byte[] data = concat(reserved, fileDataTarget);
        if (deployTransfer.getMulticastGroupAddress().equals(deployManager.getMulticastIp())) {
            multicastSocket.send(toPacket(data));
        } else {
            broadcastSocket.send(toPacket(data));
        }

        if (makeHistory) {
            deployTransferHistoryList.add(DeployTransferHistory.builder()
                                                               .deployTransferId(deployTransfer.getDeployTransferId())
                                                               .repeatPos(repeatPos + 1)
                                                               .seq(pos)
                                                               .sentDate(new Date())
                                                               .chunkSizeOrg((long) fileDataLength)
                                                               .chunkSizeSent(useCompress ? (long) fileDataTarget.length : (long) fileDataLength)
                                                               .useCompress(useCompress)
                                                               .build());
        } else {
            broadcastSocket.close();
            multicastSocket.close();
        }
    }

    public Integer getStatus() {
        if (isStop()) {
            return Constants.TAG_DEPLOYER_STATUS_STOPPING;  // 진행중
        } else {
            return Constants.TAG_DEPLOYER_STATUS_SENDING;  // 중지중
        }
    }

    public Deploy getDeploy() {
        return deploy;
    }

    public DeployTransfer getDeployTransfer() {
        return deployTransfer;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public Integer getRepeatPos() {
        return repeatPos;
    }

    public Integer getChunkCount() {
        return chunkCount;
    }

    public Integer getChunkPos() {
        return chunkPos;
    }

    public boolean isStop() {
        return stop;
    }

    private DatagramPacket toPacket(byte[] data) {
        return new DatagramPacket(data, data.length, inetAddress, multicastGroupPort);
    }

    public static byte[] concat(byte[]... args) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (byte[] bytes : args) {
            outputStream.write(bytes);
        }
        return outputStream.toByteArray();
    }

    public static int byteArrayToInt(byte[] b) {
        if (b.length == 4) {
            return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + (b[3] & 0xFF);
        } else if (b.length == 3) {
            return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8);
        } else if (b.length == 2) {
            return (b[0] << 24) + ((b[1] & 0xFF) << 16);
        } else {
            return (b[0] << 24);
        }
    }

    public static byte[] intToBytes(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder str = new StringBuilder();
        for (byte b : bytes) {
            str.append(String.format("%02X ", b));
        }
        return str.toString();
    }

    public void setStop() {
        this.stop = true;
    }

    private byte[] getSignal() {
        if (chunkPos == 0) {
            return new byte[] { (byte) 0 };
        } else if (chunkPos < chunkCount) {
            return new byte[] { (byte) 1 };
        } else {
            return new byte[] { (byte) 2 };
        }
    }

    public void setDeployTransferDao(DeployTransferDao deployTransferDao) {
        this.deployTransferDao = deployTransferDao;
    }

    public void setDeployTransferHistoryDao(DeployTransferHistoryDao deployTransferHistoryDao) {
        this.deployTransferHistoryDao = deployTransferHistoryDao;
    }
}


