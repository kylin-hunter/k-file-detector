package io.github.kylinhunter.tools.file.detector.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-06 22:31
 **/
public class ZipUtil {
    /**
     * @param files   files
     * @param zipFile zipFile
     * @return java.io.File
     * @title doZip
     * @description
     * @author BiJi'an
     * @date 2022-11-06 23:37
     */
    public static File zip(List<File> files, File zipFile) throws IOException {
        if (!zipFile.getParentFile().exists()) {
            FileUtils.forceMkdirParent(zipFile);
        }
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(zipFile)) {
            zos.setUseZip64(Zip64Mode.AsNeeded);
            for (File file : files) {
                ZipArchiveEntry entry = new ZipArchiveEntry(file, file.getName());
                zos.putArchiveEntry(entry);
                try (InputStream inputStream = new FileInputStream(file)) {
                    IOUtils.copy(inputStream, zos);
                    zos.closeArchiveEntry();
                }
            }
            zos.finish();
        } catch (Exception e) {
            throw new IOException("zip error", e);
        }
        return zipFile;
    }

    /**
     * @param file      file
     * @param unzipPath unzipPath
     * @return void
     * @title unzip
     * @description
     * @author BiJi'an
     * @date 2022-11-07 00:00
     */
    public static void unzip(File file, File unzipPath) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        try {
            byte[] buffer = new byte[IOUtils.DEFAULT_BUFFER_SIZE];
            Enumeration<ZipArchiveEntry> zipFileEntries = zipFile.getEntries();
            while (zipFileEntries.hasMoreElements()) {
                ZipArchiveEntry entry = zipFileEntries.nextElement();
                if (!entry.isDirectory()) {
                    File outputFile = new File(unzipPath + File.separator + entry.getName());
                    if (!outputFile.getParentFile().exists()) {
                        FileUtils.forceMkdirParent(outputFile);
                    }
                    try (InputStream input = zipFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(outputFile)) {
                        IOUtils.copyLarge(input, fos, buffer);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("unzip error", e);
        } finally {
            IOUtils.closeQuietly(zipFile);
        }
    }

    public static void unzip(byte[] content, File unzipPath) throws IOException {

        try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(content))) {
            byte[] buffer = new byte[IOUtils.DEFAULT_BUFFER_SIZE];

            ZipArchiveEntry entry;

            while ((entry = zipArchiveInputStream.getNextZipEntry()) != null) {
                if (!entry.isDirectory()) {
                    File outputFile = new File(unzipPath + File.separator + entry.getName());
                    if (!outputFile.getParentFile().exists()) {
                        FileUtils.forceMkdirParent(outputFile);
                    }
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        IOUtils.copyLarge(zipArchiveInputStream, fos, buffer);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("unzip error", e);
        }
    }
}