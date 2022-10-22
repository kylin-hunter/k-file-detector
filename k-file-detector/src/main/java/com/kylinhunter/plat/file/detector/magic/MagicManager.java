package com.kylinhunter.plat.file.detector.magic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.bean.FileSecurity;
import com.kylinhunter.plat.file.detector.constant.MagicMatchMode;
import com.kylinhunter.plat.file.detector.constant.MagicRisk;
import com.kylinhunter.plat.file.detector.constant.SecurityStatus;
import com.kylinhunter.plat.file.detector.extension.ExtensionFile;
import com.kylinhunter.plat.file.detector.extension.ExtensionManager;

import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 02:38
 **/
@Getter
public class MagicManager {
    private final Map<String, Magic> allMagics = Maps.newHashMap();
    private final Set<Magic> magics;

    private final MagicConfigLoader.MagicConfig magicConfig;
    private final Map<MagicRisk, Set<Magic>> riskMagics = new HashMap<>();

    private final ExtensionManager extensionManager;

    public MagicManager() {
        this.extensionManager = new ExtensionManager();
        this.magicConfig = MagicConfigLoader.load(extensionManager);
        this.magics = this.magicConfig.getMagics();
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

    /**
     * @param possibleMagicNumber possibleMagicNumber
     * @return java.util.Set<com.kylinhunter.plat.file.detector.magic.Magic>
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 21:18
     */

    public Set<Magic> detect(String possibleMagicNumber) {
        Set<Magic> detectedMagics = Sets.newHashSet();
        if (!StringUtils.isEmpty(possibleMagicNumber)) {
            for (Magic magic : magics) {
                String number = magic.getNumber();
                if (magic.getMatchMode() == MagicMatchMode.PREFIX) {
                    if (possibleMagicNumber.startsWith(number)) {
                        detectedMagics.add(magic);
                    }
                } else {
                    int i;
                    for (i = 0; i < number.length(); i++) {
                        if (number.charAt(i) != '_' && number.charAt(i) != possibleMagicNumber.charAt(i)) {
                            break;
                        }
                    }
                    if (i == number.length()) {
                        detectedMagics.add(magic);
                    }
                }
            }

        }
        return detectedMagics;
    }

    public void deletecDangerousContent(FileSecurity fileSecurity, Set<Magic> detectedMagics) {

        if (!CollectionUtils.isEmpty(detectedMagics)) {
            detectedMagics.forEach(magic -> {
                if (magic.getRisk() == MagicRisk.HIGH) {
                    fileSecurity.setSecurityStatus(SecurityStatus.DANGEROUS_CONTENT);
                    fileSecurity.addDangerousMagic(magic);
                }
            });
        }

    }
}
