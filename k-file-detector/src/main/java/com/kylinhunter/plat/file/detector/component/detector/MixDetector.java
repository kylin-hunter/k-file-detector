package com.kylinhunter.plat.file.detector.component.detector;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.component.bean.ReadMagic;
import com.kylinhunter.plat.file.detector.component.magic.MagicSelector;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-08 17:09
 **/
@C
@Data
public class MixDetector {
    private final MagicDetector magicDetector;
    private final ContentDetector contentDetector;
    private final MagicSelector magicSelector;

    public DetectResult detect(ReadMagic readMagic, String fileName) {
        DetectConext detectConext = new DetectConext(readMagic, fileName);
        magicDetector.detect(detectConext);
        contentDetector.detect(detectConext);
        return magicSelector.selectBest(detectConext);
    }
}
