package com.kylinhunter.file.detector.signature.config;

import java.io.InputStream;
import java.util.Objects;

import org.yaml.snakeyaml.Yaml;

import com.kylinhunter.file.detector.util.ResourceHelper;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-12 19:55
 **/
@Data
public class FileTypeConfigLoader {
    private static FileTypeConfig cachedData;

    /**
     * @return com.kylinhunter.file.detector.signature.config.MagicConfig
     * @throws
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-14 15:29
     */
    public static FileTypeConfig load() {
        if (cachedData != null) {
            return cachedData;
        } else {
            synchronized(FileTypeConfigLoader.class) {
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
    private static com.kylinhunter.file.detector.signature.config.FileTypeConfig load0() {

        InputStream resource =
                ResourceHelper.getInputStreamInClassPath("signature/magic_file_types.yml");
        Objects.requireNonNull(resource);
        com.kylinhunter.file.detector.signature.config.FileTypeConfig
                fileTypeConfig = new Yaml().loadAs(resource, com.kylinhunter.file.detector.signature.config.FileTypeConfig.class);
        Objects.requireNonNull(fileTypeConfig);

        cachedData = fileTypeConfig;
        return fileTypeConfig;
    }

    public static void main(String[] args) {
        com.kylinhunter.file.detector.signature.config.FileTypeConfig fileTypeConfig = FileTypeConfigLoader.load();
        fileTypeConfig.getFileTyes().forEach((k, v) -> {
            System.out.println(k + ":" + v);

        });
    }

}
