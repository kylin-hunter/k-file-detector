package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.common.util.FilenameUtil;
import com.kylinhunter.plat.file.detector.common.util.HexUtil;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.exception.DetectException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:45
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class MSOfficeDetector {
    private final FileTypeManager fileTypeManager;
    private final MagicManager magicManager;


    public void check(File file){

    }

}
