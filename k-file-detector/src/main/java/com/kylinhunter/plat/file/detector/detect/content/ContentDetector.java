package com.kylinhunter.plat.file.detector.detect.content;

import com.kylinhunter.plat.file.detector.detect.bean.DetectConext;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

public interface ContentDetector {
    Magic getMagic();

    DetectConext detect(DetectConext detectConext);
}
