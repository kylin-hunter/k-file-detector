package io.github.kylinhunter.tools.file.detector;

import java.io.File;
import java.io.InputStream;

import io.github.kylinhunter.tools.file.detector.common.component.CF;
import io.github.kylinhunter.tools.file.detector.detect.DetectManager;
import io.github.kylinhunter.tools.file.detector.detect.bean.DetectResult;
import io.github.kylinhunter.tools.file.detector.magic.MagicReader;
import io.github.kylinhunter.tools.file.detector.magic.bean.ReadMagic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
public class FileDetector {

    private static final DetectManager DETECT_MANAGER = CF.get(DetectManager.class);
    private static final MagicReader MAGIC_READER = CF.get(MagicReader.class);

    /**
     * @param content content
     * @return io.github.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectResult detect(byte[] content) {
        ReadMagic readMagic = MAGIC_READER.read(content, "", false);
        return detect(readMagic);

    }

    /**
     * @param content  content
     * @param fileName fileName
     * @return io.github.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectResult detect(byte[] content, String fileName) {
        ReadMagic readMagic = MAGIC_READER.read(content, fileName, false);
        return detect(readMagic);

    }

    /**
     * @param file file
     * @return io.github.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectResult detect(File file) {
        ReadMagic readMagic = MAGIC_READER.read(file, false);
        return detect(readMagic);
    }

    /**
     * @param input input
     * @return io.github.kylinhunter.file.detector.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 02:35
     */
    public static DetectResult detect(InputStream input) {
        return detect(input, "", -1L);

    }

    /**
     * @param input    input
     * @param fileName fileName
     * @return io.github.kylinhunter.file.detector.bean.DetectResult
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
     * @return io.github.kylinhunter.file.detector.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 02:39
     */
    public static DetectResult detect(InputStream input, String fileName, long fileSize) {
        ReadMagic readMagic = MAGIC_READER.read(input, fileName, fileSize, false);
        return detect(readMagic);
    }

    /**
     * @param readMagic readMagic
     * @return io.github.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */

    private static DetectResult detect(ReadMagic readMagic) {

        return DETECT_MANAGER.detect(readMagic);

    }

}
