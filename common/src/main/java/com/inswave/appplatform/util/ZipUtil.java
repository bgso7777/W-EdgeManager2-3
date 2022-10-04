package com.inswave.appplatform.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
 * 1. ZipUtil 생성자에 zip을 생성할 전체 경로를 넣는다.
 * 2. ZipUtil.insertZipEntry 를 통해 추가할 파일을 추가한다.
 * 3. ZipUtil.endZip 을 통해 작업을 종료한다.
 * */
public class ZipUtil {
    private FileOutputStream fos;
    private ZipOutputStream  zos;

    public ZipUtil(Path zipFullPath) throws FileNotFoundException {
        fos = new FileOutputStream(zipFullPath.toFile());
        zos = new ZipOutputStream(fos);
    }

    public void insertZipEntry(Path src, Path dest) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(src.toFile());
            ZipEntry zipEntry = new ZipEntry(dest.toString());
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                zos.closeEntry();
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void endZip() throws IOException {
        zos.close();
        fos.close();
    }

    public static void unzip(final byte[] data, final Path destPath) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data))) {
            ZipEntry entry;
            while (Objects.nonNull(entry = zipInputStream.getNextEntry())) {
                final Path toPath = destPath.resolve(entry.getName());
                if (entry.isDirectory()) {
                    if (Files.notExists(toPath)) Files.createDirectories(toPath);
                } else {
                    if (Files.notExists(toPath.getParent())) Files.createDirectories(toPath);
                    Files.copy(zipInputStream, toPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public static List<Path> unzip(final Path zipPath, final Path destPath) throws IOException {
        List<Path> pathList = new ArrayList<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry entry;
            while (Objects.nonNull(entry = zipInputStream.getNextEntry())) {
                final Path toPath = destPath.resolve(entry.getName());
                if (entry.isDirectory()) {
                    if (Files.notExists(toPath)) Files.createDirectories(toPath);
                } else {
                    if (Files.notExists(toPath.getParent())) Files.createDirectories(toPath);
                    Files.copy(zipInputStream, toPath, StandardCopyOption.REPLACE_EXISTING);
                    pathList.add(toPath);
                }
            }
        }
        return pathList;
    }

    public static Path zip(Path src, Path desc) throws IOException {
        List<Path> zipTargetPaths = Files.walk(src)
                                         .filter(path -> !Files.isDirectory(path))
                                         .collect(Collectors.toList());
        Path zipPath = desc.resolve(StringUtil.getHexCode(src.toString()) + ".zip");
        ZipUtil zip = new ZipUtil(zipPath);
        zipTargetPaths.forEach(f -> {
            zip.insertZipEntry(f, src.relativize(f));
        });
        zip.endZip();
        return zipPath;
    }

}
