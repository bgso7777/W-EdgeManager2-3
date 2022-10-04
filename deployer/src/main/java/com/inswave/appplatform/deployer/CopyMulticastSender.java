package com.inswave.appplatform.deployer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.file.Path;
import java.nio.file.Paths;

//@Component
@Slf4j
public class CopyMulticastSender {

    //    @PostConstruct
    //    public void postConstruct() {
    public static void main(String[] args) throws IOException {

        DatagramPacket packet = null;
        MulticastSocket socket = null;

        // configured value.
        int chunkSize = 1024 * 50;  //  First 8byte is reserved. : 8 + 50 = 58 byte
        String address = "127.0.0.1";
        int port = 9006;
        int useValueMbps = 5; // user configured 10MBps of Bandwidth : 10Mbps = 1.192092895507813 MB/s
        Path testFile = Paths.get("C:", "temp", "test", "CollabNetSubversionEdge-5.2.4_setup-x86_64_0_0.exe");

        // calculated value.
        int sendByteCountPerSec = (useValueMbps * 1000000) / 8;    // 1 MB => 8.3388608 Mbps
        int sendChunkCountPerSec = sendByteCountPerSec / chunkSize;
        int goalInterval = 1000 / sendChunkCountPerSec;

        File file = testFile.toFile();
        long fileLen = file.length();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");


        StopWatch fileWatch = new StopWatch("File전송 타이머 생성");
        fileWatch.start("파일전송");
        try {
            socket = new MulticastSocket();
            System.out.println("서버 생성 성공.");
            InetAddress inetAddress = InetAddress.getByName(address); // 멀티캐스트 방식으로 서버 주소를 설정함.
            byte[] readByte = new byte[chunkSize];
            int sendSeq = 0;
            while (true) {
                StopWatch packetPerWatch = new StopWatch("Packet 타이머 생성");
                packetPerWatch.start("패킷 생성 및 전송 시작");

                int seekPos = (int) randomAccessFile.getFilePointer();
                int dataLen = randomAccessFile.read(readByte, 0, chunkSize);
                byte[] data = concat(intToByteArray(sendSeq), intToByteArray(dataLen), readByte);
                packet = new DatagramPacket(data, data.length, inetAddress, port);
                socket.send(packet);

                if (dataLen < chunkSize) {
                    System.out.println("전송완료");
                    break;
                }
                sendSeq++;
                packetPerWatch.stop();
                long loadTime = packetPerWatch.getLastTaskTimeMillis();
                log.info("{}. packet: {}, dataLen: {}, data: {}byte, , progress: {}/{}, time: {}", sendSeq, packet.getLength(), dataLen, readByte.length, seekPos / chunkSize, fileLen / chunkSize, loadTime);
                if(goalInterval > loadTime) {
                    Thread.sleep(goalInterval - loadTime);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            randomAccessFile.close();
            fileWatch.stop();
            log.info("{}", fileWatch.prettyPrint());
        }
    }

    public static byte[] concat(byte[]... args) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (byte[] bytes : args) {
            outputStream.write(bytes);
        }
        return outputStream.toByteArray();
    }

    public static int byteArrayToInt(byte[] b) {
        return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + (b[3] & 0xFF);
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }

}


