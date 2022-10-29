package com.kylinhunter.plat.file.detector.parse.bean;

import java.util.List;

import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.exception.DetectException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-27 23:19
 **/
@Slf4j
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParseMagic implements Comparable<ParseMagic>, Cloneable {

    @EqualsAndHashCode.Include
    private String number;
    private int offset;
    private String desc;
    private final String td0Text;
    private boolean valid = false;
    private List<FileType> fileTypes = Lists.newArrayList();

    @Override
    public int compareTo(ParseMagic o) {
        return this.number.length() - o.number.length();
    }

    @Override
    public ParseMagic clone() {

        try {
            return (ParseMagic) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new DetectException("clone error", e);
        }
    }
}
