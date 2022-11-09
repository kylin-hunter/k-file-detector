package com.kylinhunter.plat.file.detector.content;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.common.component.CF;
import com.kylinhunter.plat.file.detector.common.util.FilenameUtil;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.content.content.MSOfficeDetector;
import com.kylinhunter.plat.file.detector.file.bean.FileType;

class MSOfficeDetectorTest {

    private static final MSOfficeDetector msOfficeDetector = CF.get(MSOfficeDetector.class);

    @Test
    void check2007() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);

        for (File file : files) {
            System.out.println("file=>" + file.getAbsolutePath());
            FileType fileType = msOfficeDetector.detect(file);
            System.out.println("fileType=>" + fileType);
            Assertions.assertEquals(FilenameUtil.getExtension(file.getName()), fileType.getExtension());

             fileType = msOfficeDetector.detect(FileUtils.readFileToByteArray(file));
            System.out.println("fileType=>" + fileType);
            Assertions.assertEquals(FilenameUtil.getExtension(file.getName()), fileType.getExtension());

        }

    }
}