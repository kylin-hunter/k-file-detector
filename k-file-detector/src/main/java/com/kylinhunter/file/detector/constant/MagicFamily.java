package com.kylinhunter.file.detector.constant;


import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-12 16:24
 **/
@Getter
public enum MagicFamily  {
    MS_OFFICE_OLD_DOCUMENT(1, "MS_OFFICE_OLD_DOCUMENT"),
    MS_OFFICE_97_DOCUMENT(2, "Microsoft Office 97-2003"),
    MS_OFFICE_OPEN_XML_FORMAT_DOCUMENT(3, "Microsoft Office  Open XML Format"),
    PIC(4, "PICTURE"),
    AV(5, "AV"),
    EXECUTABLE_FILE_WINDOWS(6, "Windows/DOS executable file"),
    EXECUTABLE_FILE(7, "EXECUTABLE_FILE"),
    EXECUTABLE_FILE_APPLE(8, "EXECUTABLE_FILE_APPLE"),
    DOCUMENT(9, "DOCUMENT"),
    UNKNOWN(10, "UNKNOWN");

    private int code;
    private String desc;

    MagicFamily(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}