package com.kylinhunter.plat.file.detector.component;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.common.component.ComponentFactory;
import com.kylinhunter.plat.file.detector.common.util.FilenameUtil;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.config.bean.FileType;

class MSOfficeDetectorTest {

    private static final MSOfficeDetector msOfficeDetector = ComponentFactory.get(MSOfficeDetector.class);

    @Test
    void check() {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);

        for (File file : files) {
            System.out.println("file=>" + file.getAbsolutePath());
            FileType fileType = msOfficeDetector.check(file);
            System.out.println("fileType=>" + fileType);
            Assertions.assertEquals(FilenameUtil.getExtension(file.getName()), fileType.getExtension());

        }

    }
}