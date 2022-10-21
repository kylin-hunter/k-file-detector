package com.kylinhunter.file.detector.extension;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import com.kylinhunter.file.detector.constant.ExtensionFamily;
import com.kylinhunter.file.detector.constant.ExtensionRisk;
import com.kylinhunter.file.detector.magic.ExtensionMagics;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExtensionFile {
    /* ==== from yaml  ===*/

    @EqualsAndHashCode.Include
    private String extension;
    private List<ExtensionFamily> families;
    private String tolerateGroup;
    private ExtensionRisk risk;

    /* ==== extended  properties===*/
    private Set<String> tolerateExtensions;
    private ExtensionMagics extensionMagics;

    @Override
    public String toString() {
        return new StringJoiner(", ", ExtensionFile.class.getSimpleName() + "[", "]")
                .add("extension='" + extension + "'")
                .add("families=" + families)
                .add("tolerateGroup='" + tolerateGroup + "'")
                .add("risk=" + risk)
                .toString();
    }
}