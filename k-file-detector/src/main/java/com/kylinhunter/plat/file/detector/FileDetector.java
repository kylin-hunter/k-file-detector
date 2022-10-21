package com.kylinhunter.plat.file.detector;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectOption;
import com.kylinhunter.plat.file.detector.constant.MagicMatchMode;
import com.kylinhunter.plat.file.detector.constant.MagicRisk;
import com.kylinhunter.plat.file.detector.constant.SafeStatus;
import com.kylinhunter.plat.file.detector.extension.ExtensionFile;
import com.kylinhunter.plat.file.detector.extension.ExtensionManager;
import com.kylinhunter.plat.file.detector.magic.ExtensionMagics;
import com.kylinhunter.plat.file.detector.magic.Magic;
import com.kylinhunter.plat.file.detector.magic.MagicManager;
import com.kylinhunter.plat.file.detector.magic.MagicReader;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
public class FileDetector {

    private static final ExtensionManager extensionManager = ConfigurationManager.getExtensionManager();
    private static final MagicManager magicManager = ConfigurationManager.getMagicManager();

    /**
     * @param content  content
     * @param fileName fileName
     * @return com.kylinhunter.file.detector.bean.DetectConext
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:42
     */
    public static DetectConext detect(byte[] content, String fileName) {
        return detect(content, fileName, DetectOption.getDefault());
    }

    /**
     * @param content  content
     * @param fileName fileName
     * @return boolean
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectConext detect(byte[] content, String fileName, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(content, fileName, false);
        return detect(possibleMagicNumber, fileName, detectOption);

    }

    /**
     * @param file file
     * @return com.kylinhunter.file.detector.bean.DetectConext
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:42
     */
    public static DetectConext detect(MultipartFile file) {
        return detect(file, DetectOption.getDefault());
    }

    /**
     * @param file file
     * @return boolean
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */

    public static DetectConext detect(MultipartFile file, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(file, false);
        return detect(possibleMagicNumber, file.getOriginalFilename(), detectOption);
    }

    /**
     * @param file file
     * @return com.kylinhunter.file.detector.bean.DetectConext
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:43
     */
    public static DetectConext detect(File file) {
        return detect(file, DetectOption.getDefault());

    }

    /**
     * @param file file
     * @return boolean
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectConext detect(File file, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(file, false);
        return detect(possibleMagicNumber, file.getName(), detectOption);
    }

    /**
     * @param input    input
     * @param fileName fileName
     * @return com.kylinhunter.file.detector.bean.DetectConext
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 02:35
     */
    public static DetectConext detect(InputStream input, String fileName) {
        return detect(input, fileName, -1L, DetectOption.getDefault());

    }

    /**
     * @param input        input
     * @param fileName     fileName
     * @param detectOption detectOption
     * @return com.kylinhunter.file.detector.bean.DetectConext
     * @title detect
     * @description
     * @author BiJi'an
     * @date 2022-10-22 02:39
     */
    public static DetectConext detect(InputStream input, String fileName, long fileSize, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(input, fileName, fileSize, false);
        return detect(possibleMagicNumber, fileName, detectOption);
    }

    /**
     * @param possibleMagicNumber possibleMagicNumber
     * @param fileName            fileName
     * @return boolean
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */

    private static DetectConext detect(String possibleMagicNumber, String fileName, DetectOption detectOption) {
        DetectConext detectConext = new DetectConext(possibleMagicNumber, FilenameUtils.getExtension(fileName));
        detectDangerousExtension(detectConext, detectOption);
        delectDisguiseExtension(detectConext, detectOption);  // check extension
        detectDangerConent(detectConext, detectOption);  // check extension

        return detectConext;

    }

    /**
     * @param detectConext detectConext
     * @param magics       magics
     * @return void
     * @title check
     * @description
     * @author BiJi'an
     * @date 2022-10-08 20:19
     */
    private static void detect(DetectConext detectConext, Set<Magic> magics) {
        if (magics != null && magics.size() > 0) {  // candidate magic numnbers
            String possibleMagicNumber = detectConext.getPossibleMagicNumber();
            for (Magic magic : magics) {
                String number = magic.getNumber();
                if (magic.getMatchMode() == MagicMatchMode.PREFIX) {
                    if (possibleMagicNumber.startsWith(number)) {
                        detectConext.addDetectedMagic(magic);
                    }
                } else {
                    int i;
                    for (i = 0; i < number.length(); i++) {
                        if (number.charAt(i) != '_' && number.charAt(i) != possibleMagicNumber.charAt(i)) {
                            break;
                        }
                    }
                    if (i == number.length()) {
                        detectConext.addDetectedMagic(magic);
                    }
                }
            }
        }
    }

    /**
     * @param detectConext detectConext
     * @param detectOption detectOption
     * @return void
     * @title delectDisguiseExtension
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:43
     */
    private static void delectDisguiseExtension(DetectConext detectConext, DetectOption detectOption) {
        if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN && detectOption.isDetectDisguiseExtension()) {

            String extension = detectConext.getExtension();
            if (!StringUtils.isEmpty(extension)) {
                ExtensionFile explicitExtensionFile = extensionManager.getFileType(extension);
                if (explicitExtensionFile != null) { // 支持的扩展名
                    ExtensionMagics explicitExtensionMagics = explicitExtensionFile.getExtensionMagics();
                    if (explicitExtensionMagics != null) {
                        detectConext.setExtensionMagics(explicitExtensionMagics);
                        detect(detectConext, explicitExtensionMagics.getMagics());
                        if (detectConext.isDetected()) {
                            detectConext.setSafeStatus(SafeStatus.SAFE);
                        } else {
                            detectDisguiseExtensionTolerate(detectConext, detectOption, explicitExtensionMagics);

                            if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN) {
                                detectConext.setSafeStatus(SafeStatus.DISGUISE);
                            }

                        }
                    }

                }

            }
        }

    }

    /**
     * @param detectConext    detectConext
     * @param detectOption    detectOption
     * @param extensionMagics extensionMagics
     * @return void
     * @title detectDisguiseExtensionTolerate
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:43
     */
    private static void detectDisguiseExtensionTolerate(DetectConext detectConext, DetectOption detectOption,
                                                        ExtensionMagics extensionMagics) {

        if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN && detectOption.isDetectDisguiseExtensionTolerate()) {
            detect(detectConext, extensionMagics.getTolerateMagics());
            if (detectConext.isDetected()) {
                detectConext.setSafeStatus(SafeStatus.DISGUISE_WARN);
            }
        }

    }

    /**
     * @param detectConext detectConext
     * @param detectOption detectOption
     * @return void
     * @title detectDangerousExtension
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:43
     */
    private static void detectDangerousExtension(DetectConext detectConext, DetectOption detectOption) {
        if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN && detectOption.isDetectDangerousExtension()) {
            String extension = detectConext.getExtension();
            Set<String> dangerousExtensions = extensionManager.getDangerousExtensions();
            if (dangerousExtensions.contains(extension)) {
                Set<String> detectDangerousExtensionExcludes = detectOption.getDetectDangerousExtensionExcludes();
                if (detectDangerousExtensionExcludes == null || !detectDangerousExtensionExcludes.contains(extension)) {
                    detectConext.setSafeStatus(SafeStatus.DANGEROUS_EXTENSION);
                    detectConext.setDangerousExtensions(dangerousExtensions);

                }
            } else {
                Set<String> detectDangerousExtensionIncludes = detectOption.getDetectDangerousExtensionIncludes();
                if (detectDangerousExtensionIncludes != null && detectDangerousExtensionIncludes.contains(extension)) {
                    detectConext.setSafeStatus(SafeStatus.DANGEROUS_EXTENSION);
                    detectConext.setDangerousExtensions(detectDangerousExtensionIncludes);

                }

            }
        }
    }

    /**
     * @param detectConext detectConext
     * @return void
     * @title checkDanger
     * @description
     * @author BiJi'an
     * @date 2022-10-08 20:19
     */
    private static void detectDangerConent(DetectConext detectConext, DetectOption detectOption) {
        if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN && detectOption.isDeletecDangerousContent()) {
            Set<Magic> magics = magicManager.getMagics(MagicRisk.HIGH);
            detect(detectConext, magics);
            if (detectConext.isDetected()) {
                detectConext.setSafeStatus(SafeStatus.DANGEROUS_CONTENT);
            }
        }
    }

}
