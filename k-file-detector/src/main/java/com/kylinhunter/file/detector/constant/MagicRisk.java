package com.kylinhunter.file.detector.constant;


import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Getter
public enum MagicRisk  {
    LOW(1, "LOW"),
    MIDDLE(2, "MIDDLE"),
    HIGH(3, "HIGH");

    private int code;
    private String name;

    MagicRisk(int code, String name) {
        this.code = code;
        this.name = name;
    }

}