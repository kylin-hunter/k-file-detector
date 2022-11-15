package io.github.kylinhunter.tools.file.detector.prepared.parse;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.prepared.parse.bean.ParseMagic;
import io.github.kylinhunter.tools.file.detector.prepared.parse.bean.YamlMessage;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-29 14:13
 **/
public class MagicYamlWriter {

    /**
     * @param yamlMessage yamlMessage
     * @param file        file
     * @return void
     * @title toFileTypeYaml
     * @description
     * @author BiJi'an
     * @date 2022-10-29 14:41
     */
    public static void writeFileType(YamlMessage yamlMessage, File file) {

        try {
            List<FileType> fileTypes = yamlMessage.getFileTypes();
            List<String> lines = Lists.newArrayList();

            lines.add("fileTypes: ");
            fileTypes.forEach(ft -> {
                lines.add("  - id: " + ft.getId());
                lines.add("    extensions: ");
                ft.getExtensions().forEach(extension -> {
                    lines.add("      - \"" + extension + "\"");

                });
                lines.add("    desc: '" + ft.getDesc().replace("'", "''") + "'");

            });
            FileUtils.writeLines(file, lines);
        } catch (IOException e) {
            throw new DetectException("write yaml to fileMagic error", e);
        }

    }

    /**
     * @return void
     * @title create
     * @description
     * @author BiJi'an
     * @date 2022-10-28 02:04
     */
    public static void writeMagics(YamlMessage yamlMessage, File file) {
        List<ParseMagic> parseMagics = yamlMessage.getParseMagics();

        List<String> lines = Lists.newArrayList();
        lines.add("magics: ");
        parseMagics.forEach(magic -> {
            lines.add("  - number: " + magic.getNumber());
            lines.add("    offset: " + magic.getOffset());
            lines.add("    desc: '" + magic.getDesc().replace("'", "''") + "'");
            lines.add("    fileTypes:  ");

            magic.getFileTypes().forEach(fileType -> {
                lines.add("      - id: " + fileType.getId());
                lines.add("        extensions: ");
                fileType.getExtensions().forEach(extension -> {
                    lines.add("          - \"" + extension + "\"");

                });

                //lines.add("        desc: '" + fileType.getDesc().replace("'", "''") + "'");
            });

        });
        try {
            FileUtils.writeLines(file, lines);
        } catch (IOException e) {
            throw new DetectException("write yaml to file error", e);
        }

    }
}
