package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.ComponentFactory;
import com.kylinhunter.plat.file.detector.common.util.MultipartFileHelper;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.common.util.UserDirUtil;
import com.kylinhunter.plat.file.detector.component.FileTypeManager;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorTest {
    private final FileTypeManager fileTypeManager = ComponentFactory.get(FileTypeManager.class);

    private void printDetectResult(DetectResult detectResult) {
        List<Magic> detectedMagics = detectResult.getDetectedMagics();
        System.out.println("===============================================");
        System.out.println("\t 1、fileName=>" + detectResult.getFileName());

        System.out.print("\t 2、detectedMagics=>");
        if (CollectionUtils.isNotEmpty(detectedMagics)) {
            Set<String> numbers = detectedMagics.stream().map(Magic::getNumber).collect(Collectors.toSet());
            System.out.print(numbers);
        }
        System.out.println();
        FileType bestFileType = detectResult.getBestFileType();
        System.out.println("\t 3、bestFileType=>" + bestFileType);
        List<FileType> allPossibleFileTypes = detectResult.getAllPossibleFileTypes();
        System.out.println("\t 4、allPossibleFileTypes=>" + allPossibleFileTypes);

        System.out.println("===============================================");
        System.out.println();

    }

    @Test
    @Order(1)
    void allDetected() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(33, files.length);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
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
            String extension = FilenameUtils.getExtension(file.getName());

            FileType bestFileType = detectResult.getBestFileType();
            Assertions.assertNotNull(bestFileType);
            Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);
            if (!StringUtils.isEmpty(extension)) {
                Assertions.assertEquals(extension, bestFileType.getExtension());
            } else {
                System.out.println(file.getName()+"=>"+bestFileType);

            }

        }
    }

    @Test
    @Order(2)
    void allUndetected() {
        File dir = ResourceHelper.getFileInClassPath("files/undetected");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(1, files.length);
        for (File file : files) {
            DetectResult detectResult = FileDetector.detect(file);
            printDetectResult(detectResult);
            Assertions.assertNull(detectResult.getBestFileType());
            Assertions.assertNull(detectResult.getAllPossibleFileTypes());

        }
    }

    private List<File> disguise(File[] files, File disguiseDir) throws IOException {

        List<File> disguisFiles = Lists.newArrayList();
        for (File file : files) {
            if (file.isFile()) {
                for (String extension : fileTypeManager.getAllExtensions()) {
                    File disguisFile = new File(disguiseDir, file.getName() + "." + extension);
                    if (!disguisFile.exists()) {
                        FileUtils.copyFile(file, disguisFile);
                    }
                    disguisFiles.add(disguisFile);

                }

            }
        }
        return disguisFiles;

    }

    @Test
    @Order(3)
    void detectDisguiseAudioVideo() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/audio_video");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(9, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/audio/video", true);
        List<File> disguiseFiles = disguise(files, disguiseDir);
        Assertions.assertEquals(4176, disguiseFiles.size());

    }

    @Test
    @Order(3)
    void detectSpecialNoExtension() {
        File dir = ResourceHelper.getFileInClassPath("tmp/special/no_extension");
        System.out.println(dir.getAbsolutePath());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {
                DetectResult detectResult = FileDetector.detect(file);
                printDetectResult(detectResult);
                FileType bestFileType = detectResult.getBestFileType();

                Assertions.assertNotNull(bestFileType);
                Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);

                String fileName = detectResult.getFileName();

                switch (fileName) {
                    case "remove_extension_doc":
                        Assertions.assertTrue(bestFileType.extensionEquals("doc"));

                        break;
                    case "remove_extension_docx":
                        Assertions.assertTrue(bestFileType.extensionEquals("docx"));

                        break;
                    case "remove_extension_ppt":
                        //                        Assertions.assertTrue(bestFileType.extensionEquals("ppt"));

                        break;
                    case "remove_extension_pptx":
                        //                        Assertions.assertTrue(bestFileType.extensionEquals("pptx"));

                        break;
                    case "remove_extension_exe":
                        //                        Assertions.assertTrue(bestFileType.extensionEquals("exe"));

                        break;
                    case "linux_execute_file":
                        //                        Assertions.assertEquals("80001", bestFileType.getId());

                        break;
                    case "mac_execute_file":
                        //                        Assertions.assertEquals("70001", bestFileType.getId());/**/

                        break;
                    default:
                        throw new RuntimeException("unkown file name => " + fileName);
                }

            }
        }

    }

    //    @Test
    //    @Order(5)
    void detectAllPossible() throws IOException {
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
                for (String extension : fileTypeManager.getAllExtensions()) {
                    File fileTmp = new File(dirTmp, file.getName() + "." + extension);
                    if (!fileTmp.exists()) {
                        FileUtils.copyFile(file, fileTmp);
                    }
                    DetectResult detectResult = FileDetector.detect(fileTmp);
                    printDetectResult(detectResult);
                    Assertions.assertNotNull(detectResult.getBestFileType());
                    Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);
                }

            }
        }
    }

    @Test
    @Order(99)
    void testOne() {
        File file = ResourceHelper.getFileInClassPath("files/detected/audio_video/avi.avi");
        DetectResult detectResult = FileDetector.detect(file);
        printDetectResult(detectResult);
        Assertions.assertNotNull(detectResult.getBestFileType());
        Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);

    }

}