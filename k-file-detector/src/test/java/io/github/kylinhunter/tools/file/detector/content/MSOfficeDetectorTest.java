package io.github.kylinhunter.tools.file.detector.content;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.kylinhunter.tools.file.detector.common.component.CF;
import io.github.kylinhunter.tools.file.detector.common.util.FilenameUtil;
import io.github.kylinhunter.tools.file.detector.common.util.ResourceHelper;
import io.github.kylinhunter.tools.file.detector.content.content.MSOfficeDetector;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

class MSOfficeDetectorTest {

    private static final MSOfficeDetector msOfficeDetector = CF.get(MSOfficeDetector.class);

    @Test
    void check2007() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);

        for (File file : files) {
            System.out.println("file=>" + file.getAbsolutePath());
            FileType[] fileTypes = msOfficeDetector.detect(file);
            System.out.println("fileType=>" + fileTypes);
            Assertions.assertEquals(Collections.singletonList(FilenameUtil.getExtension(file.getName())),
                    fileTypes[0].getExtensions());

            fileTypes = msOfficeDetector.detect(FileUtils.readFileToByteArray(file));
            System.out.println("fileType=>" + fileTypes);
            Assertions.assertEquals(Collections.singletonList(FilenameUtil.getExtension(file.getName())),
                    fileTypes[0].getExtensions());

        }

    }
}