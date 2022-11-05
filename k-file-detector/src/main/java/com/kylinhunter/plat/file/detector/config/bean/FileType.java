package com.kylinhunter.plat.file.detector.config.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FileType implements Comparable<FileType> {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    String id;
    private String extension;
    private String desc;
    private int magicMaxLengthWithOffset;

    public void reCalMagicMaxLengthWithOffset(int offset, int magicLen) {
        if (offset + magicLen > this.magicMaxLengthWithOffset) {
            this.magicMaxLengthWithOffset = offset + magicLen;
        }
    }

    public boolean extensionEquals(String extension, String... extensions) {
        if (this.extension != null && this.extension.length() > 0) {
            if (this.extension.equals(extension)) {
                return true;
            } else {
                for (String tmpExtension : extensions) {
                    if (this.extension.equals(tmpExtension)) {
                        return true;
                    }
                }
            }

        }
        return false;

    }

    @Override
    public String toString() {
        return id + "/" + extension;
    }

    @Override
    public int compareTo(FileType o) {
        return id.compareTo(o.id);
    }
}