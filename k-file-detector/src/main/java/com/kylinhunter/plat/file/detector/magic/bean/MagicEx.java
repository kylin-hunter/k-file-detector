package com.kylinhunter.plat.file.detector.magic.bean;

import java.util.List;

import com.kylinhunter.plat.file.detector.file.bean.FileType;

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
public class MagicEx {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    private String number;  // the magic number;
    private int offset;
    private String desc; // the description for the magic number
    private List<FileType> includeFileTypes; // the file types
    private List<FileType> excludeFileTypes; // the file types
    private List<FileType> topFileTypes; // the file types
    private String refMagic;
    private boolean detectContentSupport;
    private boolean enabled = true;
    private boolean extensionMustMatch;

}
