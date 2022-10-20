package com.kylinhunter.file.detector.signature.config;

import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import com.kylinhunter.file.detector.constant.MagicMatchType;
import com.kylinhunter.file.detector.exception.DetectException;
import com.kylinhunter.file.detector.signature.FileTypeHelper;
import com.kylinhunter.file.detector.util.ResourceHelper;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-12 19:55
 **/
@Data
public class MagicConfigLoader {
    private static MagicConfig cachedData;

    /**
     * @return com.kylinhunter.file.detector.signature.config.MagicConfig
     * @throws
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-14 15:29
     */
    public static MagicConfig load() {
        if (cachedData != null) {
            return cachedData;
        } else {
            synchronized(MagicConfigLoader.class) {
                if (cachedData != null) {
                    return cachedData;
                }
                cachedData = load0();
                return cachedData;
            }
        }
    }

    /**
     * @return com.kylinhunter.file.detector.signature.config.MagicConfig
     * @throws
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-13 23:14
     */
    private static MagicConfig load0() {

        InputStream resource = ResourceHelper.getInputStreamInClassPath("signature/magic.yml");
        Objects.requireNonNull(resource);
        MagicConfig magicConfig = new Yaml().loadAs(resource, MagicConfig.class);
        Objects.requireNonNull(magicConfig);

        magicConfig.getMagics().forEach(magic -> {

            if (magic.getFamilies() == null) {
                throw new DetectException("magic.getFamilies is null");
            }

            if (magic.getRisk() == null) {
                throw new DetectException("magic.getRisk is null");
            }
            String number = magic.getNumber();
            if (StringUtils.isEmpty(number)) {
                return;
            }

            // dynamic calculate other attributes

            int numberLen = number.length();
            if (numberLen % 2 != 0) {
                throw new DetectException("magic number must be even");
            }

            int byteNum = numberLen / 2;
            magic.setByteNum(byteNum);

            if (number.indexOf("_") >= 0) {
                magic.setMatchType(MagicMatchType.PREFIX_FUZZY);
            } else {
                magic.setMatchType(MagicMatchType.PREFIX);

            }

            magic.getExtensions().forEach(extension -> {
                FileType fileType = FileTypeHelper.getFileType(extension);
                if (fileType == null) {
                    throw new DetectException("no filetype,for=>" + extension);
                }
                magic.addFileType(fileType);

            });

            if (byteNum > magicConfig.getMagicMaxLength()) {
                magicConfig.setMagicMaxLength(byteNum);
            }
        });
        cachedData = magicConfig;
        return magicConfig;
    }

}
