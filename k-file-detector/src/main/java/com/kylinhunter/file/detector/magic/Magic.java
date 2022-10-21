package com.kylinhunter.file.detector.magic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import com.kylinhunter.file.detector.constant.MagicFamily;
import com.kylinhunter.file.detector.constant.MagicMatchMode;
import com.kylinhunter.file.detector.constant.MagicRisk;
import com.kylinhunter.file.detector.extension.ExtensionFile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Set<String> extensions;
    private List<MagicFamily> families;
    private MagicRisk risk;

    /* ==== extended  properties===*/
    private int magicLength; //  magic number's bytes size
    private MagicMatchMode matchMode;
    @Setter(AccessLevel.NONE)
    private Set<ExtensionFile> extensionFiles = new HashSet<>();

    public void addFileType(ExtensionFile extensionFile) {
        extensionFiles.add(extensionFile);
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
