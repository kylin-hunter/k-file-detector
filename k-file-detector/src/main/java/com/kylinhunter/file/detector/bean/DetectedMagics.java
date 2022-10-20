package com.kylinhunter.file.detector.bean;

import java.util.Set;

import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.signature.config.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-01 22:37
 **/
@Data
public class DetectedMagics {

    private Set<Magic> detectedMagics;
    private Set<String> detectedExtensions;

    public void addDetectedMagics(Magic magic) {
        if (detectedMagics == null) {
            detectedMagics = Sets.newHashSet();
        }
        detectedMagics.add(magic);

        if (detectedExtensions == null) {
            detectedExtensions = Sets.newHashSet();
        }
        this.detectedExtensions.addAll(magic.getExtensions());

    }

    public boolean isDetected() {
        return detectedMagics != null && detectedMagics.size() > 0;
    }
}
