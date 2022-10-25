package com.kylinhunter.plat.file.detector.manager;

import com.kylinhunter.plat.file.detector.magic.MagicManager;
import com.kylinhunter.plat.file.detector.type.FileTypeManager;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MType {

    FILE_TYPE(FileTypeManager.class),
    MAGIC(MagicManager.class);

    @Getter
    private final Class<?> clazz;

}
