package io.github.kylinhunter.tools.file.detector.common.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 00:31
 **/
@Slf4j
public class ResourceHelper {

    /**
     * @param path path
     * @return java.io.InputStream
     * @title getInputStreamInClassPath
     * @description
     * @author BiJi'an
     * @date 2022-10-22 00:39
     */
    public static InputStream getInputStreamInClassPath(String path) {
        InputStream inputStream = ResourceHelper.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            inputStream = ResourceHelper.class.getResourceAsStream(path);
        }
        return inputStream;

    }

    /**
     * @param classPath classPath
     * @return java.io.File
     * @title getFileInClassPath
     * @description
     * @author BiJi'an
     * @date 2022-01-01 02:12
     */

    public static File getFileInClassPath(String classPath) {
        URL url = ResourceHelper.class.getClassLoader().getResource(classPath);
        if (url == null) {
            url = ResourceHelper.class.getResource(classPath);
        }
        return getFile(url);
    }

    /**
     * @param url url
     * @return java.io.File
     * @title getFile
     * @description
     * @author BiJi'an
     * @date 2022-01-01 02:11
     */
    private static File getFile(URL url) {
        File file;
        try {
            if (url != null) {
                file = new File(url.getPath());
                if (file.exists()) {
                    return file;
                } else {
                    file = new File(url.toURI().getPath());
                    if (file.exists()) {
                        return file;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("get File error " + url, e);
        }
        return null;
    }

}
