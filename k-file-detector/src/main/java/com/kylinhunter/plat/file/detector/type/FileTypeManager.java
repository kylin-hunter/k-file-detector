package com.kylinhunter.plat.file.detector.type;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.constant.FileFamily;
import com.kylinhunter.plat.file.detector.exception.DetectException;

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
        Map<String, Set<FileType>> tolerateDatas = Maps.newHashMap();

        familyFileDatas.forEach((fileFamily, fileFamilyData) -> {
            if (duplicateFamilies.contains(fileFamily)) {
                throw new DetectException(" duplicate fileFamily " + fileFamily);
            }
            duplicateFamilies.add(fileFamily);
            if (fileFamilyData.list != null) {

                fileFamilyData.list.forEach(fileType -> {
                    fileType.setFamily(fileFamily);
                    if (StringUtils.isEmpty(fileType.getTolerateTag())) {
                        fileType.setTolerateTag(fileFamilyData.getTolerateTag());
                    }

                    if (allFileTypes.contains(fileType)) {
                        throw new DetectException(" duplicate fileType " + fileType);
                    }
                    allFileTypes.add(fileType);

                    String extension = fileType.getExtension();
                    if (StringUtils.isEmpty(extension)) {
                        throw new DetectException(" extension can't be empty");
                    }

                    extensionToFileTypes.compute(extension, (k, v) -> {
                        if (v == null) {
                            v = Sets.newHashSet();
                        }
                        v.add(fileType);
                        return v;
                    });

                    fileFamilyDatas.compute(fileFamily, (k, v) -> {
                        if (v == null) {
                            v = Sets.newHashSet();
                        }
                        if (StringUtils.isEmpty(fileType.getId())) {
                            throw new DetectException(" file type id can't be empty");

                        }
                        if (idToFileTyes.get(fileType.getId()) != null) {
                            throw new DetectException(" duplicate fileType " + fileType);

                        }
                        idToFileTyes.put(fileType.getId(), fileType);

                        v.add(fileType);
                        return v;
                    });

                    String tolerateTag = fileType.getTolerateTag();
                    if (tolerateTag != null) {
                        Set<FileType> fileTypes = tolerateDatas.compute(tolerateTag,
                                (k, v) -> {
                                    if (v == null) {
                                        v = Sets.newHashSet();
                                    }
                                    v.add(fileType);
                                    return v;
                                });
                        fileType.setTolerateFileTypes(fileTypes);
                    }

                });

            }
        });

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
    public Set<FileType> getFileTypes(FileFamily fileFamily) {
        return fileFamilyDatas.getOrDefault(fileFamily, Collections.EMPTY_SET);
    }

    @SuppressWarnings("unchecked")
    public Set<FileType> getFileTypes(String extension) {
        if (extension != null) {
            return extensionToFileTypes.getOrDefault(extension.toLowerCase(), Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;
    }

    public FileType getFileTypeById(String id) {

        return idToFileTyes.get(id);

    }

    //    /**
    //     * @param fileSecurity   fileSecurity
    //     * @param extension      extension
    //     * @param detectedMagics detectedMagics
    //     * @return void
    //     * @title detectExtensionSafe
    //     * @description
    //     * @author BiJi'an
    //     * @date 2022-10-22 22:59
    //     */
    //    public void detectExtensionSafe(FileSecurity fileSecurity, String extension, Set<Magic> detectedMagics) {
    //
    //        detectedMagics.forEach(magic -> {
    //            if (magic.getExtensions().contains(extension)) {
    //                fileSecurity.setSecurityStatus(SecurityStatus.SAFE);
    //                fileSecurity.addSafeMagic(magic);
    //            }
    //
    //        });
    //
    //    }
    //
    //    public void detectExtensionDisguise(FileSecurity fileSecurity, String extension, Set<Magic> detectedMagics) {
    //        FileType fileType = null;
    //        if (fileType != null) {
    //            ExtensionMagics extensionMagics = fileType.getExtensionMagics();
    //            if (extensionMagics != null) {
    //                Set<Magic> tolerateMagics = extensionMagics.getExtensionMagics();
    //                if (tolerateMagics != null) {
    //
    //                    detectedMagics.forEach(magic -> {
    //                        if (tolerateMagics.contains(magic)) {
    //                            fileSecurity.setSecurityStatus(SecurityStatus.DISGUISE_WARN);
    //                            fileSecurity.addTolerateExtensions(fileType.getTolerateExtensions());
    //                        }
    //                    });
    //                }
    //
    //            }
    //        }
    //    }

}
