package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;

class MSOfficeDetectorTest {

    @Test
    void checkDocx() throws IOException {
        File file = ResourceHelper.getFileInClassPath("files/detected/office/docx/docx");
        byte[] bytes = FileUtils.readFileToByteArray(file);


    }
}