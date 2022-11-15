package io.github.kylinhunter.tools.file.detector.magic;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.kylinhunter.tools.file.detector.common.component.CF;
import io.github.kylinhunter.tools.file.detector.common.util.ResourceHelper;
import io.github.kylinhunter.tools.file.detector.magic.bean.ReadMagic;

class MagicReaderTest {
    private final MagicManager magicManager = CF.get(MagicManager.class);
    private final MagicReader magicReader = CF.get(MagicReader.class);

    @Test
    void read() throws IOException {

        File file = ResourceHelper.getFileInClassPath("files/detected/other/pdf.pdf");

        ReadMagic readMagic = magicReader.read(file);
        String read1 = readMagic.getPossibleMagicNumber();

        System.out.println(read1);
        Assertions.assertEquals(magicManager.getMagicMaxLengthWitOffset() * 2, read1.length());
        Assertions.assertEquals("25504446", read1.substring(0, 8));

        readMagic = magicReader.read(file, true);
        read1 = readMagic.getPossibleMagicNumber();
        System.out.println(read1);
        Assertions.assertEquals("25504446", read1);

        byte[] bytes = FileUtils.readFileToByteArray(file);

        ReadMagic readMagic3 = magicReader.read(bytes, "1.pdf", true);
        String read3 = readMagic3.getPossibleMagicNumber();
        System.out.println(read3);
        Assertions.assertEquals("25504446", read3);

        readMagic3 = magicReader.read(bytes, "1.pdf", false);
        read3 = readMagic3.getPossibleMagicNumber();

        System.out.println(read3);
        Assertions.assertEquals("25504446", read3.substring(0, 8));
        Assertions.assertEquals(magicManager.getMagicMaxLengthWitOffset() * 2, read3.length());

    }

    @Test
    void readOffices() throws IOException {

        File dir = ResourceHelper.getFileInClassPath("files/detected/office/2007");
        System.out.println(dir.getAbsolutePath());
        Collection<File> files = FileUtils.listFiles(dir, null, true);
        Assertions.assertEquals(7, files.size());
        for (File file : files) {
            byte[] content = FileUtils.readFileToByteArray(file);

            System.out.println("file=>" + file.getName() + "/" + file.length());
            ReadMagic readMagic = magicReader.read(file, true);
            String number1 = readMagic.getPossibleMagicNumber();
            byte[] content1 = readMagic.getContent();
            System.out.println("number1/" + number1.length() + "=>" + StringUtils.substring(number1, 0, 100));
            ReadMagic readMagic2 = magicReader.read(content, file.getName(), true);
            String number2 = readMagic2.getPossibleMagicNumber();
            byte[] content2 = readMagic2.getContent();

            System.out.println("number2/" + number2.length() + "=>" + StringUtils.substring(number2, 0, 100));
            System.out.printf("c1.len= %d,c2.len=%d,f.len=%d \n", content1.length, content2.length, file.length());
            Assertions.assertArrayEquals(content, content1);
            Assertions.assertArrayEquals(content, content2);
        }

    }

}