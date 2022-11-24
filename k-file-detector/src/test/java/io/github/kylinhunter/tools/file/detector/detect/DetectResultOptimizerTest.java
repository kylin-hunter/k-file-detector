package io.github.kylinhunter.tools.file.detector.detect;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.kylinhunter.tools.file.detector.content.bean.DetectConext;
import io.github.kylinhunter.tools.file.detector.detect.bean.DetectResult;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;

class DetectResultOptimizerTest {

    private final DetectResultOptimizer detectResultOptimizer = new DetectResultOptimizer();

    @Test
    void selectBest1() {
        // test offset before  and  number longer before
        DetectConext detectConext = new DetectContextBuilder()
                .setFileName("1.txt")
                .setExtension("mp3")
                .setPossibleMagicNumber("12")

                .add(1, 0, "12")
                .add(2, 2, "1234")
                .add(31, 0, "1234")
                .add(32, 0, "123456")
                .add(33, 0, "123478", "mpeg,mpg,mp3", "mp4")
                .add(34, 0, "12347890", "mpeg,mpg,mp3")
                .add(4, 2, "123456", "mp3").get();

        DetectResult detectResult = detectResultOptimizer.optimize(detectConext);

        List<Magic> allBestMagics = detectResult.getPossibleMagics();

        int[] expected = new int[] {34, 32, 33, 31, 1, 4, 2};
        int[] actual = allBestMagics.stream().mapToInt(Magic::getId).toArray();

        System.out.println("expected=>\t" + Arrays.toString(expected));
        System.out.println("actual=>\t" + Arrays.toString(actual));
        Assertions.assertArrayEquals(expected, actual);

    }

}