package io.github.kylinhunter.tools.file.detector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.github.kylinhunter.tools.file.detector.common.component.CF;
import io.github.kylinhunter.tools.file.detector.common.util.ResourceHelper;
import io.github.kylinhunter.tools.file.detector.common.util.UserDirUtil;
import io.github.kylinhunter.tools.file.detector.detect.bean.DetectResult;
import io.github.kylinhunter.tools.file.detector.file.FileTypeManager;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorTest {

    private static final FileTypeManager fileTypeManager = CF.get(FileTypeManager.class);

    @Test
    @Order(11)
    void detectAll() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(48, files.length);
        DetectStatstic detectStatstic = new DetectStatstic(files.length);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            int index = i % 3;
            DetectResult detectResult;
            if (index == 0) {
                detectResult = FileDetector.detect(file);
            } else if (index == 1) {
                byte[] bytes = FileUtils.readFileToByteArray(file);
                detectResult = FileDetector.detect(bytes);
            } else {
                FileInputStream inputStream = FileUtils.openInputStream(file);
                detectResult = FileDetector.detect(inputStream);
            }

            detectStatstic.calAssertResult(FileDetectorHelper.assertFile(file, detectResult,
                    Collections.singletonList(-1)));
        }
        detectStatstic.print();
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(21)
    void detectAudioVideoDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/audio_video");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(9, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/disguise_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        int expectedFileNum = (fileTypeManager.getAllExtensions().size() - 1) * files.length;
        Assertions.assertEquals(expectedFileNum, disguiseFiles.size());
        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(22)
    void detectAudioVideoDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/audio_video");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(9, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(31)
    void detectExecuteDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/execute");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(13, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/disguise_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals(6021, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(32)
    void detectExecuteDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/execute");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(13, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(41)
    void detectOffice97DisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/97-2004");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(5, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/disguise_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.getAllExtensions().size() - 1) * files.length,
                disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles, 1);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(42)
    void detectOffice97DisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/97-2004");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(5, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles, -1);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(43)
    void detectOffice2007DisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(7, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/disguise_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        int expectedFileNums = (fileTypeManager.getAllExtensions().size() - 1) * files.length;
        Assertions.assertEquals(expectedFileNums, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(44)
    void detectOffice2007DisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(7, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles, -1);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(51)
    void detectOtherDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/other");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(8, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/disguise_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        int expectedFileNums = (fileTypeManager.getAllExtensions().size() - 1) * files.length;
        Assertions.assertEquals(expectedFileNums, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(52)
    void detectOtherDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/other");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(8, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, detectStatstic.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(61)
    void detectPicDisguiseByExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/pic");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(6, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/disguise_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseByExtension(files, disguiseDir);
        Assertions.assertEquals((fileTypeManager.getAllExtensions().size() - 1) * files.length,
                disguiseFiles.size());

        DetectStatstic ds = FileDetectorHelper.detect(disguiseFiles);
        Assertions.assertEquals(1, ds.getFirstFileTypeMatchRatio());

    }

    @Test
    @Order(62)
    void detectPicDisguiseWithRemoveExtension() throws IOException {
        File dir = ResourceHelper.getFileInClassPath("files/detected/pic");
        File[] files = FileUtils.listFiles(dir, null, true).toArray(new File[0]);
        Assertions.assertEquals(6, files.length);

        File disguiseDir = UserDirUtil.getDir("tmp/disguise/remove_extension", true);
        List<File> disguiseFiles = FileDetectorHelper.disguiseRemoveExtension(files, disguiseDir);
        Assertions.assertEquals(files.length, disguiseFiles.size());

        DetectStatstic detectStatstic = FileDetectorHelper.detect(disguiseFiles);
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
    void testTmp() {

        File file = ResourceHelper.getFileInClassPath("files/detected/other/html4.html");

        FileDetectorHelper.assertFile(file, FileDetector.detect(file), Collections.EMPTY_LIST);

    }

}