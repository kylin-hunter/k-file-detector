package com.kylinhunter.plat.file.detector.extension;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.CommonManager;
import com.kylinhunter.plat.file.detector.constant.ExtensionFamily;
import com.kylinhunter.plat.file.detector.constant.ExtensionRisk;

class ExtensionManagerTest {
    ExtensionManager extensionManager = CommonManager.getExtensionManager();

    @Test
    void testIsDanger() {

        Assertions.assertTrue(extensionManager.isDanger("exe"));
        Assertions.assertFalse(extensionManager.isDanger("doc"));

    }

    @Test
    void testGetFileTypes() {
        Set<ExtensionFile> extensionFiles = extensionManager.getFileTypes(ExtensionFamily.MS_OFFICE);
        extensionFiles.forEach(System.out::println);
        Assertions.assertEquals(4, extensionFiles.size());

        extensionFiles = extensionManager.getFileTypes(ExtensionRisk.HIGH);
        extensionFiles.forEach(System.out::println);
        Assertions.assertTrue(extensionFiles.size() > 0);

        ExtensionFile extensionFile = extensionManager.getFileType("exe");
        System.out.println("extensionFile=>" + extensionFile);
        Assertions.assertEquals("exe", extensionFile.getExtension());

    }

}