package com.kylinhunter.plat.file.detector.content.content;

import com.kylinhunter.plat.file.detector.content.bean.DetectConext;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

public interface ContentDetector {
    Magic getMagic();

    DetectConext detect(DetectConext detectConext);
}
