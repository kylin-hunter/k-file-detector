package com.kylinhunter.plat.file.detector.selector;

import java.util.Comparator;

import com.kylinhunter.plat.file.detector.config.bean.Magic;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-31 17:53
 **/
public class MagicComparator implements Comparator<Magic> {

    @Override
    public int compare(Magic o1, Magic o2) {
        int dimensionOffset = o1.getOffset() - o2.getOffset();

        if (dimensionOffset < 0) {
            return -1;
        } else if (dimensionOffset > 0) {
            return 1;
        } else {
            int dimensionMagicLen = o2.getLength() - o1.getLength();
            return Integer.compare(dimensionMagicLen, 0);
        }

    }
}
