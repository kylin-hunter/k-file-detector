package io.github.kylinhunter.tools.file.detector.detect.bean;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import io.github.kylinhunter.tools.file.detector.content.bean.DetectConext;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;

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
     * @return java.util.List<io.github.kylinhunter.plat.file.detector.file.bean.FileType>
     * @title getFirstFileTypes
     * @description
     * @author BiJi'an
     * @date 2022-11-09 20:20
     */
    public FileType getFirstFileType() {
        if (possibleFileTypes.size() > 0) {
            return possibleFileTypes.get(0);
        }
        return null;
    }

    /**
     * @return java.util.List<io.github.kylinhunter.plat.file.detector.file.bean.FileType>
     * @title getSecondFileType
     * @description
     * @author BiJi'an
     * @date 2022-11-09 20:20
     */
    public FileType getSecondFileType() {
        if (possibleFileTypes.size() > 1) {
            return possibleFileTypes.get(1);
        }
        return null;
    }

    public void addPossibleMagic(Magic magic) {
        if (possibleMagics == null) {
            possibleMagics = Lists.newArrayList();
        }
        possibleMagics.add(magic);
    }
}

