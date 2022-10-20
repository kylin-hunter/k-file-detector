package com.kylinhunter.file.detector.signature;

import java.util.Map;
import java.util.Set;

import com.kylinhunter.file.detector.config.ExtensionMagics;
import com.kylinhunter.file.detector.config.FileType;
import com.kylinhunter.file.detector.config.Magic;
import com.kylinhunter.file.detector.config.MagicConfigLoader;
import com.kylinhunter.file.detector.constant.MagicRisk;

import lombok.Data;

/**
 * @author BiJi'an
 * @description 参考 https://www.garykessler.net/library/file_sigs.html
 * @date 2022-10-02 14:42
 **/
@Data
public class MagicHelper {

    private static final MagicManager MAGIC_MANAGER = new MagicManager();

    static {
        init();
    }

    /**
     * @return void
     * @title init
     * @description
     * @author BiJi'an
     * @date 2022-10-21 02:47
     */
    private static void init() {
        MagicConfigLoader.MagicConfig magicConfig = MagicConfigLoader.load();
        MAGIC_MANAGER.init(magicConfig);

    }

    /**
     * @param extension
     * @return java.util.Set<java.lang.String>
     * @throws
     * @title get ALL_MAGICS
     * @description
     * @author BiJi'an
     * @date 2022-10-02 16:21
     */
    public static ExtensionMagics getExtensionMagics(String extension) {
        return MAGIC_MANAGER.getExtensionMagics(extension);

    }

    public static ExtensionMagics getExtensionMagics(FileType fileType) {
        return MAGIC_MANAGER.getExtensionMagics(fileType);
    }

    public static Set<Magic> getMagics(MagicRisk magicRisk) {
        return MAGIC_MANAGER.getMagics(magicRisk);
    }

    /**
     * @return java.util.Map<java.lang.String, java.util.Set < com.kylinhunter.file.detector.config.Magic>>
     * @throws
     * @title getEXPLICIT_EXTENSION_MAGICS
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:55
     */
    public static Map<String, ExtensionMagics> getAllExtensionMagics() {
        return MAGIC_MANAGER.getAllExtensionMagics();
    }

    /**
     * @return java.util.Map<java.lang.String, com.kylinhunter.file.detector.config.Magic>
     * @throws
     * @title getMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:54
     */
    public static Map<String, Magic> getAllMagics() {
        return MAGIC_MANAGER.getAllMagics();

    }

    /**
     * @param number
     * @return java.util.Set<java.lang.String>
     * @throws
     * @title getExtensions
     * @description
     * @author BiJi'an
     * @date 2022-10-03 23:10
     */
    public static Magic getMagic(String number) {
        return MAGIC_MANAGER.getMagic(number);
    }

    public static int getMagicMaxLength() {
        return MAGIC_MANAGER.getMagicMaxLength();
    }
}
