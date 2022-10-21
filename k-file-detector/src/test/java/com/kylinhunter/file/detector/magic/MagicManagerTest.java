package com.kylinhunter.file.detector.magic;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.file.detector.constant.MagicRisk;
import com.kylinhunter.file.detector.extension.ExtensionManager;
import com.kylinhunter.file.detector.extension.FileTypeConfigManager;

class MagicManagerTest {

    MagicManager magicManager = MagicConfigManager.getMagicManager();

    ExtensionManager extensionManager = FileTypeConfigManager.getExtensionManager();

    @Test
    void getExtensionMagics() {
        ExtensionMagics extensionMagics1 = magicManager.getExtensionMagics(extensionManager.getFileType("docx"));
        Assertions.assertNotNull(extensionMagics1);

        ExtensionMagics extensionMagics2 = magicManager.getExtensionMagics("docx");
        Assertions.assertNotNull(extensionMagics2);
        Assertions.assertSame(extensionMagics1, extensionMagics2);

    }

    @Test
    void getMagics() {
        System.out.println("============getExtensionMagics");
        magicManager.getMagics(MagicRisk.HIGH)
                .forEach(e -> System.out.println("HIGH==>" + e.getNumber() + ":" + e.getFamilies()));

        magicManager.getMagics(MagicRisk.MIDDLE)
                .forEach(e -> System.out.println("MIDDLE==>" + e.getNumber() + ":" + e.getFamilies()));

        magicManager.getMagics(MagicRisk.LOW)
                .forEach(e -> System.out.println("LOW==>" + e.getNumber() + ":" + e.getFamilies()));
    }

    @Test
    void getAllExtensionMagics() {
        Map<String, ExtensionMagics> allExtensionMagics = magicManager.getAllExtensionMagics();
        allExtensionMagics.forEach((k, extensionMagics) -> {
            System.out.println("extension: " + k);

            System.out.println(
                    "\t magic number: " + extensionMagics.getMagics().stream().map(Magic::getNumber)
                            .collect(Collectors.toSet()));

            if (extensionMagics.getTolerateExtensions() != null) {

                System.out.println(
                        "\t tolerate disguise extension: " + extensionMagics.getTolerateExtensions());
                System.out.println(
                        "\t tolerate disguise magics: " + extensionMagics.getTolerateMagics().stream()
                                .map(Magic::getNumber).collect(Collectors.toSet()));

            }

            System.out.println();
        });
    }

    @Test
    void getAllMagics() {

        System.out.println("============getMagics");
        System.out.println("magicHelper: ");
        Map<String, Magic> allMagics = magicManager.getAllMagics();
        allMagics.forEach((k, v) -> {
            System.out.println("  - number: \"" + v.getNumber() + "\"");
            System.out.println("    desc: \"" + v.getDesc() + "\"");
            System.out.println("    extensions:");

            v.getExtensions().forEach(e -> System.out.println("      - " + e));

            System.out.println("    file-types:");
            v.getFileTypes().forEach(e -> System.out.println("      - " + e));
        });
    }

    @Test
    void getMagic() {
        Magic magic = magicManager.getMagic("0D444F43");
        Assertions.assertNotNull(magic);
    }

    @Test
    void getMagicMaxLength() {
        Assertions.assertTrue(magicManager.getMagicMaxLength() > 0);

    }
}
