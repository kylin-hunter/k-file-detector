package io.github.kylinhunter.tools.file.detector.detect;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import io.github.kylinhunter.tools.file.detector.common.component.C;
import io.github.kylinhunter.tools.file.detector.content.bean.DetectConext;
import io.github.kylinhunter.tools.file.detector.detect.bean.DetectResult;
import io.github.kylinhunter.tools.file.detector.detect.bean.SortMagic;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;

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
     * @return io.github.kylinhunter.plat.file.detector.magic.bean.Magic
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
     * @return java.util.List<io.github.kylinhunter.plat.file.detector.detect.selector.bean.SortMagic>
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
        detectConext.setSortMagics(sortMagics);
        return sortMagics;

    }

    /**
     * @param detectConext detectConext
     * @return java.util.List<io.github.kylinhunter.plat.file.detector.file.bean.FileType>
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

            if (possibleFileTypes.size() > 1) {
                FileType refFileType = null;
                for (SortMagic sortMagic : sortMagics) {
                    Magic magic = sortMagic.getMagic();
                    Magic refMagic = magic.getRefMagic();
                    if (refMagic != null) {
                        for (SortMagic tmpSortMagic : sortMagics) {

                            if (refMagic == tmpSortMagic.getMagic()) {
                                refFileType = magic.getFirstFileType();
                                break;

                            }

                        }

                    }
                    if (refFileType != null) {
                        possibleFileTypes.remove(refFileType);
                        possibleFileTypes.add(0, refFileType);
                        break;
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
