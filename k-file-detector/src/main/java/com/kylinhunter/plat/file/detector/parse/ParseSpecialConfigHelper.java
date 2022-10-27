package com.kylinhunter.plat.file.detector.parse;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-28 01:33
 **/
public class ParseSpecialConfigHelper {

    private static final Map<String, String> SAME_MAGIC_NUMBERS = Maps.newTreeMap();
    private static final Set<String> INVALID_NUMBER_SPLITS = Sets.newHashSet();

    static {
        INVALID_NUMBER_SPLITS.add("[At a cluster boundary]");
    }

    static {
        SAME_MAGIC_NUMBERS.put("46726F6D202020", "46726F6D3A20");

        SAME_MAGIC_NUMBERS.put("46726F6D203F3F3F", "46726F6D3A20");

        SAME_MAGIC_NUMBERS.put("474946383761", "474946383961");

        SAME_MAGIC_NUMBERS.put("FFEx", "FFFx");

        SAME_MAGIC_NUMBERS.put("5B5645525D", "5B7665725D");

        SAME_MAGIC_NUMBERS.put("E8", "EB");
        SAME_MAGIC_NUMBERS.put("E9", "EB");

        SAME_MAGIC_NUMBERS.put("4A47030E", "4A47040E");
        SAME_MAGIC_NUMBERS.put("FDFFFFFFnn00", "FDFFFFFFnn02");
        SAME_MAGIC_NUMBERS.put("31BE", "32BE");
        SAME_MAGIC_NUMBERS.put("9500", "9501");
        SAME_MAGIC_NUMBERS.put("00004949585052", "00004D4D585052");

        SAME_MAGIC_NUMBERS.put("04000000xxxxxxxxxxxxxxxx20030000", "05000000xxxxxxxxxxxxxxxx20030000");

    }

    /**
     * @param number number
     * @return java.lang.String
     * @title getSameMagicNumber
     * @description
     * @author BiJi'an
     * @date 2022-10-28 01:42
     */

    public static String getSameMagicNumber(String number) {
        return SAME_MAGIC_NUMBERS.get(number);
    }

    /**
     * @param text text
     * @return boolean
     * @title i
     * @description
     * @author BiJi'an
     * @date 2022-10-28 01:42
     */
    public static boolean invalidNumberContent(String text) {
        for (String invalidNumberSplit : INVALID_NUMBER_SPLITS) {
            if (text.contains(invalidNumberSplit)) {
                return true;
            }
        }
        return false;
    }

}
