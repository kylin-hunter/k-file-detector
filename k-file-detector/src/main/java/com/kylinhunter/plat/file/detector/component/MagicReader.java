package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.config.FileType;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.common.util.StringUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:45
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class MagicReader {
    private final FileTypeManager fileTypeManager;
    private final MagicManager magicManager;

    /**
     * @param content content
     * @return java.lang.String
     * @title read
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:38
     */
    public String read(byte[] content) {
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
    public String read(byte[] content, String fileName, boolean accurate) {
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

    public String read(MultipartFile file) {
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
    public String read(MultipartFile file, boolean accurate) {

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
    public String read(File file) {
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
    public String read(File file, boolean accurate) {
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
    public String read(InputStream inputStream, String fileName, long fileSize, boolean accurate) {

        try {
            byte[] b = new byte[calMacgiclen(fileName, fileSize, accurate)];
            int len = inputStream.read(b);
            if (len > 0) {
                return StringUtil.bytesToHexStringV2(b, 0, len);
            }
            return StringUtil.EMPTY;
        } catch (IOException e) {
            throw new DetectException("read error", e);
        }
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
    private int calMacgiclen(String fileName, long fileSize, boolean accurate) {
        int magicLen = 0;
        if (accurate && !StringUtils.isEmpty(fileName)) {
            String extension = FilenameUtils.getExtension(fileName);
            Set<FileType> fileTypes = this.fileTypeManager.getFileTypesByExtension(extension);

            for (FileType fileType : fileTypes) {
                if (fileType.getMagicMaxLength() > magicLen) {
                    magicLen = fileType.getMagicMaxLength();
                }
            }
        }

        if (magicLen <= 0) {
            magicLen = this.magicManager.getMagicMaxLength();
        }

        if (fileSize > 0 && magicLen > fileSize) {
            magicLen = (int) fileSize;
        }
        return magicLen;
    }

}
