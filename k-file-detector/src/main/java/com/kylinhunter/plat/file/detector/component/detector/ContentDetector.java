package com.kylinhunter.plat.file.detector.component.detector;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.common.component.C;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-07 16:43
 **/
@Data
@C
public class ContentDetector implements Detector {
    private final MSOfficeDetector msOfficeDetector;

    @Override
    public DetectConext detect(DetectConext detectConext) {
        return detectConext;
    }
}
