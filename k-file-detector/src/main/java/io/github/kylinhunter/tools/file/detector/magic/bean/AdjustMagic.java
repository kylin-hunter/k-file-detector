package io.github.kylinhunter.tools.file.detector.magic.bean;

import java.util.List;

import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

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
public class AdjustMagic {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    private String number;  // the magic number;
    private String desc; // the description for the magic number
    private List<FileType> includeFileTypes; // the file types
    private List<FileType> excludeFileTypes; // the file types
    private List<FileType> topFileTypes; // the file types
    private String refMagic;
    private boolean detectContentSupport;
    private boolean enabled = true;

}
