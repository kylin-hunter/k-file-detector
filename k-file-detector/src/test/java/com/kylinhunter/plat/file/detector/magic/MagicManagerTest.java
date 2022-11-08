package com.kylinhunter.plat.file.detector.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.common.component.CF;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

class MagicManagerTest {

    private final MagicManager magicManager = CF.get(MagicManager.class);

    static void print(Magic magic) {
        System.out.println("======================================");
        System.out.println("    0、id: \"" + magic.getId() + "\"");
        System.out.println("    1、number: \"" + magic.getNumber() + "\"");
        System.out.println("    2、desc: \"" + magic.getDesc() + "\"");
        System.out.println("    3、offset: \"" + magic.getOffset() + "\"");
        System.out.println("    4、ex--fileTypes: " + magic.getFileTypes());

        System.out.println("    5、ex--length:" + magic.getLength());
        System.out.println("    6、ex--mode:" + magic.getMode());
        System.out.println("    7、ex--extensions:" + magic.getFileTypesWrapper().getExtensions());
        System.out.println("======================================");
    }

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
            System.out.println(number);
            print(magic);
        });
    }

    @Test
    void getMagic() {
        Magic magic = magicManager.getMagic("0D444F43");
        System.out.println("0D444F43:" + magic);
        Assertions.assertNotNull(magic);

        magic = magicManager.getMagic("1A45DFA3");
        System.out.println("1A45DFA3:" + magic);
        Assertions.assertNotNull(magic);

    }

    @Test
    void getMagicMaxLength() {
        int magicMaxLength = magicManager.getMagicMaxLengthWitOffset();
        System.out.println("magicMaxLength:" + magicMaxLength);
        Assertions.assertTrue(magicMaxLength > 0);

    }

    @Test
    void printAllFather() {

        List<Magic> magics = new ArrayList<>(magicManager.getAllMagics());
        magics.sort((o1, o2) -> {
            int i = o1.getOffset() - o2.getOffset();
            if (i != 0) {
                return i;
            } else {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });

        int lastPrint = 0;

        for (int i = 0; i < magics.size() - 1; i++) {
            Magic cur = magics.get(i);
            Magic next = magics.get(i + 1);
            if (next.getNumber().startsWith(cur.getNumber())) {
                if (i != lastPrint) {
                    System.out.println(i + " \t=>" + cur.getNumber());
                }
                System.out.println((i + 1) + " \t=>" + next.getNumber() + "，has child=>" + true);

                lastPrint = i + 1;
            }

        }

    }

    @Test
    void isLoadAll() {

        Assertions.assertTrue(magicManager.isLoadAll("504B030414000600"));
        Assertions.assertTrue(magicManager.isLoadAll("D0CF11E0A1B11AE1"));
        Assertions.assertFalse(magicManager.isLoadAll("667479704D345620"));
    }
}
