package com.kylinhunter.plat.file.detector.config.bean;

import java.util.List;

import com.kylinhunter.plat.file.detector.constant.MagicMode;
import com.kylinhunter.plat.file.detector.exception.DetectException;

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
    private boolean fatherFirstNoExtensionHit = true;

    /* ==== extended  properties===*/
    private int length; //  magic number's bytes size
    private MagicMode mode; // magic mode for diffrent detecting action
    private List<String> extensions; // the extension can be detected

    public Magic(String number) {
        this.number = number;
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
        return number + "/" + extensions;
    }

    @Override
    public int compareTo(Magic o) {
        return o.length - this.length;
    }
}
