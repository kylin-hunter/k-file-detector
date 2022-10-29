package com.kylinhunter.plat.file.detector.component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.config.FileTypeConfigLoader;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.exception.DetectException;

import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-20 15:51
 **/

@Component
public class FileTypeManager {
    private final String EXTENSIN_EMPTY = "!@#$%^";
    private final Map<String, Set<FileType>> extensionToFileTypes = Maps.newHashMap();
    private final Map<Integer, FileType> idToFileTyes = Maps.newHashMap();
    @Getter
    private final Set<FileType> allFileTypes = Sets.newHashSet();
    @Getter
    private final Set<String> allExtensions = Sets.newHashSet();

    public FileTypeManager() {
        init(FileTypeConfigLoader.load());
    }

    /**
     * @param fileTypeConfig fileTypeConfig
     * @return void
     * @title addDefaultData
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */
    private void init(FileTypeConfigLoader.FileTypeConfig fileTypeConfig) {
        fileTypeConfig.getFileTypes().forEach(fileType -> {
            check(fileType);
            process(fileType);
        });

    }

    /**
     * @param fileType fileType
     * @return void
     * @title processBasic
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:05
     */
    private void process(FileType fileType) {

        allFileTypes.add(fileType);
        idToFileTyes.put(fileType.getId(), fileType);

        String extension = fileType.getExtension();
        if (!StringUtils.isEmpty(extension)) {
            allExtensions.add(extension);
        } else {
            extension = EXTENSIN_EMPTY;
        }

        extensionToFileTypes.compute(extension, (k, v) -> {
            if (v == null) {
                v = Sets.newHashSet();
            }
            v.add(fileType);
            return v;
        });
    }

    /**
     * @param fileType fileType
     * @return void
     * @title check
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:54
     */
    private void check(FileType fileType) {
        String extension = fileType.getExtension();
        if (!StringUtils.isEmpty(extension)) {
            fileType.setExtension(extension.toLowerCase());
        } else {
            fileType.setExtension(StringUtils.EMPTY);
        }
        Preconditions.checkArgument(fileType.getId() > 0, " file type id can't <=0");

        if (allFileTypes.contains(fileType)) {
            throw new DetectException(" duplicate fileType " + fileType);
        }

    }

    /**
     * @param extension extension
     * @return java.util.Set<com.kylinhunter.plat.file.detector.config.bean.FileType>
     * @title getFileTypes
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:48
     */
    @SuppressWarnings("unchecked")
    public Set<FileType> getFileTypesByExtension(String extension) {
        if (extension != null) {
            return extensionToFileTypes.getOrDefault(extension.toLowerCase(), Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * @return java.util.Set<com.kylinhunter.plat.file.detector.config.bean.FileType>
     * @title getFileTypesWithNoExtension
     * @description
     * @author BiJi'an
     * @date 2022-10-29 16:14
     */

    @SuppressWarnings("unchecked")
    public Set<FileType> getFileTypesWithNoExtension() {
        return extensionToFileTypes.getOrDefault(EXTENSIN_EMPTY, Collections.EMPTY_SET);
    }

    /**
     * @param id id
     * @return com.kylinhunter.plat.file.detector.config.bean.FileType
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:48
     */
    public FileType getFileTypeById(int id) {

        return idToFileTyes.get(id);

    }

}
