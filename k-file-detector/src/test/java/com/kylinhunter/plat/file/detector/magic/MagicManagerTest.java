package com.kylinhunter.plat.file.detector.magic;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.manager.MType;
import com.kylinhunter.plat.file.detector.manager.Managers;

class MagicManagerTest {

    MagicManager magicManager = Managers.get(MType.MAGIC);

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
            System.out.println("    1、number: \"" + magic.getNumber() + "\"");
            System.out.println("    2、desc: \"" + magic.getDesc() + "\"");
            System.out.println("    3、fileTypeIds: \"" + magic.getFileTypeIds() + "\"");

            System.out.println("    4、ex--magicLength:" + magic.getMagicLength());
            System.out.println("    5、ex--mode:" + magic.getMode());

            System.out.println("    6、ex--fileTypes: " + magic.getFileTypes().stream()
                    .map(e -> e.getId() + "/" + e.getExtension()).collect(Collectors.toSet()));

            System.out.println("    7、ex--extensions:" + magic.getExtensions());
            System.out.println("======================================");

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
