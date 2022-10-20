package com.kylinhunter.file.detector.signature.config;

import java.util.Map;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class FileTypeConfig {
    private Map<String, FileType> fileTyes;

}
