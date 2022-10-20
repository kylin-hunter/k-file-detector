package com.kylinhunter.file.detector.config;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.util.ResourceHelper;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class FileTypeConfigLoader {
    private static FileTypeConfig CACHED_DATA;
    private static final String MAGIC_FILE_TYPES_LOCATION = "signature/magic_file_types.yml";

    /**
     * @return com.kylinhunter.file.detector.config.MagicConfig
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:29
     */
    public static FileTypeConfig load() {
        if (CACHED_DATA != null) {
            return CACHED_DATA;
        } else {
            synchronized(FileTypeConfigLoader.class) {
                if (CACHED_DATA != null) {
                    return CACHED_DATA;
                }
                CACHED_DATA = load0();
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
    private static FileTypeConfig load0() {

        InputStream resource = ResourceHelper.getInputStreamInClassPath(MAGIC_FILE_TYPES_LOCATION);
        Objects.requireNonNull(resource);
        FileTypeConfig fileTypeConfig = new Yaml().loadAs(resource, FileTypeConfig.class); // load config from yaml
        Objects.requireNonNull(fileTypeConfig);
        Map<String, Set<String>> tolerateDisguiseGroupDatas = Maps.newHashMap();

        fileTypeConfig.fileTyes.values().forEach(fileType -> {
            String tolerateDisguiseGroup = fileType.getTolerateGroup();
            if (tolerateDisguiseGroup != null) {
                Set<String> tolerateExtensions = tolerateDisguiseGroupDatas.compute(tolerateDisguiseGroup,
                        (k, v) -> {
                            if (v == null) {
                                v = Sets.newHashSet();
                            }
                            v.add(fileType.getExtension());
                            return v;
                        });
                fileType.setTolerateExtensions(tolerateExtensions);
            }
        });

        return fileTypeConfig;
    }

    /**
     * @author BiJi'an
     * @description
     * @date 2022-10-02 19:55
     **/
    @Data
    public static class FileTypeConfig {
        private Map<String, FileType> fileTyes; // all file types

    }

}
