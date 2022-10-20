package com.kylinhunter.file.detector.bean;

import java.util.Set;
import java.util.StringJoiner;

import com.kylinhunter.file.detector.constant.SafeStatus;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-01 22:37
 **/
@Data
public class DetectResult {

    private SafeStatus safeStatus = SafeStatus.UNKNOWN;
    private final String possibleMagicNumber;
    private final String extension;
    private final Set<String> tolerateExtensions;
    private final Set<String> extensionMagics;
    private final Set<String> potential;

    @Override
    public String toString() {
        return new StringJoiner(", ", DetectResult.class.getSimpleName() + "[", "]")
                .add("safeStatus=" + safeStatus)
                .add("possibleMagicNumber='" + possibleMagicNumber + "'")
                .add("extension='" + extension + "'")
                .add("tolerateExtensions='" + tolerateExtensions + "'")
                .toString();
    }

}
