package io.github.kylinhunter.tools.file.detector.prepared.parse.bean;

import java.util.Map;
import java.util.StringJoiner;

import com.google.common.collect.Maps;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;

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

    @Getter
    private final Map<String, ParseMagic> numberMaps = Maps.newTreeMap();

    public void add(ParseMagic parseMagic) {
        if (parseMagic.isValid()) {
            this.parseStat.magicValidNums++;
            if (numberMaps.containsKey(parseMagic.getNumber())) {
                throw new DetectException("duplicate magic number:" + parseMagic.getNumber());
            }
            numberMaps.put(parseMagic.getNumber(), parseMagic);
        } else {
            this.parseStat.magicInvalidNums++;
        }
    }



    @Override
    public String toString() {
        return new StringJoiner(", ", ParseContext.class.getSimpleName() + "[", "]")
                .add("parseStat=" + parseStat)
                .add("currentParseMagic='" + currentParseMagic.getNumber() + "'")
                .add("numberMaps=" + numberMaps.size())
                .toString();
    }
}
