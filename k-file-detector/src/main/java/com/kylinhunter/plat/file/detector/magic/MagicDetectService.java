package com.kylinhunter.plat.file.detector.magic;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.type.FileType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
public class MagicDetectService {

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
        List<Magic> detectedMagics = detectConext.getDetectedMagics();
        if (!CollectionUtils.isEmpty(detectedMagics)) { // check extension consistent
            Collections.sort(detectedMagics);
            String extension = detectConext.getExtension();
            Magic bestMagic = null;
            FileType bestFileType = null;

            for (Magic magic : detectedMagics) {
                if (bestMagic == null) {
                    bestMagic = magic;
                }
                for (FileType fileType : magic.getFileTypes()) {
                    if (fileType.extensionEquals(extension)) {
                        if (bestFileType == null) {
                            bestFileType = fileType;
                        }
                    }
                    detectResult.addPossibleFileType(fileType);
                }

            }
            if (bestFileType == null) {
                if (bestMagic != null) {
                    bestFileType = bestMagic.getFileType();
                }
            }

            detectResult.setBestMagic(bestMagic);
            detectResult.setBestFileType(bestFileType);

        }

        return detectResult;
    }

}
