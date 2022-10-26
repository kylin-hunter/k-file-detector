package com.kylinhunter.plat.file.detector.bean;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.kylinhunter.plat.file.detector.config.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description a context for detect action
 * @date 2022-10-01 22:37
 **/
@Data
public class DetectConext {
    private String possibleMagicNumber; // potential magic numbers
    private String fileName; // file name
    private String extension; // explicit extension
    private List<Magic> detectedMagics; // the detected magic messages

    public DetectConext(String possibleMagicNumber, String fileName) {
        this.possibleMagicNumber = possibleMagicNumber;
        this.fileName = fileName;
        this.extension = FilenameUtils.getExtension(fileName);
        if (this.extension != null) {
            this.extension = extension.toLowerCase();
        }
    }
}
