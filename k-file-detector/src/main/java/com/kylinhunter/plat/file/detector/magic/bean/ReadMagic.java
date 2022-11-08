package com.kylinhunter.plat.file.detector.magic.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-07 15:14
 **/
@Data
@NoArgsConstructor
public class ReadMagic {
    private String fileName;
    private String possibleMagicNumber;
    private byte[] content;
    private boolean detectContentSupport;

    public ReadMagic(String fileName, String possibleMagicNumber) {
        this(fileName, possibleMagicNumber, false, null);
    }

    public ReadMagic(String fileName, String possibleMagicNumber, boolean detectContentSupport, byte[] content) {
        this.fileName = fileName;
        this.possibleMagicNumber = possibleMagicNumber;
        this.detectContentSupport = detectContentSupport;
        if (content != null && content.length > 0) {
            this.content = content;
        } else {
            this.content = new byte[0];
        }
    }

}
