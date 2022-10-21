package com.kylinhunter.file.detector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.file.detector.magic.MagicConfigManager;
import com.kylinhunter.file.detector.magic.MagicManager;
import com.kylinhunter.file.detector.magic.MagicReader;
import com.kylinhunter.file.detector.util.MultipartFileHelper;
import com.kylinhunter.file.detector.util.ResourceHelper;

class MagicReaderTest {

    @Test
    void read() throws IOException {
        MagicManager magicManager = MagicConfigManager.getMagicManager();

        File file = ResourceHelper.getFileInClassPath("files/safe/doc.doc");

        String read1 = MagicReader.read(file);

        System.out.println(read1);
        Assertions.assertEquals(magicManager.getMagicMaxLength() * 2, read1.length());
        Assertions.assertEquals("D0CF11E0A1B11AE1", read1.substring(0, 16));

        read1 = MagicReader.read(file, true);
        System.out.println(read1);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read1);

        MultipartFile multipartFile = MultipartFileHelper.getMultipartFile(file);

        String read2 = MagicReader.read(multipartFile);
        System.out.println(read2);
        Assertions.assertEquals(magicManager.getMagicMaxLength() * 2, read2.length());
        Assertions.assertEquals("D0CF11E0A1B11AE1", read2.substring(0, 16));

        read2 = MagicReader.read(multipartFile, true);
        System.out.println(read2);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read2);

        byte[] bytes = FileUtils.readFileToByteArray(file);

        String read3 = MagicReader.read(bytes);
        System.out.println(read3);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read3.substring(0, 16));
        Assertions.assertEquals(magicManager.getMagicMaxLength() * 2, read3.length());

        read3 = MagicReader.read(bytes, "1.doc", true);
        System.out.println(read3);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read3);

        read3 = MagicReader.read(bytes, "1.doc", false);
        System.out.println(read3);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read3.substring(0, 16));
        Assertions.assertEquals(magicManager.getMagicMaxLength() * 2, read3.length());

    }

    @Test
    void readAll() throws IOException {

        File dir = ResourceHelper.getFileInClassPath("files");
        System.out.println(dir.getAbsolutePath());
        Arrays.stream(dir.listFiles()).forEach(file -> {
            System.out.println("file=>" + file.getName()+"\t magic_number=>"+ MagicReader.read(file, true));

        });

    }
}