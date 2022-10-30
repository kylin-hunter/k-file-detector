package com.kylinhunter.plat.file.detector.config;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;
import com.kylinhunter.plat.file.detector.config.bean.MagicEx;

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
    private static final String MAGIC_LOCATION_EX = "signature/magic_ex.yml";

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

        MagicConfig magicConfig = load0(MagicConfig.class, MAGIC_LOCATION);
        Objects.requireNonNull(magicConfig);

        MagicConfigEx magicConfigEx = load0(MagicConfigEx.class, MAGIC_LOCATION_EX);
        Objects.requireNonNull(magicConfigEx);

        Map<String, MagicEx> exMagics = magicConfigEx.magics.stream()
                .collect(Collectors.toMap(MagicEx::getNumber, e -> e));
        List<Magic> magics = magicConfig.magics;
        for (int i = 0; i < magics.size(); i++) {
            Magic magic = magics.get(i);
            MagicEx magicEx = exMagics.get(magic.getNumber());
            if (magicEx != null) {
                copy(magicEx, magic);
            }
        }
        return magicConfig;
    }

    private static void copy(MagicEx magicEx, Magic magic) {
        if (magicEx.getOffset() > 0) {
            magic.setOffset(magicEx.getOffset());
        }
        if (!StringUtils.isEmpty(magicEx.getDesc())) {
            magic.setDesc(magicEx.getDesc());
        }
        List<FileType> excludeFileTypes = magicEx.getExcludeFileTypes();
        if (!CollectionUtils.isEmpty(excludeFileTypes)) {
            Iterator<FileType> iterator = magic.getFileTypes().iterator();
            while (iterator.hasNext()) {
                FileType fileType = iterator.next();
                if (excludeFileTypes.contains(fileType)) {
                    iterator.remove();
                }
            }
        }
        List<FileType> includeFileTypes = magicEx.getIncludeFileTypes();
        if (!CollectionUtils.isEmpty(includeFileTypes)) {
            includeFileTypes.forEach(fileType -> {
                magic.getFileTypes().add(fileType);
            });
        }

    }

    /**
     * @return com.kylinhunter.file.detector.config.MagicConfig
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-03 23:14
     */
    private static <T> T load0(Class<T> clazz, String path) {

        InputStream resource = ResourceHelper.getInputStreamInClassPath(path);
        Objects.requireNonNull(resource);
        T magicConfig = new Yaml().loadAs(resource, clazz);
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

    /**
     * @author BiJi'an
     * @description
     * @date 2022-10-02 19:55
     **/
    @Data
    public static class MagicConfigEx {
        private List<MagicEx> magics;
    }
}
