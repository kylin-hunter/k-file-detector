package com.kylinhunter.file.detector.extension;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.file.detector.constant.ExtensionFamily;
import com.kylinhunter.file.detector.constant.ExtensionRisk;

class ExtensionManagerTest {
    ExtensionManager extensionManager = FileTypeConfigManager.getExtensionManager();

    @Test
    void testIsDanger() {

        Assertions.assertTrue(extensionManager.isDanger("exe"));
        Assertions.assertFalse(extensionManager.isDanger("doc"));

    }

    @Test
    void testGetFileTypes() {
        Set<FileType> fileTypes = extensionManager.getFileTypes(ExtensionFamily.MS_OFFICE);
        fileTypes.forEach(System.out::println);
        Assertions.assertEquals(4, fileTypes.size());

        fileTypes = extensionManager.getFileTypes(ExtensionRisk.HIGH);
        fileTypes.forEach(System.out::println);
        Assertions.assertTrue(fileTypes.size() > 0);

        FileType fileType = extensionManager.getFileType("exe");
        System.out.println("fileType=>" + fileType);
        Assertions.assertEquals("exe", fileType.getExtension());

    }

}