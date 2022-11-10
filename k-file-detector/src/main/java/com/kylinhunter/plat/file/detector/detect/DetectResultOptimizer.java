package com.kylinhunter.plat.file.detector.detect;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.content.bean.DetectConext;
import com.kylinhunter.plat.file.detector.detect.bean.DetectResult;
import com.kylinhunter.plat.file.detector.detect.bean.SortMagic;
import com.kylinhunter.plat.file.detector.file.bean.FileType;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 14:08
 **/
@Slf4j
@C
public class DetectResultOptimizer {

    /**
     * @param detectConext detectConext
     * @return com.kylinhunter.plat.file.detector.magic.bean.Magic
     * @title checkExtensionConsistent
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:35
     */
    public DetectResult optimize(DetectConext detectConext) {

        DetectResult detectResult = new DetectResult(detectConext);

        List<Magic> detectedMagics = detectConext.getDetectedMagics();
        if (!CollectionUtils.isEmpty(detectedMagics)) { // check extension consistent
            List<SortMagic> sortMagics = calPossibleMagics(detectConext);
            sortMagics.forEach(sortMagic -> detectResult.addPossibleMagic(sortMagic.getMagic()));

        }
        List<FileType> possibleFileTypes = calPossibleFileTypes(detectConext);
        detectResult.setPossibleFileTypes(possibleFileTypes);
        return detectResult;
    }

    /**
     * @param detectConext detectConext
     * @return java.util.List<com.kylinhunter.plat.file.detector.detect.selector.bean.SortMagic>
     * @title toSortMagics
     * @description
     * @author BiJi'an
     * @date 2022-11-03 23:22
     */
    private List<SortMagic> calPossibleMagics(DetectConext detectConext) {
        String extension = detectConext.getExtension();
        List<Magic> detectedMagics = detectConext.getDetectedMagics();
        List<SortMagic> sortMagics = Lists.newArrayList();
        for (Magic magic : detectedMagics) {
            SortMagic sortMagic = new SortMagic(magic, extension);
            sortMagics.add(sortMagic);
        }
        Collections.sort(sortMagics);
        for (int i = 0; i < sortMagics.size(); i++) {
            SortMagic cur = sortMagics.get(i);
            if (cur.getMagic().isExtensionMustMatch() && !cur.isMatchExtension()) {
                int betterParentMagicIndex = 0;
                for (int j = i + 1; j < sortMagics.size(); j++) {
                    SortMagic next = sortMagics.get(j);
                    if (cur.getOffset() == 0 && next.getOffset() == 0 && cur.getNumber().startsWith(next.getNumber())) {

                        if (!next.getMagic().isExtensionMustMatch()) {
                            betterParentMagicIndex = j;
                            break;
                        } else {
                            if (next.isMatchExtension()) {
                                betterParentMagicIndex = j;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                if (betterParentMagicIndex > 0) {
                    sortMagics.set(i, sortMagics.get(betterParentMagicIndex));
                    sortMagics.set(betterParentMagicIndex, cur);
                }
            }
        }
        detectConext.setSortMagics(sortMagics);
        return sortMagics;

    }

    /**
     * @param detectConext detectConext
     * @return java.util.List<com.kylinhunter.plat.file.detector.file.bean.FileType>
     * @title calPossibleFileTypes
     * @description
     * @author BiJi'an
     * @date 2022-11-10 01:03
     */
    private List<FileType> calPossibleFileTypes(DetectConext detectConext) {
        List<FileType> possibleFileTypes = Lists.newArrayList();

        List<SortMagic> sortMagics = detectConext.getSortMagics();
        if (!CollectionUtils.isEmpty(sortMagics)) {
            for (int i = 0; i < sortMagics.size(); i++) {
                SortMagic sortMagic = sortMagics.get(i);
                Magic magic = sortMagic.getMagic();
                magic.getFileTypes().forEach(fileType -> {
                    if (!possibleFileTypes.contains(fileType)) {
                        possibleFileTypes.add(fileType);
                    }
                });
                if (i == 0) {
                    if (sortMagic.isMatchExtension()) {
                        FileType matchFileType = sortMagic.getMatchFileType();
                        possibleFileTypes.remove(matchFileType);
                        possibleFileTypes.add(0, matchFileType);
                    }
                }
            }
            FileType firstFileType = possibleFileTypes.get(0);
            SortMagic firstSortMagic = sortMagics.get(0);

            if (possibleFileTypes.size() > 1) {

                for (int i = sortMagics.size() - 1; i >= 0; i--) {
                    SortMagic sortMagic = sortMagics.get(i);
                    Magic magic = sortMagic.getMagic();
                    if (magic.getRefMagic() == null && sortMagic.isMatchExtension()) {
                        FileType matchFileType = sortMagic.getMatchFileType();
                        if (!firstFileType.equals(matchFileType)) {
                            if (firstSortMagic.getMagic().isExtensionMustMatch() && !firstSortMagic
                                    .isMatchExtension()) {
                                possibleFileTypes.remove(matchFileType);
                                possibleFileTypes.add(0, matchFileType);
                            } else {
                                possibleFileTypes.remove(matchFileType);
                                possibleFileTypes.add(1, matchFileType);
                            }

                        }

                    }

                }

            }

        }
        List<FileType> contentFileTypes = detectConext.getContentFileTypes();
        if (!CollectionUtils.isEmpty(contentFileTypes)) {
            for (int i = contentFileTypes.size() - 1; i >= 0; i--) {
                FileType fileType = contentFileTypes.get(i);
                possibleFileTypes.remove(fileType);
                possibleFileTypes.add(0, fileType);

            }

        }
        return possibleFileTypes;

    }

}
