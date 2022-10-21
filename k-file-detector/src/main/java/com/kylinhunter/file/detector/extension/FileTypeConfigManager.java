package com.kylinhunter.file.detector.extension;

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
public class FileTypeConfigManager {
    private static FileTypeConfig FILE_TYPE_CONFIG;
    private static final String MAGIC_FILE_TYPES_LOCATION = "signature/magic_file_types.yml";
    private static ExtensionManager EXTENSION_MANAGER = new ExtensionManager();

    static {
        init();
    }

    /**
     * @return com.kylinhunter.file.detector.config.MagicConfig
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-04 15:29
     */
    private static void init() {
        FILE_TYPE_CONFIG = load();
        EXTENSION_MANAGER.init(FILE_TYPE_CONFIG);
    }

    public static FileTypeConfig getFileTypeConfig() {
        return FILE_TYPE_CONFIG;
    }

    public static ExtensionManager getExtensionManager() {
        return EXTENSION_MANAGER;
    }

    /**
     * @return com.kylinhunter.file.detector.config.MagicConfig
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-03 23:14
     */
    private static FileTypeConfig load() {

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
