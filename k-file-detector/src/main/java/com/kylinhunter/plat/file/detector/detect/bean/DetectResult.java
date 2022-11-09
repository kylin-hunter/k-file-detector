package com.kylinhunter.plat.file.detector.detect.bean;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import com.kylinhunter.plat.file.detector.content.bean.DetectConext;
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
    private List<Magic> possibleMagics; // the possible magic number

    @Getter
    @Setter
    private List<FileType> possibleFileTypes;

    public DetectResult(DetectConext detectConext) {
        this.fileName = detectConext.getFileName();
        this.oriMagics = detectConext.getDetectedMagics();
    }

    /**
     * @return java.util.List<com.kylinhunter.plat.file.detector.file.bean.FileType>
     * @title getFirstFileTypes
     * @description
     * @author BiJi'an
     * @date 2022-11-09 20:20
     */
    public List<FileType> getFirstFileTypes() {

        return possibleFileTypes;
    }

    /**
     * @return java.util.List<com.kylinhunter.plat.file.detector.file.bean.FileType>
     * @title getSecondFileType
     * @description
     * @author BiJi'an
     * @date 2022-11-09 20:20
     */
    public List<FileType> getSecondFileType() {
        return possibleFileTypes;
    }

    public void addPossibleMagic(Magic magic) {
        if (possibleMagics == null) {
            possibleMagics = Lists.newArrayList();
        }
        possibleMagics.add(magic);
    }
}

