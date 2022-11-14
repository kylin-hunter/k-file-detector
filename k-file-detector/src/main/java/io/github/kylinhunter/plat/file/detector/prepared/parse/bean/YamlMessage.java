package io.github.kylinhunter.plat.file.detector.prepared.parse.bean;

import java.util.List;

import io.github.kylinhunter.plat.file.detector.file.bean.FileType;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-28 16:3
 **/
@Data
public class YamlMessage {
    private final ParseContext parseContext;
    private List<ParseMagic> parseMagics;
    private List<FileType> fileTypes;

}
