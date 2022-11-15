package io.github.kylinhunter.tools.file.detector.magic.bean;

import java.util.List;

import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.constant.MagicMode;

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


    /*===ex====*/
    private Magic refMagic;
    private boolean detectContentSupport;

    /* ==== extended  properties===*/
    private int length; //  magic number's bytes size
    private MagicMode mode; // magic mode for diffrent detecting action
    private FileTypesWrapper fileTypesWrapper;
    private boolean enabled = true;

    public Magic(String number) {
        this.number = number;
        this.setLength(number.length() / 2);
    }

    public FileType getFirstFileType() {
        if (fileTypes != null && fileTypes.size() > 0) {
            return fileTypes.get(0);
        }
        return null;
    }

    public FileType getSecondFileType() {
        if (fileTypes != null && fileTypes.size() > 1) {
            return fileTypes.get(1);
        }
        return null;
    }

    @Override
    public String toString() {
        return number + "/" + (fileTypesWrapper != null ? fileTypesWrapper.extensions : "");
    }

    @Override
    public int compareTo(Magic o) {
        return o.length - this.length;
    }
}
