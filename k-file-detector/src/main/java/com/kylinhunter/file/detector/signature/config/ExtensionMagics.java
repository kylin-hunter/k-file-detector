package com.kylinhunter.file.detector.signature.config;

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
 * @date 2022-10-12 19:55
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExtensionMagics {
    @EqualsAndHashCode.Include
    private String extension;
    @Setter(AccessLevel.NONE)
    private Set<Magic> explicitMagics = new HashSet<>();

    private Set<String> tolerateExtensions;

    @Setter(AccessLevel.NONE)
    private Set<Magic> tolerateMagics;

    private int magicMaxLength = 1;

    public ExtensionMagics(String extension) {
        this.extension = extension;
    }

    public void addExplicitMagic(Magic magic) {
        explicitMagics.add(magic);
        if (magic.getByteNum() > magicMaxLength) {
            magicMaxLength = magic.getByteNum();
        }
    }

    public void addTolerateMagics(Set<Magic> magics) {
        if (tolerateMagics == null) {
            tolerateMagics = Sets.newHashSet();
        }
        tolerateMagics.addAll(magics);
    }

}
