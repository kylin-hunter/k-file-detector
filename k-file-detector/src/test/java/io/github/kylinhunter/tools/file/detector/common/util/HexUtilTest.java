package io.github.kylinhunter.tools.file.detector.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HexUtilTest {
    @Test
    void bytesToHex() {

        byte[] bytes = new byte[] {127, 126, 15};
        String hexV1 = HexUtil.toStringV1(bytes, 0, bytes.length);
        System.out.println(hexV1);
        Assertions.assertTrue(HexUtil.isHex(hexV1));

        String hex = HexUtil.toString(bytes, 0, bytes.length);
        System.out.println(hex);
        Assertions.assertTrue(HexUtil.isHex(hex));

        Assertions.assertEquals(hex, hexV1);

        byte[] bytesNew = HexUtil.toBytes(hex);
        Assertions.assertArrayEquals(bytes,bytesNew);

    }
}