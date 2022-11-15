package io.github.kylinhunter.tools.file.detector.magic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import io.github.kylinhunter.tools.file.detector.common.component.C;
import io.github.kylinhunter.tools.file.detector.common.util.FilenameUtil;
import io.github.kylinhunter.tools.file.detector.common.util.HexUtil;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.FileTypeManager;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;
import io.github.kylinhunter.tools.file.detector.magic.bean.ReadMagic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:45
 **/
@Slf4j
@RequiredArgsConstructor
@C
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
    public ReadMagic read(byte[] content) {
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
    public ReadMagic read(byte[] content, String fileName, boolean accurate) {
        if (content != null && content.length > 0) {

            int magicLen = calMacgiclen(fileName, content.length, accurate);
            String possibleNumber = HexUtil.toString(content, 0, magicLen);
            ReadMagic readMagic = new ReadMagic(fileName, possibleNumber);
            detectContentSupport(readMagic);
            if (readMagic.isDetectContent()) {
                readMagic.setContent(content);
            }
            return readMagic;
        }
        return null;
    }

    /**
     * @param file file
     * @return java.lang.String
     * @title read
     * @description
     * @author BiJi'an
     * @date 2022-10-04 10:31
     */
    public ReadMagic read(File file) {
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
    public ReadMagic read(File file, boolean accurate) {
        try (InputStream is = new FileInputStream(file)) {
            return read(is, file.getName(), file.length(), accurate);
        } catch (Exception e) {
            log.error("read magic number error", e);
        }
        return null;
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
    public ReadMagic read(InputStream inputStream, String fileName, long fileSize, boolean accurate) {

        try {
            byte[] headContent = new byte[calMacgiclen(fileName, fileSize, accurate)];
            int len = inputStream.read(headContent);
            if (len > 0) {
                String possibleMagic = HexUtil.toString(headContent, 0, len);
                ReadMagic readMagic = new ReadMagic(fileName, possibleMagic);
                detectContentSupport(readMagic);
                if (readMagic.isDetectContent()) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    IOUtils.copy(inputStream, byteArrayOutputStream);
                    byte[] remainedContent = byteArrayOutputStream.toByteArray();
                    byte[] content = new byte[len + remainedContent.length];
                    System.arraycopy(headContent, 0, content, 0, len);
                    System.arraycopy(remainedContent, 0, content, len, remainedContent.length);
                    readMagic.setContent(content);
                }
                return readMagic;
            }
            return null;
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
            String extension = FilenameUtil.getExtension(fileName);
            Set<FileType> fileTypes = this.fileTypeManager.getFileTypesByExtension(extension);
            for (FileType fileType : fileTypes) {
                if (fileType.getMagicMaxLengthWithOffset() > magicLen) {
                    magicLen = fileType.getMagicMaxLengthWithOffset();
                }
            }
        }

        if (magicLen <= 0) {
            magicLen = this.magicManager.getMagicMaxLengthWitOffset();
        }

        if (fileSize > 0 && magicLen > fileSize) {
            magicLen = (int) fileSize;
        }
        return magicLen;
    }

    /**
     * @param readMagic readMagic
     * @return boolean
     * @title detectContentSupport
     * @description
     * @author BiJi'an
     * @date 2022-11-07 14:42
     */
    @SuppressWarnings("unchecked")
    private void detectContentSupport(ReadMagic readMagic) {
        String possibleNumber = readMagic.getPossibleMagicNumber();
        List<Magic> detectedMagics = magicManager.detect(possibleNumber);
        if (detectedMagics.size() > 0) {
            readMagic.setDetectedMagics(detectedMagics);

            Set<Magic> contentSupportMagics = magicManager.getContentSupportMagics();
            for (Magic magic : contentSupportMagics) {
                if (magic.getOffset() == 0 && possibleNumber.startsWith(magic.getNumber())) {
                    readMagic.addContentMagic(magic);
                    readMagic.setDetectContent(true);
                }
            }
        } else {
            readMagic.setDetectedMagics(Collections.EMPTY_LIST);
            readMagic.setContentMagics(Collections.EMPTY_LIST);
            readMagic.setDetectContent(true);
        }

    }

}
