package com.kylinhunter.plat.file.detector.type;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        fileFamilyDatas.forEach((fileFamily, familyFileType) -> {
            System.out.println(fileFamily);
            familyFileType.forEach(fileType -> {

                System.out.println("\t id: " + fileType.getId());
                System.out.println("\t extension: " + fileType.getExtension());
                System.out.println("\t desc: " + fileType.getDesc());
                System.out.println("\t family: " + fileType.getFamily());

                ExtensionMagics extensionMagics = fileType.getExtensionMagics();

                System.out.println("\t extensionMagics: ");
                Set<Magic> magics = extensionMagics.getMagics();
                if (magics != null) {
                    System.out.println("\t\t magic_number: " + magics.stream().map(e -> e.getNumber()).collect(
                            Collectors.toSet()));

                }
                System.out.println("\t\t magic_max_length: " + extensionMagics.getMagicMaxLength());

                TolerateMagics tolerateMagics = fileType.getTolerateMagics();
                System.out.println("\t tolerateMagics: ");

                Set<FileType> fileTypes = tolerateMagics.getFileTypes();
                if (fileTypes != null) {

                    System.out.println("\t\t fileTypes: " + fileTypes.stream()
                            .map(e -> e.getId() + "/" + e.getExtension()).collect(Collectors.toSet()));
                }
                System.out.println("\t\t extensions: " + tolerateMagics.getExtensions());
                magics = tolerateMagics.getMagics();
                if (magics != null) {
                    System.out.println("\t\t magic_number: " + magics.stream().map(e -> e.getNumber()).collect(
                            Collectors.toSet()));

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
        Set<FileType> fileTypes = fileTypeManager.getFileTypesByExtension(FileFamily.MS_OFFICE_97_2003);
        fileTypes.forEach(System.out::println);
        Assertions.assertTrue(fileTypes.size() > 1);

        fileTypes = fileTypeManager.getFileTypesByExtension("exe");
        fileTypes.forEach(System.out::println);
        Assertions.assertEquals(1, fileTypes.size());

    }

}