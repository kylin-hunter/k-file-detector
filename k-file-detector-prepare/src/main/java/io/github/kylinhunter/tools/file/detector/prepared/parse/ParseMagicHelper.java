package io.github.kylinhunter.tools.file.detector.prepared.parse;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-28 01:33
 **/
public class ParseMagicHelper {

    private static final Map<String, String> SAME_MAGIC_NUMBERS = Maps.newTreeMap();
    private static final Set<String> INVALID_NUMBER_SPLITS = Sets.newHashSet();

    private static final Set<String> INVALID_NUMBER = Sets.newHashSet();
    private static final Pattern PATTERN_VALID_MAGIC_NUMBER = Pattern.compile("^[A-Fa-f0-9xn]+$");
    private static final Pattern PATTERN_VALID_EXTENSION = Pattern.compile("^[a-z0-9\\._]+$");

    private static final Map<String, String> SPECIAL_EXTENSIONS = Maps.newHashMap();

    static {
        SPECIAL_EXTENSIONS.put("wallet", "wallet");
        SPECIAL_EXTENSIONS.put("manifest", "manifest");
        SPECIAL_EXTENSIONS.put("tar.bz2", "tar.bz2");
        SPECIAL_EXTENSIONS.put("enn (where nn are numbers)", "enn");
        SPECIAL_EXTENSIONS.put("exnn (where nn are numbers)", "exnn");
        SPECIAL_EXTENSIONS.put("onepkg", "onepkg");
        SPECIAL_EXTENSIONS.put("attachment", "attachment");
        SPECIAL_EXTENSIONS.put("spvchain", "spvchain");
        SPECIAL_EXTENSIONS.put("torrent", "torrent");

    }

    static {
        INVALID_NUMBER_SPLITS.add("[At a cluster boundary]");
        INVALID_NUMBER_SPLITS.add("29,152");
    }

    static {
        INVALID_NUMBER.add("00");
        INVALID_NUMBER.add("000000");
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
    public static boolean isSkipParseMagicNumberBySplit(String text) {
        for (String invalidNumberSplit : INVALID_NUMBER_SPLITS) {
            if (text.contains(invalidNumberSplit)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param text
     * @return boolean
     * @throws
     * @title isSkipParseMagicNumber
     * @description
     * @author BiJi'an
     * @date 2022-10-30 21:18
     */
    public static boolean isSkipParseMagicNumber(String text) {

        for (String number : INVALID_NUMBER) {
            if (text.equals(number)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param str str
     * @return boolean
     * @title isHexStr
     * @description
     * @author BiJi'an
     * @date 2022-10-27 16:33
     */
    public static boolean isValidMagicNumber(String str) {
        return !StringUtils.isEmpty(str) && PATTERN_VALID_MAGIC_NUMBER.matcher(str).matches();
    }

    /**
     * @param str str
     * @return boolean
     * @title isValidExtension
     * @description
     * @author BiJi'an
     * @date 2022-10-28 23:43
     */
    public static boolean isValidExtension(String str) {
        return !StringUtils.isEmpty(str) && PATTERN_VALID_EXTENSION.matcher(str).matches();
    }

    /**
     * @param str str
     * @return boolean
     * @title isLongExtension
     * @description
     * @author BiJi'an
     * @date 2022-10-28 22:25
     */
    public static String speicalExtension(String str) {
        return SPECIAL_EXTENSIONS.getOrDefault(str.toLowerCase(), str);

    }

}
