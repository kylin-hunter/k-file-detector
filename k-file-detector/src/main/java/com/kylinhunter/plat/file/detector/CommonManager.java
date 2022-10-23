package com.kylinhunter.plat.file.detector;

import com.kylinhunter.plat.file.detector.type.FileTypeManager;
import com.kylinhunter.plat.file.detector.magic.MagicManager;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-22 01:48
 **/
public class CommonManager {
    private static final MagicManager MAGIC_MANAGER = new MagicManager();
    private static final FileTypeManager EXTENSION_MANAGER = MAGIC_MANAGER.getFileTypeManager();

    public static MagicManager getMagicManager() {
        return MAGIC_MANAGER;
    }

    public static FileTypeManager getFileTypeManager() {
        return EXTENSION_MANAGER;
    }
}
