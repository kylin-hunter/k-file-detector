package com.kylinhunter.file.detector.constant;


import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-12 16:24
 **/
@Getter
public enum MagicMatchType  {
    PREFIX(1, "PREFIX"),
    PREFIX_FUZZY(2, "PREFIX_FUZZY");

    private int code;
    private String name;

    MagicMatchType(int code, String name) {
        this.code = code;
        this.name = name;
    }

}