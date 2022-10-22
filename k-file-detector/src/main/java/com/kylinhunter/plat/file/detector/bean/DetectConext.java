package com.kylinhunter.plat.file.detector.bean;

import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.collections4.CollectionUtils;

import com.kylinhunter.plat.file.detector.constant.SecurityStatus;
import com.kylinhunter.plat.file.detector.magic.ExtensionMagics;
import com.kylinhunter.plat.file.detector.magic.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-01 22:37
 **/
@Data
public class DetectConext {

    private SecurityStatus securityStatus = SecurityStatus.UNKNOWN;
    private final String possibleMagicNumber; // potential magic numbers
    private final String extension; // explicit extension
    private Set<Magic> detectedMagics; // the detected magic messages

    private Set<String> dangerousExtensions;
    private ExtensionMagics extensionMagics;

    public void setSecurityStatus(SecurityStatus securityStatus) {
        this.securityStatus = securityStatus;
    }

    public boolean isDetected() {
        return CollectionUtils.isEmpty(detectedMagics);
    }

    private String msg;

    @Override
    public String toString() {
        return new StringJoiner(", ", DetectConext.class.getSimpleName() + "[", "]")
                .add("securityStatus=" + securityStatus)
                .add("possibleMagicNumber='" + possibleMagicNumber + "'")
                .add("extension='" + extension + "'")
                .add("magics=" + detectedMagics)
                .toString();
    }

}
