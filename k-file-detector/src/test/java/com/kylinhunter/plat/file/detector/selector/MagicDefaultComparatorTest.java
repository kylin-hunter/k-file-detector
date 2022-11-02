package com.kylinhunter.plat.file.detector.selector;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.common.component.ComponentFactory;
import com.kylinhunter.plat.file.detector.component.FileTypeManager;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

class MagicDefaultComparatorTest {
    private final FileTypeManager fileTypeManager = ComponentFactory.get(FileTypeManager.class);

    @Test
    void test() {
        List<FileType> fileTypes = Lists.newArrayList();
        fileTypes.add(fileTypeManager.getFileTypeById("0_pdf"));
        Magic magica1 = new Magic("a1");
        magica1.setOffset(0);
        magica1.setFileTypes(fileTypes);

        Magic magica21 = new Magic("a2__12");
        magica21.setOffset(2);
        magica21.setFileTypes(fileTypes);

        Magic magica22 = new Magic("a2__123456");
        magica22.setOffset(2);
        magica22.setFileTypes(fileTypes);

        Magic magica23 = new Magic("a2__1234");
        magica23.setOffset(2);
        magica23.setFileTypes(fileTypes);

        Magic magica3 = new Magic("a3");
        magica3.setOffset(1);
        magica3.setFileTypes(fileTypes);

        List<FileType> fileTypesb = Lists.newArrayList();
        fileTypesb.add(fileTypeManager.getFileTypeById("0_mp3"));

        Magic magicb1 = new Magic("b1");
        magicb1.setOffset(0);
        magicb1.setFileTypes(fileTypesb);

        Magic magicb21 = new Magic("b2__12");
        magicb21.setOffset(2);
        magicb21.setFileTypes(fileTypesb);

        Magic magicb22 = new Magic("b2__123456");
        magicb22.setOffset(2);
        magicb22.setFileTypes(fileTypesb);

        Magic magicb23 = new Magic("b2__1234");
        magicb23.setOffset(2);
        magicb23.setFileTypes(fileTypesb);

        Magic magicb3 = new Magic("b3");
        magicb3.setOffset(1);
        magicb3.setFileTypes(fileTypesb);

        List<SortMagic> sortMagics = Lists.newArrayList();
        sortMagics.add(new SortMagic(magica1, "mp3"));

        sortMagics.add(new SortMagic(magica21, "mp3"));

        sortMagics.add(new SortMagic(magica22, "mp3"));

        sortMagics.add(new SortMagic(magica23, "mp3"));

        sortMagics.add(new SortMagic(magica3, "mp3"));

        sortMagics.add(new SortMagic(magicb1, "mp3"));

        sortMagics.add(new SortMagic(magicb21, "mp3"));

        sortMagics.add(new SortMagic(magicb22, "mp3"));

        sortMagics.add(new SortMagic(magicb23, "mp3"));

        sortMagics.add(new SortMagic(magicb3, "mp3"));

        String[] expect =
                Stream.of(
                        magicb1, magicb3, magicb22, magicb23, magicb21,
                        magica1, magica3, magica22, magica23, magica21

                ).map(Magic::getNumber)
                        .toArray(String[]::new);
        sortMagics.sort(new MagicDefaultComparator());
        String[] actual = sortMagics.stream().map(e -> e.getMagic().getNumber()).toArray(String[]::new);

        sortMagics.forEach(e -> System.out.println(e.getMagic().getFileTypes() + "/" +
                e.getMagic().getOffset() + "/" + e.getMagic().getNumber()));
        Assertions.assertArrayEquals(expect, actual);

    }

}