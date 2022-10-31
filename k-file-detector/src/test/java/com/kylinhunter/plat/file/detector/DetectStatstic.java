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
    int bestMatchNums = 0;
    int secondBestMatchNums = 0;
    int otherMatchNums = 0;
    int noMatchNums = 0;

    public void calAssertResult(int assertResult) {
        if (assertResult == 1) {
            bestMatchNums++;
        } else if (assertResult == 2) {
            secondBestMatchNums++;
        } else if (assertResult == 3) {
            otherMatchNums++;
        } else {
            noMatchNums++;
        }
    }

    public void print() {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);

        System.out.println(String.format("bestMatchNums=%d , allNumber=%d,match ratio=%s", bestMatchNums, allNums,
                format.format(getBestMatchRatio())));

        System.out.println(String.format("secondBestMatchNums=%d , allNumber=%d,match ratio=%s", secondBestMatchNums,
                allNums, format.format(getSecondBestMatchRatio())));

        System.out.println(String.format("otherMatchNums=%d , allNumber=%d,match ratio=%s", otherMatchNums, allNums,
                format.format(getOtherMatchRatio())));

        System.out.println(String.format("noMatchNums=%d , allNumber=%d,match ratio=%s", noMatchNums, allNums,
                format.format(getNoMatchRatio())));

    }

    public double getBestMatchRatio() {
        return (double) bestMatchNums / allNums;
    }

    public double getSecondBestMatchRatio() {
        return (double) secondBestMatchNums / allNums;
    }

    public double getOtherMatchRatio() {
        return (double) otherMatchNums / allNums;
    }

    public double getNoMatchRatio() {
        return (double) noMatchNums / allNums;
    }
}
