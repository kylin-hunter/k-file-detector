package com.kylinhunter.plat.file.detector.bean;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.magic.Magic;
import com.kylinhunter.plat.file.detector.type.FileType;

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
    private FileType bestFileType; // the best  of all detected file type
    @Getter
    @Setter
    private Set<FileType> allBestFileTypes; // all detected file type

    @Getter
    private Set<FileType> allPossibleFileTypes;  // all possible file type

    @Getter
    private Magic bestMagic; // the best of all detedted magics

    @Getter
    private final List<Magic> detectedMagics; // the detected magic numbers

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
            allPossibleFileTypes = Sets.newHashSet();
        }
        allPossibleFileTypes.add(fileType);
    }

    /**
     * @param bestFileType bestFileType
     * @return void
     * @title trySetBestFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-25 02:24
     */
    public void trySetBestFileType(FileType bestFileType) {
        if (this.bestFileType == null) {
            this.bestFileType = bestFileType;
        }
    }

    /**
     * @param bestMagic bestMagic
     * @return void
     * @title trySetBestMagic
     * @description
     * @author BiJi'an
     * @date 2022-10-25 02:24
     */
    public void trySetBestMagic(Magic bestMagic) {
        if (this.bestMagic == null) {
            this.bestMagic = bestMagic;
        }
    }
}
