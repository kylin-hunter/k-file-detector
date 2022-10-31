package com.kylinhunter.plat.file.detector.selector;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.config.bean.Magic;

class MagicComparatorTest {

    @Test
    void test() {
        List<Magic> magics = Lists.newArrayList();
        Magic magic1 = new Magic("a123");
        magic1.setOffset(0);

        Magic magic21 = new Magic("c1");
        magic21.setOffset(2);
        magic21.setLength(magic21.getNumber().length());

        Magic magic22 = new Magic("c231");
        magic22.setOffset(2);
        magic22.setLength(magic22.getNumber().length());

        Magic magic23 = new Magic("c21");
        magic23.setOffset(2);
        magic23.setLength(magic23.getNumber().length());

        Magic magic3 = new Magic("b123");
        magic3.setOffset(1);

        magics.add(magic1);

        magics.add(magic21);

        magics.add(magic22);

        magics.add(magic23);

        magics.add(magic3);

        String[] string1 =
                Stream.of(magic1, magic3, magic22, magic23, magic21).map(Magic::getNumber)
                        .toArray(String[]::new);
        magics.sort(new MagicComparator());
        String[] string2 = magics.stream().map(Magic::getNumber).toArray(String[]::new);
        magics.forEach(e -> System.out.println(e.getOffset() + "/" + e.getNumber()));
        Assertions.assertArrayEquals(string1, string2);

    }

}