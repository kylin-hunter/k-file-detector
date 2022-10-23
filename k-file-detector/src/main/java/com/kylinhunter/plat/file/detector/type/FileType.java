package com.kylinhunter.plat.file.detector.type;

import java.util.Set;
import java.util.StringJoiner;

import com.kylinhunter.plat.file.detector.constant.FileFamily;
import com.kylinhunter.plat.file.detector.magic.ExtensionMagics;
import com.kylinhunter.plat.file.detector.magic.TolerateMagics;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FileType {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    String id;
    private String extension;
    private String desc;
    private String tolerateTag;

    /* ==== extended  properties===*/
    private FileFamily family;
    private Set<FileType> tolerateFileTypes;
    private ExtensionMagics extensionMagics;
    private TolerateMagics tolerateMagics;

    @Override
    public String toString() {
        return new StringJoiner(", ", FileType.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("extension='" + extension + "'")
                .toString();
    }
}