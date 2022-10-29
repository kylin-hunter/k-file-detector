package com.kylinhunter.plat.file.detector.parse.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-29 14:44
 **/
public class ParseStat {
    @Getter
    @Setter
    private int trNums;
    @Getter
    private int invalidTrNums;
    @Getter
    private int validTrNums;
    @Getter
    protected int invalidMagicNums;
    @Getter
    protected int validMagicNums;

    @Getter
    private int duplicateFileTypeNums;

    @Getter
    private int noExtensionNums;

    @Getter
    private int extensionNums;

    public void incrementDuplicateFileTypeNums() {
        duplicateFileTypeNums++;
    }

    public void incrementInvalidTrNums() {
        invalidTrNums++;
    }

    public void incrementNoExtensionNums() {
        noExtensionNums++;
    }

    public void incrementExtensionNums() {
        extensionNums++;
    }

    public void incrementValidTrNums() {
        validTrNums++;
    }
}
