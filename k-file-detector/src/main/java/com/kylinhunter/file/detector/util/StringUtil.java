package com.kylinhunter.file.detector.util;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 00:42
 **/

import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @author: BiJi'an
 * @create: 2021-08-10 14:46
 **/
public class StringUtil extends StringUtils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * @param b
     * @return java.lang.String
     * @throws
     * @title bytesToHexStringV2
     * @description
     * @author BiJi'an
     * @date 2022-10-14 00:06
     */
    public static String bytesToHexStringV2(byte[] b, int off, int len) {

        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return StringUtils.EMPTY;
        }
        char[] hexChars = new char[len * 2];
        int j;
        for (int i = 0; i < len; i++) {
            j = off + i;
            int v = b[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * @param bytes
     * @return java.lang.String
     * @throws
     * @title bytesToHexString
     * @description
     * @author BiJi'an
     * @date 2022-10-14 00:06
     */
    public static String bytesToHexString(byte[] bytes, int off, int len) {
        if (bytes == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > bytes.length) || (len < 0) ||
                ((off + len) > bytes.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
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

}
