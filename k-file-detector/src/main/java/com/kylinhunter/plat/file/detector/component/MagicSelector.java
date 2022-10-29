package com.kylinhunter.plat.file.detector.component;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 14:08
 **/
@Slf4j
@Component
public class MagicSelector {

    /**
     * @param detectConext detectConext
     * @return com.kylinhunter.plat.file.detector.config.bean.Magic
     * @title checkExtensionConsistent
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:35
     */
    public DetectResult selectBest(DetectConext detectConext) {

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
