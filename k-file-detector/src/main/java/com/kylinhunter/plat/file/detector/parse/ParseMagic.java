package com.kylinhunter.plat.file.detector.parse;

import java.util.List;

import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.config.FileType;

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
public class ParseMagic implements Comparable<ParseMagic> {
    @EqualsAndHashCode.Include
    private final String td0Text;
    private boolean valid = false;
    private String offset;
    private String number;
    private String desc;

    private List<FileType> fileTypes = Lists.newArrayList();

    @Override
    public int compareTo(ParseMagic o) {
        return this.number.length() - o.number.length();
    }
}
