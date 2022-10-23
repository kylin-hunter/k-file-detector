package com.kylinhunter.plat.file.detector.type;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.CommonManager;
import com.kylinhunter.plat.file.detector.constant.FileFamily;
import com.kylinhunter.plat.file.detector.magic.ExtensionMagics;
import com.kylinhunter.plat.file.detector.magic.Magic;
import com.kylinhunter.plat.file.detector.magic.TolerateMagics;

class FileTypeManagerTest {
    FileTypeManager fileTypeManager = CommonManager.getFileTypeManager();

    @Test
    void getFileFamilyDatas() {
        Map<FileFamily, Set<FileType>> fileFamilyDatas = fileTypeManager.getFileFamilyDatas();

        fileFamilyDatas.forEach((fileFamily, fileTypes) -> {
            System.out.println(fileFamily);
            fileTypes.forEach(fileType -> {

                System.out.println("\t id: " + fileType.getId());
                System.out.println("\t extension: " + fileType.getExtension());
                System.out.println("\t desc: " + fileType.getDesc());
                System.out.println("\t family: " + fileType.getFamily());
                if (fileType.getTolerateFileTypes() != null) {
                    System.out
                            .println("\t tolerateFileTypes: " + Arrays.toString(fileType.getTolerateFileTypes().stream()
                                    .map(e -> e.getId() + "/" + e.getExtension()).toArray(String[]::new)));
                }

                ExtensionMagics extensionMagics = fileType.getExtensionMagics();
                if (extensionMagics != null) {
                    System.out.println("\t extensionMagics: " + extensionMagics.getExtension());

                    Set<Magic> magics = extensionMagics.getMagics();
                    magics.forEach(magic -> {
                        System.out.println("\t\t magic number: " + magic.getNumber());
                    });
                }
                TolerateMagics tolerateMagics = fileType.getTolerateMagics();

                if (tolerateMagics != null) {
                    Set<String> tolerateExtensions = tolerateMagics.getExtensions();
                    System.out.println("\t tolerateExtensions: " + tolerateExtensions);
                    Set<Magic> magics = tolerateMagics.getMagics();
                    if (magics != null) {
                        magics.forEach(magic -> {
                            System.out.println("\t\t magic number: " + magic.getNumber());
                        });
                    }

                }

                System.out.println("================");

            });
        });

        Assertions.assertTrue(fileFamilyDatas.size() > 0);

    }

    @Test
    void getExtensionDatas() {
        Map<String, Set<FileType>> extensionDatas = fileTypeManager.getExtensionToFileTypes();

        extensionDatas.forEach((fileFamily, fileTypes) -> {
            System.out.println(fileFamily);
            fileTypes.forEach(e -> {
                System.out.println("\tid:" + e.getId());
                System.out.println("\textension:" + e.getExtension());
                System.out.println("\tdesc:" + e.getDesc());

            });
        });

        Assertions.assertTrue(extensionDatas.size() > 0);
    }

    @Test
    void getFileTypes() {
        Set<FileType> fileTypes = fileTypeManager.getFileTypes(FileFamily.MS_OFFICE_97_2003);
        fileTypes.forEach(System.out::println);
        Assertions.assertTrue(fileTypes.size() > 1);

        fileTypes = fileTypeManager.getFileTypes("exe");
        fileTypes.forEach(System.out::println);
        Assertions.assertEquals(1, fileTypes.size());

    }

}