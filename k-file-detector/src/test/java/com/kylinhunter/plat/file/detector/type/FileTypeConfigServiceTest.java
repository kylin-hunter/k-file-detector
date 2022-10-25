package com.kylinhunter.plat.file.detector.type;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.manager.Service;
import com.kylinhunter.plat.file.detector.manager.ServiceFactory;
import com.kylinhunter.plat.file.detector.constant.FileFamily;

class FileTypeConfigServiceTest {
    FileTypeConfigService fileTypeConfigService = ServiceFactory.get(Service.FILE_TYPE);

    @Test
    void getFileFamilyDatas() {
        Map<FileFamily, Set<FileType>> fileFamilyDatas = fileTypeConfigService.getFileFamilyDatas();

        fileFamilyDatas.forEach((fileFamily, familyFileType) -> {
            System.out.println(fileFamily);
            familyFileType.forEach(fileType -> {

                System.out.println("\t 1、id: " + fileType.getId());
                System.out.println("\t 2、extension: " + fileType.getExtension());
                System.out.println("\t 3、desc: " + fileType.getDesc());
                System.out.println("\t 4、tolerateTag: " + fileType.getTolerateTag());
                System.out.println("\t 5、ex---family: " + fileType.getFamily());

                Set<FileType> fileTypes = fileType.getTolerateFileTypes();
                if (fileTypes != null) {
                    System.out.println("\t 6、ex---tolerate fileTypes: " + fileTypes.stream()
                            .map(e -> e.getId() + "/" + e.getExtension()).collect(Collectors.toSet()));
                }
                System.out.println("\t 7、ex---magicMaxLength: " + fileType.getMagicMaxLength());

                System.out.println("======================================");

            });
        });

        Assertions.assertTrue(fileFamilyDatas.size() > 0);

    }

    @Test
    void getExtensionDatas() {
        Map<String, Set<FileType>> extensionDatas = fileTypeConfigService.getExtensionToFileTypes();

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
    void getFileTypesByExtension() {
        Set<FileType> fileTypes = fileTypeConfigService.getFileTypesByExtension(FileFamily.MS_OFFICE_97_2003);
        fileTypes.forEach(System.out::println);
        Assertions.assertTrue(fileTypes.size() > 1);

    }

    @Test
    void getFileTypes() {

        Set<FileType> fileTypes = fileTypeConfigService.getFileTypesByExtension("exe");
        fileTypes.forEach(System.out::println);
        Assertions.assertEquals(1, fileTypes.size());

    }

}