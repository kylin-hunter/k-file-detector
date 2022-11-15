package io.github.kylinhunter.tools.file.detector.content.bean;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import io.github.kylinhunter.tools.file.detector.common.util.FilenameUtil;
import io.github.kylinhunter.tools.file.detector.detect.bean.SortMagic;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;
import io.github.kylinhunter.tools.file.detector.magic.bean.ReadMagic;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description a context for detect action
 * @date 2022-10-01 22:37
 **/
@Data
@NoArgsConstructor
public class DetectConext {
    private String fileName = ""; // file name
    private String extension = ""; // explicit extension
    private List<Magic> detectedMagics; // the detected magic messages
    private List<FileType> contentFileTypes; // the detected file types by content
    private ReadMagic readMagic;
    private List<SortMagic> sortMagics;

    public DetectConext(ReadMagic readMagic) {

        this.readMagic = readMagic;
        this.detectedMagics = readMagic.getDetectedMagics();
        String fileName = readMagic.getFileName();
        if (fileName != null && fileName.length() > 0) {
            this.fileName = fileName;
            this.extension = FilenameUtil.getExtension(fileName);
            if (this.extension != null) {
                this.extension = extension.toLowerCase();
            }
        }

    }

    public void addContentFileType(FileType fileType) {
        if (fileType != null) {
            if (contentFileTypes == null) {
                contentFileTypes = Lists.newArrayList();
            }
            if (!contentFileTypes.contains(fileType)) {
                contentFileTypes.add(fileType);
            }
        }

    }

    public String getPossibleMagicNumber() {
        return readMagic.getPossibleMagicNumber();
    }

}
