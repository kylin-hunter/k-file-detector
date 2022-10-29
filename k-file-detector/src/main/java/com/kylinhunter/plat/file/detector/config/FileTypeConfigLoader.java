package com.kylinhunter.plat.file.detector.config;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.yaml.snakeyaml.Yaml;

import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.config.bean.FileType;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class FileTypeConfigLoader {
    private static FileTypeConfig CACHED_DATA;
    private static final String MAGIC_FILE_TYPES_LOCATION = "signature/file_types.yml";
    private static final String MAGIC_FILE_TYPES_EX_LOCATION = "signature/file_types_ex.yml";

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
    public static FileTypeConfig load0() {

        FileTypeConfig fileTypeConfig = load0(MAGIC_FILE_TYPES_LOCATION);
        Objects.requireNonNull(fileTypeConfig);

        FileTypeConfig fileTypeConfigEx = load0(MAGIC_FILE_TYPES_EX_LOCATION);
        Objects.requireNonNull(fileTypeConfigEx);

        fileTypeConfig.fileTypes.addAll(fileTypeConfigEx.getFileTypes());
        return fileTypeConfig;
    }

    public static FileTypeConfig load0(String path) {

        InputStream resource = ResourceHelper.getInputStreamInClassPath(path);
        Objects.requireNonNull(resource);
        Yaml yaml = new Yaml();

        FileTypeConfig fileTypeConfig = yaml.loadAs(resource, FileTypeConfig.class); // load config from yaml
        Objects.requireNonNull(fileTypeConfig);
        return fileTypeConfig;
    }

    /**
     * @author BiJi'an
     * @description
     * @date 2022-10-02 19:55
     **/
    @Data
    public static class FileTypeConfig {
        private List<FileType> fileTypes;
    }

}
