package io.github.kylinhunter.tools.file.detector.content.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-13 12:25
 **/
@RequiredArgsConstructor
@Getter
public enum ContentDetectType {
    MS_OFFICE_2007(true, "504B030414000600"),
    XML(false, ""),
    HTML(false, "");

    private final boolean relatedMagic;
    private final String magicNumer;

}
