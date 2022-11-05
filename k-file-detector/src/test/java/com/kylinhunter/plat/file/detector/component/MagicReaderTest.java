package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.common.component.ComponentFactory;
import com.kylinhunter.plat.file.detector.common.util.MultipartFileHelper;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;

class MagicReaderTest {
    private final MagicManager magicManager = ComponentFactory.get(MagicManager.class);
    private final MagicReader magicReader = ComponentFactory.get(MagicReader.class);

    @Test
    void read() throws IOException {

        File file = ResourceHelper.getFileInClassPath("files/detected/other/pdf.pdf");

        String read1 = magicReader.read(file);

        System.out.println(read1);
        Assertions.assertEquals(magicManager.getMagicMaxLengthWitOffset() * 2, read1.length());
        Assertions.assertEquals("25504446", read1.substring(0, 8));

        read1 = magicReader.read(file, true);
        System.out.println(read1);
        Assertions.assertEquals("25504446", read1);

        MultipartFile multipartFile = MultipartFileHelper.getMultipartFile(file);

        String read2 = magicReader.read(multipartFile);
        System.out.println(read2);
        Assertions.assertEquals(magicManager.getMagicMaxLengthWitOffset() * 2, read2.length());
        Assertions.assertEquals("25504446", read2.substring(0, 8));

        read2 = magicReader.read(multipartFile, true);
        System.out.println(read2);
        Assertions.assertEquals("25504446", read2);

        byte[] bytes = FileUtils.readFileToByteArray(file);

        String read3 = magicReader.read(bytes);
        System.out.println(read3);
        Assertions.assertEquals("25504446", read3.substring(0, 8));
        Assertions.assertEquals(magicManager.getMagicMaxLengthWitOffset() * 2, read3.length());

        read3 = magicReader.read(bytes, "1.pdf", true);
        System.out.println(read3);
        Assertions.assertEquals("25504446", read3);

        read3 = magicReader.read(bytes, "1.pdf", false);
        System.out.println(read3);
        Assertions.assertEquals("25504446", read3.substring(0, 8));
        Assertions.assertEquals(magicManager.getMagicMaxLengthWitOffset() * 2, read3.length());

    }

    @Test
    void readAll() {

        File dir = ResourceHelper.getFileInClassPath("files/detected");
        System.out.println(dir.getAbsolutePath());
        Collection<File> files = FileUtils.listFiles(dir, null, true);
        files.forEach(file -> System.out
                .println("file=>" + file.getName() + "\t magic_number=>" + magicReader.read(file, true)));

    }
}