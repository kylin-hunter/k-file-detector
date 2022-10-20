package com.kylinhunter.file.detector.signature;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.bean.DetectOption;
import com.kylinhunter.file.detector.config.FileType;
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

    private Map<String, Set<String>> tolerateDisguiseGroupDatas = Maps.newHashMap();

    private Set<String> dangerousExtensionsDefault = Sets.newHashSet();
    private Set<String> detectDangerousExtensionIncludes = Sets.newHashSet();
    private Set<String> detectDangerousExtensionExcludes = Sets.newHashSet();
    private Set<String> dangerousExtensions = Sets.newHashSet();

    public void add(Map<String, FileType> fileTypes) {
        fileTypes.values().forEach(fileType -> {
            add(fileType);
        });
    }

    /**
     * @param fileType
     * @return void
     * @throws
     * @title addDefaultData
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */
    public void add(FileType fileType) {
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
                dangerousExtensionsDefault.add(fileType.getExtension());
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
     * @param extension
     * @return boolean
     * @throws
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
     * @param risk
     * @return com.kylinhunter.file.detector.config.FileType
     * @throws
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-09 16:04
     */
    public Set<FileType> getFileTypes(ExtensionRisk risk) {
        if (risk != null) {
            return riskFileTypes.getOrDefault(risk, Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;

    }

    /**
     * @param extensionFamily
     * @return java.util.Set<config.FileType>
     * @throws
     * @title getFileTypes
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:36
     */
    public Set<FileType> getFileTypes(ExtensionFamily extensionFamily) {
        return extensionFamilyFileTypes.getOrDefault(extensionFamily, Collections.EMPTY_SET);
    }

    public FileType getFileType(String extension) {
        if (extension != null) {
            return allFileTypes.get(extension.toLowerCase());
        }
        return null;
    }

    public void initialize(DetectOption detectOption) {
        this.detectDangerousExtensionIncludes = detectOption.getDetectDangerousExtensionIncludes();
        if (detectDangerousExtensionIncludes != null) {
            this.dangerousExtensions.addAll(detectDangerousExtensionIncludes);
        }
        this.detectDangerousExtensionExcludes = detectOption.getDetectDangerousExtensionExcludes();
        if (detectDangerousExtensionExcludes != null) {
            this.dangerousExtensions.removeAll(detectDangerousExtensionExcludes);

        }
    }

}
