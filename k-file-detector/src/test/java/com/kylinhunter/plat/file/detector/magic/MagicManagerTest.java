package com.kylinhunter.plat.file.detector.magic;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.ConfigurationManager;
import com.kylinhunter.plat.file.detector.constant.MagicRisk;

class MagicManagerTest {

    MagicManager magicManager = ConfigurationManager.getMagicManager();

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
        Map<String, Magic> allExtensionMagics = magicManager.getAllMagics();
        allExtensionMagics.forEach((k, v) -> {
            System.out.println("extension: " + k);
            System.out.println("magic: " + v);
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
            v.getExtensionFiles().forEach(e -> System.out.println("      - " + e));
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
