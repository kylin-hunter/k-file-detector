package com.kylinhunter.plat.file.detector.bean;

import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.magic.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-01 22:37
 **/
@Data
public class DetectConext {
    private String possibleMagicNumber; // potential magic numbers
    private String fileName; // explicit extension
    private String extension; // explicit extension
    private Set<Magic> detectedMagics; // the detected magic messages

    public DetectConext(String possibleMagicNumber, String fileName) {
        this.possibleMagicNumber = possibleMagicNumber;
        this.fileName = fileName;
        this.extension = FilenameUtils.getExtension(fileName);
    }

    /**
     * @return java.util.Set<com.kylinhunter.plat.file.detector.magic.Magic>
     * @title createDetectedMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:43
     */
    public Set<Magic> resetDetectedMagics() {
        this.detectedMagics = Sets.newHashSet();
        return detectedMagics;
    }
}
