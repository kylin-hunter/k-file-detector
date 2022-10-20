package com.kylinhunter.file.detector.signature;

import java.util.Map;
import java.util.Set;

import com.kylinhunter.file.detector.constant.ExtensionFamily;
import com.kylinhunter.file.detector.constant.ExtensionRisk;
import com.kylinhunter.file.detector.signature.config.FileType;
import com.kylinhunter.file.detector.signature.config.FileTypeConfig;
import com.kylinhunter.file.detector.signature.config.FileTypeConfigLoader;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-17 16:20
 **/
public class FileTypeHelper {
    private static final ExtensionManager EXTENSION_MANAGER = new ExtensionManager();

    static {
        FileTypeConfig fileTypeConfig = FileTypeConfigLoader.load();
        EXTENSION_MANAGER.add(fileTypeConfig.getFileTyes());
    }

    public static Map<String, FileType> getAllFileTypes() {
        return EXTENSION_MANAGER.getAllFileTypes();
    }

    /**
     * @param extension
     * @return com.kylinhunter.file.detector.signature.constant.FileType
     * @throws
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-17 16:21
     */
    public static FileType getFileType(String extension) {
        return EXTENSION_MANAGER.getFileType(extension);
    }

    /**
     * @param fileTypefamily
     * @return com.kylinhunter.file.detector.signature.constant.FileType
     * @throws
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-17 16:21
     */
    public static Set<FileType> getFileTypes(ExtensionFamily fileTypefamily) {
        return EXTENSION_MANAGER.getFileTypes(fileTypefamily);
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
    public static Set<FileType> getFileTypes(ExtensionRisk risk) {
        return EXTENSION_MANAGER.getFileTypes(risk);

    }

    /**
     * @param extension
     * @return boolean
     * @throws
     * @title isDanger
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:20
     */
    public static boolean isDanger(String extension) {
        return EXTENSION_MANAGER.isDanger(extension);
    }

    /**
     * @return boolean
     * @throws
     * @title getExtensionManager
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:20
     */
    public static ExtensionManager getExtensionManager() {
        return EXTENSION_MANAGER;
    }



}
