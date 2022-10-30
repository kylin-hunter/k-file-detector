package com.kylinhunter.plat.file.detector.bean;

import java.util.List;

import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author BiJi'an
 * @description the result for detect
 * @date 2022-10-01 22:37
 **/
public class DetectResult {

    @Getter
    private final String fileName;

    @Getter
    @Setter
    private List<Magic> allPossibleMagics; // the possible magic number
    @Getter
    private List<FileType> allPossibleFileTypes = Lists.newArrayList();  // all possible file type

    @Getter
    private List<Magic> allBestMagics = Lists.newArrayList(); // the best of all detedted magics

    public DetectResult(DetectConext detectConext) {
        this.fileName = detectConext.getFileName();
    }

    public FileType getFirstFileType() {
        if (allPossibleFileTypes != null && allPossibleFileTypes.size() > 0) {
            return allPossibleFileTypes.get(0);
        }
        return null;
    }

    public FileType getSecondFileType() {
        if (allPossibleFileTypes != null && allPossibleFileTypes.size() > 1) {
            return allPossibleFileTypes.get(1);
        }
        return null;
    }
}

