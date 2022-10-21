package com.kylinhunter.plat.file.detector;

import com.kylinhunter.plat.file.detector.extension.ExtensionManager;
import com.kylinhunter.plat.file.detector.magic.MagicManager;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-22 01:48
 **/
public class ConfigurationManager {
    private static final MagicManager MAGIC_MANAGER = new MagicManager();
    private static final ExtensionManager EXTENSION_MANAGER = MAGIC_MANAGER.getExtensionManager();

    public static MagicManager getMagicManager() {
        return MAGIC_MANAGER;
    }

    public static ExtensionManager getExtensionManager() {
        return EXTENSION_MANAGER;
    }
}
