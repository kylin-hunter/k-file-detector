package io.github.kylinhunter.tools.file.detector.detect;

import io.github.kylinhunter.tools.file.detector.common.component.C;
import io.github.kylinhunter.tools.file.detector.content.MixContentDetector;
import io.github.kylinhunter.tools.file.detector.content.bean.DetectConext;
import io.github.kylinhunter.tools.file.detector.detect.bean.DetectResult;
import io.github.kylinhunter.tools.file.detector.magic.bean.ReadMagic;

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
     * @return io.github.kylinhunter.plat.file.detector.detect.bean.DetectResult
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
