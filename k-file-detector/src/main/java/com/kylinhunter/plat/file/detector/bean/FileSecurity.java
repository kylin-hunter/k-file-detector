package com.kylinhunter.plat.file.detector.bean;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.constant.SecurityStatus;
import com.kylinhunter.plat.file.detector.magic.Magic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-01 22:37
 **/

@NoArgsConstructor
public class FileSecurity {
    @Getter
    @Setter
    private SecurityStatus securityStatus = SecurityStatus.UNKNOWN;
    private Set<String> detectedMagicNumbers;

    @Setter
    private Set<String> dangerousExtensions;
    private Set<String> tolerateExtensions;

    private Set<String> dangerousMagicNumbers;
    private Set<String> safeMagicNumbers;

    public FileSecurity(Set<Magic> detectedMagics) {
        detectedMagicNumbers = detectedMagics.stream().map(e -> e.getNumber()).collect(Collectors.toSet());
    }

    public boolean isDetected() {
        return securityStatus != SecurityStatus.UNKNOWN;
    }

    public void addSafeMagic(Magic magic) {
        if (safeMagicNumbers == null) {
            safeMagicNumbers = Sets.newHashSet();
        }
        safeMagicNumbers.add(magic.getNumber());

    }

    public void addDangerousMagic(Magic magic) {
        if (dangerousMagicNumbers == null) {
            dangerousMagicNumbers = Sets.newHashSet();
        }
        dangerousMagicNumbers.add(magic.getNumber());

    }

    public void addTolerateExtensions(Set<String> extenions) {
        if (tolerateExtensions == null) {
            tolerateExtensions = Sets.newHashSet();
        }
        tolerateExtensions.addAll(extenions);
    }

    public String getDesc() {
        StringBuffer sb = new StringBuffer();
        switch (securityStatus) {

            case DANGEROUS_EXTENSION: {
                sb.append("dangerous extensions = ");
                sb.append(dangerousExtensions != null ? dangerousExtensions.toString() : "");
                break;
            }
            case DISGUISE_WARN: {

                sb.append("tolerate extensions = ");
                sb.append(tolerateExtensions != null ? tolerateExtensions.toString() : "");
                break;

            }
            case DISGUISE: {
                sb.append("detected magic numbers = ");
                sb.append(detectedMagicNumbers != null ? detectedMagicNumbers.toString() : "");
                break;

            }
            case SAFE: {

                sb.append("safe magic number = ");
                sb.append(safeMagicNumbers != null ? safeMagicNumbers.toString() : "");
                break;

            }
            case DANGEROUS_CONTENT: {
                sb.append("dangerous magic number = ");
                sb.append(dangerousMagicNumbers != null ? dangerousMagicNumbers.toString() : "");
                break;

            }
            default: {
                sb.append("UNKNOWN");
            }
        }
        return sb.toString();
    }

}
