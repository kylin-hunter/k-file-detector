package com.kylinhunter.file.detector.signature;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.constant.MagicRisk;
import com.kylinhunter.file.detector.signature.config.ExtensionMagics;
import com.kylinhunter.file.detector.signature.config.FileType;
import com.kylinhunter.file.detector.signature.config.Magic;
import com.kylinhunter.file.detector.signature.config.MagicConfig;
import com.kylinhunter.file.detector.signature.config.MagicConfigLoader;

import lombok.Data;

/**
 * @author BiJi'an
 * @description 参考 https://www.garykessler.net/library/file_sigs.html
 * @date 2022-10-12 14:42
 **/
@Data
public class MagicHelper {

    // extension > magic number

    private static final Map<String, ExtensionMagics> EXTENSION_MAGICS = new HashMap<>();
    private static final Map<String, Magic> ALL_MAGICS = Maps.newHashMap();
    private static MagicConfig magicConfig;
    private static final Map<MagicRisk, Set<Magic>> RISK_MAGICS = new HashMap<>();

    static {
        init();
    }

    private static void init() {
        magicConfig = MagicConfigLoader.load();
        magicConfig.getMagics().forEach(magic -> {
            ALL_MAGICS.put(magic.getNumber(), magic);
            magic.getFileTypes().forEach(fileType -> EXTENSION_MAGICS
                    .compute(fileType.getExtension(), (extension, extensionMagics) -> {
                        if (extensionMagics == null) {
                            extensionMagics = new ExtensionMagics(extension);
                        }
                        extensionMagics.addExplicitMagic(magic);
                        return extensionMagics;
                    }));

            RISK_MAGICS.compute(magic.getRisk(), (risk, magics) -> {
                if (magics == null) {
                    magics = Sets.newHashSet();
                }
                magics.add(magic);
                return magics;
            });

        });

        EXTENSION_MAGICS.forEach((extension, extensionMagics) -> {

            Set<String> tolerateExtensions = FileTypeHelper.getFileType(extension).getTolerateExtensions();
            if (tolerateExtensions != null && tolerateExtensions.size() > 0) {
                extensionMagics.setTolerateExtensions(tolerateExtensions);

                for (String tolerateExtension : tolerateExtensions) {
                    ExtensionMagics tmpExtensionMagics = EXTENSION_MAGICS.get(tolerateExtension);
                    extensionMagics.addTolerateMagics(tmpExtensionMagics.getExplicitMagics());

                }

            }

        });

    }

    /**
     * @param extension
     * @return java.util.Set<java.lang.String>
     * @throws
     * @title get ALL_MAGICS
     * @description
     * @author BiJi'an
     * @date 2022-10-12 16:21
     */
    public static ExtensionMagics getExtensionMagics(String extension) {
        return EXTENSION_MAGICS.get(extension);

    }

    public static ExtensionMagics getExtensionMagics(FileType fileType) {
        if (fileType != null) {
            return EXTENSION_MAGICS.get(fileType.getExtension());
        }
        return null;
    }

    public static Set<Magic> getMagics(MagicRisk magicRisk) {
        if (magicRisk != null) {
            return RISK_MAGICS.getOrDefault(magicRisk, Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * @return java.util.Map<java.lang.String, java.util.Set < com.kylinhunter.file.detector.signature.config.Magic>>
     * @throws
     * @title getEXPLICIT_EXTENSION_MAGICS
     * @description
     * @author BiJi'an
     * @date 2022-10-14 15:55
     */
    public static Map<String, ExtensionMagics> getAllExtensionMagics() {
        return EXTENSION_MAGICS;
    }

    /**
     * @return java.util.Map<java.lang.String, com.kylinhunter.file.detector.signature.config.Magic>
     * @throws
     * @title getMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-14 15:54
     */
    public static Map<String, Magic> getAllMagics() {
        return ALL_MAGICS;

    }

    /**
     * @param number
     * @return java.util.Set<java.lang.String>
     * @throws
     * @title getExtensions
     * @description
     * @author BiJi'an
     * @date 2022-10-13 23:10
     */
    public static Magic getMagic(String number) {
        return ALL_MAGICS.get(number);
    }

    public static int getMagicMaxLength() {
        return magicConfig.getMagicMaxLength();
    }
}
