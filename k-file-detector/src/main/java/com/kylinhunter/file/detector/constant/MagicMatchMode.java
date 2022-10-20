package com.kylinhunter.file.detector.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Getter
@AllArgsConstructor
public enum MagicMatchMode {
    PREFIX(1, "prefix magic number"),
    PREFIX_FUZZY(2, "fuzzy prefix  magic number");

    private final int code;
    private final String name;

}