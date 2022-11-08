package com.kylinhunter.plat.file.detector.magic;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.file.FileTypeManager;
import com.kylinhunter.plat.file.detector.file.bean.FileType;
import com.kylinhunter.plat.file.detector.magic.bean.FileTypesWrapper;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;
import com.kylinhunter.plat.file.detector.magic.constant.MagicMode;

import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 02:38
 **/
@C
public class MagicManager {

    public final static char MAGIC_SKIP_X = 'x';
    public final static char MAGIC_SKIP_N = 'n';

    @Getter
    private final Map<String, Magic> numberMagics = Maps.newHashMap(); // magic number to Magic object

    @Getter
    private final Set<Magic> allMagics = Sets.newHashSet(); // all Magic objects

    @Getter
    private final Set<Magic> contentSupportMagics = Sets.newHashSet();

    private final FileTypeManager fileTypeManager;

    @Getter
    private int magicMaxLengthWitOffset = 1;

    public MagicManager(FileTypeManager fileTypeManager) {
        this.fileTypeManager = fileTypeManager;
        MagicConfigLoader.MagicConfig magicConfig = MagicConfigLoader.load();
        magicConfig.getMagics().forEach(magic -> {
            check(magic);
            process(magic);
        });
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
        if (CollectionUtils.isEmpty(magic.getFileTypes())) {
            throw new DetectException("file types can't be empty");

        }
        int numberLen = number.length();
        if (numberLen % 2 != 0) {
            throw new DetectException("magic number must be even");
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
    private void process(Magic magic) {
        if (magic.isDetectContentSupport()) {
            contentSupportMagics.add(magic);
        }
        allMagics.add(magic);
        numberMagics.put(magic.getNumber(), magic);

        magic.setLength(magic.getNumber().length() / 2);

        if (magic.getOffset() > 0) {
            magic.setMode(MagicMode.OFFSET);
        } else {
            magic.setMode(MagicMode.PREFIX);
        }

        List<FileType> fileTypes = magic.getFileTypes();
        fileTypes = fileTypes.stream().
                map(oldFileType -> {

                    FileType newFileType = fileTypeManager.getFileTypeById(oldFileType.getId());
                    if (newFileType == null) {
                        throw new DetectException("no file type :" + oldFileType.getId());
                    }
                    newFileType.reCalMagicMaxLengthWithOffset(magic.getOffset(), magic.getLength());
                    return newFileType;
                }).collect(Collectors.toList());
        magic.setFileTypes(fileTypes);
        magic.setFileTypesWrapper(new FileTypesWrapper(fileTypes));

        if (magic.getOffset() + magic.getLength() > this.magicMaxLengthWitOffset) {
            this.magicMaxLengthWitOffset = magic.getOffset() + magic.getLength();
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

}
