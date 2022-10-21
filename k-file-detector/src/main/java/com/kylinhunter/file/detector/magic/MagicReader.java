package com.kylinhunter.file.detector.magic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.file.detector.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:45
 **/
@Slf4j
public class MagicReader {
    /**
     * @param content content
     * @return java.lang.String
     * @title read
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:38
     */
    public static String read(byte[] content) {
        return read(content, null, false);
    }

    /**
     * @param content content
     * @return java.lang.String
     * @title read
     * @description
     * @author BiJi'an
     * @date 2022-10-04 17:09
     */
    public static String read(byte[] content, String fileName, boolean accurate) {
        if (content != null && content.length > 0) {

            int magicLen = calMacgiclen(fileName, content.length, accurate);
            return StringUtil.bytesToHexStringV2(content, 0, magicLen);
        }
        return StringUtils.EMPTY;
    }

    /**
     * @param file file
     * @return java.lang.String
     * @title read magic
     * @description
     * @author BiJi'an
     * @date 2022-10-02 16:47
     */

    public static String read(MultipartFile file) {
        return read(file, false);

    }

    /**
     * @param file     file
     * @param accurate accurate
     * @return java.lang.String
     * @title read
     * @description
     * @author BiJi'an
     * @date 2022-10-04 17:09
     */
    public static String read(MultipartFile file, boolean accurate) {

        try (InputStream is = file.getInputStream()) {
            return read(is, file.getOriginalFilename(), file.getSize(), accurate);
        } catch (Exception e) {
            log.error("read magic number error", e);
        }
        return StringUtil.EMPTY;
    }

    /**
     * @param file file
     * @return java.lang.String
     * @title read
     * @description
     * @author BiJi'an
     * @date 2022-10-04 10:31
     */
    public static String read(File file) {
        return read(file, false);
    }

    /**
     * @param file     file
     * @param accurate accurate
     * @return java.lang.String
     * @title read
     * @description
     * @author BiJi'an
     * @date 2022-10-04 17:10
     */
    public static String read(File file, boolean accurate) {
        try (InputStream is = new FileInputStream(file)) {
            return read(is, file.getName(), file.length(), accurate);
        } catch (Exception e) {
            log.error("read magic number error", e);
        }
        return StringUtil.EMPTY;
    }

    /**
     * @param inputStream inputStream
     * @param fileSize    fileSize
     * @return java.lang.String
     * @title read
     * @description
     * @author BiJi'an
     * @date 2022-10-04 10:31
     */
    private static String read(InputStream inputStream, String fileName, long fileSize, boolean accurate)
            throws IOException {

        byte[] b = new byte[calMacgiclen(fileName, fileSize, accurate)];
        int len = inputStream.read(b);
        if (len > 0) {
            return StringUtil.bytesToHexStringV2(b, 0, len);
        }
        return StringUtil.EMPTY;
    }

    /**
     * @param fileName fileName
     * @param fileSize fileSize
     * @param accurate accurate
     * @return int
     * @title calMacgiclen
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:36
     */
    private static int calMacgiclen(String fileName, long fileSize, boolean accurate) {
        MagicManager magicManager = MagicConfigManager.getMagicManager();
        int magicLen = 0;
        if (accurate && !StringUtils.isEmpty(fileName)) {
            String extension = FilenameUtils.getExtension(fileName);
            if (!StringUtils.isEmpty(extension)) {
                ExtensionMagics extensionMagics = magicManager.getExtensionMagics(extension);
                if (extensionMagics != null) {
                    magicLen = extensionMagics.getMagicMaxLength();
                }
            }

        }
        if (magicLen <= 0) {
            magicLen = magicManager.getMagicMaxLength();
        }

        if (fileSize > 0 && magicLen > fileSize) {
            magicLen = (int) fileSize;
        }
        return magicLen;
    }

}