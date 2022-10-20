package com.kylinhunter.file.detector.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilTest {
    @Test
    void bytesToHex() {

        byte[] bytes = new byte[] {127, 126, 15};
        String hexString = StringUtil.bytesToHexString(bytes, 0, bytes.length);
        System.out.println(hexString);
        String hexStr = StringUtil.bytesToHexStringV2(bytes, 0, bytes.length);
        System.out.println(hexStr);
        Assertions.assertEquals(hexStr, hexString);

    }
}