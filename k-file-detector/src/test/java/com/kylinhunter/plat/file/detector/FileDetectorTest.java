package com.kylinhunter.plat.file.detector;

import static com.kylinhunter.plat.file.detector.constant.SecurityStatus.DANGEROUS_CONTENT;
import static com.kylinhunter.plat.file.detector.constant.SecurityStatus.DANGEROUS_EXTENSION;
import static com.kylinhunter.plat.file.detector.constant.SecurityStatus.DISGUISE;
import static com.kylinhunter.plat.file.detector.constant.SecurityStatus.DISGUISE_WARN;
import static com.kylinhunter.plat.file.detector.constant.SecurityStatus.SAFE;
import static com.kylinhunter.plat.file.detector.constant.SecurityStatus.UNKNOWN;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.bean.DetectOption;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.bean.FileSecurity;
import com.kylinhunter.plat.file.detector.extension.ExtensionFile;
import com.kylinhunter.plat.file.detector.extension.ExtensionManager;
import com.kylinhunter.plat.file.detector.magic.Magic;
import com.kylinhunter.plat.file.detector.util.MultipartFileHelper;
import com.kylinhunter.plat.file.detector.util.ResourceHelper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorTest {
    private static final ExtensionManager extensionManager = CommonManager.getExtensionManager();

    private void printDetectResult(DetectResult detectResult) {
        Set<Magic> detectedMagics = detectResult.getDetectedMagics();
        System.out.println("===============================================");
        System.out.println("fileName=>" + detectResult.getFileName());
        System.out.println("************detected magics********************");

        if (CollectionUtils.isNotEmpty(detectedMagics)) {
            int index = 0;
            for (Magic magic : detectedMagics) {
                index++;
                System.out.println("\t magic[" + index + "] 's number=>" + magic.getNumber());
                System.out.println("\t magic[" + index + "] 's extensions=>" + magic.getExtensions());

            }

        } else {
            System.out.println("\t can't detete magic numbers");

        }
        System.out.println("##########file  security######################");

        FileSecurity fileSecurity = detectResult.getFileSecurity();
        System.out.println("\t securityStatus=>" + fileSecurity.getSecurityStatus());
        System.out.println("\t securityDesc=>" + fileSecurity.getDesc());
        System.out.println("===============================================");
        System.out.println();

    }

    @Test
    @Order(1)
    void checkDefault() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/basic");
        File[] files = Objects.requireNonNull(dir.listFiles());
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile() && file.getName().indexOf(".") > 0) {
                int index = i % 4;
                DetectResult detectResult;
                if (index == 0) {
                    detectResult = FileDetector.detect(file);

                } else if (index == 1) {
                    MultipartFile multipartFile = MultipartFileHelper.getMultipartFile(file);
                    detectResult = FileDetector.detect(multipartFile);
                } else if (index == 2) {
                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    detectResult = FileDetector.detect(bytes, file.getName());
                } else {
                    FileInputStream inputStream = FileUtils.openInputStream(file);
                    detectResult = FileDetector.detect(inputStream, file.getName());
                }
                printDetectResult(detectResult);
                if (extensionManager.isDanger(FilenameUtils.getExtension(file.getName()))) {
                    Assertions.assertEquals(DANGEROUS_EXTENSION, detectResult.getSafeStatus());
                } else {
                    Assertions.assertEquals(SAFE, detectResult.getSafeStatus());
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

        File file1 = new File(dir, "exe.exe");
        DetectResult detectResult = FileDetector.detect(file1);
        printDetectResult(detectResult);
        Assertions.assertEquals(DANGEROUS_EXTENSION, detectResult.getSafeStatus());

        detectResult = FileDetector.detect(file1, detectOption);
        printDetectResult(detectResult);
        Assertions.assertEquals(DANGEROUS_EXTENSION, detectResult.getSafeStatus());

        File file2 = new File(dir, "sh.sh");
        detectResult = FileDetector.detect(file2);
        printDetectResult(detectResult);
        Assertions.assertEquals(DANGEROUS_EXTENSION, detectResult.getSafeStatus());
        detectResult = FileDetector.detect(file2, detectOption);
        printDetectResult(detectResult);
        Assertions.assertEquals(UNKNOWN, detectResult.getSafeStatus());

        File file3 = new File(dir, "doc.doc");
        detectResult = FileDetector.detect(file3);
        printDetectResult(detectResult);
        Assertions.assertEquals(SAFE, detectResult.getSafeStatus());
        detectResult = FileDetector.detect(file3, detectOption);
        printDetectResult(detectResult);
        Assertions.assertEquals(DANGEROUS_EXTENSION, detectResult.getSafeStatus());
    }

    @Test
    @Order(3)
    void checkDisguise() {
        File dir = ResourceHelper.getFileInClassPath("files/special/disguise");
        System.out.println(dir.getAbsolutePath());
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> file.isFile() && file.getName().indexOf(".") > 0).forEach(file -> {
            DetectResult detectResult = FileDetector.detect(file);
            printDetectResult(detectResult);
            Assertions.assertEquals(DISGUISE, detectResult.getSafeStatus());
        });

    }

    @Test
    @Order(4)
    void checkDisguiseWarn() {
        File dir = ResourceHelper.getFileInClassPath("files/special/disguise_warn");
        System.out.println(dir.getAbsolutePath());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile() && file.getName().indexOf(".") > 0) {
                DetectResult detectResult = FileDetector.detect(file);
                printDetectResult(detectResult);
                Assertions.assertEquals(DISGUISE_WARN, detectResult.getSafeStatus());
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
                DetectResult detectResult = FileDetector.detect(file);
                printDetectResult(detectResult);
                Assertions.assertEquals(DANGEROUS_CONTENT, detectResult.getSafeStatus());
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
                DetectResult detectResult = FileDetector.detect(file);
                printDetectResult(detectResult);
                Assertions.assertEquals(UNKNOWN, detectResult.getSafeStatus());
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
                DetectResult detectResult = FileDetector.detect(file);
                printDetectResult(detectResult);
                Assertions.assertEquals(SAFE, detectResult.getSafeStatus());
            }
        }
    }

    @Test
    @Order(8)
    void checkAll() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/basic");
        System.out.println(dir.getAbsolutePath());
        File dirTmp = new File(System.getProperty("user.dir"), "tmp/files");
        if (!dirTmp.exists()) {
            if (!dirTmp.mkdirs()) {
                throw new IOException("mkdirs error");
            }
        }

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {

                for (ExtensionFile extensionFile : extensionManager.getAllFileTypes().values()) {
                    File fileTmp = new File(dirTmp, file.getName() + "." + extensionFile.getExtension());
                    if (!fileTmp.exists()) {
                        FileUtils.copyFile(file, fileTmp);
                    }

                    DetectResult detectResult = FileDetector.detect(fileTmp);
                    System.out.println(fileTmp.getName() + "=>" + detectResult.getSafeStatus());
                }
            }

        }
    }
}