package com.kylinhunter.plat.file.detector.common.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author BiJi'an
 * @description
 * @date 2022/10/22
 **/
public class StringUtil extends StringUtils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final Pattern PATTERN_HEX = Pattern.compile("^[A-Fa-f0-9]+$");

    /**
     * @param bytes bytes
     * @return java.lang.String
     * @title bytesToHexStringV2
     * @description
     * @author BiJi'an
     * @date 2022-10-04 00:06
     */
    public static String bytesToHexStringV2(byte[] bytes, int off, int len) {

        if (check(bytes, off, len)) {
            return StringUtils.EMPTY;
        }
        char[] hexChars = new char[len * 2];
        int j;
        for (int i = 0; i < len; i++) {
            j = off + i;
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * @param bytes bytes
     * @return java.lang.String
     * @title bytesToHexString
     * @description
     * @author BiJi'an
     * @date 2022-10-04 00:06
     */
    public static String bytesToHexString(byte[] bytes, int off, int len) {
        if (check(bytes, off, len)) {
            return StringUtils.EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        String hv;

        int j;
        for (int i = 0; i < len; i++) {
            j = off + i;
            hv = Integer.toHexString(bytes[j] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }

        return builder.toString();
    }

    /**
     * @param bytes bytes
     * @param off   off
     * @param len   len
     * @return boolean
     * @title check
     * @description
     * @author BiJi'an
     * @date 2022-10-27 16:33
     */
    private static boolean check(byte[] bytes, int off, int len) {
        if (bytes == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > bytes.length) || (len < 0) ||
                ((off + len) > bytes.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else {
            return len == 0;
        }

    }

    /**
     * @param str str
     * @return boolean
     * @title isHexStr
     * @description
     * @author BiJi'an
     * @date 2022-10-27 16:33
     */
    public static boolean isHexStr(String str) {
        return PATTERN_HEX.matcher(str).matches();
    }

}
