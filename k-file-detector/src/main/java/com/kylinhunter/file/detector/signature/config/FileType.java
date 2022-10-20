package com.kylinhunter.file.detector.signature.config;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import com.kylinhunter.file.detector.constant.ExtensionFamily;
import com.kylinhunter.file.detector.constant.ExtensionRisk;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-12 16:24
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FileType {
    @EqualsAndHashCode.Include
    private String extension;
    private List<ExtensionFamily> families;
    private String tolerateGroup;
    private ExtensionRisk risk;

    private Set<String> tolerateExtensions;

    @Override
    public String toString() {
        return new StringJoiner(", ", FileType.class.getSimpleName() + "[", "]")
                .add("extension='" + extension + "'")
                .add("families=" + families)
                .add("tolerateGroup='" + tolerateGroup + "'")
                .toString();
    }
}