package com.kylinhunter.file.detector.config;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.file.detector.magic.MagicConfigManager;
import com.kylinhunter.file.detector.magic.MagicManager;
import com.kylinhunter.file.detector.constant.MagicRisk;
import com.kylinhunter.file.detector.magic.ExtensionMagics;
import com.kylinhunter.file.detector.magic.Magic;

class MagicManagerTest {

    @Test
    void test() {
        MagicManager magicManager = MagicConfigManager.getMagicManager();

        System.out.println("============getAllExplicitExtensionMagics");
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
                                .map(e -> e.getNumber()).collect(Collectors.toSet()));

            }

            System.out.println();
        });

        System.out.println("============getExtensionMagics");
        magicManager.getMagics(MagicRisk.HIGH).forEach(e -> {
            System.out.println("HIGH==>" + e.getNumber() + ":" + e.getFamilies());
        });

        magicManager.getMagics(MagicRisk.MIDDLE).forEach(e -> {
            System.out.println("MIDDLE==>" + e.getNumber() + ":" + e.getFamilies());
        });

        magicManager.getMagics(MagicRisk.LOW).forEach(e -> {
            System.out.println("LOW==>" + e.getNumber() + ":" + e.getFamilies());
        });

        System.out.println("============getMagics");
        System.out.println("magicHelper: ");
        Map<String, Magic> allMagics = magicManager.getAllMagics();
        allMagics.forEach((k, v) -> {
            System.out.println("  - number: \"" + v.getNumber() + "\"");
            System.out.println("    desc: \"" + v.getDesc() + "\"");
            System.out.println("    extensions:");

            v.getExtensions().forEach(e -> {
                System.out.println("      - " + e);

            });

            System.out.println("    file-types:");
            v.getFileTypes().forEach(e -> {
                System.out.println("      - " + e);

            });
        });

        System.out.println("============getMagicMaxLength");

        System.out.println(magicManager.getMagicMaxLength());

        Assertions.assertEquals(
                allExtensionMagics.values().stream().flatMap(e -> e.getMagics().stream())
                        .collect(Collectors.toSet()).size(),
                allMagics.keySet().size());
    }
}