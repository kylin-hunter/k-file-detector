package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorTest {

    private static final FileTypeManager fileTypeManager = ComponentFactory.get(FileTypeManager.class);

    @Test
    @Order(11)
    void detectAll() throws IOException {
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

            detectStatstic.calAssertResult(
                    FileDetectorHelper.assertFile(file, detectResult, Arrays.asList(-1, 102, 202, 300)));
        }
        detectStatstic.print();
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypesMatchRatio());
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.9);

    }

    @Test
    @Order(21)
    void detectAudioVideoDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/audio_video");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(9, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/audio_video_disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.allExtensionSize() - 1) * files.length, disguiseFiles.size());

        DetectStatstic detectStatstic =
                FileDetectorHelper.detect(disguiseFiles, Arrays.asList(-1, 102, 202, 300), true);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypesMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(22)
    void detectAudioVideoDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/audio_video");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(9, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/audio_video_remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypesMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(31)
    void detectExecuteDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/execute");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(3, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/execute_disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        System.out.println("fileTypeManager.allExtensionSize()=>" + fileTypeManager.allExtensionSize());
        Assertions.assertEquals(fileTypeManager.allExtensionSize() * files.length - 1, disguiseFiles.size());

        DetectStatstic ds = FileDetectorHelper.detect(disguiseFiles, Arrays.asList(-1, 102, 201, 202, 300), true);
        Assertions.assertTrue(ds.getFirstFileTypesMatchRatio() > 0.99);
        Assertions.assertTrue(ds.getFirstFileTypeMatchRatio() > 0.99);

    }

    @Test
    @Order(32)
    void detectExecuteDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/execute");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(3, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/execute_remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypesMatchRatio());

    }

    @Test
    @Order(41)
    void detectOfficeDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(12, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/office_disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.allExtensionSize() - 1) * files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles, Arrays.asList(
                -1, 102, 202, 300), true);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypesMatchRatio());
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.8);

    }

    @Test
    @Order(42)
    void detectOfficeDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(12, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/office_remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles, Arrays.asList(
                -1, 102, 202, 300));
        Assertions.assertTrue(detectStatstic.getFirstFileTypesMatchRatio() > 0.99);
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.9);

    }

    @Test
    @Order(51)
    void detectOtherDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/other");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(3, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/pic_disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.allExtensionSize() - 1) * files.length, disguiseFiles.size());

        DetectStatstic ds = FileDetectorHelper.detect(disguiseFiles, Arrays.asList(-1, 102, 201, 202, 300), true);
        Assertions.assertTrue(ds.getFirstFileTypesMatchRatio() > 0.99);

        Assertions.assertTrue(ds.getFirstFileTypeMatchRatio() > 0.99);

    }

    @Test
    @Order(52)
    void detectOtherDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/other");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(3, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/pic_remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypesMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(61)
    void detectPicDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/pic");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(6, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/pic_disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.allExtensionSize() - 1) * files.length, disguiseFiles.size());

        DetectStatstic ds = FileDetectorHelper.detect(disguiseFiles, Arrays.asList(-1, 102, 202, 300), true);
        Assertions.assertEquals(1, ds.getFirstFileTypesMatchRatio());
        Assertions.assertEquals(1, ds.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(62)
    void detectPicDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/pic");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(6, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/pic_remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypesMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(98)
    void allUndetected() {
        File dir = ResourceHelper.getFileInClassPath("files/undetected");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(1, files.length);
        for (File file : files) {
            DetectResult detectResult = FileDetector.detect(file);
            FileDetectorHelper.printDetectResult(detectResult);
            Assertions.assertTrue(CollectionUtils.isEmpty(detectResult.getFirstFileTypes()));
            Assertions.assertTrue(CollectionUtils.isEmpty(detectResult.getAllPossibleFileTypes()));
        }
    }

    @Test
    @Order(99)
    void testTmp() throws IOException {
        File file1 = ResourceHelper.getFileInClassPath("files/detected/other/zip.zip");
        //                FileDetectorHelper.assertFile(file1, FileDetector.detect(file1), 1);
        //        File file2 = UserDirUtil.getFile("tmp/disguise/audio_video_disguise_by_extension/avi.avi#.cdr",
        //        false);
        File file2 = UserDirUtil.getFile("tmp/disguise/office_remove_extension/xls@xls#_noextension", false);

        FileDetectorHelper.assertFile(file2, FileDetector.detect(file2), Arrays.asList(
                -1, 102, 202, 300), true);

    }

}