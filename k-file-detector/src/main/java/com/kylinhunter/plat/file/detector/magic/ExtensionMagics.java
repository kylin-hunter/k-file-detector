package com.kylinhunter.plat.file.detector.magic;

import java.util.HashSet;
import java.util.Set;

import com.kylinhunter.plat.file.detector.type.FileType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExtensionMagics {
    @EqualsAndHashCode.Include
    private String extension;
    @Setter(AccessLevel.NONE)
    private Set<Magic> magics = new HashSet<>();
    private int magicMaxLength = 1;

    public ExtensionMagics(FileType fileType) {
        this.extension = fileType.getExtension();
    }

    /**
     * @param magic magic
     * @return void
     * @title addExplicitMagic
     * @description
     * @author BiJi'an
     * @date 2022-10-21 02:07
     */
    public void addMagic(Magic magic) {
        magics.add(magic);
        if (magic.getMagicLength() > magicMaxLength) {
            magicMaxLength = magic.getMagicLength();
        }
    }

}
