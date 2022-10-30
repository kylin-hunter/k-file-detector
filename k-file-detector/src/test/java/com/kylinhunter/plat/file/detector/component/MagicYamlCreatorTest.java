package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.parse.bean.ParseContext;
import com.kylinhunter.plat.file.detector.parse.bean.ParseMagic;
import com.kylinhunter.plat.file.detector.parse.bean.ParseStat;
import com.kylinhunter.plat.file.detector.parse.bean.YamlMessage;

class MagicYamlCreatorTest {
    MagicYamlCreator magicYamlCreator = new MagicYamlCreator();

    @Test
    void readMagicNumberByReg() {
        String td0Text = "[12 byte offset] 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        ParseMagic parseMagic = magicYamlCreator.processTdsMagicDefault(td0Text);
        Assertions.assertEquals(12, parseMagic.getOffset());

        Assertions.assertEquals("000000000000000000000000000000000000000000000000", parseMagic.getNumber());

        td0Text = "[512 (0x200) byte offset] 00 00 00 00 00 00 00 00";
        parseMagic = magicYamlCreator.processTdsMagicDefault(td0Text);
        Assertions.assertEquals(512, parseMagic.getOffset());
        Assertions.assertEquals("0000000000000000", parseMagic.getNumber());

        td0Text = "[29,152 (0x71E0) byte offset] 57 69 6E 5A 69 70";
        parseMagic = magicYamlCreator.processTdsMagicDefault(td0Text);
        Assertions.assertFalse(parseMagic.isValid());
        Assertions.assertEquals(0, parseMagic.getOffset());

    }

    @Test
    void toYaml() throws IOException {
        File fileMagic = new File(System.getProperty("user.dir"), "tmp/magic.yml");
        FileUtils.forceMkdirParent(fileMagic);

        File fileFileTypes = new File(System.getProperty("user.dir"), "tmp/file_types.yml");
        FileUtils.forceMkdirParent(fileFileTypes);

        YamlMessage yamlMessage = magicYamlCreator.toYaml(fileMagic, fileFileTypes);
        Assertions.assertEquals(544, yamlMessage.getParseMagics().size());

        ParseContext parseContext = yamlMessage.getParseContext();
        ParseStat parseStat = parseContext.getParseStat();
        Assertions.assertEquals(1173, parseStat.getTrNums());
        Assertions.assertEquals(5, parseStat.getTrInvalidNums());
        Assertions.assertEquals(1168, parseStat.getTrValidNums());
        Assertions.assertEquals(parseStat.getTrNums(), parseStat.getTrInvalidNums() + parseStat.getTrValidNums());

        Assertions.assertEquals(7, parseStat.getMagicInvalidNums());
        Assertions.assertEquals(544, parseStat.getMagicValidNums());

        Assertions.assertEquals(644, parseStat.getExtensionNums());
        Assertions.assertEquals(37, parseStat.getExtensionNoneNums());
        Assertions.assertEquals(664, parseStat.getFileTypeNums());
        Assertions.assertEquals(17, parseStat.getFileTypeDuplicateNums());

        Assertions.assertEquals(parseStat.getFileTypeNums() + parseStat.getFileTypeDuplicateNums(),
                parseStat.getExtensionNums() + parseStat.getExtensionNoneNums());

    }
}