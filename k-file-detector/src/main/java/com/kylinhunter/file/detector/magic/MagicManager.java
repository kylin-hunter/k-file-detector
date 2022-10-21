package com.kylinhunter.file.detector.magic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.constant.MagicRisk;
import com.kylinhunter.file.detector.extension.FileType;
import com.kylinhunter.file.detector.extension.FileTypeConfigManager;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 02:38
 **/
public class MagicManager {
    private final Map<String, ExtensionMagics> extensionMagics = new HashMap<>();
    private final Map<String, Magic> allMagics = Maps.newHashMap();
    private MagicConfigManager.MagicConfig magicConfig;
    private final Map<MagicRisk, Set<Magic>> riskMagics = new HashMap<>();

    public void init(MagicConfigManager.MagicConfig magicConfig) {
        this.magicConfig = magicConfig;
        magicConfig.getMagics().forEach(magic -> {
            allMagics.put(magic.getNumber(), magic);
            magic.getFileTypes().forEach(fileType -> extensionMagics
                    .compute(fileType.getExtension(), (extension, extensionMagics) -> {
                        if (extensionMagics == null) {
                            extensionMagics = new ExtensionMagics(extension);
                        }
                        extensionMagics.addMagic(magic);
                        return extensionMagics;
                    }));

            riskMagics.compute(magic.getRisk(), (risk, magics) -> {
                if (magics == null) {
                    magics = Sets.newHashSet();
                }
                magics.add(magic);
                return magics;
            });

        });

        extensionMagics.forEach((extension, extensionMagics) -> {

            Set<String> tolerateExtensions =
                    FileTypeConfigManager.getExtensionManager().getFileType(extension).getTolerateExtensions();
            if (tolerateExtensions != null && tolerateExtensions.size() > 0) {
                extensionMagics.setTolerateExtensions(tolerateExtensions);

                for (String tolerateExtension : tolerateExtensions) {
                    ExtensionMagics tmpExtensionMagics = this.extensionMagics.get(tolerateExtension);
                    extensionMagics.addTolerateMagics(tmpExtensionMagics.getMagics());

                }

            }

        });
    }

    /**
     * @param extension extension
     * @return java.util.Set<java.lang.String>
     * @title get allMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-02 16:21
     */
    public ExtensionMagics getExtensionMagics(String extension) {
        return extensionMagics.get(extension);

    }

    /**
     * @param fileType fileType
     * @return com.kylinhunter.file.detector.magic.ExtensionMagics
     * @title getExtensionMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:34
     */
    public ExtensionMagics getExtensionMagics(FileType fileType) {
        if (fileType != null) {
            return extensionMagics.get(fileType.getExtension());
        }
        return null;
    }

    /**
     * @param magicRisk magicRisk
     * @return java.util.Set<com.kylinhunter.file.detector.magic.Magic>
     * @title getMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:34
     */
    @SuppressWarnings("unchecked")
    public Set<Magic> getMagics(MagicRisk magicRisk) {
        if (magicRisk != null) {
            return riskMagics.getOrDefault(magicRisk, Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * @return java.util.Map<java.lang.String, java.util.Set < com.kylinhunter.file.detector.magic.Magic>>
     * @title getEXPLICIT_EXTENSION_MAGICS
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:55
     */
    public Map<String, ExtensionMagics> getAllExtensionMagics() {
        return extensionMagics;
    }

    /**
     * @return java.util.Map<java.lang.String, com.kylinhunter.file.detector.magic.Magic>
     * @title getMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:54
     */
    public Map<String, Magic> getAllMagics() {
        return allMagics;

    }

    /**
     * @param number number
     * @return java.util.Set<java.lang.String>
     * @title getExtensions
     * @description
     * @author BiJi'an
     * @date 2022-10-03 23:10
     */
    public Magic getMagic(String number) {
        return allMagics.get(number);
    }

    /**
     * @return int
     * @title getMagicMaxLength
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:34
     */
    public int getMagicMaxLength() {
        return magicConfig.getMagicMaxLength();
    }
}
