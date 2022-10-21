package com.kylinhunter.file.detector.config;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.file.detector.constant.ExtensionFamily;
import com.kylinhunter.file.detector.constant.ExtensionRisk;
import com.kylinhunter.file.detector.extension.ExtensionManager;
import com.kylinhunter.file.detector.extension.FileType;
import com.kylinhunter.file.detector.extension.FileTypeConfigManager;

class ExtensionManagerTest {
    ExtensionManager extensionManager = FileTypeConfigManager.getExtensionManager();

    @Test
    void testIsDanger() {

        Assertions.assertEquals(true, extensionManager.isDanger("exe"));
        Assertions.assertEquals(false, extensionManager.isDanger("doc"));

    }

    @Test
    void testGetFileTypes() {
        Set<FileType> fileTypes = extensionManager.getFileTypes(ExtensionFamily.MS_OFFICE);
        fileTypes.forEach(System.out::println);
        Assertions.assertEquals(4, fileTypes.size());

        fileTypes = extensionManager.getFileTypes(ExtensionRisk.HIGH);
        fileTypes.forEach(System.out::println);
        Assertions.assertEquals(true, fileTypes.size() > 0);

        FileType fileType = extensionManager.getFileType("exe");
        System.out.println("fileType=>" + fileType);
        Assertions.assertEquals("exe", fileType.getExtension());

    }

}