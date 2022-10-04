package com.inswave.appplatform.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;

public class ByteUtil {
    public static byte[] toBytesFixedLength(String value, int fixedLength) {
        return Arrays.copyOfRange(value.getBytes(StandardCharsets.UTF_8), 0, fixedLength);
    }

    public static byte[] toBytes(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }

    public static byte[] toBytes(long x) {
        //noinspection Since15
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long toLong(byte[] bytes) {
        //noinspection Since15
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static int toInt(byte[] b) {
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

    public static byte[] toByte(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            bytes = bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return bytes;
    }

    public static byte[] toByte(List<Path> files) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        files.forEach(path -> {
            try {
                outputStream.write(Files.readAllBytes(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return outputStream.toByteArray();
    }

    public static Object toObject(byte[] value) {
        ByteArrayInputStream bis = new ByteArrayInputStream(value);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return o;
    }

    public static byte[][] divide(byte[] source, int divideSize) {
        byte[][] ret = null;
        if (source.length <= divideSize) {
            ret = new byte[1][source.length];
            ret[0] = source;
            return ret;
        } else {
            int lastLength = source.length % divideSize;
            ret = new byte[(int) Math.ceil(source.length / (double) divideSize)][divideSize];
            int start = 0;
            for (int i = 0; i < ret.length; i++) {
                if (i < ret.length - 1) {
                    ret[i] = Arrays.copyOfRange(source, start, start + divideSize);
                } else {
                    ret[i] = Arrays.copyOfRange(source, start, start + lastLength);
                }
                start += divideSize;
            }
            return ret;
        }
    }

    public static byte[] concat(byte[]... args) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (byte[] bytes : args) {
            outputStream.write(bytes);
        }
        return outputStream.toByteArray();
    }

    public static byte[] concat(byte[] a, byte[] b) throws IOException {
        return concat(a, b);
    }

    public static BitSet base64ToBitSet(String base64str) throws IOException {
        byte[] byteArray = Crypto.base64Decode(base64str);
        return BitSet.valueOf(byteArray);
    }

    public static String base64ToBinaryString(String base64str) throws IOException {
        BitSet b = base64ToBitSet(base64str);
        final StringBuilder buffer = new StringBuilder(b.length());
        IntStream.range(0, b.length()).mapToObj(i -> b.get(i) ? "1" : "0").forEach(buffer::append);
        return buffer.toString();
    }
}
