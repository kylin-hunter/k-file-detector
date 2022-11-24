package io.github.kylinhunter.tools.file.detector.file.bean;

import java.util.List;

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
    private List<String> extensions;
    private String desc;

    //  calculate by  FileTypeConfigLoader
    private FileType sameRef;
    //  calculate by  MagicManager
    private int magicMaxLengthWithOffset;

    public void reCalMagicMaxLengthWithOffset(int offset, int magicLen) {
        if (offset + magicLen > this.magicMaxLengthWithOffset) {
            this.magicMaxLengthWithOffset = offset + magicLen;
        }
    }

    public boolean extensionEquals(String extension) {

        if (extensions.size() > 0) {
            if (extensions.size() == 1) {
                return extensions.get(0).equals(extension);
            } else {
                if (this.extensions.contains(extension)) {
                    return true;
                }
            }
        }

        return false;

    }

    @Override
    public String toString() {
        return id + "/" + extensions;
    }

    @Override
    public int compareTo(FileType o) {
        return id.compareTo(o.id);
    }
}