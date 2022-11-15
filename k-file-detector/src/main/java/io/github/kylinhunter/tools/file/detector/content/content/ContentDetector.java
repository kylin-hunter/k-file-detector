package io.github.kylinhunter.tools.file.detector.content.content;

import io.github.kylinhunter.tools.file.detector.content.bean.DetectConext;
import io.github.kylinhunter.tools.file.detector.content.constant.ContentDetectType;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

public interface ContentDetector {
    ContentDetectType getContentDetectType();

    FileType[] detect(DetectConext detectConext);

    FileType[] detect(byte[] bytes);
}
