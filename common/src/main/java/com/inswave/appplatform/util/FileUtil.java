package com.inswave.appplatform.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    /**
     * 파일에서 읽은 정보를 리턴
     *
     * @return
     */
    public static StringBuffer getFileToStringBuffer(String fileName) throws FileNotFoundException {
        StringBuffer sb = new StringBuffer();
        BufferedReader pin = null;
        FileReader fin = null;

        try {
            fin = new FileReader(fileName);
            pin = new BufferedReader(fin);
            String temp = "";
            while ((temp = pin.readLine()) != null)
                sb.append(temp + "\n");
        } catch (FileNotFoundException fe) {
            throw fe;
        } catch (Exception e) {
        } finally {
            try {
                if (pin != null) pin.close();
            } catch (Exception ce) {
            }
            try {
                if (fin != null) fin.close();
            } catch (Exception ce) {
            }
        }
        return sb;
    }

    public static StringBuffer getFileToStringBuffer(String fileName, String encode) throws FileNotFoundException {
        StringBuffer sb = new StringBuffer("");

        InputStreamReader in = null;
        BufferedReader pin = null;
        try {
            in = new InputStreamReader(new FileInputStream(fileName), encode);
            pin = new BufferedReader(in);
            String temp;
            while ((temp = pin.readLine()) != null) {
                sb.append(new String(temp.getBytes(), encode) + "\n");
            }
        } catch (FileNotFoundException fe) {
            throw fe;
        } catch (Exception e) {
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (pin != null)
                try {
                    pin.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

        return sb;
    }

    public static byte[] getBytesFromFile(String fileName) throws IOException {

        File file = new File(fileName);
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        }

        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public static void writeFile(byte[] data, String fileName) throws IOException {
        OutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
    }

    public static void saveStringBufferToFileAppend(String fileName, StringBuffer sb) throws Exception {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(fileName, true));
            printWriter.print(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printWriter != null) printWriter.close();
            } catch (Exception e1) {
            }
        }
    }

    public static void saveStringBufferToFile(String fileName, StringBuffer sb) throws Exception {
        OutputStreamWriter out = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(fileName,true);
            out = new OutputStreamWriter(fos, "UTF-8");

            if (sb == null || sb.toString().equals("null"))
                out.write("");
            else
                out.write(sb.toString());
            out.flush();
        } catch (FileNotFoundException e) {
            fos = new FileOutputStream(fileName);
            out = new OutputStreamWriter(fos, "UTF-8");

            if (sb == null || sb.toString().equals("null"))
                out.write("");
            else
                out.write(sb.toString());

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e1) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e2) {
            }
        }
        //System.out.println("saveStringBufferToFile : "+fileName);
    }

    public static void saveStringBufferToFile2(String fileName, StringBuffer sb) throws Exception {
        OutputStreamWriter out = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(fileName, false);
            out = new OutputStreamWriter(fos, "UTF-8");

            if (sb == null || sb.toString().equals("null"))
                out.write("");
            else
                out.write(sb.toString());
            out.flush();
        } catch (FileNotFoundException e) {
            fos = new FileOutputStream(fileName);
            out = new OutputStreamWriter(fos, "UTF-8");

            if (sb == null || sb.toString().equals("null"))
                out.write("");
            else
                out.write(sb.toString());

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e1) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e2) {
            }
        }
        //System.out.println("saveStringBufferToFile : "+fileName);
    }

    /**
     * 파일 이름과 객체를 받아 파일에 저장
     *
     * @param fileName
     * @param obj
     * @throws Exception
     */
    public static void saveObjectToFile(final String fileName, final Object obj) throws Exception {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(obj);
            oos.flush();
        } catch (Exception e) {
            //System.out.println("saveObjectToFile exception");
            throw e;
        } finally {
            try {
                if (oos != null) oos.close();
            } catch (Exception e) {
                System.out.println("saveObjectToFile oos exception");
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (Exception e) {
                System.out.println("saveObjectToFile fos exception");
                e.printStackTrace();
            }
        }
        //System.out.println("saveObjectToFile : "+fileName);
    }

    /**
     * 파일을 읽어 Object 로 변환 후 리턴.
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Object getFileToObject(String fileName) throws Exception {
        Object obj = null;

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);

            obj = ois.readObject();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (Exception e) {
            }
            try {
                if (fis != null) fis.close();
            } catch (Exception e) {
            }
        }
        return obj;
    }

    public static StringBuffer getJarResource(Class cls, String resource) {
        StringBuffer sb = new StringBuffer("");
        try {
            InputStream inputStream = cls.getResourceAsStream(resource);
            BufferedInputStream breader = new BufferedInputStream(inputStream);
            while (breader.available() > 0)
                sb.append((char) breader.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static void writeObject(Object obj, String fileName) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(obj);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll(List<Path> files) {
        files.forEach(path -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static List<Path> split(Path file) throws IOException {
        return split(file, 1024 * 1024 * 5);    // Default 5MB
    }

    public static List<Path> split(Path file, int splitByteSize) {
        List<Path> resultList = new ArrayList<>();
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(file.toFile(), "r");
            FileChannel channel = rf.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(splitByteSize);

            int fileSeq = 0;
            while (channel.read(buffer) > 0) {
                File chunkFile = new File(file.toAbsolutePath() +
                        ".part." + StringUtils.leftPad(String.valueOf(fileSeq), 5, "0"));
                FileChannel chunkFileChannel = new FileOutputStream(chunkFile).getChannel();

                buffer.flip();
                chunkFileChannel.write(buffer);
                buffer.clear();

                chunkFileChannel.close();
                fileSeq++;
                resultList.add(chunkFile.toPath());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rf != null) {
                    rf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public static Path merge(Path newPath, List<Path> files, boolean removeSplittedFiles) {
        try {
            Files.deleteIfExists(newPath);
            Files.createDirectories(newPath.getParent());
            Files.createFile(newPath);
            for (int i = 0; i < files.size(); i++) {
                Files.write(newPath, Files.readAllBytes(files.get(i)), StandardOpenOption.APPEND, StandardOpenOption.SYNC);
                if (removeSplittedFiles) {
                    Files.deleteIfExists(files.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newPath;
    }

    public static void main(String[] argv) {
        try {
            String message = "abcd";
            String fileName = "C:/Temp/test.txt";

            String fileNameMin = fileName+".min";
            File minFile = new File(fileNameMin);
            if(minFile.exists()) {
                if(minFile.length()>message.length())
                    FileUtil.saveStringBufferToFile2(fileNameMin, new StringBuffer(message));
            } else {
                FileUtil.saveStringBufferToFile2(fileNameMin, new StringBuffer(message));
            }

            String fileNameMax = fileName+".max";
            File maxFile = new File(fileNameMax);
            if(maxFile.exists()) {
                if(maxFile.length()<message.length())
                    FileUtil.saveStringBufferToFile2(fileNameMax, new StringBuffer(message));
            } else {
                FileUtil.saveStringBufferToFile2(fileNameMax, new StringBuffer(message));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized byte[] getBytes(File file, long offset, int size) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        byte[] data = new byte[size];
        randomAccessFile.seek(offset);
        int len = randomAccessFile.read(data, 0, size);
        randomAccessFile.close();
        data = Arrays.copyOfRange(data, 0, len);
        return data;
    }

    public static synchronized int getBytes(File file, long offset, int size, byte[] fillArray) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.seek(offset);
        int length = randomAccessFile.read(fillArray, 0, size);
        randomAccessFile.close();
        return length;
    }
}
