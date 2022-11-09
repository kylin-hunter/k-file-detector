package com.kylinhunter.plat.file.detector.detect;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kylinhunter.plat.file.detector.content.bean.DetectConext;
import com.kylinhunter.plat.file.detector.detect.bean.DetectResult;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

class DetectResultOptimizerTest {

    private final DetectResultOptimizer detectResultOptimizer = new DetectResultOptimizer();

    @Test
    void selectBest1() {
        // test offset before  and  number longer before
        DetectConext detectConext = new DetectContextBuilder()
                .setFileName("1.txt")
                .setExtension("mp3")
                .setPossibleMagicNumber("12")

                .add(1, true, 0, "12")
                .add(2, true, 2, "1234")
                .add(31, true, 0, "1234")
                .add(32, true, 0, "123456")
                .add(33, true, 0, "123478", "0_mp3_306770805", "0_mp4")
                .add(34, true, 0, "12347890", "0_mp3_306770805")
                .add(4, true, 2, "123456", "0_mp3").get();

        DetectResult detectResult = detectResultOptimizer.optimize(detectConext);

        List<Magic> allBestMagics = detectResult.getPossibleMagics();

        int[] expected = new int[] {34, 32, 33, 31, 1, 4, 2};
        int[] actual = allBestMagics.stream().mapToInt(Magic::getId).toArray();

        System.out.println("expected=>\t" + Arrays.toString(expected));
        System.out.println("actual=>\t" + Arrays.toString(actual));
        Assertions.assertArrayEquals(expected, actual);

    }

}