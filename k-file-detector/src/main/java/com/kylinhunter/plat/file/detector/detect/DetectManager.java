package com.kylinhunter.plat.file.detector.detect;

import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.content.MixContentDetector;
import com.kylinhunter.plat.file.detector.content.bean.DetectConext;
import com.kylinhunter.plat.file.detector.detect.bean.DetectResult;
import com.kylinhunter.plat.file.detector.magic.bean.ReadMagic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-08 17:09
 **/
@C
@Data
public class DetectManager {
    private final MixContentDetector mixContentDetector;
    private final DetectResultOptimizer detectResultOptimizer;

    /**
     * @param readMagic readMagic
     * @return com.kylinhunter.plat.file.detector.detect.bean.DetectResult
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-11-09 14:58
     */
    public DetectResult detect(ReadMagic readMagic) {
        DetectConext detectConext = mixContentDetector.detect(readMagic);
        return detectResultOptimizer.optimize(detectConext);
    }
}
