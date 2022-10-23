package com.kylinhunter.plat.file.detector;

import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.kylinhunter.plat.file.detector.bean.DetectOption;
import com.kylinhunter.plat.file.detector.magic.Magic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
class FileSecurityDetector {

    /**
     * @param fileName       fileName
     * @param detectedMagics detectedMagics
     * @param detectOption   detectOption
     * @return com.kylinhunter.plat.file.detector.magic.Magic
     * @title checkExtensionConsistent
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:35
     */
    public static Magic checkExtensionConsistent(String fileName, Set<Magic> detectedMagics,
                                                 DetectOption detectOption) {
        Magic consistentMagic = null;
        String extension = FilenameUtils.getExtension(fileName);

        if (!StringUtils.isEmpty(extension)) { // check extension consistentif(m
            for (Magic magic : detectedMagics) {
                if (magic.getExtensions().contains(extension)) {

                    if (consistentMagic == null) {
                        consistentMagic = magic;

                    } else {
                        if (consistentMagic.getMagicLength() < magic.getMagicLength()) {
                            consistentMagic = magic;
                        }
                    }
                }
            }

        }
        return consistentMagic;

    }

}
