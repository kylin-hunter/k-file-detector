package com.kylinhunter.file.detector.signature.config;

import java.util.List;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class MagicConfig {
    private List<com.kylinhunter.file.detector.signature.config.Magic> magics;
    private int magicMaxLength = 1;

}
