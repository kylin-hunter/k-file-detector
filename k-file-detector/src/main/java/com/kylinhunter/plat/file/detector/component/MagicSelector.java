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

    private final int LEVEL_FILE_TYPE = 1000000000;

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

        List<Magic> allPossibleMagics = detectConext.getDetectedMagics();
        if (!CollectionUtils.isEmpty(allPossibleMagics)) { // check extension consistent
            String extension = detectConext.getExtension();

            SortMagic firsNoMatchExtension = null;
            int extensionMatch = 0;
            List<SortMagic> sortMagics = Lists.newArrayList();
            for (Magic magic : allPossibleMagics) {
                SortMagic sortMagic = new SortMagic(magic, extension);
                if (firsNoMatchExtension == null) {
                    if (sortMagic.getMatchExtension() == 1) {
                        extensionMatch++;
                    } else {
                        firsNoMatchExtension = sortMagic;

                    }
                }
                sortMagics.add(sortMagic);
            }
            if (sortMagics.size() > 0) {

                sortMagics.sort(magicDefaultComparator);

                for (int i = 0; i < sortMagics.size(); i++) {
                    SortMagic curMagic = sortMagics.get(i);
                    if (i + 1 < sortMagics.size()) {
                        SortMagic nexSortMagic = sortMagics.get(i + 1);
                        if (curMagic.getMagic().getNumber().contains(nexSortMagic.getMagic().getNumber())) {
                            if (curMagic.getMagic().isFatherMustExtensionHit()) {
                                if (curMagic.getMatchExtension() != 1) {
                                    sortMagics.set(i, nexSortMagic);
                                    sortMagics.set(i + 1, curMagic);
                                }

                            }
                        }
                    }

                }
            }

            SortMagic sortMagic = sortMagics.get(0);
            //            if (firsNoMatchExtension != null && firsNoMatchExtension != sortMagic) {
            //                if (firsNoMatchExtension.getMagic().getNumber().contains(sortMagic.getMagic().getNumber
            //                ())) {
            //                    sortMagics.remove(firsNoMatchExtension);
            //                    sortMagics.add(0, firsNoMatchExtension);
            //                }
            //            }

            List<FileType> allPossibleFileTypes = detectResult.getAllPossibleFileTypes();  // all possible file type

            if (sortMagics.size() > 1) {
                SortMagic sortMagic1 = sortMagics.get(0);
                Magic magic1 = sortMagic1.getMagic();
                SortMagic sortMagic2 = sortMagics.get(1);
                Magic magic2 = sortMagic2.getMagic();
                if (magic2.getNumber().contains(magic1.getNumber())) {

                    if (sortMagics.get(1).getMatchExtension() == 0) {
                        if (!magic2.isFatherMustExtensionHit()) {
                            sortMagics.set(0, sortMagic2);
                            sortMagics.set(1, sortMagic1);
                            sortMagic2.setMustFirst(true);
                        }


                    }
                }

            }

            List<Magic> allPossibleMagics1 = sortMagics.stream().map(e ->
                    e.getMagic()
            ).collect(Collectors.toList());

            detectResult
                    .setAllPossibleMagics(allPossibleMagics1);

            sortMagics.forEach(e -> {
                allPossibleFileTypes.addAll(e.getMagic().getFileTypes());
            });

            int index = 0;
            while (true) {
                boolean has = false;
                for (int i = 0; i < sortMagics.size(); i++) {
                    sortMagic = sortMagics.get(i);
                    List<FileType> fileTypes = sortMagic.getMagic().getFileTypes();
                    if (index < fileTypes.size()) {
                        //                        allPossibleFileTypes.add(fileTypes.get(index));
                        has = true;
                    }

                }
                if (!has) {
                    break;
                }
                index++;

            }
            for (int i = sortMagics.size() - 1; i >= 0; i--) {
                SortMagic e = sortMagics.get(i);
                if (e.getMatchExtension() == 1) {
                    allPossibleFileTypes.remove(e.getTargetExtension());
                    allPossibleFileTypes.add(0, e.getTargetExtension());
                } else {
                    if (e.isMustFirst()) {
                        allPossibleFileTypes.remove(e.getMagic().getFirstFileType());
                        allPossibleFileTypes.add(0, e.getMagic().getFirstFileType());
                    }
                }
            }

            return detectResult;
        }
        return null;
    }

    protected void a() {

    }

    /**
     * @param magic     magic
     * @param extension extension
     * @return com.kylinhunter.plat.file.detector.config.bean.FileType
     * @title getBestFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-30 23:10
     */
    private FileType getBestFileType(Magic magic, String extension) {
        List<FileType> fileTypes = magic.getFileTypes();

        for (FileType fileType : fileTypes) {
            if (fileType.extensionEquals(extension)) {
                return fileType;
            }
        }
        return null;
    }

}
