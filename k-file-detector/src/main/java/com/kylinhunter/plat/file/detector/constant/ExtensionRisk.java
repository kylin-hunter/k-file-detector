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
public enum ExtensionRisk {
    LOW(1, "LOW"),
    MIDDLE(2, "MIDDLE"),
    HIGH(3, "HIGH");

    private final int code;
    private final String name;

}