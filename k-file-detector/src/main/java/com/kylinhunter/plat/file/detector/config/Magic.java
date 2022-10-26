package com.kylinhunter.plat.file.detector.config;

import java.util.List;
import java.util.StringJoiner;

import com.kylinhunter.plat.file.detector.constant.MagicMode;

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
public class Magic implements Comparable<Magic> {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    private String number;  // the magic number;
    private String desc; // the description for the magic number
    private List<String> fileTypeIds;  // the file type id can be detected

    /* ==== extended  properties===*/
    private int magicLength; //  magic number's bytes size
    private MagicMode mode; // magic mode for diffrent detecting action
    private FileType fileType; // first  best  file type
    private List<FileType> fileTypes; // the file type  can be detected  ,reference  the field=> fileTypeIds
    private List<String> extensions; // the extension can be detected

    @Override
    public String toString() {
        return new StringJoiner(", ", Magic.class.getSimpleName() + "[", "]")
                .add("number='" + number + "'")
                .add("desc='" + desc + "'")
                .add("fileTypeIds=" + fileTypeIds)
                .add("extensions=" + extensions)
                .toString();
    }

    @Override
    public int compareTo(Magic o) {
        return o.magicLength - this.magicLength;
    }
}
