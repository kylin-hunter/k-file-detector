package com.kylinhunter.file.detector.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Getter
@AllArgsConstructor
public enum SafeStatus {
    UNKNOWN(1, "unknown"),
    SAFE(2, "safe"),
    DANGEROUS_EXTENSION(3, "dangerous extension"),
    DISGUISE_WARN(4, "disguise extension warn"),
    DISGUISE(5, "disguise extension "),
    DANGEROUS_CONTENT(6, "dangerous content");

    private final int code;
    private final String name;

}