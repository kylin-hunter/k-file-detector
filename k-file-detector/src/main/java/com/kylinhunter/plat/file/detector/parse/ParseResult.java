package com.kylinhunter.plat.file.detector.parse;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-27 23:18
 **/

import java.util.List;
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
public class ParseResult {
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
    @Setter
    private ParseMagic parseMagic;
    private int nextFileTypeId;
    @Getter
    private Map<String, ParseMagic> numberMaps = Maps.newTreeMap();

    @Getter
    @Setter
    private List<ParseMagic> parseMagics;

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

    public void incrementInvalidTrNums() {
        invalidTrNums++;
    }

    public void incrementValidTrNums() {
        validTrNums++;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ParseResult.class.getSimpleName() + "[", "]")
                .add("trNums=" + trNums)
                .add("invalidTrNums=" + invalidTrNums)
                .add("validTrNums=" + validTrNums)
                .add("invalidMagicNums=" + invalidMagicNums)
                .add("validMagicNums=" + validMagicNums)
                .add("parseMagic='" + parseMagic.getNumber() + "'")
                .add("numberMaps=" + numberMaps.size())
                .toString();
    }
}
