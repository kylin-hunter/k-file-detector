package com.kylinhunter.plat.file.detector.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilTest {
    @Test
    void bytesToHex() {

        byte[] bytes = new byte[] {127, 126, 15};
        String hex = StringUtil.bytesToHexString(bytes, 0, bytes.length);
        System.out.println(hex);
        Assertions.assertTrue(StringUtil.isHex(hex));

        String hexV2 = StringUtil.bytesToHexStringV2(bytes, 0, bytes.length);
        System.out.println(hexV2);
        Assertions.assertTrue(StringUtil.isHex(hexV2));

        Assertions.assertEquals(hexV2, hex);

    }
}