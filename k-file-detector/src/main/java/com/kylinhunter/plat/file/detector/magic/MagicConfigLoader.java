package com.kylinhunter.plat.file.detector.magic;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.constant.MagicMatchMode;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.type.FileType;
import com.kylinhunter.plat.file.detector.type.FileTypeConfigLoader;
import com.kylinhunter.plat.file.detector.type.FileTypeManager;
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
    public static MagicConfig load(FileTypeManager fileTypeManager) {
        if (CACHED_DATA != null) {
            return CACHED_DATA;
        } else {
            synchronized(FileTypeConfigLoader.class) {
                if (CACHED_DATA != null) {
                    return CACHED_DATA;
                }
                CACHED_DATA = load0(fileTypeManager);
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
    private static MagicConfig load0(FileTypeManager fileTypeManager) {

        InputStream resource = ResourceHelper.getInputStreamInClassPath(MAGIC_LOCATION);
        Objects.requireNonNull(resource);
        MagicConfig magicConfig = new Yaml().loadAs(resource, MagicConfig.class);
        Objects.requireNonNull(magicConfig);

        magicConfig.getMagics().forEach(magic -> {
            Set<String> fileTypeIds = magic.getFileTypeIds();
            if (fileTypeIds != null) {
                Set<String> extensions = Sets.newHashSet();
                Set<FileType> fileTypes = Sets.newHashSet();

                fileTypeIds.forEach(id -> {
                    FileType fileType = fileTypeManager.getFileTypeById(id);
                    if (fileType == null) {
                        throw new DetectException("no file type :" + id);
                    }
                    extensions.add(fileType.getExtension());
                    fileTypes.add(fileType);

                });
                magic.setExtensions(extensions);
                magic.setFileTypes(fileTypes);
            }

            String number = magic.getNumber();
            if (StringUtils.isEmpty(number)) {
                throw new DetectException("number can't be empty");

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
