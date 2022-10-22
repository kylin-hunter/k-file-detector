package com.kylinhunter.plat.file.detector.magic;

import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import com.kylinhunter.plat.file.detector.constant.MagicMatchMode;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.extension.ExtensionConfigLoader;
import com.kylinhunter.plat.file.detector.extension.ExtensionFile;
import com.kylinhunter.plat.file.detector.extension.ExtensionManager;
import com.kylinhunter.plat.file.detector.util.ResourceHelper;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class MagicConfigLoader {
    private static MagicConfig CACHED_DATA;
    private static final String MAGIC_LOCATION = "signature/magic.yml";

    /**
     * @return void
     * @title init
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:19
     */
    public static MagicConfig load(ExtensionManager extensionManager) {
        if (CACHED_DATA != null) {
            return CACHED_DATA;
        } else {
            synchronized(ExtensionConfigLoader.class) {
                if (CACHED_DATA != null) {
                    return CACHED_DATA;
                }
                CACHED_DATA = load0(extensionManager);
                return CACHED_DATA;
            }
        }
    }

    /**
     * @return com.kylinhunter.file.detector.config.MagicConfig
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-03 23:14
     */
    private static MagicConfig load0(ExtensionManager extensionManager) {

        InputStream resource = ResourceHelper.getInputStreamInClassPath(MAGIC_LOCATION);
        Objects.requireNonNull(resource);
        MagicConfig magicConfig = new Yaml().loadAs(resource, MagicConfig.class);
        Objects.requireNonNull(magicConfig);

        magicConfig.getMagics().forEach(magic -> {
            Set<String> extensions = magic.getExtensions();
            magic.setExtensions(extensions.stream().map(e -> e.toLowerCase()).collect(Collectors.toSet()));
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

            extensions.forEach(extension -> {
                ExtensionFile extensionFile = extensionManager.getFileType(extension);
                if (extensionFile == null) {
                    throw new DetectException("no filetype,for=>" + extension);
                }
                magic.addFileType(extensionFile);

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
        private Set<Magic> magics;
        private int magicMaxLength = 1;

    }
}
