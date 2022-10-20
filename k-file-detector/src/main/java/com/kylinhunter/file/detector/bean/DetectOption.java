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
    private Set<String> detectDangerousExtensionIncludes = Sets.newHashSet();
    /* remove danger extension such as exe,vbx */
    private Set<String> detectDangerousExtensionExcludes = Sets.newHashSet();

    private boolean detectDisguiseExtension = true;  /* detect disguise extension  */
    private boolean detectDisguiseExtensionTolerate = true;  /* detect  disguise tolerate extension   */

    private boolean deletecDangerousContent = true;  /* deletec dangerous contentr */

    private static final DetectOption DEFAULT_CHECK_OPTION = new DetectOption();

    public static DetectOption getDefault() {
        return DEFAULT_CHECK_OPTION;
    }

    /**
     * @param extension extension
     * @return void
     * @title addDangerousExtensionIncludes
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */
    public void addDangerousExtensionIncludes(String extension) {
        if (!StringUtils.isEmpty(extension)) {
            detectDangerousExtensionIncludes.add(extension);
        }

    }

    /**
     * @param extension extension
     * @return void
     * @title exclude
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */

    public void addDangerousExtensionExcludes(String extension) {
        if (!StringUtils.isEmpty(extension)) {
            detectDangerousExtensionExcludes.add(extension);
        }
    }

}
