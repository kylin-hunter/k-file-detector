package io.github.kylinhunter.tools.file.detector;

import java.text.NumberFormat;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-31 01:02
 **/
@Data
public class DetectStatstic {
    final int allNums;
    Map<Integer, Integer> matchNums = Maps.newHashMap();

    public void calAssertResult(int assertResult) {
        matchNums.compute(assertResult, (k, v) -> {
            if (v == null) {
                v = 1;
            } else {
                v++;
            }
            return v;
        });
    }

    public void print() {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);

        matchNums.forEach((level, num) -> {
            double ratio = (double) num / allNums;
            System.out.printf("position=%d ,%d/%d = %s%n", level, num, allNums, format.format(ratio));

        });
    }

    public double getFirstFileTypeMatchRatio() {
        return (double) matchNums.getOrDefault(1, 0) / allNums;
    }

    public double getSecondFileTypeMatchRatio() {
        return (double) matchNums.getOrDefault(2, 0) / allNums;
    }

    public int getMatchNum(int position) {
        return  matchNums.getOrDefault(position,0);
    }

}
