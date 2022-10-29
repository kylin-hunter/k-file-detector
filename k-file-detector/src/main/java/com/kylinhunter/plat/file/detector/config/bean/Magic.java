package com.kylinhunter.plat.file.detector.config.bean;

import java.util.List;

import com.kylinhunter.plat.file.detector.constant.MagicMode;

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

    /* ==== extended  properties===*/
    private int length; //  magic number's bytes size
    private MagicMode mode; // magic mode for diffrent detecting action
    private FileType fileType; // first  best  file type
    private List<FileType> fileTypes; // the file type  can be detected  ,reference  the field=> fileTypeIds
    private List<String> extensions; // the extension can be detected

    public Magic(String number) {
        this.number = number;
    }

    public FileType getFileType() {
        if (fileTypes != null && fileTypes.size() > 0) {
            return fileTypes.get(0);
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
