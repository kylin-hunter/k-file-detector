package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.ComponentFactory;
import com.kylinhunter.plat.file.detector.common.util.FilenameUtil;
import com.kylinhunter.plat.file.detector.common.util.MultipartFileHelper;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.common.util.UserDirUtil;
import com.kylinhunter.plat.file.detector.component.FileTypeManager;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorTest {
    private static final FileTypeManager fileTypeManager = ComponentFactory.get(FileTypeManager.class);

    private static final Map<String, FileType> SPECIAL_FILETYPES = Maps.newHashMap();

    @BeforeAll
    static void beforeAll() {
        SPECIAL_FILETYPES.put("linux-execute", fileTypeManager.getFileTypeById("1_29837182"));
        SPECIAL_FILETYPES.put("mac-execute", fileTypeManager.getFileTypeById("1_1600260370"));

    }

    private void printDetectResult(DetectResult detectResult) {
        List<Magic> allPossibleMagics = detectResult.getAllPossibleMagics();
        System.out.println("===============================================");
        System.out.println("\t 1、fileName=>" + detectResult.getFileName());

        System.out.print("\t 2、allPossibleMagics=>");

        if (CollectionUtils.isNotEmpty(allPossibleMagics)) {
            List<String> numbers = allPossibleMagics.stream().map(Magic::getNumber).collect(Collectors.toList());
            System.out.print(numbers);
            System.out.println();
        }

        List<Magic> allBestMagics = detectResult.getAllBestMagics();
        System.out.print("\t 3、allBestMagics=>");
        if (CollectionUtils.isNotEmpty(allBestMagics)) {
            List<String> numbers = allBestMagics.stream().map(Magic::getNumber).collect(Collectors.toList());
            System.out.print(numbers);
            System.out.println();

        }
        FileType firstFileType = detectResult.getFirstFileType();
        System.out.println("\t 4、firstFileType=>" + firstFileType);

        FileType secondFileType = detectResult.getSecondFileType();
        System.out.println("\t 5、secondFileType=>" + secondFileType);

        List<FileType> allPossibleFileTypes = detectResult.getAllPossibleFileTypes();
        System.out.println("\t 6、allPossibleFileTypes=>" + allPossibleFileTypes);

        System.out.println("===============================================");
        System.out.println();

    }

    private int assertFile(File file, DetectResult detectResult) {
        printDetectResult(detectResult);
        FileType firstFileType = detectResult.getFirstFileType();
        FileType secondFileType = detectResult.getSecondFileType();
        Assertions.assertNotNull(firstFileType);
        Assertions.assertTrue(detectResult.getAllPossibleFileTypes().size() > 0);
        String fileName = file.getName();
        fileName = fileName.replace("@", ".");
        int index = fileName.indexOf("#");
        if (index > 0) {
            fileName = fileName.substring(0, index);
        }

        String extension = FilenameUtil.getExtension(fileName);

        int result = 0;
        if (!StringUtils.isEmpty(extension)) {
            if (firstFileType.extensionEquals(extension)) {
                result = 1;
            }

        } else {

            FileType fileType = SPECIAL_FILETYPES.get(fileName);

            if (firstFileType.getId().equals(fileType.getId())) {
                result = 1;
            }

        }

        if (result <= 0) {
            if (!StringUtils.isEmpty(extension)) {
                if (secondFileType.extensionEquals(extension)) {
                    result = 2;
                }

            } else {
                FileType fileType = SPECIAL_FILETYPES.get(fileName);
                if (secondFileType.getId().equals(fileType.getId())) {
                    result = 2;
                }

            }

            result = 3;
        }

        Assertions.assertTrue(result >= 1);
        return result;

    }

    private List<File> disguise(File[] files, File disguiseDir) throws IOException {

        List<File> disguisFiles = Lists.newArrayList();
        for (File file : files) {
            if (file.isFile()) {
                String fileExtension = FilenameUtil.getExtension(file.getName());

                for (String extension : fileTypeManager.getAllExtensions()) {
                    File disguisFile = new File(disguiseDir, file.getName() + "#." + extension);
                    if (!disguisFile.exists()) {
                        FileUtils.copyFile(file, disguisFile);
                    }
                    disguisFiles.add(disguisFile);

                }
                if (!StringUtils.isEmpty(fileExtension)) {

                    File disguisFile = new File(disguiseDir, file.getName().replace(".", "@") + "#_noextension");
                    if (!disguisFile.exists()) {
                        FileUtils.copyFile(file, disguisFile);
                    }
                    disguisFiles.add(disguisFile);

                } else {

                    File disguisFile = new File(disguiseDir, file.getName());
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
    @Order(1)
    void allDetected() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(33, files.length);
        DetectStatstic detectStatstic = new DetectStatstic(files.length);
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

            int assertResult = assertFile(file, detectResult);
            //            System.out.println("assertResult==>" + assertResult);
            detectStatstic.calAssertResult(assertResult);

        }
        detectStatstic.print();
        Assertions.assertTrue(detectStatstic.getBestRedio() == 1);

    }

    @Test
    @Order(2)
    void detectDisguiseAudioVideo() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/audio_video");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(9, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/audio_video", true);
        List<File> disguiseFiles = disguise(files, disguiseDir);
        Assertions.assertEquals(4158, disguiseFiles.size());

        DetectStatstic detectStatstic = new DetectStatstic(disguiseFiles.size());

        for (File file : disguiseFiles) {
            int assertResult = assertFile(file, FileDetector.detect(file));
            detectStatstic.calAssertResult(assertResult);
        }
        detectStatstic.print();
        Assertions.assertTrue(detectStatstic.getBestRedio() > 0.88d);

    }

    @Test
    @Order(3)
    void detectDisguiseExecute() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/execute");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(3, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/execute", true);
        List<File> disguiseFiles = disguise(files, disguiseDir);
        Assertions.assertEquals(1386, disguiseFiles.size());

        DetectStatstic detectStatstic = new DetectStatstic(disguiseFiles.size());

        for (File file : disguiseFiles) {
            int assertResult = assertFile(file, FileDetector.detect(file));
            detectStatstic.calAssertResult(assertResult);
        }
        detectStatstic.print();
        Assertions.assertTrue(detectStatstic.getBestRedio() > 0.6d);

    }

    @Test
    @Order(4)
    void detectDisguiseOffice() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(12, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/office", true);
        List<File> disguiseFiles = disguise(files, disguiseDir);
        Assertions.assertEquals(5544, disguiseFiles.size());

        DetectStatstic detectStatstic = new DetectStatstic(disguiseFiles.size());

        for (File file : disguiseFiles) {
            int assertResult = assertFile(file, FileDetector.detect(file));
            detectStatstic.calAssertResult(assertResult);
        }
        detectStatstic.print();
        Assertions.assertTrue(detectStatstic.getBestRedio() > 0.15d);

    }

    @Test
    @Order(5)
    void detectDisguisePic() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/pic");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(6, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/pic", true);
        List<File> disguiseFiles = disguise(files, disguiseDir);
        Assertions.assertEquals(2772, disguiseFiles.size());

        DetectStatstic detectStatstic = new DetectStatstic(disguiseFiles.size());

        for (File file : disguiseFiles) {
            int assertResult = assertFile(file, FileDetector.detect(file));
            detectStatstic.calAssertResult(assertResult);
        }
        detectStatstic.print();
        Assertions.assertTrue(detectStatstic.getBestRedio() > 0.83d);

    }

    @Test
    @Order(6)
    void detectDisguiseOhter() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/other");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(3, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/other", true);
        List<File> disguiseFiles = disguise(files, disguiseDir);
        Assertions.assertEquals(1386, disguiseFiles.size());

        DetectStatstic detectStatstic = new DetectStatstic(disguiseFiles.size());

        for (File file : disguiseFiles) {
            int assertResult = assertFile(file, FileDetector.detect(file));
            detectStatstic.calAssertResult(assertResult);
        }
        detectStatstic.print();
        Assertions.assertTrue(detectStatstic.getBestRedio() > 0.66d);

    }

    @Test
    @Order(98)
    void allUndetected() {
        File dir = ResourceHelper.getFileInClassPath("files/undetected");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(1, files.length);
        for (File file : files) {
            DetectResult detectResult = FileDetector.detect(file);
            printDetectResult(detectResult);
            Assertions.assertNull(detectResult.getFirstFileType());
            Assertions.assertTrue(CollectionUtils.isEmpty(detectResult.getAllPossibleFileTypes()));
        }
    }

    @Test
    @Order(99)
    void testOne() {
        //        File file = UserDirUtil.getFile("tmp/disguise/audio_video/m4v.m4v#.avif", false);
        //        File file = ResourceHelper.getFileInClassPath("files/detected/other/zip.zip");
        File file = ResourceHelper.getFileInClassPath("files/detected/execute/mac-execute");
        assertFile(file, FileDetector.detect(file));

    }

}