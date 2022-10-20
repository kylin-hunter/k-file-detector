package com.kylinhunter.file.detector.signature;

import java.io.File;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kylinhunter.file.detector.bean.DetectConext;
import com.kylinhunter.file.detector.bean.DetectOption;
import com.kylinhunter.file.detector.constant.MagicMatchType;
import com.kylinhunter.file.detector.constant.MagicRisk;
import com.kylinhunter.file.detector.constant.SafeStatus;
import com.kylinhunter.file.detector.signature.config.ExtensionMagics;
import com.kylinhunter.file.detector.signature.config.FileType;
import com.kylinhunter.file.detector.signature.config.Magic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description 文件magic code检查
 * @date 2022-10-02 14:08
 **/
@Slf4j
public class FileSignatureDetector {

    public static DetectConext detect(byte[] content, String fileName) {
        return detect(content, fileName, DetectOption.getDefault());
    }

    /**
     * @param content
     * @param fileName
     * @return boolean
     * @throws
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    public static DetectConext detect(byte[] content, String fileName, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(content, fileName, false);
        return detect(possibleMagicNumber, fileName, detectOption);

    }

    public static DetectConext detect(MultipartFile file) {
        return detect(file, DetectOption.getDefault());
    }

    /**
     * @param file
     * @return boolean
     * @throws
     * @title safe
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */

    public static DetectConext detect(MultipartFile file, DetectOption detectOption) {
        String possibleMagicNumber = MagicReader.read(file, false);
        return detect(possibleMagicNumber, file.getOriginalFilename(), detectOption);
    }

    public static DetectConext detect(File file) {
        return detect(file, DetectOption.getDefault());

    }

    /**
     * @param file
     * @return boolean
     * @throws
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
     * @param possibleMagicNumber
     * @param fileName
     * @return boolean
     * @throws
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
     * @param detectConext
     * @param magics
     * @return void
     * @throws
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
                if (magic.getMatchType() == MagicMatchType.PREFIX) {
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
     * @return boolean
     * @throws
     * @title isValid
     * @description
     * @author BiJi'an
     * @date 2022-10-07 10:23
     */
    private static void delectDisguiseExtension(DetectConext detectConext, DetectOption detectOption) {
        if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN && detectOption.isDetectDisguiseExtension()) {

            String extension = detectConext.getExtension();
            if (!StringUtils.isEmpty(extension)) {
                FileType explicitFileType = FileTypeHelper.getFileType(extension);
                if (explicitFileType != null) { // 支持的扩展名
                    ExtensionMagics explicitExtensionMagics = MagicHelper.getExtensionMagics(explicitFileType);
                    detectConext.setExtensionMagics(explicitExtensionMagics);
                    detect(detectConext, explicitExtensionMagics.getExplicitMagics());
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

    private static void detectDisguiseExtensionTolerate(DetectConext detectConext, DetectOption detectOption,
                                                        ExtensionMagics extensionMagics) {

        if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN && detectOption.isDetectDisguiseExtensionTolerate()) {
            detect(detectConext, extensionMagics.getTolerateMagics());
            if (detectConext.isDetected()) {
                detectConext.setSafeStatus(SafeStatus.DISGUISE_WARN);
            }
        }

    }

    private static void detectDangerousExtension(DetectConext detectConext, DetectOption detectOption) {
        if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN && detectOption.isDetectDangerousExtension()) {
            String extension = detectConext.getExtension();
            ExtensionManager extensionManager = FileTypeHelper.getExtensionManager();
            Set<String> dangerousExtensions = extensionManager.getDangerousExtensions();
            if (dangerousExtensions.contains(extension)) {
                detectConext.setSafeStatus(SafeStatus.DANGEROUS_EXTENSION);
                detectConext.setDangerousExtensions(dangerousExtensions);
            }
        }
    }

    /**
     * @param detectConext
     * @return void
     * @throws
     * @title checkDanger
     * @description
     * @author BiJi'an
     * @date 2022-10-08 20:19
     */
    private static void detectDangerConent(DetectConext detectConext, DetectOption detectOption) {
        if (detectConext.getSafeStatus() == SafeStatus.UNKNOWN && detectOption.isDeletecDangerousContent()) {
            Set<Magic> magics = MagicHelper.getMagics(MagicRisk.HIGH);
            detect(detectConext, magics);
            if (detectConext.isDetected()) {
                detectConext.setSafeStatus(SafeStatus.DANGEROUS_CONTENT);
            }
        }
    }

}
