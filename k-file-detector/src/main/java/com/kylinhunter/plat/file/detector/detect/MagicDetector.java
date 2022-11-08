package com.kylinhunter.plat.file.detector.detect;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.detect.bean.DetectConext;
import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.magic.MagicManager;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-08 16:52
 **/
@Data
@C
public class MagicDetector implements Detector {

    private final MagicManager magicManager;

    /**
     * @param detectConext detectConext
     * @return com.kylinhunter.plat.file.detector.detect.bean.DetectConext
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-26 00:56
     */
    public DetectConext detect(DetectConext detectConext) {
        String possibleMagicNumber = detectConext.getPossibleMagicNumber();
        List<Magic> detectedMagics = Lists.newArrayList();

        if (!StringUtils.isEmpty(possibleMagicNumber)) {
            for (Magic magic : magicManager.getAllMagics()) {
                String number = magic.getNumber();

                int magicIndex;
                int offset = magic.getOffset() * 2;
                for (magicIndex = 0; magicIndex < number.length(); magicIndex++) {
                    if (offset < possibleMagicNumber.length()) {
                        char c = number.charAt(magicIndex);
                        if (c != MagicManager.MAGIC_SKIP_X) {
                            char c2 = possibleMagicNumber.charAt(offset);

                            if (c == MagicManager.MAGIC_SKIP_N) {
                                if (!Character.isDigit(c2)) {
                                    break;
                                }
                            } else {
                                if (c != c2) {
                                    break;
                                }
                            }

                        }
                    } else {
                        break;
                    }
                    offset++;

                }
                if (magicIndex == number.length()) {
                    detectedMagics.add(magic);
                }

            }

        }
        detectConext.setDetectedMagics(detectedMagics);

        return detectConext;
    }
}
