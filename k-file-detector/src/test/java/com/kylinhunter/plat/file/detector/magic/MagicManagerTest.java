package com.kylinhunter.plat.file.detector.magic;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.CommonManager;

class MagicManagerTest {

    MagicManager magicManager = CommonManager.getMagicManager();

    @Test
    void getAllMagics() {
        Assertions.assertTrue(magicManager.getAllMagics().size() > 0);

    }

    @Test
    void getNumberMagics() {

        System.out.println("============getNumberMagics");
        System.out.println("magicHelper: ");
        Map<String, Magic> allMagics = magicManager.getNumberMagics();
        allMagics.forEach((number, magic) -> {
            System.out.println("  - number: \"" + magic.getNumber() + "\"");
            System.out.println("    desc: \"" + magic.getDesc() + "\"");
            System.out.println("    extensions:");
            magic.getExtensions().forEach(e -> System.out.println("      - " + e));
        });
    }

    @Test
    void getMagic() {
        Magic magic = magicManager.getMagic("0D444F43");
        System.out.println("0D444F43:" + magic);
        Assertions.assertNotNull(magic);
    }

    @Test
    void getMagicMaxLength() {
        int magicMaxLength = magicManager.getMagicMaxLength();
        System.out.println("magicMaxLength:" + magicMaxLength);
        Assertions.assertTrue(magicMaxLength > 0);

    }
}
