package com.kylinhunter.file.detector.signature.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import com.kylinhunter.file.detector.constant.MagicFamily;
import com.kylinhunter.file.detector.constant.MagicMatchType;
import com.kylinhunter.file.detector.constant.MagicRisk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-12 19:55
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Magic {
    @EqualsAndHashCode.Include
    private String number;
    private String desc;
    private Set<String> extensions;
    private List<MagicFamily> families;
    private MagicRisk risk;

    private int byteNum;
    private MagicMatchType matchType;
    private Set<FileType> fileTypes = new HashSet<>();

    public void addFileType(FileType fileType) {
        fileTypes.add(fileType);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Magic.class.getSimpleName() + "[", "]")
                .add("number='" + number + "'")
                .add("desc='" + desc + "'")
                .add("extensions=" + extensions)
                .add("families=" + families)
                .add("risk=" + risk)
                .toString();
    }
}
