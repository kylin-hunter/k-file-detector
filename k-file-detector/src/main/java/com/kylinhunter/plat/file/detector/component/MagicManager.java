package com.kylinhunter.plat.file.detector.component;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.config.Magic;
import com.kylinhunter.plat.file.detector.config.MagicConfigLoader;
import com.kylinhunter.plat.file.detector.constant.MagicMode;
import com.kylinhunter.plat.file.detector.exception.DetectException;

import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 02:38
 **/
@Component
public class MagicManager {
    private final char HEX_PLACE_HOLDER = 'x';
    @Getter
    private final Map<String, Magic> numberMagics = Maps.newHashMap(); // magic number to Magic object
    @Getter
    private final Set<Magic> allMagics = Sets.newHashSet(); // all Magic objects
    private final FileTypeManager fileTypeManager;

    @Getter
    private int magicMaxLength = 1;

    public MagicManager(FileTypeManager fileTypeManager) {
        this.fileTypeManager = fileTypeManager;
        MagicConfigLoader.MagicConfig magicConfig = MagicConfigLoader.load();
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

        //        List<String> fileTypeIds = magic.getFileTypeIds();
        //        if (fileTypeIds != null) {
        //            List<String> extensions = Lists.newArrayList();
        //            List<FileType> fileTypes = Lists.newArrayList();
        //
        //            FileType firstFileType = null;
        //            for (String id : fileTypeIds) {
        //                FileType fileType = fileTypeManager.getFileTypeById(id);
        //                if (fileType == null) {
        //                    throw new DetectException("no file type :" + id);
        //                }
        //                if (firstFileType == null) {
        //                    firstFileType = fileType;
        //                }
        //                fileType.reCalMaxMagicLen(magic.getMagicLength());
        //                extensions.add(fileType.getExtension());
        //                fileTypes.add(fileType);
        //            }
        //
        //            magic.setExtensions(extensions);
        //            magic.setFileType(firstFileType);
        //            magic.setFileTypes(fileTypes);
        //        }

        if (number.indexOf(HEX_PLACE_HOLDER) >= 0) {
            magic.setMode(MagicMode.PREFIX_FUZZY);
        } else {
            magic.setMode(MagicMode.PREFIX);

        }

        if (magicLength > this.magicMaxLength) {
            this.magicMaxLength = magicLength;
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
     * @param detectConext detectConext
     * @return com.kylinhunter.plat.file.detector.bean.DetectConext
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-26 00:56
     */
    public DetectConext detect(DetectConext detectConext) {
        String possibleMagicNumber = detectConext.getPossibleMagicNumber();
        List<Magic> detectedMagics = Lists.newArrayList();
        detectConext.setDetectedMagics(detectedMagics);
        if (!StringUtils.isEmpty(possibleMagicNumber)) {
            for (Magic magic : allMagics) {
                String number = magic.getNumber();
                if (magic.getMode() == MagicMode.PREFIX) {
                    if (possibleMagicNumber.startsWith(number)) {
                        detectedMagics.add(magic);
                    }
                } else {
                    int i;
                    for (i = 0; i < number.length(); i++) {
                        if (number.charAt(i) != HEX_PLACE_HOLDER && number.charAt(i) != possibleMagicNumber.charAt(i)) {
                            break;
                        }
                    }
                    if (i == number.length()) {
                        detectedMagics.add(magic);
                    }
                }
            }

        }
        return detectConext;
    }

}
