package com.kylinhunter.plat.file.detector.detect;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.detect.bean.DetectConext;
import com.kylinhunter.plat.file.detector.common.component.CF;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;
import com.kylinhunter.plat.file.detector.magic.bean.ReadMagic;

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