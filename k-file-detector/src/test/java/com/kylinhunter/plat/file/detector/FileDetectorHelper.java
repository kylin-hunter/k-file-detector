package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
        if (realFileType != null && fileType != null) {
            return realFileType.equals(fileType);
        }
        return false;

    }

    private static boolean checkSpecialFile(String fileName, List<FileType> fileTypes) {
        FileType realFileType = SPECIAL_FILETYPES.get(fileName);
        if (realFileType != null && fileTypes != null) {
            for (FileType fileType : fileTypes) {
                if (realFileType.equals(fileType)) {
                    return true;
                }

            }
        }
        return false;

    }

    private static boolean checkExtensions(FileType fileType, String extension, String... extensions) {
        if (extensions != null && fileType != null) {

            if (fileType.extensionEquals(extension)) {
                return true;
            }

            for (String tmpExtension : extensions) {
                if (fileType.extensionEquals(tmpExtension)) {
                    return true;
                }

            }
        }
        return false;

    }

    private static boolean checkExtensions(List<FileType> fileTypes, String extension, String... extensions) {
        if (extensions != null && fileTypes != null) {
            for (FileType fileType : fileTypes) {
                if (fileType.extensionEquals(extension)) {
                    return true;
                }
            }

            for (FileType fileType : fileTypes) {
                for (String tmpExtension : extensions) {
                    if (fileType.extensionEquals(tmpExtension)) {
                        return true;
                    }

                }

            }
        }
        return false;

    }

    public static void printDetectResult(DetectResult detectResult) {
        System.out.println("===============================================");
        System.out.println("\t fileName=>" + detectResult.getFileName());

        System.out.print("\t oriMagics=>");
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
        List<FileType> firstFileTypes = detectResult.getFirstFileTypes();
        System.out.println("\t firstFileTypes=>" + firstFileTypes);

        List<FileType> secondFileTypes = detectResult.getSecondFileType();
        System.out.println("\t secondFileTypes=>" + secondFileTypes);

        List<FileType> allPossibleFileTypes = detectResult.getAllPossibleFileTypes();
        System.out.println("\t allPossibleFileTypes=>" + allPossibleFileTypes);

        System.out.println("===============================================");
        System.out.println();

    }

    public static int assertFile(File file, DetectResult detectResult, List<Integer> targetLevels) {
        return assertFile(file, detectResult, targetLevels, false);
    }

    public static int assertFile(File file, DetectResult detectResult, List<Integer> targetLevels, boolean printLevel) {
        List<FileType> firstFileTypes = detectResult.getFirstFileTypes();
        List<FileType> secondFileTypes = detectResult.getSecondFileType();
        Assertions.assertFalse(CollectionUtils.isEmpty(firstFileTypes));

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

        if (matchLevel <= 0) {

            if (checkExtensions(firstFileTypes, extension, realExtensions)) {
                matchLevel = 100;
            } else {
                if (checkSpecialFile(fileName, firstFileTypes)) {
                    matchLevel = 100;
                }
            }
            if (matchLevel == 100) {
                if (checkExtensions(firstFileTypes.get(0), extension, realExtensions)) {
                    matchLevel = 101;
                } else {
                    if (checkSpecialFile(fileName, firstFileTypes.get(0))) {
                        matchLevel = 101;
                    }
                }
                if (matchLevel != 101) {
                    matchLevel = 102;
                }

            }

        }

        if (matchLevel <= 0) {

            if (checkExtensions(secondFileTypes, extension, realExtensions)) {
                matchLevel = 200;
            } else {
                if (checkSpecialFile(fileName, secondFileTypes)) {
                    matchLevel = 200;
                }
            }

            if (matchLevel == 200) {
                if (checkExtensions(secondFileTypes.get(0), extension, realExtensions)) {
                    matchLevel = 201;
                } else {
                    if (checkSpecialFile(fileName, secondFileTypes.get(0))) {
                        matchLevel = 201;
                    }
                }
                if (matchLevel != 201) {
                    matchLevel = 202;
                }

            }

        }
        if (matchLevel <= 0) {

            if (checkExtensions(allPossibleFileTypes, extension, realExtensions)) {
                matchLevel = 300;

            } else {
                if (checkSpecialFile(fileName, allPossibleFileTypes)) {
                    matchLevel = 300;

                }

            }

        }

        //        System.out.println("matchLevel=>" + matchLevel);
        Assertions.assertTrue(matchLevel >= 1);
        if (targetLevels.contains(matchLevel)) {
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

    public static DetectStatstic detect(List<File> disguiseFiles) {
        return detect(disguiseFiles, Arrays.asList(-1, 102, 201, 202, 300), false);

    }

    public static DetectStatstic detect(List<File> disguiseFiles, List<Integer> targetLevel) {
        return detect(disguiseFiles, targetLevel, false);

    }

    public static DetectStatstic detect(List<File> disguiseFiles, List<Integer> targetLevel,
                                        boolean printLevel) {
        DetectStatstic detectStatstic = new DetectStatstic(disguiseFiles.size());

        for (File file : disguiseFiles) {
            detectStatstic.calAssertResult(FileDetectorHelper.assertFile(file, FileDetector.detect(file), targetLevel,
                    printLevel));
        }
        detectStatstic.print();
        return detectStatstic;
    }

}