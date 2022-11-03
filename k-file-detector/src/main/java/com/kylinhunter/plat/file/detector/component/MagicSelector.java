package com.kylinhunter.plat.file.detector.component;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;
import com.kylinhunter.plat.file.detector.selector.MagicDefaultComparator;
import com.kylinhunter.plat.file.detector.selector.SortMagic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 14:08
 **/
@Slf4j
@Component
public class MagicSelector {

    private final MagicDefaultComparator magicDefaultComparator = new MagicDefaultComparator();

    /**
     * @param detectConext detectConext
     * @return com.kylinhunter.plat.file.detector.config.bean.Magic
     * @title checkExtensionConsistent
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:35
     */
    public DetectResult selectBest(DetectConext detectConext) {

        DetectResult detectResult = new DetectResult(detectConext);

        List<Magic> detectedMagics = detectConext.getDetectedMagics();
        if (!CollectionUtils.isEmpty(detectedMagics)) { // check extension consistent
            List<SortMagic> sortMagics = toSortMagics(detectedMagics, detectConext.getExtension());
            detectResult.setAllPossibleMagics(
                    sortMagics.stream().map(SortMagic::getMagic).collect(Collectors.toList()));
            detectResult.setAllPossibleFileTypes(calAllPossibleFileTypes(sortMagics));
        }

        return detectResult;
    }

    private List<FileType> calAllPossibleFileTypes(List<SortMagic> sortMagics) {
        List<FileType> allPossibleFileTypes = Lists.newArrayList();  // all possible file type
        sortMagics.forEach(e -> allPossibleFileTypes.addAll(e.getMagic().getFileTypes()));

        for (int i = sortMagics.size() - 1; i >= 0; i--) {
            SortMagic sortMagic = sortMagics.get(i);
            if (sortMagic.isMatchExtension()) {
                allPossibleFileTypes.remove(sortMagic.getMatchFileType());
                allPossibleFileTypes.add(0, sortMagic.getMatchFileType());
            } else {
                if (i == 0 && sortMagic.isMustFirst()) {
                    allPossibleFileTypes.remove(sortMagic.getMagic().getFirstFileType());
                    allPossibleFileTypes.add(0, sortMagic.getMagic().getFirstFileType());
                }
            }
        }
        return allPossibleFileTypes;
    }

    /**
     * @param sortMagics sortMagics
     * @return void
     * @title revise
     * @description
     * @author BiJi'an
     * @date 2022-11-04 00:01
     */
    private void revise(List<SortMagic> sortMagics) {
        if (sortMagics.size() > 1) {
            SortMagic sortMagic1 = sortMagics.get(0);
            Magic magic1 = sortMagic1.getMagic();
            SortMagic sortMagic2 = sortMagics.get(1);
            Magic magic2 = sortMagic2.getMagic();
            if (magic2.getNumber().contains(magic1.getNumber())) {
                if (!sortMagic2.isMatchExtension()) {
                    if (!magic2.isExtensionMustHitAsFather()) {
                        sortMagics.set(0, sortMagic2);
                        sortMagics.set(1, sortMagic1);
                        sortMagic2.setMustFirst(true);
                    }
                }
            }
        }
    }

    /**
     * @param allPossibleMagics allPossibleMagics
     * @param extension         extension
     * @return java.util.List<com.kylinhunter.plat.file.detector.selector.SortMagic>
     * @title toSortMagics
     * @description
     * @author BiJi'an
     * @date 2022-11-03 23:22
     */
    private List<SortMagic> toSortMagics(List<Magic> allPossibleMagics, String extension) {
        List<SortMagic> sortMagics = Lists.newArrayList();
        for (Magic magic : allPossibleMagics) {
            SortMagic sortMagic = new SortMagic(magic, extension);
            sortMagics.add(sortMagic);
        }
        if (sortMagics.size() > 1) {
            sortMagics.sort(magicDefaultComparator);
        }
        revise(sortMagics);
        return sortMagics;

    }
}
