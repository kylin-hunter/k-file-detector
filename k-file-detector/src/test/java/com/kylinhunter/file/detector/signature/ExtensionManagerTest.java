package com.kylinhunter.file.detector.signature;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.bean.DetectOption;

class ExtensionManagerTest {

    @Test
    void test() {

        ExtensionManager extensionManager = new ExtensionManager();
        extensionManager.setDangerousExtensions(Sets.newHashSet("exe"));
        Assertions.assertEquals(false, extensionManager.isDanger("xxx"));
        Assertions.assertEquals(true, extensionManager.isDanger("exe"));
        DetectOption detectOption = new DetectOption();
        detectOption.addDangerousExtensionIncludes("xxx");
        detectOption.addDangerousExtensionExcludes("exe");
        extensionManager.initialize(detectOption);
        Assertions.assertEquals(true, extensionManager.isDanger("xxx"));
        Assertions.assertEquals(false, extensionManager.isDanger("exe"));
    }
}