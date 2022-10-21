package com.kylinhunter.file.detector.magic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.constant.MagicRisk;
import com.kylinhunter.file.detector.extension.ExtensionFile;
import com.kylinhunter.file.detector.extension.ExtensionManager;

import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 02:38
 **/
@Getter
public class MagicManager {
    private final Map<String, Magic> allMagics = Maps.newHashMap();
    private MagicConfigLoader.MagicConfig magicConfig;
    private final Map<MagicRisk, Set<Magic>> riskMagics = new HashMap<>();

    private final ExtensionManager extensionManager;

    public MagicManager() {
        this.extensionManager = new ExtensionManager();
        this.magicConfig = MagicConfigLoader.load(extensionManager);
        Map<String, ExtensionMagics> allExtensionMagics = new HashMap<>();
        magicConfig.getMagics().forEach(magic -> {
            allMagics.put(magic.getNumber(), magic);
            Set<ExtensionFile> extensionFiles = magic.getExtensionFiles();
            extensionFiles.forEach(fileType -> {
                        ExtensionMagics extensionMagicsDist = allExtensionMagics
                                .compute(fileType.getExtension(), (extension, extensionMagics) -> {
                                    if (extensionMagics == null) {
                                        extensionMagics = new ExtensionMagics(extension);
                                    }
                                    extensionMagics.addMagic(magic);
                                    return extensionMagics;
                                });
                        fileType.setExtensionMagics(extensionMagicsDist);
                    }

            );

            riskMagics.compute(magic.getRisk(), (risk, magics) -> {
                if (magics == null) {
                    magics = Sets.newHashSet();
                }
                magics.add(magic);
                return magics;
            });

        });

        allExtensionMagics.forEach((extension, extensionMagics) -> {

            Set<String> tolerateExtensions =
                    extensionManager.getFileType(extension).getTolerateExtensions();
            if (tolerateExtensions != null && tolerateExtensions.size() > 0) {
                extensionMagics.setTolerateExtensions(tolerateExtensions);

                for (String tolerateExtension : tolerateExtensions) {
                    ExtensionMagics tmpExtensionMagics = allExtensionMagics.get(tolerateExtension);
                    extensionMagics.addTolerateMagics(tmpExtensionMagics.getMagics());

                }

            }

        });
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
