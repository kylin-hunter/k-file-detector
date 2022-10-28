package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.parse.ParseContext;
import com.kylinhunter.plat.file.detector.parse.ParseMagic;
import com.kylinhunter.plat.file.detector.parse.YamlMessage;

class MagicYamlCreatorTest {
    MagicYamlCreator magicYamlCreator = new MagicYamlCreator();

    @Test
    void readMagicNumberByReg() {
        String td0Text = "[12 byte offset] 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        ParseMagic parseMagic = magicYamlCreator.parseMagicNumber(td0Text);
        Assertions.assertEquals(12, parseMagic.getOffset());

        Assertions.assertEquals("000000000000000000000000000000000000000000000000", parseMagic.getNumber());

        td0Text = "[512 (0x200) byte offset] 00 00 00 00 00 00 00 00";
        parseMagic = magicYamlCreator.parseMagicNumber(td0Text);
        Assertions.assertEquals(512, parseMagic.getOffset());
        Assertions.assertEquals("0000000000000000", parseMagic.getNumber());

        td0Text = "[29,152 (0x71E0) byte offset] 57 69 6E 5A 69 70";
        parseMagic = magicYamlCreator.parseMagicNumber(td0Text);
        Assertions.assertEquals(false, parseMagic.isValid());
        Assertions.assertEquals(0, parseMagic.getOffset());

    }

    @Test
    void toYaml() throws IOException {
        File file = new File(System.getProperty("user.dir"), "tmp/magic.yml");
        FileUtils.forceMkdirParent(file);
        YamlMessage yamlMessage = magicYamlCreator.toYaml(file);
        Assertions.assertEquals(546, yamlMessage.getParseMagics().size());

        ParseContext parseContext = yamlMessage.getParseContext();
        Assertions.assertEquals(1173, parseContext.getTrNums());
        Assertions.assertEquals(5, parseContext.getInvalidTrNums());
        Assertions.assertEquals(1168, parseContext.getValidTrNums());

        Assertions.assertEquals(5, parseContext.getInvalidMagicNums());
        Assertions.assertEquals(546, parseContext.getValidMagicNums());
        Assertions.assertEquals(parseContext.maxMagicId(), parseContext.getValidMagicNums());

        Assertions.assertEquals(687, parseContext.maxFileTypeId());
        Assertions.assertEquals(650, parseContext.getExtensionNums());
        Assertions.assertEquals(37, parseContext.getNoExtensionNums());

        Assertions.assertEquals(parseContext.maxFileTypeId(),
                parseContext.getExtensionNums() + parseContext.getNoExtensionNums());

    }
}