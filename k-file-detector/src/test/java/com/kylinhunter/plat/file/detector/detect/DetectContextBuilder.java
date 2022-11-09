package com.kylinhunter.plat.file.detector.detect;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.compress.utils.Lists;

import com.kylinhunter.plat.file.detector.content.bean.DetectConext;
import com.kylinhunter.plat.file.detector.common.component.CF;
import com.kylinhunter.plat.file.detector.magic.bean.ReadMagic;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.file.FileTypeManager;
import com.kylinhunter.plat.file.detector.file.bean.FileType;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-01 10:57
 **/
class DetectContextBuilder {
    private final List<Magic> magics;
    private final DetectConext detectConext;
    private final FileTypeManager fileTypeManager = CF.get(FileTypeManager.class);

    public DetectContextBuilder() {

        this.magics = Lists.newArrayList();
        detectConext = new DetectConext();
        detectConext.setReadMagic(new ReadMagic());
        detectConext.setDetectedMagics(this.magics);
    }

    public DetectContextBuilder setPossibleMagicNumber(String possibleMagicNumber) {
        detectConext.getReadMagic().setPossibleMagicNumber(possibleMagicNumber);
        return this;
    }

    public DetectContextBuilder setExtension(String extension) {
        detectConext.setExtension(extension);
        return this;
    }

    public DetectContextBuilder setFileName(String fileName) {
        detectConext.setFileName(fileName);
        return this;
    }

    public DetectContextBuilder add(int magicId, boolean extensionMustHitAsFather, int offset, String number,
                                    String... fileTypeIds) {
        Magic magic = new Magic();
        List<FileType> fileTypes = Lists.newArrayList();
        magic.setFileTypes(fileTypes);
        magic.setId(magicId);
        magic.setOffset(offset);
        magic.setExtensionMustHitAsFather(extensionMustHitAsFather);
        if (number == null || number.length() <= 0 || number.length() % 2 != 0) {
            throw new DetectException("invalid number=>" + number);
        } else {
            magic.setNumber(number);
            magic.setLength(number.length() / 2);
        }
        Stream.of(fileTypeIds).forEach(fileTypeId -> {
            FileType fileType = fileTypeManager.getFileTypeById(fileTypeId);
            if (fileType == null) {
                throw new DetectException("invalid fileTypeId=>" + fileTypeId);

            } else {
                fileTypes.add(fileType);
            }
        });

        magics.add(magic);
        return this;
    }

    public DetectConext get() {
        return detectConext;
    }
}
