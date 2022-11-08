package com.kylinhunter.plat.file.detector.detect;

import com.kylinhunter.plat.file.detector.detect.bean.DetectConext;
import com.kylinhunter.plat.file.detector.selector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.magic.bean.ReadMagic;
import com.kylinhunter.plat.file.detector.selector.MagicSelector;

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
    private final MixContentDetector mixContentDetector;
    private final MagicSelector magicSelector;

    public DetectResult detect(ReadMagic readMagic) {
        DetectConext detectConext = new DetectConext(readMagic);
        magicDetector.detect(detectConext);
        mixContentDetector.detect(detectConext);
        return magicSelector.selectBest(detectConext);
    }
}
