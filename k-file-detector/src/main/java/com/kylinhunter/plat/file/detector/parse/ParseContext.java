package com.kylinhunter.plat.file.detector.parse;

import java.util.Map;
import java.util.StringJoiner;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 14:08
 **/
@Slf4j
public class ParseContext {
    @Getter
    @Setter
    private int trNums;
    @Getter
    private int invalidTrNums;
    @Getter
    private int validTrNums;
    @Getter
    private int invalidMagicNums;
    @Getter
    private int validMagicNums;

    @Getter
    private int noExtensionNums;

    @Getter
    private int extensionNums;
    @Getter
    @Setter
    private ParseMagic currentParseMagic;

    private int nextFileTypeId;
    private int nextMagicId;

    @Getter
    private final Map<String, ParseMagic> numberMaps = Maps.newTreeMap();

    public void add(ParseMagic parseMagic) {
        if (parseMagic.isValid()) {
            validMagicNums++;
            numberMaps.put(parseMagic.getNumber(), parseMagic);
        } else {
            invalidMagicNums++;
        }
    }

    public String nextFileTypeId() {
        return ++nextFileTypeId + "";
    }

    public int maxFileTypeId() {
        return nextFileTypeId;
    }

    public int nextMagicId() {
        return ++nextMagicId;
    }

    public int maxMagicId() {
        return nextMagicId;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", ParseContext.class.getSimpleName() + "[", "]")
                .add("trNums=" + trNums)
                .add("invalidTrNums=" + invalidTrNums)
                .add("validTrNums=" + validTrNums)
                .add("invalidMagicNums=" + invalidMagicNums)
                .add("validMagicNums=" + validMagicNums)
                .add("nextFileTypeId=" + nextFileTypeId)
                .add("nextMagicId=" + nextMagicId)
                .add("currentParseMagic='" + currentParseMagic.getNumber() + "'")
                .add("numberMaps=" + numberMaps.size())
                .toString();
    }
}
