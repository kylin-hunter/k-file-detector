package com.kylinhunter.plat.file.detector.magic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.bean.DetectConext;
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

public class MagicManager {
    @Getter
    private final Map<String, Magic> numberMagics = Maps.newHashMap();
    @Getter
    private final Set<Magic> allMagics = Sets.newHashSet();
    private final MagicConfigLoader.MagicConfig magicConfig;
    @Getter
    private final FileTypeManager fileTypeManager;

    public MagicManager() {
        this.fileTypeManager = new FileTypeManager();
        this.magicConfig = MagicConfigLoader.load(fileTypeManager);

        magicConfig.getMagics().forEach(magic -> {
            check(magic);
            processExProperties(magic);
            processBasic(magic);

        });

    }

    /**
     * @param magic magic
     * @return void
     * @title processBasic
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:19
     */
    private void processBasic(Magic magic) {
        allMagics.add(magic);
        numberMagics.put(magic.getNumber(), magic);
    }

    /**
     * @param magic magic
     * @return void
     * @title check
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:12
     */
    private void check(Magic magic) {
        String number = magic.getNumber();
        if (StringUtils.isEmpty(number)) {
            throw new DetectException("number can't be empty");

        }
        if (allMagics.contains(magic)) {
            throw new DetectException("duplicated magic number:" + number);
        }
    }

    /**
     * @param magic magic
     * @return void
     * @title processBasic
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:13
     */
    private void processExProperties(Magic magic) {
        String number = magic.getNumber();

        int numberLen = number.length();
        if (numberLen % 2 != 0) {
            throw new DetectException("magic number must be even");
        }

        int magicLength = numberLen / 2;
        magic.setMagicLength(magicLength);

        Set<String> fileTypeIds = magic.getFileTypeIds();
        if (fileTypeIds != null) {
            Set<String> extensions = Sets.newHashSet();
            Set<FileType> fileTypes = Sets.newHashSet();

            fileTypeIds.forEach(id -> {
                FileType fileType = fileTypeManager.getFileTypeById(id);
                if (fileType == null) {
                    throw new DetectException("no file type :" + id);
                }
                fileType.reCalMaxMagicLen(magic.getMagicLength());
                extensions.add(fileType.getExtension());
                fileTypes.add(fileType);

            });
            magic.setExtensions(extensions);
            magic.setFileTypes(fileTypes);
        }

        if (number.contains("_")) {
            magic.setMatchMode(MagicMatchMode.PREFIX_FUZZY);
        } else {
            magic.setMatchMode(MagicMatchMode.PREFIX);

        }

        if (magicLength > magicConfig.getMagicMaxLength()) {
            magicConfig.setMagicMaxLength(magicLength);
        }
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
     * @param detectConext detectConext
     * @return java.util.Set<com.kylinhunter.plat.file.detector.magic.Magic>
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 21:18
     */

    public void detect(DetectConext detectConext) {
        String possibleMagicNumber = detectConext.getPossibleMagicNumber();
        List<Magic> detectedMagics = Lists.newArrayList();
        detectConext.setDetectedMagics(detectedMagics);
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
    }

}
