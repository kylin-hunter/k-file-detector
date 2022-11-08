package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.selector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.CF;
import com.kylinhunter.plat.file.detector.magic.bean.ReadMagic;
import com.kylinhunter.plat.file.detector.detect.MixDetector;
import com.kylinhunter.plat.file.detector.magic.MagicReader;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
public class FileDetector {

    private static final MixDetector MIX_DETECTOR = CF.get(MixDetector.class);
    private static final MagicReader MAGIC_READER = CF.get(MagicReader.class);

    /**
     * @param content  content
     * @param fileName fileName
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectResult detect(byte[] content, String fileName) {
        ReadMagic readMagic = MAGIC_READER.read(content, fileName, false);
        return detect(readMagic, fileName);

    }

    /**
     * @param file file
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */

    public static DetectResult detect(MultipartFile file) {
        ReadMagic readMagic = MAGIC_READER.read(file, false);
        return detect(readMagic, file.getOriginalFilename());
    }

    /**
     * @param file file
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectResult detect(File file) {
        ReadMagic readMagic = MAGIC_READER.read(file, false);
        return detect(readMagic, file.getName());
    }

    /**
     * @param input    input
     * @param fileName fileName
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 02:35
     */
    public static DetectResult detect(InputStream input, String fileName) {
        return detect(input, fileName, -1L);

    }

    /**
     * @param input    input
     * @param fileName fileName
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 02:39
     */
    public static DetectResult detect(InputStream input, String fileName, long fileSize) {
        ReadMagic readMagic = MAGIC_READER.read(input, fileName, fileSize, false);
        return detect(readMagic, fileName);
    }

    /**
     * @param readMagic readMagic
     * @param fileName  fileName
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */

    private static DetectResult detect(ReadMagic readMagic, String fileName) {

        return MIX_DETECTOR.detect(readMagic, fileName);

    }

}
