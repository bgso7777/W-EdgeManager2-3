package com.inswave.appplatform.deployer;

import com.inswave.appplatform.util.ChecksumUtil;
import com.inswave.appplatform.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

@Slf4j
//@Component
public class CopyMulticastReceiver extends Thread {

//    @PostConstruct
    public void postConstruct() {
        new CopyMulticastReceiver().start();
    }

    public static void main(String[] args) {
        Thread thread = new CopyMulticastReceiver();
        thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(thread::interrupt));
    }

    @Override
    public void run() {
        DatagramPacket packet = null;
        MulticastSocket socket = null;
        int chunkSize = 1024 * 50;
        String address = "239.107.107.55";
        int port = 8089;
        try {
            socket = new MulticastSocket(port);
            System.out.println("클라이언트 생성." + address + ":" + String.valueOf(port));

            // 그룹에 조인(라우터가 보냄)
            InetAddress inetAddress = InetAddress.getByName(address); // 멀티 캐스트를 위한 아이피 설정  //"224.128.1.5"
            socket.joinGroup(inetAddress);

            byte[] buf = new byte[64 * 1024 * 1024];

            packet = new DatagramPacket(buf, buf.length);

            while (true) {
                // 패킷 수신
                int endIdx = 0;
                socket.receive(packet);
                byte[] packetData = packet.getData();

                long deployId = MulticastSender.bytesToLong(Arrays.copyOfRange(packetData, 0, 8));
                long deployTransferId = MulticastSender.bytesToLong(Arrays.copyOfRange(packetData, 8, 16));
                int hashType = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, 8, 9));
                byte[] orgHash = Arrays.copyOfRange(packetData, 9, 25);
                int orgFileNameLength = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, 25, 29));
                endIdx = 29 + orgFileNameLength;
                String orgFileName = new String(Arrays.copyOfRange(packetData, 29, endIdx));
                int deployType = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 4));
                int totalCount = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 4));
                int segmentIndex = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 4));
                byte[] segmentHash = Arrays.copyOfRange(packetData, endIdx, endIdx += 16);
                int segmentDataLength = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 4));
                int segmentCompress = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 1));
                String deployTime = new String(Arrays.copyOfRange(packetData, endIdx, endIdx+14));
                String installTime = new String(Arrays.copyOfRange(packetData, endIdx, endIdx+14));

                byte[] data = Arrays.copyOfRange(packetData, endIdx, segmentDataLength);

                log.info("Multicast : {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}", deployId, deployTransferId, hashType, orgHash, orgFileName, deployType, totalCount, segmentIndex, segmentHash, segmentDataLength, segmentCompress, deployTime, installTime);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
