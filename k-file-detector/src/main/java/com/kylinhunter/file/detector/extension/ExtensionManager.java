package com.kylinhunter.file.detector.extension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.constant.ExtensionFamily;
import com.kylinhunter.file.detector.constant.ExtensionRisk;
import com.kylinhunter.file.detector.exception.DetectException;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-20 15:51
 **/
@Data
public class ExtensionManager {
    private Map<String, FileType> allFileTypes = Maps.newHashMap();
    private Map<ExtensionRisk, Set<FileType>> riskFileTypes = Maps.newHashMap();
    private Map<ExtensionFamily, Set<FileType>> extensionFamilyFileTypes = Maps.newHashMap();
    private Set<String> dangerousExtensions = Sets.newHashSet();

    public void init(FileTypeConfigManager.FileTypeConfig fileTypeConfig) {
        fileTypeConfig.getFileTyes().values().forEach(this::add);
    }

    /**
     * @param fileType fileType
     * @return void
     * @title addDefaultData
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */
    private void add(FileType fileType) {
        if (fileType != null) {

            if (allFileTypes.get(fileType.getExtension()) != null) {
                throw new DetectException("duplicate extension" + fileType.getExtension());
            }

            allFileTypes.put(fileType.getExtension(), fileType);

            riskFileTypes.compute(fileType.getRisk(), (risk, fileTypes) -> {
                if (fileTypes == null) {
                    fileTypes = Sets.newHashSet();
                }
                fileTypes.add(fileType);
                return fileTypes;
            });
            if (fileType.getRisk() == ExtensionRisk.HIGH) {
                dangerousExtensions.add(fileType.getExtension());
            }

            List<ExtensionFamily> families = fileType.getFamilies();
            if (families != null) {
                for (ExtensionFamily extensionFamily : families) {

                    extensionFamilyFileTypes.compute(extensionFamily, (k1, v1) -> {
                        if (v1 == null) {
                            v1 = Sets.newHashSet();
                        }
                        v1.add(fileType);
                        return v1;
                    });
                }
            }

        }

    }

    /**
     * @param extension extension
     * @return boolean
     * @title isDanger
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */

    public boolean isDanger(String extension) {
        if (!StringUtils.isEmpty(extension)) {
            return dangerousExtensions.contains(extension);
        }
        return false;

    }

    /**
     * @param risk risk
     * @return com.kylinhunter.file.detector.extension.FileType
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-09 16:04
     */
    @SuppressWarnings("unchecked")
    public Set<FileType> getFileTypes(ExtensionRisk risk) {
        if (risk != null) {
            return riskFileTypes.getOrDefault(risk, Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * @param extensionFamily extensionFamily
     * @return java.util.Set<config.FileType>
     * @title getFileTypes
     * @description
     * @author BiJi'an`
     * @date 2022-10-20 16:36
     */
    @SuppressWarnings("unchecked")
    public Set<FileType> getFileTypes(ExtensionFamily extensionFamily) {
        return extensionFamilyFileTypes.getOrDefault(extensionFamily, Collections.EMPTY_SET);
    }

    /**
     * @param extension extension
     * @return com.kylinhunter.file.detector.extension.FileType
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-21 22:26
     */
    public FileType getFileType(String extension) {
        if (extension != null) {
            return allFileTypes.get(extension.toLowerCase());
        }
        return null;
    }

}
