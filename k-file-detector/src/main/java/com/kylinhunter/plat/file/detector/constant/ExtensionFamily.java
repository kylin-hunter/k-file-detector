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
public enum ExtensionFamily {
    MS_OFFICE_97_2003(1, "Microsoft Office 97-2003"),
    MS_OFFICE(2, "Microsoft Office"),
    PICTURE(3, "picture"),
    AUDIO_VIDEO(4, "audio or video"),
    EXECUTABLE_FILE(5, "linux or unix executable file "),
    EXECUTABLE_FILE_WINDOWS(6, "Windows/DOS executable file"),
    DOCUMENT(7, "common document"),
    OTHERS(8, "others"),
    UNKNOWN(9, "unknown");

    private final int code;
    private final String desc;
}