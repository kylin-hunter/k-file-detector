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
public enum MagicFamily {
    MS_OFFICE_OLD(1, "MS_OFFICE_OLD"),
    MS_OFFICE_97_2003(2, "Microsoft Office 97-2003"),
    MS_OFFICE(3, "Microsoft Office  Open XML Format"),
    PICTURE(4, "picture"),
    AUDIO_VIDEO(5, "audio or video"),
    EXECUTABLE_FILE(6, "linux or unix executable file "),
    EXECUTABLE_FILE_WINDOWS(7, "Windows/DOS executable file"),
    EXECUTABLE_FILE_MAC(8, "mac executable file"),
    DOCUMENT(9, "common document"),
    UNKNOWN(10, "unknown");

    private final int code;
    private final String desc;

}