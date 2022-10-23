package com.kylinhunter.plat.file.detector.type;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.constant.FileFamily;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.magic.TolerateMagics;

import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-20 15:51
 **/

public class FileTypeManager {
    @Getter
    private final Map<String, Set<FileType>> extensionToFileTypes = Maps.newHashMap();
    @Getter
    private final Map<String, FileType> idToFileTyes = Maps.newHashMap();
    @Getter
    private final Set<FileType> allFileTypes = Sets.newHashSet();
    @Getter
    private final Map<FileFamily, Set<FileType>> fileFamilyDatas = Maps.newHashMap();
    private final Map<String, Set<FileType>> allTolerateDatas = Maps.newHashMap();

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
        Map<FileFamily, FileTypeConfigLoader.FileFamilyData> familyFileDatas = fileTypeConfig.getConfig();

        Set<FileFamily> duplicateFamilies = Sets.newHashSet();

        familyFileDatas.forEach((fileFamily, fileFamilyData) -> {
            if (duplicateFamilies.contains(fileFamily)) {
                throw new DetectException(" duplicate fileFamily " + fileFamily);
            }
            duplicateFamilies.add(fileFamily);

            List<FileType> familyFileTypes = fileFamilyData.getList();
            if (familyFileTypes == null || familyFileTypes.size() <= 0) {
                throw new DetectException(" fileTypes can't be  empty");

            }

            familyFileTypes.forEach(fileType -> {
                check(fileFamily, fileFamilyData, fileType);
                processBasic(fileType);
                proccessExtensionToFile(fileType);
                processFileFamilyDatas(fileFamily, fileType);
                processTolerateTag(fileType);

            });

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
    private void processBasic(FileType fileType) {
        allFileTypes.add(fileType);
        idToFileTyes.put(fileType.getId(), fileType);
    }

    /**
     * @param fileType fileType
     * @return void
     * @title check
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:54
     */
    private void check(FileFamily fileFamily, FileTypeConfigLoader.FileFamilyData fileFamilyData, FileType fileType) {
        String extension = fileType.getExtension();
        if (StringUtils.isEmpty(extension)) {
            throw new DetectException(" extension can't be empty");
        }

        if (StringUtils.isEmpty(fileType.getId())) {
            throw new DetectException(" file type id can't be empty");

        }
        if (StringUtils.isEmpty(fileType.getTolerateTag())) {
            fileType.setTolerateTag(fileFamilyData.getTolerateTag());
        }
        if (allFileTypes.contains(fileType)) {
            throw new DetectException(" duplicate fileType " + fileType);
        }
        fileType.setFamily(fileFamily);

    }

    /**
     * @param fileFamily fileFamily
     * @return void
     * @title processFileFamilyDatas
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:52
     */
    private void processFileFamilyDatas(FileFamily fileFamily, FileType fileType) {
        fileFamilyDatas.compute(fileFamily, (k, v) -> {
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
     * @title proccessExtensionToFile
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:51
     */

    private void proccessExtensionToFile(FileType fileType) {
        extensionToFileTypes.compute(fileType.getExtension(), (k, v) -> {
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
     * @title processTolerateTag
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:01
     */
    private void processTolerateTag(FileType fileType) {
        String tolerateTag = fileType.getTolerateTag();

        if (tolerateTag != null) {
            Set<FileType> fileTypes = allTolerateDatas.compute(tolerateTag,
                    (k, v) -> {
                        if (v == null) {
                            v = Sets.newHashSet();
                        }
                        v.add(fileType);
                        return v;
                    });
            TolerateMagics tolerateMagics = fileType.getTolerateMagics();
            tolerateMagics.setFileTypes(fileTypes);
        }

    }

    /**
     * @param fileFamily fileFamily
     * @return java.util.Set<config.FileType>
     * @title getFileTypes
     * @description
     * @author BiJi'an`
     * @date 2022-10-20 16:36
     */
    @SuppressWarnings("unchecked")
    public Set<FileType> getFileTypesByExtension(FileFamily fileFamily) {
        return fileFamilyDatas.getOrDefault(fileFamily, Collections.EMPTY_SET);
    }

    /**
     * @param extension extension
     * @return java.util.Set<com.kylinhunter.plat.file.detector.type.FileType>
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
     * @param id id
     * @return com.kylinhunter.plat.file.detector.type.FileType
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:48
     */
    public FileType getFileTypeById(String id) {

        return idToFileTyes.get(id);

    }

}
