package com.kylinhunter.plat.file.detector.selector;

import java.util.Comparator;

import com.kylinhunter.plat.file.detector.selector.bean.SortMagic;

import lombok.RequiredArgsConstructor;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-31 17:53
 **/
@RequiredArgsConstructor
public class MagicDefaultComparator implements Comparator<SortMagic> {

    @Override
    public int compare(SortMagic o1, SortMagic o2) {

        int matchExtension = o2.getMatchExtensionInt() - o1.getMatchExtensionInt();
        if (matchExtension < 0) {
            return -1;
        } else if (matchExtension > 0) {
            return 1;
        } else {
            int dimensionOffset = o1.getMagic().getOffset() - o2.getMagic().getOffset();

            if (dimensionOffset < 0) {
                return -1;
            } else if (dimensionOffset > 0) {
                return 1;
            } else {
                int dimensionMagicLen = o2.getMagic().getLength() - o1.getMagic().getLength();
                return Integer.compare(dimensionMagicLen, 0);
            }
        }

    }
}
