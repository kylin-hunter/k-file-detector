package com.kylinhunter.plat.file.detector.bean;

import java.util.List;

import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.config.bean.Magic;
import com.kylinhunter.plat.file.detector.config.bean.FileType;

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
    private FileType bestFileType; // the best  of all detected file type

    @Getter
    private List<FileType> allPossibleFileTypes;  // all possible file type

    @Getter
    @Setter
    private Magic bestMagic; // the best of all detedted magics

    @Getter
    private final List<Magic> detectedMagics; // the detected magic number

    public DetectResult(DetectConext detectConext) {
        this.fileName = detectConext.getFileName();
        this.detectedMagics = detectConext.getDetectedMagics();
    }

    /**
     * @param fileType fileType
     * @return void
     * @title addPossibleFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-25 02:01
     */
    public void addPossibleFileType(FileType fileType) {
        if (allPossibleFileTypes == null) {
            allPossibleFileTypes = Lists.newArrayList();
        }
        allPossibleFileTypes.add(fileType);
    }
}

