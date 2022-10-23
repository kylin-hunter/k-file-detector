package com.kylinhunter.plat.file.detector.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Getter
@AllArgsConstructor
public enum FileFamily {
    MS_OFFICE_BEFORE_97(1, "Microsoft Office Before 1997"),
    MS_OFFICE_97_2003(2, "Microsoft Office 97-2003"),
    MS_OFFICE(3, "Microsoft Office  Open XML Format"),
    PICTURE(4, "picture"),
    AUDIO_VIDEO(5, "audio or video"),
    EXECUTABLE_FILE_WINDOWS(6, "Windows/DOS executable file"),
    EXECUTABLE_FILE_MAC(7, "mac executable file"),
    EXECUTABLE_FILE(8, "linux or unix executable file "),
    DOCUMENT(9, "common document"),
    OTHERS(10, "common document"),
    UNKNOWN(11, "unknown");

    private final int code;
    private final String desc;

}