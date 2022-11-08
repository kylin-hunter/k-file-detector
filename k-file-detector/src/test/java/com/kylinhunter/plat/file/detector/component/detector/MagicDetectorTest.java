package com.kylinhunter.plat.file.detector.component.detector;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.common.component.CF;
import com.kylinhunter.plat.file.detector.component.bean.ReadMagic;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

class MagicDetectorTest {
    private final MagicDetector magicDetector = CF.get(MagicDetector.class);

    @Test
    void detect() {
        ReadMagic readMagic = new ReadMagic("25504446");
        DetectConext detectConext = new DetectConext(readMagic);
        magicDetector.detect(detectConext);
        List<Magic> detectMagics = detectConext.getDetectedMagics();
        Assertions.assertTrue(detectMagics.size() > 0);
        Assertions.assertTrue(detectMagics.get(0).getFileTypesWrapper().getExtensions().contains("pdf"));
    }
}