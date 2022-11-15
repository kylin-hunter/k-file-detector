package io.github.kylinhunter.tools.file.detector.content.content;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import io.github.kylinhunter.tools.file.detector.content.bean.DetectConext;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.ReadMagic;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-13 13:10
 **/
public abstract class AbstractContentDetector implements ContentDetector {
    FileType[] EMTPY = new FileType[0];

    @Override
    public FileType[] detect(DetectConext detectConext) {
        ReadMagic readMagic = detectConext.getReadMagic();
        return detect(readMagic.getContent());

    }

    public FileType[] detect(File file) {
        try {
            return detect(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            throw new DetectException("check error", e);

        }
    }

    @Override
    public FileType[] detect(byte[] bytes) {

        if (bytes != null && bytes.length > 0) {
            return detectContent(bytes);
        }
        return EMTPY;
    }

    public abstract FileType[] detectContent(byte[] bytes);
}
