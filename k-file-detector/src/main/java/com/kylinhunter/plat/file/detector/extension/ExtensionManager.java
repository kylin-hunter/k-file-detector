package com.kylinhunter.plat.file.detector.extension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.constant.ExtensionFamily;
import com.kylinhunter.plat.file.detector.constant.ExtensionRisk;
import com.kylinhunter.plat.file.detector.exception.DetectException;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-20 15:51
 **/
@Data
public class ExtensionManager {
    private Map<String, ExtensionFile> allFileTypes = Maps.newHashMap();
    private Map<ExtensionRisk, Set<ExtensionFile>> riskFileTypes = Maps.newHashMap();
    private Map<ExtensionFamily, Set<ExtensionFile>> extensionFamilyFileTypes = Maps.newHashMap();
    private Set<String> dangerousExtensions = Sets.newHashSet();

    public ExtensionManager() {
        ExtensionConfigLoader.FileTypeConfig fileTypeConfig = ExtensionConfigLoader.load();
        fileTypeConfig.getFileTyes().values().forEach(this::add);
    }

    /**
     * @param extensionFile extensionFile
     * @return void
     * @title addDefaultData
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */
    private void add(ExtensionFile extensionFile) {
        if (extensionFile != null) {

            if (allFileTypes.get(extensionFile.getExtension()) != null) {
                throw new DetectException("duplicate extension" + extensionFile.getExtension());
            }

            allFileTypes.put(extensionFile.getExtension(), extensionFile);

            riskFileTypes.compute(extensionFile.getRisk(), (risk, fileTypes) -> {
                if (fileTypes == null) {
                    fileTypes = Sets.newHashSet();
                }
                fileTypes.add(extensionFile);
                return fileTypes;
            });
            if (extensionFile.getRisk() == ExtensionRisk.HIGH) {
                dangerousExtensions.add(extensionFile.getExtension());
            }

            List<ExtensionFamily> families = extensionFile.getFamilies();
            if (families != null) {
                for (ExtensionFamily extensionFamily : families) {

                    extensionFamilyFileTypes.compute(extensionFamily, (k1, v1) -> {
                        if (v1 == null) {
                            v1 = Sets.newHashSet();
                        }
                        v1.add(extensionFile);
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
     * @return com.kylinhunter.file.detector.extension.ExtensionFile
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-09 16:04
     */
    @SuppressWarnings("unchecked")
    public Set<ExtensionFile> getFileTypes(ExtensionRisk risk) {
        if (risk != null) {
            return riskFileTypes.getOrDefault(risk, Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * @param extensionFamily extensionFamily
     * @return java.util.Set<config.ExtensionFile>
     * @title getExtensionFiles
     * @description
     * @author BiJi'an`
     * @date 2022-10-20 16:36
     */
    @SuppressWarnings("unchecked")
    public Set<ExtensionFile> getFileTypes(ExtensionFamily extensionFamily) {
        return extensionFamilyFileTypes.getOrDefault(extensionFamily, Collections.EMPTY_SET);
    }

    /**
     * @param extension extension
     * @return com.kylinhunter.file.detector.extension.ExtensionFile
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-21 22:26
     */
    public ExtensionFile getFileType(String extension) {
        if (extension != null) {
            return allFileTypes.get(extension.toLowerCase());
        }
        return null;
    }

}
