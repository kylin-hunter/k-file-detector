package com.kylinhunter.plat.file.detector.common.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.kylinhunter.plat.file.detector.exception.InitException;

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
                throw new IOException("dir is a file " + dir.getAbsolutePath());
            } else {
                return dir;
            }
        } else {
            FileUtils.forceMkdir(dir);
            return dir;
        }

    }
}
