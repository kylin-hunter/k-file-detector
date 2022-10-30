package com.kylinhunter.plat.file.detector.component;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.bean.DetectConext;
import com.kylinhunter.plat.file.detector.bean.DetectResult;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.config.bean.Magic;

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

            allPossibleMagics.forEach(magic -> {
                magic.getFileTypes().forEach(fileType -> {
                    if (!allPossibleFileTypes.contains(fileType)) {
                        allPossibleFileTypes.add(fileType);
                    }
                });
            });

            String extension = detectConext.getExtension();

            List<FileType> allBestFileTypes = Lists.newArrayList();

            for (int i = 0; i < allPossibleMagics.size(); i++) {
                Magic magic = allPossibleMagics.get(i);
                FileType fileType = this.getBestFileType(magic, extension);
                if (fileType != null) {
                    if (!allBestFileTypes.contains(fileType)) {
                        allBestFileTypes.add(fileType);
                    }
                    allBestMagics.add(magic);
                }
            }

            if (allBestMagics.size() <= 0) {
                allBestMagics.add(allPossibleMagics.get(0));
            }
            if (allPossibleFileTypes.size() <= 0) {
                allPossibleFileTypes.add(allBestMagics.get(0).getFirstFileType());
            }

            for (int i = allBestFileTypes.size() - 1; i >= 0; i--) {
                FileType fileType = allBestFileTypes.get(0);
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

    public static class MagicComparator implements Comparator<Magic> {

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

}
