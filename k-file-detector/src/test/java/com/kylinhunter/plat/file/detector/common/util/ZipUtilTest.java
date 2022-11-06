package com.kylinhunter.plat.file.detector.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ZipUtilTest {

    @Test
    @Order(1)
    public void testDoZip() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        List<File> fileList = Arrays.stream(files).collect(Collectors.toList());
        File file = UserDirUtil.getFile("/tmp/testzip.zip", false);
        FileUtils.delete(file);
        ZipUtil.zip(fileList, file);
    }

    @Test
    @Order(2)
    public void testUnZip() throws IOException {
        File file = UserDirUtil.getFile("/tmp/testzip.zip", false);
        File dir = UserDirUtil.getDir("/tmp/testzip", true);
        FileUtils.forceDelete(dir);
        ZipUtil.unzip(file, dir);
    }

    @Test
    @Order(3)
    public void unzipOffice() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);

        for (File file : files) {
            File office = UserDirUtil.getDir("/tmp/office/" + file.getName(), true);

            ZipUtil.unzip(file, office);

        }
    }
}