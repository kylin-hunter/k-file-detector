package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.ComponentFactory;
import com.kylinhunter.plat.file.detector.common.util.FilenameUtil;
import com.kylinhunter.plat.file.detector.component.FileTypeManager;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileDetectorHelper {
    private static final FileTypeManager fileTypeManager = ComponentFactory.get(FileTypeManager.class);

    private static final Map<String, FileType> SPECIAL_FILETYPES = Maps.newHashMap();

    static {
        SPECIAL_FILETYPES.put("linux-execute", fileTypeManager.getFileTypeById("1_29837182"));
        SPECIAL_FILETYPES.put("mac-execute", fileTypeManager.getFileTypeById("1_1600260370"));

    }

    private static boolean checkSpecialFile(String fileName, FileType fileType) {
        FileType realFileType = SPECIAL_FILETYPES.get(fileName);
        return realFileType != null && realFileType.equals(fileType);
    }

    public static void printDetectResult(DetectResult detectResult) {
        System.out.println("===============================================");
        System.out.println("\t 1、fileName=>" + detectResult.getFileName());

        System.out.print("\t 2-1、oriMagics=>");
        List<Magic> oriMagics = detectResult.getOriMagics();

        if (CollectionUtils.isNotEmpty(oriMagics)) {
            List<String> numbers = oriMagics.stream().map(Magic::getNumber).collect(Collectors.toList());
            System.out.print(numbers);
            System.out.println();
        }

        List<Magic> allPossibleMagics = detectResult.getAllPossibleMagics();

        System.out.print("\t allPossibleMagics=>");

        if (CollectionUtils.isNotEmpty(allPossibleMagics)) {
            List<String> numbers = allPossibleMagics.stream().map(Magic::getNumber).collect(Collectors.toList());
            System.out.print(numbers);

        }

        System.out.println();
        FileType firstFileType = detectResult.getFirstFileType();
        System.out.println("\t firstFileType=>" + firstFileType);

        FileType secondFileType = detectResult.getSecondFileType();
        System.out.println("\t secondFileType=>" + secondFileType);

        List<FileType> allPossibleFileTypes = detectResult.getAllPossibleFileTypes();
        System.out.println("\t allPossibleFileTypes=>" + allPossibleFileTypes);

        System.out.println("===============================================");
        System.out.println();

    }

    public static int assertFile(File file, DetectResult detectResult, int targetLevel) {
        return assertFile(file, detectResult, targetLevel, false);
    }

    public static int assertFile(File file, DetectResult detectResult, int targetLevel, boolean printLevel) {
        FileType firstFileType = detectResult.getFirstFileType();
        FileType secondFileType = detectResult.getSecondFileType();
if(firstFileType==null){
    System.out.println();
}
        Assertions.assertNotNull(firstFileType);

        List<FileType> allPossibleFileTypes = detectResult.getAllPossibleFileTypes();
        Assertions.assertTrue(allPossibleFileTypes.size() > 0);
        String fileName = file.getName();
        fileName = fileName.replace("@", ".");
        int index = fileName.indexOf("#");
        if (index > 0) {
            fileName = fileName.substring(0, index);
        }

        String extension = FilenameUtil.getExtension(fileName);
        Pattern p = Pattern.compile("&([a-z-0-9|]+)&");
        Matcher matcher = p.matcher(fileName);
        String[] realExtensions = new String[0];
        if (matcher.find()) {
            String realExtensionStr = matcher.group(1);
            realExtensions = StringUtils.split(realExtensionStr, '|');

        }

        int matchLevel = -1;
        if (firstFileType.extensionEquals(extension, realExtensions)) {
            matchLevel = 1;
        } else {
            if (checkSpecialFile(fileName, firstFileType)) {
                matchLevel = 1;
            }
        }

        if (matchLevel <= 0) {

            if (secondFileType.extensionEquals(extension, realExtensions)) {
                matchLevel = 2;
            } else {
                if (checkSpecialFile(fileName, secondFileType)) {
                    matchLevel = 2;
                }
            }

        }
        if (matchLevel <= 0) {

            if (!StringUtils.isEmpty(extension)) {

                for (FileType fileType : allPossibleFileTypes) {
                    if (fileType.getExtension().equals(extension)) {
                        matchLevel = 3;
                        break;
                    }
                }

            } else {
                for (FileType possibleFileType : allPossibleFileTypes) {
                    if (checkSpecialFile(fileName, possibleFileType)) {
                        matchLevel = 3;
                        break;
                    }
                }

            }

        }

        //        System.out.println("matchLevel=>" + matchLevel);
        Assertions.assertTrue(matchLevel >= 1);
        if (matchLevel >= targetLevel) {
            if (printLevel) {
                System.out.println("\t ******matchLevel=>" + matchLevel);
            }
            printDetectResult(detectResult);

        }

        return matchLevel;

    }

    public static List<File> disguiseByExtension(File[] files, File disguiseDir) throws IOException {

        List<File> disguisFiles = Lists.newArrayList();
        for (File file : files) {
            if (file.isFile()) {
                String fileExtension = FilenameUtil.getExtension(file.getName());
                for (String extension : fileTypeManager.getAllExtensions()) {
                    if (!fileExtension.equalsIgnoreCase(extension)) {
                        File disguisFile = new File(disguiseDir, file.getName() + "#." + extension);
                        if (!disguisFile.exists()) {
                            FileUtils.copyFile(file, disguisFile);
                        }
                        disguisFiles.add(disguisFile);
                    }
                }

            }
        }
        return disguisFiles;

    }

    public static List<File> disguiseRemoveExtension(File[] files, File disguiseDir) throws IOException {

        List<File> disguisFiles = Lists.newArrayList();
        for (File file : files) {
            if (file.isFile()) {
                String fileExtension = FilenameUtil.getExtension(file.getName());

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

    public static DetectStatstic detectStatstic(List<File> disguiseFiles) {
        return detectStatstic(disguiseFiles, 2, false);

    }

    public static DetectStatstic detectStatstic(List<File> disguiseFiles, int targetLevel) {
        return detectStatstic(disguiseFiles, targetLevel, false);

    }

    public static DetectStatstic detectStatstic(List<File> disguiseFiles, int targetLevel, boolean printLevel) {
        DetectStatstic detectStatstic = new DetectStatstic(disguiseFiles.size());

        for (File file : disguiseFiles) {
            detectStatstic.calAssertResult(FileDetectorHelper.assertFile(file, FileDetector.detect(file), targetLevel,
                    printLevel));
        }
        detectStatstic.print();
        return detectStatstic;
    }

}