package com.kylinhunter.file.detector.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-18 20:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectOption {

    private boolean detectDangerousExtension = true;  /* check danger */

    private String detectDangerousExtensionIncludes;  /* check danger */
    private String detectDangerousExtensionExcludes;  /* check danger */

    private boolean detectDisguiseExtension = true;  /* tolerate disguise  */
    private boolean detectDisguiseExtensionTolerate = true;  /* tolerate disguise  */

    private boolean deletecDangerousContent = true;  /* check danger */

    private static final DetectOption DEFAULT_CHECK_OPTION = new DetectOption();

    public static DetectOption getDefault() {
        return DEFAULT_CHECK_OPTION;
    }


}
