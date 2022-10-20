package com.kylinhunter.file.detector.config;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

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
    private Set<String> tolerateExtensions;
    @Setter(AccessLevel.NONE)
    private Set<Magic> tolerateMagics;
    private int magicMaxLength = 1;

    public ExtensionMagics(String extension) {
        this.extension = extension;
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

    /**
     * @param magics magics
     * @return void
     * @title addTolerateMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-21 02:07
     */
    public void addTolerateMagics(Set<Magic> magics) {
        if (tolerateMagics == null) {
            tolerateMagics = Sets.newHashSet();
        }
        tolerateMagics.addAll(magics);
    }

}
