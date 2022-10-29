package com.kylinhunter.plat.file.detector.parse.bean;

import java.util.Map;
import java.util.StringJoiner;

import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.exception.DetectException;

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
    private ParseStat parseStat = new ParseStat();
    @Getter
    @Setter
    private ParseMagic currentParseMagic;
    private int nextFileTypeId;

    @Getter
    private final Map<String, ParseMagic> numberMaps = Maps.newTreeMap();

    public void add(ParseMagic parseMagic) {
        if (parseMagic.isValid()) {
            this.parseStat.validMagicNums++;
            if (numberMaps.containsKey(parseMagic.getNumber())) {
                throw new DetectException("duplicate magic number:" + parseMagic.getNumber());
            }
            numberMaps.put(parseMagic.getNumber(), parseMagic);
        } else {
            this.parseStat.invalidMagicNums++;
        }
    }

    public int generateNextFileTypeId() {
        return ++nextFileTypeId;
    }

    public int getMaxFileTypeId() {
        return nextFileTypeId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ParseContext.class.getSimpleName() + "[", "]")
                .add("parseStat=" + parseStat)

                .add("nextFileTypeId=" + nextFileTypeId)
                .add("currentParseMagic='" + currentParseMagic.getNumber() + "'")
                .add("numberMaps=" + numberMaps.size())
                .toString();
    }
}
