package com.kylinhunter.plat.file.detector.bean;

import java.util.Set;

import com.kylinhunter.plat.file.detector.constant.SecurityStatus;
import com.kylinhunter.plat.file.detector.magic.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-01 22:37
 **/
@Data
public class DetectResult {

    private final String fileName;
    private Set<Magic> detectedMagics;
    private Set<String> explicitFileType;


    public SecurityStatus getSafeStatus() {
        return SecurityStatus.UNKNOWN;
    }

}
