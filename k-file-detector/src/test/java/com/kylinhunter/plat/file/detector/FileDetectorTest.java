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

import com.kylinhunter.plat.file.detector.common.component.CF;
import com.kylinhunter.plat.file.detector.common.util.MultipartFileHelper;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.common.util.UserDirUtil;
import com.kylinhunter.plat.file.detector.detect.bean.DetectResult;
import com.kylinhunter.plat.file.detector.file.FileTypeManager;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorTest {

    private static final FileTypeManager fileTypeManager = CF.get(FileTypeManager.class);

    @Test
    @Order(11)
    void detectAll() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(37, files.length);
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

            detectStatstic.calAssertResult(FileDetectorHelper.assertFile(file, detectResult));
        }
        detectStatstic.print();
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
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

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
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
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(31)
    void detectExecuteDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/execute");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(5, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/execute_disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        System.out.println("fileTypeManager.allExtensionSize()=>" + fileTypeManager.allExtensionSize());
        Assertions.assertEquals(fileTypeManager.allExtensionSize() * files.length - 1, disguiseFiles.size());

        DetectStatstic ds = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertTrue(ds.getFirstFileTypeMatchRatio() > 0.99);
        Assertions.assertTrue(ds.getFirstFileTypeMatchRatio() > 0.99);

    }

    @Test
    @Order(32)
    void detectExecuteDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/execute");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(5, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/execute_remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(41)
    void detectOffice97DisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/97-2004");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(6, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/office/97-2004/disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.allExtensionSize() - 1) * files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles, Arrays.asList(
                -1, 2, 3));
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.8);

    }

    @Test
    @Order(42)
    void detectOffice97DisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/97-2004");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(6, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/office/97-2004/remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles, Arrays.asList(
                -1, 1, 2, 3));
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.99);
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.9);

    }

    @Test
    @Order(43)
    void detectOffice2007DisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(7, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/office/2007/disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.allExtensionSize() - 1) * files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.8);

    }

    @Test
    @Order(44)
    void detectOffice2007DisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(7, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/office/2007/remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles, Arrays.asList(
                -1, 2, 3));
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.99);
        Assertions.assertTrue(detectStatstic.getFirstFileTypeMatchRatio() > 0.9);

    }

    @Test
    @Order(51)
    void detectOtherDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/other");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(4, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/pic_disguise_by_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.allExtensionSize() - 1) * files.length, disguiseFiles.size());

        DetectStatstic ds = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertTrue(ds.getFirstFileTypeMatchRatio() > 0.99);

        Assertions.assertTrue(ds.getFirstFileTypeMatchRatio() > 0.99);

    }

    @Test
    @Order(52)
    void detectOtherDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/other");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(4, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/pic_remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
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

        DetectStatstic ds = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, ds.getFirstFileTypeMatchRatio());
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
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
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
            FileDetectorHelper.printDetectResult(detectResult, 0);
            Assertions.assertNull(detectResult.getFirstFileType());
            Assertions.assertTrue(CollectionUtils.isEmpty(detectResult.getPossibleFileTypes()));
        }
    }

    @Test
    @Order(99)
    void testTmp() throws IOException {

        File file2 = UserDirUtil.getFile("tmp/disguise/office_remove_extension/xls@xls#_noextension", false);

        FileDetectorHelper.assertFile(file2, FileDetector.detect(file2), Arrays.asList(
                -1, 2, 3));

    }

}