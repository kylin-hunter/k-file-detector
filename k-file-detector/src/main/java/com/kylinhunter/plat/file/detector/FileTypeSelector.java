package com.kylinhunter.plat.file.detector;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.magic.Magic;
import com.kylinhunter.plat.file.detector.type.FileType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
class FileTypeSelector {

    /**
     * @param detectConext detectConext
     * @return com.kylinhunter.plat.file.detector.magic.Magic
     * @title checkExtensionConsistent
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:35
     */
    public static DetectResult selectBest(DetectConext detectConext) {

        DetectResult detectResult = new DetectResult(detectConext);

        String extension = detectConext.getExtension();
        List<Magic> detectedMagics = detectConext.getDetectedMagics();
        if (!CollectionUtils.isEmpty(detectedMagics)) { // check extension consistent
            detectedMagics.sort(FileTypeSelector::compare);
            //            detectedMagics.forEach(System.out::println);
            for (Magic magic : detectedMagics) {
                detectResult.trySetBestMagic(magic);
                Set<FileType> fileTypes = magic.getFileTypes();
                if (fileTypes != null) {
                    for (FileType fileType : fileTypes) {
                        if (fileType.getExtension().equals(extension)) {
                            detectResult.trySetBestFileType(fileType);
                            detectResult.setAllBestFileTypes(fileTypes);
                        }
                        detectResult.addPossibleFileType(fileType);
                    }
                }
            }
            if (detectResult.getBestFileType() == null) {
                Magic bestMagic = detectResult.getBestMagic();
                detectResult.trySetBestFileType(bestMagic.getFileTypes().iterator().next());
                detectResult.setAllBestFileTypes(bestMagic.getFileTypes());

            }

        }

        return detectResult;
    }

    /**
     * @param m1 m1
     * @param m2 m2
     * @return int
     * @title compare
     * @description
     * @author BiJi'an
     * @date 2022-10-25 01:56
     */
    private static int compare(Magic m1, Magic m2) {
        return m2.getMagicLength() - m1.getMagicLength();
    }
}
