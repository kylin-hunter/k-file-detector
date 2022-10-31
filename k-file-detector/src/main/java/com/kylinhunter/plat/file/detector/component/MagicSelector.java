package com.kylinhunter.plat.file.detector.component;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;
import com.kylinhunter.plat.file.detector.selector.MagicComparator;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 14:08
 **/
@Slf4j
@Component
public class MagicSelector {

    private final MagicComparator magicComparator = new MagicComparator();

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
            allPossibleMagics.sort(magicComparator);
            detectResult.setAllPossibleMagics(allPossibleMagics);

            List<FileType> allPossibleFileTypes = detectResult.getAllPossibleFileTypes();  // all possible file type
            List<Magic> allBestMagics = detectResult.getAllBestMagics();

            String extension = detectConext.getExtension();

            List<FileType> allBestFileTypes = Lists.newArrayList();

            Magic firstMagic = allPossibleMagics.get(0);
            for (Magic magic : allPossibleMagics) {
                FileType fileType = this.getBestFileType(magic, extension);
                if (fileType != null) {
                    if (!allBestFileTypes.contains(fileType)) {
                        allBestFileTypes.add(fileType);
                    }
                    allBestMagics.add(magic);
                }
            }

            if (allBestMagics.size() <= 0) {

                Magic bestMagic = null;
                for (int i = 0; i < allPossibleMagics.size(); i++) {

                    Magic curMagic = allPossibleMagics.get(i);

                    if (i + 1 < allPossibleMagics.size()) {
                        Magic nextMagic = allPossibleMagics.get(i + 1);
                        if (curMagic.getNumber().contains(nextMagic.getNumber())) {
                            if (curMagic.isFatherFirstNoExtensionHit()) {
                                bestMagic = curMagic;
                                break;
                            }

                        } else {
                            bestMagic = curMagic;
                            break;
                        }
                    } else {
                        bestMagic = curMagic;
                    }

                }
                if (bestMagic == null) {
                    bestMagic = firstMagic;
                }
                allBestMagics.add(bestMagic);
                allBestFileTypes.addAll(bestMagic.getFileTypes());
            } else {
                if (firstMagic != allBestMagics.get(0)) {
                    Magic bestMagic = allBestMagics.get(0);
                    if (firstMagic.getNumber().contains(bestMagic.getNumber())) {
                        if (firstMagic.isFatherFirstNoExtensionHit()) {
                            allBestMagics.add(0, firstMagic);
                            FileType firstFileType = firstMagic.getFirstFileType();
                            if (!allBestFileTypes.contains(firstFileType)) {
                                allBestFileTypes.add(0, firstFileType);
                            }
                        }
                    }

                }

            }

            allBestMagics.forEach(magic -> magic.getFileTypes().forEach(fileType -> {
                if (!allPossibleFileTypes.contains(fileType)) {
                    allPossibleFileTypes.add(fileType);
                }
            }));

            allPossibleMagics.forEach(magic -> {
                if (!allBestMagics.contains(magic)) {
                    magic.getFileTypes().forEach(fileType -> {
                        if (!allPossibleFileTypes.contains(fileType)) {
                            allPossibleFileTypes.add(fileType);
                        }
                    });
                }
            });
            if (allBestMagics.size() >= 2 && allBestFileTypes.size() >= 2) {
                if (allBestFileTypes.get(0).extensionEquals(allBestFileTypes.get(1).getExtension())) {
                    allBestFileTypes.set(1, allBestMagics.get(1).getFirstFileType());
                }
            }

            for (int i = allBestFileTypes.size() - 1; i >= 0; i--) {
                FileType fileType = allBestFileTypes.get(i);
                allPossibleFileTypes.remove(fileType);
                allPossibleFileTypes.add(0, fileType);

            }

        }

        return detectResult;
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
