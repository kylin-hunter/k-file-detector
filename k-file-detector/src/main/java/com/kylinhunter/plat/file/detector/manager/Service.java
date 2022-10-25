package com.kylinhunter.plat.file.detector.manager;

import com.kylinhunter.plat.file.detector.magic.MagicDetectService;
import com.kylinhunter.plat.file.detector.magic.MagicConfigService;
import com.kylinhunter.plat.file.detector.type.FileTypeConfigService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Service {

    FILE_TYPE(FileTypeConfigService.class),
    MAGIC(MagicConfigService.class),
    MAGIC1(MagicDetectService.class);

    @Getter
    private final Class<?> clazz;

}
