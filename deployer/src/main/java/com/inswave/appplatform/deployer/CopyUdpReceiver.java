package com.inswave.appplatform.deployer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

@Slf4j
//@Component
public class CopyUdpReceiver extends Thread {
//    @PostConstruct
    public void postConstruct() {
        new CopyUdpReceiver().start();
    }

    public static void main(String[] args) {
        Thread thread = new CopyUdpReceiver();
        thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(thread::interrupt));
    }

    @Override
    public void run() {
        DatagramPacket packet = null;
        DatagramSocket socket = null;
        int port = 8090;
        try {
            socket = new DatagramSocket(port);
            System.out.println("클라이언트 생성. : " + String.valueOf(port));

            // 그룹에 조인(라우터가 보냄)
            byte[] buf = new byte[64 * 1024 * 1024];

            packet = new DatagramPacket(buf, buf.length);

            while (true) {
                // 패킷 수신
                int endIdx = 0;
                socket.receive(packet);
                byte[] packetData = packet.getData();
//                log.info("packetData Arrays.toString : {}", Arrays.toString(packetData));
                log.info("packetData Arrays.copyOfRange : {}", Arrays.copyOfRange(packetData, 0, 1024));
//
//                long deployId = MulticastSender.bytesToLong(Arrays.copyOfRange(packetData, 0, 8));
//                long deployTransferId = MulticastSender.bytesToLong(Arrays.copyOfRange(packetData, 8, 16));
//                int hashType = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, 8, 9));
//                byte[] orgHash = Arrays.copyOfRange(packetData, 9, 25);
//                int orgFileNameLength = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, 25, 29));
//                endIdx = 29 + orgFileNameLength;
//                String orgFileName = new String(Arrays.copyOfRange(packetData, 29, endIdx));
//                int deployType = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 4));
//                int totalCount = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 4));
//                int segmentIndex = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 4));
//                byte[] segmentHash = Arrays.copyOfRange(packetData, endIdx, endIdx += 16);
//                int segmentDataLength = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 4));
//                int segmentCompress = MulticastSender.byteArrayToInt(Arrays.copyOfRange(packetData, endIdx, endIdx += 1));
//                String deployTime = new String(Arrays.copyOfRange(packetData, endIdx, endIdx+14));
//                String installTime = new String(Arrays.copyOfRange(packetData, endIdx, endIdx+14));
//
//                byte[] data = Arrays.copyOfRange(packetData, endIdx, segmentDataLength);
//
//                log.info("UDP : {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}", deployId, deployTransferId, hashType, orgHash, orgFileName, deployType, totalCount, segmentIndex, segmentHash, segmentDataLength, segmentCompress, deployTime, installTime);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
