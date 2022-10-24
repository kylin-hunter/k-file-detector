package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.magic.Magic;
import com.kylinhunter.plat.file.detector.type.FileType;
import com.kylinhunter.plat.file.detector.type.FileTypeManager;
import com.kylinhunter.plat.file.detector.util.MultipartFileHelper;
import com.kylinhunter.plat.file.detector.util.ResourceHelper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorTest {
    private static final FileTypeManager fileTypeManager = CommonManager.getFileTypeManager();

    private void printDetectResult(DetectResult detectResult) {
        List<Magic> detectedMagics = detectResult.getDetectedMagics();
        System.out.println("===============================================");
        System.out.println("\t 1、fileName=>" + detectResult.getFileName());

        System.out.print("\t 2、detectedMagics=>");
        if (CollectionUtils.isNotEmpty(detectedMagics)) {
            Set<String> numbers = detectedMagics.stream().map(e -> e.getNumber()).collect(Collectors.toSet());
            System.out.print(numbers);
        }
        System.out.println();
        FileType bestFileType = detectResult.getBestFileType();
        System.out.println("\t 3、bestFileType=>" + bestFileType);
        Set<FileType> allPossibleFileTypes = detectResult.getAllPossibleFileTypes();
        System.out.println("\t 4、allPossibleFileTypes=>" + allPossibleFileTypes);

        System.out.println("===============================================");
        System.out.println();

    }

    @Test
    @Order(1)
    void detectAll() throws IOException {
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

                Assertions.assertNotNull(detectResult.getBestFileType());
                Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);

            }
        }
    }

    @Test
    @Order(2)
    void checkDisguise() {
        File dir = ResourceHelper.getFileInClassPath("files/special/disguise");
        System.out.println(dir.getAbsolutePath());
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> file.isFile() && file.getName().indexOf(".") > 0).forEach(file -> {
            DetectResult detectResult = FileDetector.detect(file);
            printDetectResult(detectResult);
            Assertions.assertNotNull(detectResult.getBestFileType());
            Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);

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
                Assertions.assertNotNull(detectResult.getBestFileType());
                Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);
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
                Assertions.assertNotNull(detectResult.getBestFileType());
                Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);
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
                Assertions.assertNull(detectResult.getBestFileType());
                Assertions.assertNull(detectResult.getAllPossibleFileTypes());
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
                Assertions.assertNotNull(detectResult.getBestFileType());
                Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);
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
                for (String extension : fileTypeManager.getExtensionToFileTypes().keySet()) {
                    File fileTmp = new File(dirTmp, file.getName() + "." + extension);
                    if (!fileTmp.exists()) {
                        FileUtils.copyFile(file, fileTmp);
                    }
                    DetectResult detectResult = FileDetector.detect(fileTmp);
                    Assertions.assertNotNull(detectResult.getBestFileType());
                    Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);
                }

            }

        }
    }
}