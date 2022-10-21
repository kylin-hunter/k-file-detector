package com.kylinhunter.file.detector.magic;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import com.kylinhunter.file.detector.constant.MagicMatchMode;
import com.kylinhunter.file.detector.exception.DetectException;
import com.kylinhunter.file.detector.extension.FileType;
import com.kylinhunter.file.detector.extension.FileTypeConfigManager;
import com.kylinhunter.file.detector.util.ResourceHelper;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class MagicConfigManager {
    private static MagicManager MAGIC_MANAGER = new MagicManager();
    private static MagicConfig MAGIC_CONFIG;
    private static final String MAGIC_LOCATION = "signature/magic.yml";

    static {
        init();
    }

    /**
     * @return void
     * @title init
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:19
     */
    private static void init() {
        MAGIC_CONFIG = load();
        MAGIC_MANAGER.init(MAGIC_CONFIG);
    }

    public static MagicManager getMagicManager() {
        return MAGIC_MANAGER;
    }

    /**
     * @return com.kylinhunter.file.detector.config.MagicConfig
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-03 23:14
     */
    private static MagicConfig load() {

        InputStream resource = ResourceHelper.getInputStreamInClassPath(MAGIC_LOCATION);
        Objects.requireNonNull(resource);
        MagicConfig magicConfig = new Yaml().loadAs(resource, MagicConfig.class);
        Objects.requireNonNull(magicConfig);

        magicConfig.getMagics().forEach(magic -> {

            Objects.requireNonNull(magic.getFamilies(), "magic.getFamilies is null");
            Objects.requireNonNull(magic.getRisk(), "magic.getRisk is null");
            String number = magic.getNumber();
            if (StringUtils.isEmpty(number)) {
                return;
            }

            // dynamic calculate other attributes

            int numberLen = number.length();
            if (numberLen % 2 != 0) {
                throw new DetectException("magic number must be even");
            }

            int magicLength = numberLen / 2;
            magic.setMagicLength(magicLength);
            if (number.contains("_")) {
                magic.setMatchMode(MagicMatchMode.PREFIX_FUZZY);
            } else {
                magic.setMatchMode(MagicMatchMode.PREFIX);

            }

            magic.getExtensions().forEach(extension -> {
                FileType fileType = FileTypeConfigManager.getExtensionManager().getFileType(extension);
                if (fileType == null) {
                    throw new DetectException("no filetype,for=>" + extension);
                }
                magic.addFileType(fileType);

            });

            if (magicLength > magicConfig.getMagicMaxLength()) {
                magicConfig.setMagicMaxLength(magicLength);
            }
        });
        return magicConfig;
    }

    /**
     * @author BiJi'an
     * @description
     * @date 2022-10-02 19:55
     **/
    @Data
    public static class MagicConfig {
        private List<Magic> magics;
        private int magicMaxLength = 1;

    }
}
