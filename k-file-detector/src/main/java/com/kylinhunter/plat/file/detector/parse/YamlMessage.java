package com.kylinhunter.plat.file.detector.parse;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-28 16:3
 **/
@RequiredArgsConstructor
public class YamlMessage {
    @Getter
    private ParseContext parseContext = new ParseContext();

    @Getter
    @Setter
    private List<ParseMagic> parseMagics;

}
