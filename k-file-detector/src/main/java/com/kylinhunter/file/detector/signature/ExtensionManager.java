package com.kylinhunter.file.detector.signature;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.file.detector.bean.DetectOption;
import com.kylinhunter.file.detector.constant.ExtensionFamily;
import com.kylinhunter.file.detector.constant.ExtensionRisk;
import com.kylinhunter.file.detector.exception.DetectException;
import com.kylinhunter.file.detector.signature.config.FileType;

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
    private Set<String> dangerousExtensionsInclude = Sets.newHashSet();
    private Set<String> dangerousExtensionsExclude = Sets.newHashSet();
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

            String tolerateDisguiseGroup = fileType.getTolerateGroup();
            if (tolerateDisguiseGroup != null) {

                Set<String> tolerateExtensions = tolerateDisguiseGroupDatas.compute(tolerateDisguiseGroup,
                        (k, v) -> {
                            if (v == null) {
                                v = Sets.newHashSet();
                            }
                            v.add(fileType.getExtension());
                            return v;
                        });
                fileType.setTolerateExtensions(tolerateExtensions);
            }

        }

    }

    /**
     * @param extension
     * @return void
     * @throws
     * @title include
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */
    public void addDangerousExtensionInclude(String extension) {
        if (!StringUtils.isEmpty(extension)) {

            dangerousExtensionsInclude.add(extension);
            dangerousExtensions.add(extension);
        }

    }

    /**
     * @param extension
     * @return void
     * @throws
     * @title exclude
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */

    public void addDangerousExtensionExclude(String extension) {
        if (!StringUtils.isEmpty(extension)) {
            dangerousExtensionsExclude.add(extension);
            dangerousExtensions.remove(extension);
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
     * @return com.kylinhunter.file.detector.signature.config.FileType
     * @throws
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-19 16:04
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
        String detectDangerousExtensionIncludes = detectOption.getDetectDangerousExtensionIncludes();
        if (detectDangerousExtensionIncludes != null) {
            String[] includes = StringUtils.split(detectDangerousExtensionIncludes, ",");
            if (includes != null && includes.length > 0) {
                for (String include : includes) {
                    this.addDangerousExtensionInclude(include);
                }
            }

        }

        String detectDangerousExtensionExcludes = detectOption.getDetectDangerousExtensionExcludes();
        if (detectDangerousExtensionExcludes != null) {
            String[] excludes = StringUtils.split(detectDangerousExtensionExcludes, ",");
            if (excludes != null && excludes.length > 0) {
                for (String exclude : excludes) {
                    this.addDangerousExtensionExclude(exclude);

                }
            }
        }

    }

}
