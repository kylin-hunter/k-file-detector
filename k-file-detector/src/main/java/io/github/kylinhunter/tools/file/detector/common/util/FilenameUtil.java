package io.github.kylinhunter.tools.file.detector.common.util;

import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-30 18:25
 **/
public class FilenameUtil {
    private final static Map<String, String> SPEICAL_EXTENSION = Maps.newHashMap();

    static {
        SPEICAL_EXTENSION.put("z", "tar.z");
        SPEICAL_EXTENSION.put("gz", "tar.gz");
    }

    public static String getExtension(final String fileName) {

        String extension = FilenameUtils.getExtension(fileName);
        if (extension != null && extension.length() > 0) {
            String extensionNew = SPEICAL_EXTENSION.get(extension);
            if (extensionNew != null && extensionNew.length() > 0) {
                return extensionNew.toLowerCase();
            }

            return extension.toLowerCase();

        }
        return StringUtils.EMPTY;
    }
}
