package com.kylinhunter.file.detector.signature;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.config.ExtensionMagics;
import com.kylinhunter.file.detector.config.FileType;
import com.kylinhunter.file.detector.config.Magic;
import com.kylinhunter.file.detector.config.MagicConfigLoader;
import com.kylinhunter.file.detector.constant.MagicRisk;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 02:38
 **/
public class MagicManager {
    private final Map<String, ExtensionMagics> EXTENSION_MAGICS = new HashMap<>();
    private final Map<String, Magic> ALL_MAGICS = Maps.newHashMap();
    private MagicConfigLoader.MagicConfig magicConfig;
    private final Map<MagicRisk, Set<Magic>> RISK_MAGICS = new HashMap<>();

    public void init(MagicConfigLoader.MagicConfig magicConfig) {
        this.magicConfig = magicConfig;
        magicConfig.getMagics().forEach(magic -> {
            ALL_MAGICS.put(magic.getNumber(), magic);
            magic.getFileTypes().forEach(fileType -> EXTENSION_MAGICS
                    .compute(fileType.getExtension(), (extension, extensionMagics) -> {
                        if (extensionMagics == null) {
                            extensionMagics = new ExtensionMagics(extension);
                        }
                        extensionMagics.addMagic(magic);
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
                    extensionMagics.addTolerateMagics(tmpExtensionMagics.getMagics());

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
     * @date 2022-10-02 16:21
     */
    public ExtensionMagics getExtensionMagics(String extension) {
        return EXTENSION_MAGICS.get(extension);

    }

    public ExtensionMagics getExtensionMagics(FileType fileType) {
        if (fileType != null) {
            return EXTENSION_MAGICS.get(fileType.getExtension());
        }
        return null;
    }

    public Set<Magic> getMagics(MagicRisk magicRisk) {
        if (magicRisk != null) {
            return RISK_MAGICS.getOrDefault(magicRisk, Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * @return java.util.Map<java.lang.String, java.util.Set < com.kylinhunter.file.detector.config.Magic>>
     * @throws
     * @title getEXPLICIT_EXTENSION_MAGICS
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:55
     */
    public Map<String, ExtensionMagics> getAllExtensionMagics() {
        return EXTENSION_MAGICS;
    }

    /**
     * @return java.util.Map<java.lang.String, com.kylinhunter.file.detector.config.Magic>
     * @throws
     * @title getMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:54
     */
    public Map<String, Magic> getAllMagics() {
        return ALL_MAGICS;

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
    public Magic getMagic(String number) {
        return ALL_MAGICS.get(number);
    }

    public int getMagicMaxLength() {
        return magicConfig.getMagicMaxLength();
    }
}
