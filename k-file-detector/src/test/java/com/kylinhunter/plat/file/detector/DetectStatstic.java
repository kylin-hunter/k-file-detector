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
    int bestNums = 0;
    int secondBestNums = 0;
    int otherNums = 0;

    public void calAssertResult(int assertResult) {
        if (assertResult == 1) {
            bestNums++;
        } else if (assertResult == 2) {
            secondBestNums++;
        } else {
            otherNums++;
        }
    }

    public void print() {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);
        System.out.println("best ratio：" + format.format(getBestRedio()));
        System.out.println("second best ratio:：" + format.format(getSecondBestRedio()));
        System.out.println("other  ratio:：" + format.format(getOtherRedio()));
    }

    public double getBestRedio() {
        return (double) bestNums / allNums;
    }

    public double getSecondBestRedio() {
        return (double) secondBestNums / allNums;
    }

    public double getOtherRedio() {
        return (double) otherNums / allNums;
    }
}
