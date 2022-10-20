package com.kylinhunter.file.detector;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.file.detector.constant.MagicRisk;
import com.kylinhunter.file.detector.signature.MagicHelper;
import com.kylinhunter.file.detector.signature.config.ExtensionMagics;
import com.kylinhunter.file.detector.signature.config.Magic;

class MagicHelperTest {

    @Test
    void test() {

        System.out.println("============getAllExplicitExtensionMagics");
        Map<String, ExtensionMagics> allExtensionMagics = MagicHelper.getAllExtensionMagics();
        allExtensionMagics.forEach((k, extensionMagics) -> {
            System.out.println("extension: " + k);

            System.out.println(
                    "\t magic number: " + extensionMagics.getExplicitMagics().stream().map(Magic::getNumber)
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
        MagicHelper.getMagics(MagicRisk.HIGH).forEach(e -> {
            System.out.println("HIGH==>" + e.getNumber() + ":" + e.getFamilies());
        });

        MagicHelper.getMagics(MagicRisk.MIDDLE).forEach(e -> {
            System.out.println("MIDDLE==>" + e.getNumber() + ":" + e.getFamilies());
        });

        MagicHelper.getMagics(MagicRisk.LOW).forEach(e -> {
            System.out.println("LOW==>" + e.getNumber() + ":" + e.getFamilies());
        });

        System.out.println("============getMagics");
        System.out.println("magicHelper: ");
        Map<String, Magic> allMagics = MagicHelper.getAllMagics();
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

        System.out.println(MagicHelper.getMagicMaxLength());

        Assertions.assertEquals(
                allExtensionMagics.values().stream().flatMap(e -> e.getExplicitMagics().stream())
                        .collect(Collectors.toSet()).size(),
                allMagics.keySet().size());
    }
}