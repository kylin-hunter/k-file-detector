package com.kylinhunter.file.detector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.kylinhunter.file.detector.bean.DetectConext;
import com.kylinhunter.file.detector.bean.DetectOption;
import com.kylinhunter.file.detector.constant.SafeStatus;
import com.kylinhunter.file.detector.extension.FileType;
import com.kylinhunter.file.detector.extension.FileTypeConfigManager;
import com.kylinhunter.file.detector.util.ResourceHelper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorTest {

    @Test
    @Order(1)
    void checkDefault() {
        File dir = ResourceHelper.getFileInClassPath("files/safe");
        System.out.println(dir.getAbsolutePath());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile() && file.getName().indexOf(".") > 0) {
                DetectConext detectConext = FileDetector.detect(file);
                System.out.println(file.getName() + "=>" + detectConext.getSafeStatus());
                if (FileTypeConfigManager.getExtensionManager().isDanger(FilenameUtils.getExtension(file.getName()))) {
                    Assertions.assertEquals(SafeStatus.DANGEROUS_EXTENSION, detectConext.getSafeStatus());
                } else {
                    Assertions.assertEquals(SafeStatus.SAFE, detectConext.getSafeStatus());
                }

            }
        }
    }

    @Test
    @Order(2)
    void checkDangerExtension() {
        File dir = ResourceHelper.getFileInClassPath("files/special/danger_extension");
        DetectOption detectOption = DetectOption.custom()
                .addDangerousExtensionIncludes("doc")
                .addDangerousExtensionExcludes("sh");
        System.out.println(dir.getAbsolutePath());

        File file1 = new File(dir, "exe.exe");
        DetectConext detectConext = FileDetector.detect(file1);
        System.out.println(file1.getName() + "=>" + detectConext.getSafeStatus());
        Assertions.assertEquals(SafeStatus.DANGEROUS_EXTENSION, detectConext.getSafeStatus());

        detectConext = FileDetector.detect(file1, detectOption);
        System.out.println(file1.getName() + "=>" + detectConext.getSafeStatus());
        Assertions.assertEquals(SafeStatus.DANGEROUS_EXTENSION, detectConext.getSafeStatus());

        File file2 = new File(dir, "sh.sh");
        detectConext = FileDetector.detect(file2);
        System.out.println(file2.getName() + "=>" + detectConext.getSafeStatus());
        Assertions.assertEquals(SafeStatus.DANGEROUS_EXTENSION, detectConext.getSafeStatus());
        detectConext = FileDetector.detect(file2, detectOption);
        System.out.println(file2.getName() + "=>" + detectConext.getSafeStatus());
        Assertions.assertEquals(SafeStatus.UNKNOWN, detectConext.getSafeStatus());

        File file3 = new File(dir, "doc.doc");
        detectConext = FileDetector.detect(file3);
        System.out.println(file3.getName() + "=>" + detectConext.getSafeStatus());
        Assertions.assertEquals(SafeStatus.SAFE, detectConext.getSafeStatus());
        detectConext = FileDetector.detect(file3, detectOption);
        System.out.println(file3.getName() + "=>" + detectConext.getSafeStatus());
        Assertions.assertEquals(SafeStatus.DANGEROUS_EXTENSION, detectConext.getSafeStatus());
    }

    @Test
    @Order(3)
    void checkDisguise() {
        File dir = ResourceHelper.getFileInClassPath("files/special/disguise");
        System.out.println(dir.getAbsolutePath());
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> file.isFile() && file.getName().indexOf(".") > 0).forEach(file -> {
            DetectConext detectConext = FileDetector.detect(file);
            System.out.println(file.getName() + "=>" + detectConext.getSafeStatus());
            Assertions.assertEquals(SafeStatus.DISGUISE, detectConext.getSafeStatus());
        });

    }

    @Test
    @Order(4)
    void checkDisguiseWarn() {
        File dir = ResourceHelper.getFileInClassPath("files/special/disguise_warn");
        System.out.println(dir.getAbsolutePath());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile() && file.getName().indexOf(".") > 0) {
                DetectConext detectConext = FileDetector.detect(file);
                System.out.println(file.getName() + "=>" + detectConext.getSafeStatus());
                Assertions.assertEquals(SafeStatus.DISGUISE_WARN, detectConext.getSafeStatus());
            }
        }
    }

    @Test
    @Order(5)
    void checkDangerContent() {
        File dir = ResourceHelper.getFileInClassPath("files/special/danger_content");
        System.out.println(dir.getAbsolutePath());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {
                DetectConext detectConext = FileDetector.detect(file);
                System.out.println(file.getName() + "=>" + detectConext.getSafeStatus());
                Assertions.assertEquals(SafeStatus.DANGEROUS_CONTENT, detectConext.getSafeStatus());
            }
        }
    }

    @Test
    @Order(6)
    void checkUnknown() {
        File dir = ResourceHelper.getFileInClassPath("files/special/unknown");
        System.out.println(dir.getAbsolutePath());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile() && file.getName().indexOf(".") > 0) {
                DetectConext detectConext = FileDetector.detect(file);
                System.out.println(file.getName() + "=>" + detectConext.getSafeStatus());
                Assertions.assertEquals(SafeStatus.UNKNOWN, detectConext.getSafeStatus());
            }
        }
    }

    @Test
    @Order(7)
    void checkUnrecognized() {
        File dir = ResourceHelper.getFileInClassPath("files/special/unrecognized");
        System.out.println(dir.getAbsolutePath());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile() && file.getName().indexOf(".") > 0) {
                DetectConext detectConext = FileDetector.detect(file);
                System.out.println(file.getName() + "=>" + detectConext.getSafeStatus());
                Assertions.assertEquals(SafeStatus.SAFE, detectConext.getSafeStatus());
            }
        }
    }

    @Test
    @Order(8)
    void checkAll() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/safe");
        System.out.println(dir.getAbsolutePath());
        File dirTmp = new File(System.getProperty("user.dir"), "tmp/files");
        if (!dirTmp.exists()) {
            if (!dirTmp.mkdirs()) {
                throw new IOException("mkdirs error");
            }
        }

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {

                for (FileType fileType : FileTypeConfigManager.getExtensionManager().getAllFileTypes().values()) {
                    File fileTmp = new File(dirTmp, file.getName() + "." + fileType.getExtension());
                    if (!fileTmp.exists()) {
                        FileUtils.copyFile(file, fileTmp);
                    }

                    DetectConext detectConext = FileDetector.detect(fileTmp);
                    System.out.println(fileTmp.getName() + "=>" + detectConext.getSafeStatus());
                }
            }

        }
    }
}