package com.kylinhunter.plat.file.detector.content.content;

import com.kylinhunter.plat.file.detector.content.bean.DetectConext;
import com.kylinhunter.plat.file.detector.content.constant.ContentDetectType;
import com.kylinhunter.plat.file.detector.file.bean.FileType;

public interface ContentDetector {
    ContentDetectType getContentDetectType();

    FileType[] detect(DetectConext detectConext);

    FileType[] detect(byte[] bytes);
}
