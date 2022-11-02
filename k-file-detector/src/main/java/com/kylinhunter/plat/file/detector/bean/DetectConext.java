package com.kylinhunter.plat.file.detector.bean;

import java.util.List;

import com.kylinhunter.plat.file.detector.common.util.FilenameUtil;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description a context for detect action
 * @date 2022-10-01 22:37
 **/
@Data
@NoArgsConstructor
public class DetectConext {
    private String possibleMagicNumber; // potential magic number
    private String fileName; // file name
    private String extension; // explicit extension
    private List<Magic> detectedMagics; // the detected magic messages

    public DetectConext(String possibleMagicNumber, String fileName) {
        this.possibleMagicNumber = possibleMagicNumber;
        this.fileName = fileName;
        this.extension = FilenameUtil.getExtension(fileName);
        if (this.extension != null) {
            this.extension = extension.toLowerCase();
        }
    }
}
