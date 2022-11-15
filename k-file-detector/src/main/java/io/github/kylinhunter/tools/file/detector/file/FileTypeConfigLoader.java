package io.github.kylinhunter.tools.file.detector.file;

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
import io.github.kylinhunter.tools.file.detector.file.bean.AdjustFileType;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 19:55
 **/
@Data
public class FileTypeConfigLoader {
    private static FileTypeConfig CACHED_DATA;
    private static final String FILE_TYPES_LOCATION = "signature/file_types.yml";
    private static final String FILE_TYPES_ADJUST_LOCATION = "signature/file_types_adjust.yml";

    /**
     * @return io.github.kylinhunter.file.detector.config.MagicConfig
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
     * @return io.github.kylinhunter.file.detector.config.MagicConfig
     * @title load
     * @description
     * @author BiJi'an
     * @date 2022-10-03 23:14
     */
    public static FileTypeConfig load0() {

        FileTypeConfig fileTypeConfig = load0(FileTypeConfig.class, FILE_TYPES_LOCATION);
        Objects.requireNonNull(fileTypeConfig);

        List<FileType> fileTypes = fileTypeConfig.fileTypes;
        Map<String, FileType> fileTypeMap = fileTypes.stream()
                .collect(Collectors.toMap(FileType::getId, e -> e));

        AdjustFileTypeConfig adjustFileTypeConfig = load0(AdjustFileTypeConfig.class, FILE_TYPES_ADJUST_LOCATION);
        Objects.requireNonNull(adjustFileTypeConfig);

        for (AdjustFileType adjustFileType : adjustFileTypeConfig.adjustFileTypes) {
            List<String> extensions = adjustFileType.getExtensions();
            if (adjustFileType.isCreate()) {
                FileType fileTypeNew = new FileType();
                fileTypeNew.setId(adjustFileType.getId());
                fileTypeNew.setExtensions(extensions);
                fileTypeNew.setDesc(adjustFileType.getDesc());
                if (fileTypes.contains(fileTypeNew)) {
                    throw new DetectException("invalid  adjust file type :" + adjustFileType.getId());
                }
                fileTypes.add(fileTypeNew);
                FileType fileTypeOld = fileTypeMap.get(fileTypeNew.getId());
                if (fileTypeOld != null) {
                    throw new DetectException("invalid   file type :" + fileTypeNew.getId());
                }
                fileTypeMap.put(fileTypeNew.getId(), fileTypeNew);

            } else {
                FileType fileTypeOld = fileTypeMap.get(adjustFileType.getId());
                if (fileTypeOld == null) {
                    throw new DetectException("invalid  adjust file type :" + adjustFileType.getId());
                }
                String sameRef = adjustFileType.getSameRef();
                if (!StringUtils.isEmpty(sameRef)) {
                    FileType fileTypeSame = fileTypeMap.get(sameRef);
                    if (fileTypeSame == null) {
                        throw new DetectException("invalid  same ref file type :" + adjustFileType.getId());
                    }
                    fileTypeOld.setSameRef(fileTypeSame);
                }
                if (!CollectionUtils.isEmpty(extensions)) {
                    fileTypeOld.setExtensions(extensions);
                }

            }

        }
        return fileTypeConfig;
    }

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
    public static class FileTypeConfig {
        private List<FileType> fileTypes;
    }

    @Data
    public static class AdjustFileTypeConfig {
        private List<AdjustFileType> adjustFileTypes;
    }

}
