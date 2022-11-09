package com.kylinhunter.plat.file.detector.content;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.content.bean.DetectConext;
import com.kylinhunter.plat.file.detector.content.content.ContentDetector;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;
import com.kylinhunter.plat.file.detector.magic.bean.ReadMagic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-07 16:43
 **/
@Data
@C
public class MixContentDetector {

    private Map<String, ContentDetector> contentDetectors = Maps.newHashMap();

    public MixContentDetector(List<ContentDetector> detectors) {
        for (ContentDetector contentDetector : detectors) {
            Magic magic = contentDetector.getMagic();
            if (magic != null) {
                contentDetectors.put(magic.getNumber(), contentDetector);
            }

        }
    }

    public DetectConext detect(ReadMagic readMagic) {
        DetectConext detectConext = new DetectConext(readMagic);

        if (readMagic.isDetectContent()) {
            for (Magic magic : readMagic.getContentMagics()) {
                ContentDetector contentDetector = contentDetectors.get(magic.getNumber());
                if (contentDetector != null) {
                    contentDetector.detect(detectConext);
                }
            }
        }

        return detectConext;
    }
}
