package io.github.kylinhunter.tools.file.detector.common.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import io.github.kylinhunter.tools.file.detector.exception.InitException;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-30 14:44
 **/
public class UserDirUtil {
    private static final File USER_DIR = new File(System.getProperty("user.dir"));

    static {
        if (!USER_DIR.exists()) {
            throw new InitException("user.dir no exist");
        }
    }

    /**
     * @param child  child
     * @param create create
     * @return java.io.File
     * @title getDir
     * @description
     * @author BiJi'an
     * @date 2022-10-30 14:49
     */
    public static File getDir(String child, boolean create) throws IOException {
        File dir = new File(USER_DIR, child);
        if (dir.exists()) {
            if (dir.isFile()) {
                throw new IOException(" is a file " + dir.getAbsolutePath());
            } else {
                return dir;
            }
        } else {
            if (create) {
                FileUtils.forceMkdir(dir);
            }
            return dir;
        }

    }

    public static File getFile(String child, boolean create) throws IOException {
        File file = new File(USER_DIR, child);
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException(" is a file " + file.getAbsolutePath());
            } else {
                return file;
            }
        } else {
            if (create) {
                FileUtils.forceMkdirParent(file);
            }
            return file;
        }

    }
}
