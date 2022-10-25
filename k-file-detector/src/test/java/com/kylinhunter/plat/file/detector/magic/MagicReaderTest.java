package com.kylinhunter.plat.file.detector.magic;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.manager.Service;
import com.kylinhunter.plat.file.detector.manager.ServiceFactory;
import com.kylinhunter.plat.file.detector.util.MultipartFileHelper;
import com.kylinhunter.plat.file.detector.util.ResourceHelper;

class MagicReaderTest {

    @Test
    void read() throws IOException {
        MagicConfigService magicConfigService = ServiceFactory.get(Service.MAGIC);

        File file = ResourceHelper.getFileInClassPath("files/basic/doc.doc");

        String read1 = MagicReader.read(file);

        System.out.println(read1);
        Assertions.assertEquals(magicConfigService.getMagicMaxLength() * 2, read1.length());
        Assertions.assertEquals("D0CF11E0A1B11AE1", read1.substring(0, 16));

        read1 = MagicReader.read(file, true);
        System.out.println(read1);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read1);

        MultipartFile multipartFile = MultipartFileHelper.getMultipartFile(file);

        String read2 = MagicReader.read(multipartFile);
        System.out.println(read2);
        Assertions.assertEquals(magicConfigService.getMagicMaxLength() * 2, read2.length());
        Assertions.assertEquals("D0CF11E0A1B11AE1", read2.substring(0, 16));

        read2 = MagicReader.read(multipartFile, true);
        System.out.println(read2);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read2);

        byte[] bytes = FileUtils.readFileToByteArray(file);

        String read3 = MagicReader.read(bytes);
        System.out.println(read3);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read3.substring(0, 16));
        Assertions.assertEquals(magicConfigService.getMagicMaxLength() * 2, read3.length());

        read3 = MagicReader.read(bytes, "1.doc", true);
        System.out.println(read3);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read3);

        read3 = MagicReader.read(bytes, "1.doc", false);
        System.out.println(read3);
        Assertions.assertEquals("D0CF11E0A1B11AE1", read3.substring(0, 16));
        Assertions.assertEquals(magicConfigService.getMagicMaxLength() * 2, read3.length());

    }

    @Test
    void readAll() {

        File dir = ResourceHelper.getFileInClassPath("files/basic");
        System.out.println(dir.getAbsolutePath());
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).forEach(file -> System.out
                .println("file=>" + file.getName() + "\t magic_number=>" + MagicReader.read(file, true)));

    }
}