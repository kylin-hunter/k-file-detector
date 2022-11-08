package com.kylinhunter.plat.file.detector.magic.bean;

import java.util.List;

import com.kylinhunter.plat.file.detector.magic.constant.MagicMode;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.file.bean.FileType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description the description for magic
 * @date 2022-10-02 19:55
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Magic implements Comparable<Magic> {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    private String number;  // the magic number;
    private int id;
    private int offset;
    private String desc; // the description for the magic number
    private List<FileType> fileTypes; // the file types

    private boolean extensionMustHitAsFather = false;
    private boolean detectContentSupport;


    /* ==== extended  properties===*/
    private int length; //  magic number's bytes size
    private MagicMode mode; // magic mode for diffrent detecting action
    private FileTypesWrapper fileTypesWrapper;

    public Magic(String number) {
        this.number = number;
        this.setLength(number.length() / 2);
    }

    public FileType getFirstFileType() {
        if (fileTypes != null && fileTypes.size() > 0) {
            return fileTypes.get(0);
        }
        throw new DetectException("no first file type");
    }

    public FileType getSecondFileType() {
        if (fileTypes != null && fileTypes.size() > 1) {
            return fileTypes.get(1);
        }
        return null;
    }

    @Override
    public String toString() {
        return number + "/" + fileTypesWrapper.extensions;
    }

    @Override
    public int compareTo(Magic o) {
        return o.length - this.length;
    }
}
