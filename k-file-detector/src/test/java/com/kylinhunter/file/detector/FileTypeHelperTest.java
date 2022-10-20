package com.kylinhunter.file.detector;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.file.detector.bean.DetectOption;
import com.kylinhunter.file.detector.constant.ExtensionRisk;
import com.kylinhunter.file.detector.signature.ExtensionManager;
import com.kylinhunter.file.detector.signature.FileTypeHelper;
import com.kylinhunter.file.detector.config.FileType;

class FileTypeHelperTest {

    @Test
    void test() {

        FileTypeHelper.getAllFileTypes().forEach((name, fileType) -> {

            System.out.println("name:" + name + ",fileType:" + fileType);
            FileType tmpFileType = FileTypeHelper.getFileType(fileType.getExtension());
            Assertions.assertEquals(tmpFileType, fileType);

            fileType.getFamilies().forEach(fileFamily -> {
                System.out.println("==>family name:" + fileFamily.name());

                Set<FileType> fileTypes = FileTypeHelper.getFileTypes(fileFamily);
                fileTypes.forEach(f -> System.out.println("========>family extension:" + f.getExtension()));
            });

            Set<String> tolerateExtensions = fileType.getTolerateExtensions();
            System.out.println("===========>getTolerateExtensions:" + tolerateExtensions);

        });

        Set<FileType> fileTypes = FileTypeHelper.getFileTypes(ExtensionRisk.HIGH);
        System.out.println("==> risk HIGH:");
        fileTypes.forEach(System.out::println);

        fileTypes = FileTypeHelper.getFileTypes(ExtensionRisk.MIDDLE);
        System.out.println("==> risk MIDDLE:");
        fileTypes.forEach(System.out::println);

        fileTypes = FileTypeHelper.getFileTypes(ExtensionRisk.LOW);
        System.out.println("==> risk LOW:");
        fileTypes.forEach(System.out::println);



    }
}