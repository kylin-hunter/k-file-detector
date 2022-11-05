package com.kylinhunter.plat.file.detector;

import java.text.NumberFormat;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-31 01:02
 **/
@Data
public class DetectStatstic {
    final int allNums;
    int firstFileTypeMatchNums = 0;
    int secondFileTypeMatchNums = 0;
    int firstFileTypesMatchNums = 0;
    int secondFileTypesMatchNums = 0;
    int otherMatchNums = 0;
    int noMatchNums = 0;

    public void calAssertResult(int assertResult) {
        if (assertResult > 100 && assertResult < 199 && assertResult % 100 == 1) {
            firstFileTypeMatchNums++;
            firstFileTypesMatchNums++;

        } else if (assertResult > 100 && assertResult < 199 && assertResult % 100 == 2) {
            firstFileTypesMatchNums++;
        } else if (assertResult > 200 && assertResult < 299 && assertResult % 200 == 1) {
            secondFileTypeMatchNums++;
            secondFileTypesMatchNums++;

        } else if (assertResult > 200 && assertResult < 299 && assertResult % 200 == 2) {
            secondFileTypesMatchNums++;
        } else if (assertResult == 300) {
            otherMatchNums++;
        } else {
            noMatchNums++;
        }
    }

    public void print() {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);

        System.out.printf("firstFileTypeMatchNums=%d , allNumber=%d,match ratio=%s%n", firstFileTypeMatchNums,
                allNums, format.format(getFirstFileTypeMatchRatio()));
        System.out.printf("firstFileTypesMatchNums=%d , allNumber=%d,match ratio=%s%n", firstFileTypesMatchNums,
                allNums, format.format(getFirstFileTypesMatchRatio()));

        System.out.printf("secondFileTypeMatchNums=%d , allNumber=%d,match ratio=%s%n", secondFileTypeMatchNums,
                allNums, format.format(getSecondFileTypeMatchRatio()));

        System.out.printf("secondFileTypesMatchNums=%d , allNumber=%d,match ratio=%s%n", secondFileTypesMatchNums,
                allNums, format.format(getSecondFileTypesMatchRatio()));

        System.out.printf("otherMatchNums=%d , allNumber=%d,match ratio=%s%n", otherMatchNums,
                allNums, format.format(getOtherMatchRatio()));

        System.out.printf("noMatchNums=%d , allNumber=%d,match ratio=%s%n", noMatchNums,
                allNums, format.format(getNoMatchRatio()));

    }

    public double getFirstFileTypeMatchRatio() {
        return (double) firstFileTypeMatchNums / allNums;
    }

    public double getFirstFileTypesMatchRatio() {
        return (double) firstFileTypesMatchNums / allNums;
    }

    public double getSecondFileTypeMatchRatio() {
        return (double) secondFileTypeMatchNums / allNums;
    }

    public double getSecondFileTypesMatchRatio() {
        return (double) secondFileTypesMatchNums / allNums;
    }

    public double getOtherMatchRatio() {
        return (double) otherMatchNums / allNums;
    }

    public double getNoMatchRatio() {
        return (double) noMatchNums / allNums;
    }
}
