package com.kylinhunter.plat.file.detector.magic;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class ExtensionMagics {
    @Setter(AccessLevel.NONE)
    private Set<Magic> magics;
    private int magicMaxLength = 1;

    /**
     * @param magic magic
     * @return void
     * @title addExplicitMagic
     * @description
     * @author BiJi'an
     * @date 2022-10-21 02:07
     */
    public void addMagic(Magic magic) {
        if (magics == null) {
            magics = Sets.newHashSet();
        }
        magics.add(magic);
        if (magic.getMagicLength() > magicMaxLength) {
            magicMaxLength = magic.getMagicLength();
        }
    }

}
