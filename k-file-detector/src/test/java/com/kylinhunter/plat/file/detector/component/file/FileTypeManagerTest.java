package com.kylinhunter.plat.file.detector.component.file;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.common.component.CF;
import com.kylinhunter.plat.file.detector.config.bean.FileType;

class FileTypeManagerTest {
    private final FileTypeManager fileTypeManager = CF.get(FileTypeManager.class);

    private static void print(FileType fileType) {
        System.out.println("\t ======================================");
        System.out.println("\t 1、id: " + fileType.getId());
        System.out.println("\t 2、extension: " + fileType.getExtension());
        System.out.println("\t 3、desc: " + fileType.getDesc());
        System.out.println("\t ======================================");
    }

    @Test
    void getAllFileTypes() {
        Set<FileType> allFileTypes = fileTypeManager.getAllFileTypes();
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

}