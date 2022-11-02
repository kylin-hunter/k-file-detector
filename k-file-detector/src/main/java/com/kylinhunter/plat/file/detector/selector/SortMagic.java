package com.kylinhunter.plat.file.detector.selector;

import java.util.List;

import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-02 22:13
 **/
@Data
public class SortMagic {
    private Magic magic;
    private int matchExtension;
    private FileType targetExtension;
    private String extension;
    private boolean mustFirst;

    public SortMagic(Magic magic, String extension) {
        this.magic = magic;
        this.extension = extension;
        this.matchExtension = 0;

        List<FileType> fileTypes = magic.getFileTypes();

        for (FileType fileType : fileTypes) {
            if (fileType.extensionEquals(extension)) {
                this.matchExtension = 1;
                this.targetExtension = fileType;
                break;
            }
        }

    }
}
