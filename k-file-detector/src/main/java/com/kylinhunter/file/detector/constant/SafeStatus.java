package com.kylinhunter.file.detector.constant;


import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-12 16:24
 **/
@Getter
public enum SafeStatus  {
    UNKNOWN(1, "UNKNOWN"),
    SAFE(2, "SAFE"),
    DANGEROUS_EXTENSION(3, "文件可能存在危害"),
    DISGUISE_WARN(4, "DISGUISE_WARN"),
    DISGUISE(5, "文件可能存在危害"),
    DANGEROUS_CONTENT(6, "文件可能存在危害");

    private int code;
    private String name;

    SafeStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public boolean greaterThan(SafeStatus safeStatus) {

        if (this.code > safeStatus.code) {
            return true;
        } else {
            return false;
        }

    }

}