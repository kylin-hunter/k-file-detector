package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.ComponentFactory;
import com.kylinhunter.plat.file.detector.component.MagicManager;
import com.kylinhunter.plat.file.detector.component.MagicReader;
import com.kylinhunter.plat.file.detector.component.MagicSelector;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
public class FileDetector {

    private static MagicManager magicManager = ComponentFactory.get(MagicManager.class);
    private static MagicReader magicReader = ComponentFactory.get(MagicReader.class);
    private static MagicSelector magicSelector = ComponentFactory.get(MagicSelector.class);

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
        String possibleMagicNumber = magicReader.read(content, fileName, false);
        return detect(possibleMagicNumber, fileName);

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
        String possibleMagicNumber = magicReader.read(file, false);
        return detect(possibleMagicNumber, file.getOriginalFilename());
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
        String possibleMagicNumber = magicReader.read(file, false);
        return detect(possibleMagicNumber, file.getName());
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
        String possibleMagicNumber = magicReader.read(input, fileName, fileSize, false);
        return detect(possibleMagicNumber, fileName);
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

    private static DetectResult detect(String possibleMagicNumber, String fileName) {
        DetectConext detectConext = new DetectConext(possibleMagicNumber, fileName);
        detectConext.setDetectedMagics(magicManager.detect(detectConext.getPossibleMagicNumber()));
        return magicSelector.selectBest(detectConext);

    }

}
