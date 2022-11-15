package io.github.kylinhunter.tools.file.detector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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

import com.google.common.collect.Maps;
import io.github.kylinhunter.tools.file.detector.common.component.CF;
import io.github.kylinhunter.tools.file.detector.common.util.FilenameUtil;
import io.github.kylinhunter.tools.file.detector.detect.bean.DetectResult;
import io.github.kylinhunter.tools.file.detector.file.FileTypeManager;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;

class FileDetectorHelper {
    private static final FileTypeManager FILE_TYPE_MANAGER = CF.get(FileTypeManager.class);

    private static final Map<String, FileType> SPECIAL_FILETYPES = Maps.newHashMap();

    static {
        SPECIAL_FILETYPES.put("linux-execute", FILE_TYPE_MANAGER.getFileTypeById("none_7F45"));
        SPECIAL_FILETYPES.put("mac-execute", FILE_TYPE_MANAGER.getFileTypeById("none_CFFA"));

    }

    private static boolean checkSpecialFile(String fileName, FileType fileType) {
        FileType realFileType = SPECIAL_FILETYPES.get(fileName);
        if (realFileType != null && fileType != null) {
            return realFileType.equals(fileType);
        }
        return false;

    }

    private static boolean checkExtensions(FileType fileType, List<String> extensions) {
        if (extensions != null && extensions.size() > 0 && fileType != null) {
            for (String tmpExtension : extensions) {
                if (fileType.extensionEquals(tmpExtension)) {
                    return true;
                }
            }
        }
        return false;

    }

    public static void printDetectResult(DetectResult detectResult, int matchPositin) {
        System.out.println("===============================================");

        System.out.println("\t matchPositin=>" + matchPositin);
        System.out.println("\t fileName=>" + detectResult.getFileName());

        System.out.print("\t oriMagics=>");
        List<Magic> oriMagics = detectResult.getOriMagics();

        if (CollectionUtils.isNotEmpty(oriMagics)) {
            List<String> numbers = oriMagics.stream().map(Magic::getNumber).collect(Collectors.toList());
            System.out.print(numbers);
            System.out.println();
        }

        List<Magic> allPossibleMagics = detectResult.getPossibleMagics();

        System.out.print("\t possibleMagics=>");

        if (CollectionUtils.isNotEmpty(allPossibleMagics)) {
            List<String> numbers = allPossibleMagics.stream().map(Magic::getNumber).collect(Collectors.toList());
            System.out.print(numbers);
        }

        System.out.println();
        FileType firstFileType = detectResult.getFirstFileType();
        System.out.println("\t firstFileType=>" + firstFileType);

        FileType secondFileType = detectResult.getSecondFileType();
        System.out.println("\t secondFileType=>" + secondFileType);

        List<FileType> allPossibleFileTypes = detectResult.getPossibleFileTypes();

        System.out.println("\t allPossibleFileTypes=>" + allPossibleFileTypes);

        for (int i = 0; i < allPossibleFileTypes.size(); i++) {
            System.out.println("\t allPossibleFileType[" + i + "]=>" + allPossibleFileTypes.get(i));

        }

        System.out.println("===============================================");
        System.out.println();

    }

    public static int assertFile(File file, DetectResult detectResult) {
        return assertFile(file, detectResult, Collections.singletonList(1));
    }

    public static int assertFile(File file, DetectResult detectResult, List<Integer> targetLevels) {

        Assertions.assertNotNull(detectResult.getFirstFileType());

        List<FileType> possibleFileTypes = detectResult.getPossibleFileTypes();
        Assertions.assertTrue(possibleFileTypes.size() > 0);
        String fileName = file.getName();
        fileName = fileName.replace("@", ".");
        int index = fileName.indexOf("#");
        if (index > 0) {
            fileName = fileName.substring(0, index);
        }

        List<String> allExtensions = Lists.newArrayList();
        fileName = fileName.toLowerCase();
        String extension = FilenameUtil.getExtension(fileName);
        if (!StringUtils.isEmpty(extension)) {
            allExtensions.add(extension);
        }
        Pattern p = Pattern.compile("&([a-z-0-9|]+)&");
        Matcher matcher = p.matcher(fileName);
        if (matcher.find()) {
            String realExtensionStr = matcher.group(1);
            String[] realExtensions = StringUtils.split(realExtensionStr, '|');
            Collections.addAll(allExtensions, realExtensions);

        }

        int position = -1;
        for (int i = 0; i < possibleFileTypes.size(); i++) {

            FileType fileType = possibleFileTypes.get(i);

            if (checkExtensions(fileType, allExtensions)) {
                position = i + 1;
                break;
            } else {
                if (checkSpecialFile(fileName, fileType)) {
                    position = i + 1;
                    break;
                }
            }

        }

        //        System.out.println("position=>" + position);
        if (!targetLevels.contains(position)) {
            System.out.println("\t assertFile=>" + file.getAbsolutePath());
            printDetectResult(detectResult, position);
        }
        Assertions.assertTrue(position >= 1);

        return position;

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static List<File> disguiseByExtension(File[] files, File disguiseDir) throws IOException {

        List<File> disguisFiles = Lists.newArrayList();
        for (File file : files) {
            if (file.isFile()) {
                String fileExtension = FilenameUtil.getExtension(file.getName());

                for (String extension : FILE_TYPE_MANAGER.getAllExtensions()) {
                    if (!fileExtension.equalsIgnoreCase(extension)) {
                        File disguisFile = new File(disguiseDir, file.getName() + "#." + extension);

                        if (disguisFile.exists() && disguisFile.lastModified() < file.lastModified()) {
                            disguiseDir.delete();
                        }

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
        return disguiseRemoveExtension(files, disguiseDir, false);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public static List<File> disguiseRemoveExtension(File[] files, File disguiseDir, boolean force) throws IOException {

        List<File> disguisFiles = Lists.newArrayList();
        for (File file : files) {
            if (file.isFile()) {
                String fileExtension = FilenameUtil.getExtension(file.getName());

                if (!StringUtils.isEmpty(fileExtension)) {

                    File disguisFile = new File(disguiseDir, file.getName().replace(".", "@") + "#_noextension");
                    if (disguisFile.exists() && disguisFile.lastModified() < file.lastModified()) {
                        disguiseDir.delete();
                    }
                    if (force || !disguisFile.exists()) {
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

    public static DetectStatstic detect(List<File> disguiseFiles, Integer... targetLevel) {
        return detect(disguiseFiles, Arrays.asList(targetLevel));

    }

    public static DetectStatstic detect(List<File> disguiseFiles) {
        return detect(disguiseFiles, Collections.singletonList(1));

    }

    public static DetectStatstic detect(List<File> disguiseFiles, List<Integer> targetLevel) {
        DetectStatstic detectStatstic = new DetectStatstic(disguiseFiles.size());
        for (File file : disguiseFiles) {
            DetectResult detectResult = FileDetector.detect(file);
            int assertResult = FileDetectorHelper.assertFile(file, detectResult, targetLevel);
            detectStatstic.calAssertResult(assertResult);
        }
        detectStatstic.print();
        return detectStatstic;
    }

}