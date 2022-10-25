package com.kylinhunter.plat.file.detector.magic;

import java.util.Set;
import java.util.StringJoiner;

import com.kylinhunter.plat.file.detector.constant.MagicMode;
import com.kylinhunter.plat.file.detector.type.FileType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description the description for magic
 * @date 2022-10-02 19:55
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Magic {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    private String number;  // the magic number;
    private String desc; // the description for the magic number
    private Set<String> fileTypeIds;  // the file type id can be detected

    /* ==== extended  properties===*/
    private int magicLength; //  magic number's bytes size
    private MagicMode mode; // magic mode for diffrent detecting action
    private Set<FileType> fileTypes; // the file type  can be detected  ,reference  the field=> fileTypeIds
    private Set<String> extensions; // the extension can be detected

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
