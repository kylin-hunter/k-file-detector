package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.config.Magic;

class MagicHtmlParserTest {

    @Test
    void parse() throws IOException {
        MagicHtmlParser magicHtmlParser = new MagicHtmlParser();
        List<Magic> magics = magicHtmlParser.parse();

        List<String> lines = Lists.newArrayList();
        lines.add("magics: ");
        magics.forEach(magic -> {
            lines.add("  - number: " + magic.getNumber());
            lines.add("    desc: \"" + magic.getDesc().replace("\"","\\\"") + "\"");
            lines.add("    fileTypes:  ");

            magic.getFileTypes().forEach(fileType -> {
                lines.add("      - id: " + fileType.getId());
                lines.add("        extension: \"" + fileType.getExtension() + "\"");
                lines.add("        desc: \"" + fileType.getDesc().replace("\"","\\\"")  + "\"");
            });

        });
        System.out.println("stringBuilder=>\n" + lines);

        File file = new File(System.getProperty("user.dir"), "tmp/magic.yml");
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("mkdirs error");
            }
        }
        FileUtils.writeLines(file, lines);
        Assertions.assertEquals(551, magics.size());
    }
}