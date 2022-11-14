package io.github.kylinhunter.plat.file.detector.content.content;

import io.github.kylinhunter.plat.file.detector.content.bean.DetectConext;
import io.github.kylinhunter.plat.file.detector.content.constant.ContentDetectType;
import io.github.kylinhunter.plat.file.detector.file.bean.FileType;

public interface ContentDetector {
    ContentDetectType getContentDetectType();

    FileType[] detect(DetectConext detectConext);

    FileType[] detect(byte[] bytes);
}
