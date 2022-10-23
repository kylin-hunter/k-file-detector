package com.kylinhunter.plat.file.detector.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.constant.MagicMatchMode;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.type.FileType;
import com.kylinhunter.plat.file.detector.type.FileTypeManager;

import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 02:38
 **/
@Getter
public class MagicManager {
    private final Map<String, Magic> numberMagics = Maps.newHashMap();
    private final Map<String, Set<Magic>> fileTypeMagics = Maps.newHashMap();

    private final Set<Magic> allMagics = Sets.newHashSet();
    private final MagicConfigLoader.MagicConfig magicConfig;
    private final FileTypeManager fileTypeManager;

    public MagicManager() {
        this.fileTypeManager = new FileTypeManager();
        this.magicConfig = MagicConfigLoader.load(fileTypeManager);
        Map<FileType, ExtensionMagics> allExtensionMagics = new HashMap<>();
        Map<FileType, TolerateMagics> allTolerateMagics = new HashMap<>();

        magicConfig.getMagics().forEach(magic -> {
            String number = magic.getNumber();
            if (allMagics.contains(magic)) {
                throw new DetectException("duplicated magic number:" + number);
            }
            allMagics.add(magic);
            numberMagics.put(number, magic);
            Set<FileType> fileTypes = magic.getFileTypes();
            fileTypes.forEach(fileType -> {

                        fileTypeMagics.compute(fileType.getId(), (k, v) -> {
                            if (v == null) {
                                v = Sets.newHashSet();
                            }
                            v.add(magic);
                            return v;
                        });

                        fileType.setExtensionMagics(allExtensionMagics
                                .compute(fileType, (k, extensionMagics) -> {
                                    if (extensionMagics == null) {
                                        extensionMagics = new ExtensionMagics(k);
                                    }
                                    extensionMagics.addMagic(magic);
                                    return extensionMagics;
                                }));
                    }

            );

        });


        fileTypeManager.getAllFileTypes().forEach(fileType -> {


            fileType.setTolerateMagics(allTolerateMagics
                    .compute(fileType, (k, tolerateMagics) -> {
                        if (tolerateMagics == null) {
                            tolerateMagics = new TolerateMagics(fileType);
                        }


                        Set<FileType> tolerateFileTypes = fileType.getTolerateFileTypes();

                        if (!CollectionUtils.isEmpty(tolerateFileTypes)) {

                            TolerateMagics finalTolerateMagics = tolerateMagics;
                            tolerateFileTypes.forEach(ft -> {
                                Set<Magic> magics = fileTypeMagics.get(ft.getId());
                                finalTolerateMagics.addMagic(magics);
                                finalTolerateMagics.addExtension(ft.getExtension());
                            });

                        }

                        return tolerateMagics;
                    }));
        });

        allExtensionMagics.forEach((fileType, extensionMagics) -> {



        });
    }

    /**
     * @return java.util.Map<java.lang.String, com.kylinhunter.file.detector.magic.Magic>
     * @title getAllMagics
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:54
     */
    public Map<String, Magic> getNumberMagics() {
        return numberMagics;

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
        return numberMagics.get(number);
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
            for (Magic magic : allMagics) {
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

}
