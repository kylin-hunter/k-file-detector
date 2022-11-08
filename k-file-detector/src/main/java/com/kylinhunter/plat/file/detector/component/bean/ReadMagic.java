package com.kylinhunter.plat.file.detector.component.bean;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-07 15:14
 **/
@Data
public class ReadMagic {
    private String possibleMagicNumber;
    private byte[] content;
    private boolean checkContent;

    public ReadMagic(String possibleMagicNumber) {
        this(possibleMagicNumber, null);
    }

    public ReadMagic(String possibleMagicNumber, byte[] content) {
        this.possibleMagicNumber = possibleMagicNumber;
        if (content != null && content.length > 0) {
            this.content = content;
            this.checkContent = true;
        } else {
            this.content = new byte[0];
        }
    }
}
