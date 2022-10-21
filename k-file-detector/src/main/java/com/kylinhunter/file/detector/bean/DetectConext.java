package com.kylinhunter.file.detector.bean;

import java.util.Set;
import java.util.StringJoiner;

import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.constant.SafeStatus;
import com.kylinhunter.file.detector.magic.ExtensionMagics;
import com.kylinhunter.file.detector.magic.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-01 22:37
 **/
@Data
public class DetectConext {

    private SafeStatus safeStatus = SafeStatus.UNKNOWN;
    private final String possibleMagicNumber; // potential magic numbers
    private final String extension; // explicit extension
    private Set<Magic> detectedMagics; // the detected magic messages

    private Set<String> dangerousExtensions;
    private ExtensionMagics extensionMagics;

    public void addDetectedMagic(Magic magic) {
        if (detectedMagics == null) {
            detectedMagics = Sets.newHashSet();
        }
        detectedMagics.add(magic);
    }

    public void setSafeStatus(SafeStatus safeStatus) {
        this.safeStatus = safeStatus;
    }

    public boolean isDetected() {
        return safeStatus != SafeStatus.UNKNOWN || detectedMagics != null && detectedMagics.size() > 0;
    }

    private String msg;

    @Override
    public String toString() {
        return new StringJoiner(", ", DetectConext.class.getSimpleName() + "[", "]")
                .add("safeStatus=" + safeStatus)
                .add("possibleMagicNumber='" + possibleMagicNumber + "'")
                .add("extension='" + extension + "'")
                .add("detectedMagics=" + detectedMagics)
                .toString();
    }

}
