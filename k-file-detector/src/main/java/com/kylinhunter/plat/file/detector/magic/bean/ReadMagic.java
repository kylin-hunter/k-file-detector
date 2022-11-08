package com.kylinhunter.plat.file.detector.magic.bean;

import java.util.Set;

import com.google.common.collect.Sets;

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
    private Set<Magic> contentMagics;

    public ReadMagic(String fileName, String possibleMagicNumber) {
        this(fileName, possibleMagicNumber, null);
    }

    public ReadMagic(String fileName, String possibleMagicNumber, byte[] content) {
        this.fileName = fileName;
        this.possibleMagicNumber = possibleMagicNumber;
        if (content != null && content.length > 0) {
            this.content = content;
        } else {
            this.content = new byte[0];
        }
    }

    public void addContentMagic(Magic magic) {
        if (contentMagics == null) {
            contentMagics = Sets.newHashSet();
        }
        contentMagics.add(magic);
    }

    public boolean hasContentMagics() {
        return contentMagics != null && contentMagics.size() > 0;
    }

}
