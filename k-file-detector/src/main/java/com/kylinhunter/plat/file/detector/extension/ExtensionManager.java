package com.kylinhunter.plat.file.detector.extension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.bean.DetectOption;
import com.kylinhunter.plat.file.detector.bean.FileSecurity;
import com.kylinhunter.plat.file.detector.constant.ExtensionFamily;
import com.kylinhunter.plat.file.detector.constant.ExtensionRisk;
import com.kylinhunter.plat.file.detector.constant.SecurityStatus;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.magic.ExtensionMagics;
import com.kylinhunter.plat.file.detector.magic.Magic;

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

    /**
     * @param extension    extension
     * @param detectOption detectOption
     * @return void
     * @title isDanger
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:43
     */
    public void detectDangerourExtension(FileSecurity fileSecurity, String extension,
                                         DetectOption detectOption) {
        if (detectOption.isDetectDangerousExtension()) {
            if (dangerousExtensions.contains(extension)) {
                Set<String> detectDangerousExtensionExcludes = detectOption.getDetectDangerousExtensionExcludes();
                if (detectDangerousExtensionExcludes == null || !detectDangerousExtensionExcludes.contains(extension)) {
                    fileSecurity.setSecurityStatus(SecurityStatus.DANGEROUS_EXTENSION);
                }
            } else {
                Set<String> detectDangerousExtensionIncludes = detectOption.getDetectDangerousExtensionIncludes();
                if (detectDangerousExtensionIncludes != null && detectDangerousExtensionIncludes.contains(extension)) {
                    fileSecurity.setSecurityStatus(SecurityStatus.DANGEROUS_EXTENSION);
                    fileSecurity.setDangerousExtensions(dangerousExtensions);
                }

            }

        }

    }

    /**
     * @param fileSecurity   fileSecurity
     * @param extension      extension
     * @param detectedMagics detectedMagics
     * @return void
     * @throws
     * @title detectExtensionSafe
     * @description
     * @author BiJi'an
     * @date 2022-10-22 22:59
     */
    public void detectExtensionSafe(FileSecurity fileSecurity, String extension, Set<Magic> detectedMagics) {

        detectedMagics.forEach(magic -> {
            if (magic.getExtensions().contains(extension)) {
                fileSecurity.setSecurityStatus(SecurityStatus.SAFE);
                fileSecurity.addSafeMagic(magic);
            }

        });

    }

    public void detectExtensionDisguise(FileSecurity fileSecurity, String extension, Set<Magic> detectedMagics) {
        ExtensionFile fileType = this.getFileType(extension);
        if (fileType != null) {
            ExtensionMagics extensionMagics = fileType.getExtensionMagics();
            if (extensionMagics != null) {
                Set<Magic> tolerateMagics = extensionMagics.getTolerateMagics();
                if (tolerateMagics != null) {

                    detectedMagics.forEach(magic -> {
                        if (tolerateMagics.contains(magic)) {
                            fileSecurity.setSecurityStatus(SecurityStatus.DISGUISE_WARN);
                            fileSecurity.addTolerateExtensions(fileType.getTolerateExtensions());
                        }
                    });
                }

            }
        }
    }

}
