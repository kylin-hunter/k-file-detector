package io.github.kylinhunter.tools.file.detector.detect.bean;

import java.util.List;

import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-02 22:13
 **/
@Data
public class SortMagic implements Comparable<SortMagic> {
    private Magic magic;
    private boolean matchExtension;
    private FileType matchFileType;
    private String extension;
    private int offset;
    private String number;


    public SortMagic(Magic magic, String extension) {
        this.magic = magic;
        this.offset = magic.getOffset();
        this.number = magic.getNumber();
        this.extension = extension;
        this.matchExtension = false;

        List<FileType> fileTypes = magic.getFileTypes();

        for (FileType fileType : fileTypes) {
            if (fileType.extensionEquals(extension)) {
                this.matchExtension = true;
                this.matchFileType = fileType;
                break;
            }
        }

    }

    public String getNumber() {
        return magic.getNumber();
    }

    @Override
    public int compareTo(SortMagic o) {
        int offsetDiff = this.offset - o.offset;
        if (offsetDiff < 0) {
            return -1;
        } else if (offsetDiff > 0) {
            return 1;
        } else {
            int dimensionMagicLen = o.number.length() - this.number.length();
            return Integer.compare(dimensionMagicLen, 0);
        }
    }
}