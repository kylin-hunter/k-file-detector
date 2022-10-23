package com.kylinhunter.plat.file.detector.magic;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import com.kylinhunter.plat.file.detector.constant.MagicMatchMode;
import com.kylinhunter.plat.file.detector.type.FileType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Magic {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    private String number;
    private String desc;
    private Set<String> fileTypeIds;

    /* ==== extended  properties===*/
    private int magicLength; //  magic number's bytes size
    private MagicMatchMode matchMode;
    private Set<FileType> fileTypes = new HashSet<>();
    private Set<String> extensions;

    @Override
    public String toString() {
        return new StringJoiner(", ", Magic.class.getSimpleName() + "[", "]")
                .add("number='" + number + "'")
                .add("desc='" + desc + "'")
                .add("fileTypeIds=" + fileTypeIds)
                .add("extensions=" + extensions)
                .toString();
    }
}
