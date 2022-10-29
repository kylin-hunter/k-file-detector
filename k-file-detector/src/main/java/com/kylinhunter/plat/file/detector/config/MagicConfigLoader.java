package com.kylinhunter.plat.file.detector.config;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

import lombok.Data;

/**
 * @author BiJi'an
 * @description a tool for load magic.yaml
 * @date 2022-10-02 19:55
 **/
@Data
public class MagicConfigLoader {
    private static MagicConfig CACHED_DATA;
    private static final String MAGIC_LOCATION = "signature/magic.yml";
    private static final String MAGIC_EX_LOCATION = "signature/magic_ex.yml";

    /**
     * @return void
     * @title init
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:19
     */
    public static MagicConfig load() {
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

    public static MagicConfig load0() {

        MagicConfig magicConfig = load0(MAGIC_LOCATION);
        Objects.requireNonNull(magicConfig);

        MagicConfig magicConfigEx = load0(MAGIC_EX_LOCATION);
        Objects.requireNonNull(magicConfigEx);

        Map<String, Magic> exMagics = magicConfigEx.magics.stream()
                .collect(Collectors.toMap(Magic::getNumber, e -> e));
        List<Magic> magics = magicConfig.magics;
        for (int i = 0; i < magics.size(); i++) {
            Magic magic = magics.get(i);
            Magic magicEx = exMagics.get(magic.getNumber());
            if (magicEx != null) {
                magics.set(i, magicEx);
            }
        }
        return magicConfig;
    }

    /**
     * @return com.kylinhunter.file.detector.config.MagicConfig
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-03 23:14
     */
    private static MagicConfig load0(String path) {

        InputStream resource = ResourceHelper.getInputStreamInClassPath(path);
        Objects.requireNonNull(resource);
        MagicConfig magicConfig = new Yaml().loadAs(resource, MagicConfig.class);
        Objects.requireNonNull(magicConfig);
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
    }
}
