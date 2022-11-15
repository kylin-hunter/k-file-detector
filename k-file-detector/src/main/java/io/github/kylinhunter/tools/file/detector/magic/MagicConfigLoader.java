package io.github.kylinhunter.tools.file.detector.magic;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import io.github.kylinhunter.tools.file.detector.common.util.ResourceHelper;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.FileTypeConfigLoader;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.AdjustMagic;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;

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
    private static final String MAGIC_ADJUST_LOCATION = "signature/magic_adjust.yml";

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

        Map<String, Magic> magicMap = magicConfig.magics.stream().collect(Collectors.toMap(Magic::getNumber, e -> e));

        AdjustMagicConfig adjustMagicConfig = load0(AdjustMagicConfig.class, MAGIC_ADJUST_LOCATION);
        Objects.requireNonNull(adjustMagicConfig);

        for (AdjustMagic adjustMagic : adjustMagicConfig.adjustMagics) {
            Magic magic = magicMap.get(adjustMagic.getNumber());
            if (magic != null) {
                processAdjustMagic(adjustMagic, magic, magicMap);
            } else {
                throw new DetectException("invalid  adjust magic :" + adjustMagic.getNumber());
            }
            if (!magic.isEnabled()) {
                magicConfig.magics.remove(magic);
            }
        }
        return magicConfig;
    }

    private static void processAdjustMagic(AdjustMagic adjustMagic, Magic magic, Map<String, Magic> magicMap) {

        magic.setEnabled(adjustMagic.isEnabled());
        String refMagic = adjustMagic.getRefMagic();
        if (!StringUtils.isEmpty(refMagic)) {
            Magic refrenceMagic = magicMap.get(refMagic);
            if (refrenceMagic == null) {
                throw new DetectException("invalid refMagic :" + refMagic);
            }
            magic.setRefMagic(refrenceMagic);
        }
        magic.setDetectContentSupport(adjustMagic.isDetectContentSupport());

        if (!StringUtils.isEmpty(adjustMagic.getDesc())) {
            magic.setDesc(adjustMagic.getDesc());
        }
        List<FileType> excludeFileTypes = adjustMagic.getExcludeFileTypes();
        if (!CollectionUtils.isEmpty(excludeFileTypes)) {
            magic.getFileTypes().removeIf(excludeFileTypes::contains);
        }
        List<FileType> includeFileTypes = adjustMagic.getIncludeFileTypes();
        if (!CollectionUtils.isEmpty(includeFileTypes)) {
            includeFileTypes.forEach(fileType -> magic.getFileTypes().add(fileType));
        }
        List<FileType> topFileTypes = adjustMagic.getTopFileTypes();
        if (!CollectionUtils.isEmpty(topFileTypes)) {
            List<FileType> fileTypes = magic.getFileTypes();
            fileTypes.removeIf(topFileTypes::contains);
            fileTypes.addAll(0, topFileTypes);
        }

    }

    /**
     * @return io.github.kylinhunter.file.detector.config.MagicConfig
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
    public static class AdjustMagicConfig {
        private List<AdjustMagic> adjustMagics;
    }
}
