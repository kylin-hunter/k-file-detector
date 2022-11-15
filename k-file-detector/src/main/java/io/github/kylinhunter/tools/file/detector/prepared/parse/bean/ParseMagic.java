package io.github.kylinhunter.tools.file.detector.prepared.parse.bean;

import java.util.List;

import com.google.common.collect.Lists;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

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

        return number.compareTo(o.number);

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
