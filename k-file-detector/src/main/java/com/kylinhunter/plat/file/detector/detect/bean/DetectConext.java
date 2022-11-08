package com.kylinhunter.plat.file.detector.detect.bean;

import java.util.List;

import com.kylinhunter.plat.file.detector.common.util.FilenameUtil;
import com.kylinhunter.plat.file.detector.magic.bean.ReadMagic;
import com.kylinhunter.plat.file.detector.file.bean.FileType;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

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
    private String fileName = ""; // file name
    private String extension = ""; // explicit extension
    private List<Magic> detectedMagics; // the detected magic messages
    private List<FileType> contentFileTypes; // the detected file types by content
    private ReadMagic readMagic;

    public DetectConext(ReadMagic readMagic) {
        this(readMagic, null);
    }

    public DetectConext(ReadMagic readMagic, String fileName) {
        this.readMagic = readMagic;
        if (fileName != null && fileName.length() > 0) {
            this.fileName = fileName;
            this.extension = FilenameUtil.getExtension(fileName);
            if (this.extension != null) {
                this.extension = extension.toLowerCase();
            }
        }

    }

    public String getPossibleMagicNumber() {
        return readMagic.getPossibleMagicNumber();
    }

    public boolean isCheckContent() {
        return readMagic.isCheckContent();
    }
}
