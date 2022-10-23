package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectOption;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.bean.FileSecurity;
import com.kylinhunter.plat.file.detector.magic.Magic;
import com.kylinhunter.plat.file.detector.magic.MagicManager;
import com.kylinhunter.plat.file.detector.magic.MagicReader;
import com.kylinhunter.plat.file.detector.type.FileTypeManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
public class FileDetector {

    private static final FileTypeManager fileTypeManager = CommonManager.getFileTypeManager();
    private static final MagicManager magicManager = CommonManager.getMagicManager();

    /**
     * @param content  content
     * @param fileName fileName
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:42
     */
    public static DetectResult detect(byte[] content, String fileName) {
        return detect(content, fileName, DetectOption.getDefault());
    }

    /**
     * @param content  content
     * @param fileName fileName
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectResult detect(byte[] content, String fileName, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(content, fileName, false);
        return detect(possibleMagicNumber, fileName, detectOption);

    }

    /**
     * @param file file
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:42
     */
    public static DetectResult detect(MultipartFile file) {
        return detect(file, DetectOption.getDefault());
    }

    /**
     * @param file file
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */

    public static DetectResult detect(MultipartFile file, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(file, false);
        return detect(possibleMagicNumber, file.getOriginalFilename(), detectOption);
    }

    /**
     * @param file file
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:43
     */
    public static DetectResult detect(File file) {
        return detect(file, DetectOption.getDefault());

    }

    /**
     * @param file file
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectResult detect(File file, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(file, false);
        return detect(possibleMagicNumber, file.getName(), detectOption);
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
        return detect(input, fileName, -1L, DetectOption.getDefault());

    }

    /**
     * @param input        input
     * @param fileName     fileName
     * @param detectOption detectOption
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 02:39
     */
    public static DetectResult detect(InputStream input, String fileName, long fileSize, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(input, fileName, fileSize, false);
        return detect(possibleMagicNumber, fileName, detectOption);
    }

    /**
     * @param possibleMagicNumber possibleMagicNumber
     * @param fileName            fileName
     * @return com.kylinhunter.file.detector.bean.DetectResult
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */

    private static DetectResult detect(String possibleMagicNumber, String fileName, DetectOption detectOption) {
        DetectResult detectResult = new DetectResult(fileName);
        DetectConext detectConext = new DetectConext(possibleMagicNumber, fileName);
        Set<Magic> detectedMagics = magicManager.detect(detectConext);
        detectResult.setDetectedMagics(detectedMagics);

        FileSecurity fileSecurity = detectFileSecurity(fileName, detectedMagics, detectOption);
        return detectResult;

    }

    /**
     * @param fileName       fileName
     * @param detectedMagics detectedMagics
     * @param detectOption   detectOption
     * @return com.kylinhunter.plat.file.detector.bean.FileSecurity
     * @title delectDisguiseExtension
     * @description
     * @author BiJi'an
     * @date 2022-10-22 21:49
     */

    private static FileSecurity detectFileSecurity(String fileName, Set<Magic> detectedMagics,

                                                   DetectOption detectOption) {

        FileSecurityDetector.checkExtensionConsistent(fileName, detectedMagics, detectOption);
        return null;
    }

}
