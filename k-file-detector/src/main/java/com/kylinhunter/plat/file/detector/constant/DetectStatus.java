package com.kylinhunter.plat.file.detector.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Getter
@AllArgsConstructor
public enum DetectStatus {
    UNKNOWN(1, "unknown"),
    HIGH(2, "detect unique possible file type"),
    MIDDLE(3, "detect more than one  possible file type"),
    LOW(4, "disguise extension ");

    private final int code;
    private final String name;

}