package com.kylinhunter.plat.file.detector.bean;

import java.util.Collections;
import java.util.List;

import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

import lombok.AccessLevel;
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

    @Getter(AccessLevel.PUBLIC)
    private final List<Magic> oriMagics; // the possible magic number

    @Getter
    @Setter
    private List<Magic> allPossibleMagics; // the possible magic number

    @SuppressWarnings("unchecked")
    @Getter
    @Setter
    private List<FileType> allPossibleFileTypes = Collections.EMPTY_LIST;  // all possible file type

    public DetectResult(DetectConext detectConext) {
        this.fileName = detectConext.getFileName();
        this.oriMagics = detectConext.getDetectedMagics();
    }

    public FileType getFirstFileType() {
        if (allPossibleFileTypes.size() > 0) {
            return allPossibleFileTypes.get(0);
        }
        return null;
    }

    public FileType getSecondFileType() {
        if (allPossibleFileTypes.size() > 1) {
            return allPossibleFileTypes.get(1);
        }
        return null;
    }
}

