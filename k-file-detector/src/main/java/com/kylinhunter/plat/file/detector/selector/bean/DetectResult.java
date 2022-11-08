package com.kylinhunter.plat.file.detector.selector.bean;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.kylinhunter.plat.file.detector.detect.bean.DetectConext;
import com.kylinhunter.plat.file.detector.file.bean.FileType;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

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

    private List<FileType> allPossibleFileTypes;

    public DetectResult(DetectConext detectConext) {
        this.fileName = detectConext.getFileName();
        this.oriMagics = detectConext.getDetectedMagics();
    }

    @SuppressWarnings("unchecked")
    public List<FileType> getFirstFileTypes() {
        if (allPossibleMagics != null && allPossibleMagics.size() > 0) {
            return allPossibleMagics.get(0).getFileTypes();
        }
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    public List<FileType> getSecondFileType() {
        if (allPossibleMagics != null && allPossibleMagics.size() > 1) {
            return allPossibleMagics.get(1).getFileTypes();
        }
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    public List<FileType> getAllPossibleFileTypes() {
        if (allPossibleFileTypes == null) {
            if (allPossibleMagics != null) {
                allPossibleFileTypes =
                        allPossibleMagics.stream().flatMap(e -> e.getFileTypes().stream()).collect(Collectors.toList());

            } else {
                allPossibleFileTypes = Collections.EMPTY_LIST;
            }
        }
        return allPossibleFileTypes;

    }
}

