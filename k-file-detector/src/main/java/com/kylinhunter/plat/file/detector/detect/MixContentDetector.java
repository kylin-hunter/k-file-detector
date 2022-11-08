package com.kylinhunter.plat.file.detector.detect;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.detect.bean.DetectConext;
import com.kylinhunter.plat.file.detector.detect.content.ContentDetector;
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
public class MixContentDetector implements Detector {
    private Map<String, ContentDetector> contentDetectors = Maps.newHashMap();

    public MixContentDetector(List<ContentDetector> detectors) {
        for (ContentDetector contentDetector : detectors) {
            Magic magic = contentDetector.getMagic();
            if (magic != null) {
                contentDetectors.put(magic.getNumber(), contentDetector);
            }

        }
    }

    @Override
    public DetectConext detect(DetectConext detectConext) {

        ReadMagic readMagic = detectConext.getReadMagic();
        if (readMagic.hasContentMagics()) {
            for (Magic magic : readMagic.getContentMagics()) {
                ContentDetector contentDetector = contentDetectors.get(magic.getNumber());
                contentDetector.detect(detectConext);
            }
        }

        return detectConext;
    }
}
