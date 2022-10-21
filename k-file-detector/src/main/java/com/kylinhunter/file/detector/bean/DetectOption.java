package com.kylinhunter.file.detector.bean;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-08 20:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectOption {

    private boolean detectDangerousExtension = true;  /* detect Dangerous Extension */

    /* Add danger extension such as exe,vbx  */
    private Set<String> detectDangerousExtensionIncludes;
    /* remove danger extension such as exe,vbx */
    private Set<String> detectDangerousExtensionExcludes;

    private boolean detectDisguiseExtension = true;  /* detect disguise extension  */
    private boolean detectDisguiseExtensionTolerate = true;  /* detect  disguise tolerate extension   */

    private boolean deletecDangerousContent = true;  /* deletec dangerous contentr */

    private static final DetectOption DEFAULT_CHECK_OPTION = new DetectOption();

    public static DetectOption getDefault() {
        return DEFAULT_CHECK_OPTION;
    }

    public static DetectOption custom() {
        DetectOption detectOption = new DetectOption();
        return detectOption;
    }

    /**
     * @param extension extension
     * @return void
     * @title addDangerousExtensionIncludes
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */
    public DetectOption addDangerousExtensionIncludes(String extension) {
        if (!StringUtils.isEmpty(extension)) {
            if (detectDangerousExtensionIncludes == null) {
                detectDangerousExtensionIncludes = Sets.newHashSet();
            }
            detectDangerousExtensionIncludes.add(extension);
        }
        return this;

    }

    /**
     * @param extension extension
     * @return void
     * @title exclude
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */

    public DetectOption addDangerousExtensionExcludes(String extension) {
        if (!StringUtils.isEmpty(extension)) {
            if (detectDangerousExtensionExcludes == null) {
                detectDangerousExtensionExcludes = Sets.newHashSet();
            }
            detectDangerousExtensionExcludes.add(extension);
        }
        return this;
    }

}
