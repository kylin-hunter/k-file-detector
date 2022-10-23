package com.kylinhunter.plat.file.detector.magic;

import java.util.Set;

import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.type.FileType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class TolerateMagics {

    private Set<FileType> fileTypes;
    @Setter(AccessLevel.NONE)
    private Set<String> extensions;

    @Setter(AccessLevel.NONE)
    private Set<Magic> magics;

    /**
     * @param magics explictMagic
     * @return void
     * @title addTolerateMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-21 02:07
     */
    public void addMagic(Set<Magic> magics) {

        if (this.magics == null) {
            this.magics = Sets.newHashSet();
        }
        if (magics != null) {
            this.magics.addAll(magics);
        }
    }

    /**
     * @param extension extension
     * @return void
     * @title addExtension
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:28
     */
    public void addExtension(String extension) {
        if (extensions == null) {
            extensions = Sets.newHashSet();
        }
        extensions.add(extension);

    }

}
