package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.parse.ParseMagic;
import com.kylinhunter.plat.file.detector.parse.ParseResult;

class MagicYamlCreatorTest {
    MagicYamlCreator magicYamlCreator = new MagicYamlCreator();

    @Test
    void readMagicNumberByReg() {
        String td0Text = "[12 byte offset] 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        ParseMagic parseMagic = magicYamlCreator.parseMagicNumber(td0Text);
        Assertions.assertEquals("12", parseMagic.getOffset());

        Assertions.assertEquals("000000000000000000000000000000000000000000000000", parseMagic.getNumber());

        td0Text = "[512 (0x200) byte offset] 00 00 00 00 00 00 00 00";
        parseMagic = magicYamlCreator.parseMagicNumber(td0Text);
        Assertions.assertEquals("512", parseMagic.getOffset());
        Assertions.assertEquals("0000000000000000", parseMagic.getNumber());

        td0Text = "[29,152 (0x71E0) byte offset] 57 69 6E 5A 69 70";
        parseMagic = magicYamlCreator.parseMagicNumber(td0Text);
        Assertions.assertEquals("29,152", parseMagic.getOffset());
        Assertions.assertEquals("57696E5A6970", parseMagic.getNumber());
    }

    @Test
    void toYaml() throws IOException {

        ParseResult parseResult = magicYamlCreator.parse();
        List<ParseMagic> parseMagics = parseResult.getParseMagics();
        List<String> lines = magicYamlCreator.toYaml(parseMagics);

        System.out.println("stringBuilder=>\n" + lines);

        File file = new File(System.getProperty("user.dir"), "tmp/magic.yml");
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("mkdirs error");
            }
        }
        FileUtils.writeLines(file, lines);
        Assertions.assertEquals(1173, parseResult.getTrNums());
        Assertions.assertEquals(5, parseResult.getInvalidTrNums());
        Assertions.assertEquals(1168, parseResult.getValidTrNums());
        Assertions.assertEquals(4, parseResult.getInvalidMagicNums());
        Assertions.assertEquals(547, parseResult.getValidMagicNums());
        Assertions.assertEquals(547, parseResult.getParseMagics().size());

    }
}