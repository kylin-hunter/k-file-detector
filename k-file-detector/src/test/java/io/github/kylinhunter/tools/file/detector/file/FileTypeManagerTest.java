package io.github.kylinhunter.tools.file.detector.file;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Maps;
import io.github.kylinhunter.tools.file.detector.common.component.CF;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

class FileTypeManagerTest {
    private final FileTypeManager fileTypeManager = CF.get(FileTypeManager.class);

    private static void print(FileType fileType) {
        System.out.println("\t ======================================");
        System.out.println("\t 1、id: " + fileType.getId());
        System.out.println("\t 2、extensions: " + fileType.getExtensions());
        System.out.println("\t 3、desc: " + fileType.getDesc());
        System.out.println("\t ======================================");
    }

    @Test
    void getAllFileTypes() {
        List<FileType> allFileTypes = fileTypeManager.getAllFileTypes();
        allFileTypes.forEach(fileType -> {
            System.out.println(fileType.getId());
            FileTypeManagerTest.print(fileType);
        });
        Assertions.assertTrue(allFileTypes.size() > 0);

    }

    @Test
    void getFileTypesByExtension() {
        Set<FileType> fileTypes = fileTypeManager.getFileTypesByExtension("exe");
        fileTypes.forEach(System.out::println);
        Assertions.assertEquals(1, fileTypes.size());

        fileTypes = fileTypeManager.getFileTypesByExtension("heic");
        fileTypes.forEach(System.out::println);
        Assertions.assertEquals(1, fileTypes.size());
    }

    @Test
    void getFileTypeById() {

        //        FileType fileType = fileTypeManager.getFileTypeById(100);
        //        Assertions.assertNotNull(fileType);
        //        fileType = fileTypeManager.getFileTypeById(100001);
        //        Assertions.assertNotNull(fileType);
    }

    @Test
    void getFileTypesWithNoExtension() {
        Set<FileType> fileTypesWithNoExtension = fileTypeManager.getFileTypesWithNoExtension();
        fileTypesWithNoExtension.forEach((fileType) -> {
            System.out.println(fileType.getId());
            FileTypeManagerTest.print(fileType);
        });
        Assertions.assertTrue(fileTypesWithNoExtension.size() > 0);
    }

    @Test
    void getAllExtensions() {
        Set<String> allExtensions = fileTypeManager.getAllExtensions();
        allExtensions.forEach(System.out::println);
        Assertions.assertTrue(allExtensions.size() > 0);
    }

    @Test
    void findSameFileType() {
        List<FileType> allFileTypes = fileTypeManager.getAllFileTypes();
        Map<String, String> descs = Maps.newHashMap();
        System.out.println(allFileTypes.size());
        allFileTypes.forEach(fileType -> {

            String extension = String.join(",", fileType.getExtensions());
            String desc = fileType.getDesc();
            desc = desc.replaceAll("[\\s()-,.?:;\"!']", "");
            String duplicatStr = extension + "_" + desc;
            String duplicateFileTypeId = descs.get(duplicatStr);
            if (duplicateFileTypeId != null && duplicateFileTypeId.length() > 0) {

                FileType fileTypeOld = fileTypeManager.getFileTypeById(duplicateFileTypeId);
                System.out.println("duplicateOri :" + fileTypeOld.getId() + "/" + fileTypeOld.getExtensions()
                        + "/" + fileTypeOld.getDesc());

                System.out.println("duplicateNew :" + fileType.getId() + "/" + extension + "/" + desc);

            } else {
                descs.put(duplicatStr, fileType.getId());
            }
        });
        System.out.println("duplicateOri===========");
        for (FileType fileType : fileTypeManager.getAllFileTypes()) {
            FileType sameRef = fileType.getSameRef();
            if (sameRef != null) {
                System.out.println("duplicateOri :" + sameRef.getId() + "/" + sameRef.getExtensions()
                        + "/" + sameRef.getDesc());

                System.out.println("duplicateNew :" + fileType.getId() + "/" + fileType.getExtensions()
                        + "/" + fileType.getDesc());

            }

        }

    }

}