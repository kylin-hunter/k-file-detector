package io.github.kylinhunter.tools.file.detector.prepared.parse.bean;

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
    private int trInvalidNums;
    @Getter
    private int trValidNums;

    @Getter
    protected int magicInvalidNums;
    @Getter
    protected int magicValidNums;

    @Getter
    private int extensionNoneNums;

    @Getter
    private int fileTypeDuplicateNums;
    @Getter
    private int fileTypeNums;

    public void incrementTrInvalidNum() {
        trInvalidNums++;
    }

    public void incrementTrValidNum() {
        trValidNums++;
    }

    public void incrementExtensionNoneNum() {
        extensionNoneNums++;
    }



    public void incrementFileTypeNum() {
        fileTypeNums++;
    }

    public void incrementFileTypeDuplicateNum() {
        fileTypeDuplicateNums++;
    }

}
