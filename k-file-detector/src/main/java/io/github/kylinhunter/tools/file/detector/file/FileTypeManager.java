package io.github.kylinhunter.tools.file.detector.file;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.kylinhunter.tools.file.detector.common.component.C;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

import lombok.Getter;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-20 15:51
 **/

@C
public class FileTypeManager {
    private final String EXTENSIN_EMPTY = "!@#$%^";
    private final Map<String, Set<FileType>> extensionToFileTypes = Maps.newHashMap();
    private final Map<String, FileType> idToFileTyes = Maps.newHashMap();
    @Getter
    private final List<FileType> allFileTypes = Lists.newArrayList();
    @Getter
    private final Set<String> allExtensions = Sets.newHashSet();

    public FileTypeManager() {
        init(FileTypeConfigLoader.load());
    }

    /**
     * @param fileTypeConfig fileTypeConfig
     * @return void
     * @title addDefaultData
     * @description
     * @author BiJi'an
     * @date 2022-10-20 16:12
     */
    private void init(FileTypeConfigLoader.FileTypeConfig fileTypeConfig) {
        fileTypeConfig.getFileTypes().forEach(fileType -> {
            check(fileType);
            process(fileType);
        });

    }

    /**
     * @param fileType fileType
     * @return void
     * @title processBasic
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:05
     */
    private void process(FileType fileType) {

        allFileTypes.add(fileType);
        idToFileTyes.put(fileType.getId(), fileType);

        List<String> extensions = fileType.getExtensions();
        if (!CollectionUtils.isEmpty(extensions)) {
            allExtensions.addAll(extensions);
            extensions.forEach(extension -> extensionToFileTypes.compute(extension, (k, v) -> {
                if (v == null) {
                    v = Sets.newHashSet();
                }
                v.add(fileType);
                return v;
            }));
        } else {

            extensionToFileTypes.compute(EXTENSIN_EMPTY, (k, v) -> {
                if (v == null) {
                    v = Sets.newHashSet();
                }
                v.add(fileType);
                return v;
            });
        }

    }

    /**
     * @param fileType fileType
     * @return void
     * @title check
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:54
     */
    private void check(FileType fileType) {
        List<String> extensions = fileType.getExtensions();
        if (!CollectionUtils.isEmpty(extensions)) {

            fileType.setExtensions(extensions.stream().map(e -> e.toLowerCase()).collect(Collectors.toList()));
        } else {
            fileType.setExtensions(Collections.EMPTY_LIST);
        }
        Preconditions.checkArgument(!StringUtils.isEmpty(fileType.getId()), " file type id can't be empty");

        if (allFileTypes.contains(fileType)) {
            throw new DetectException(" duplicate fileType " + fileType);
        }

    }

    /**
     * @param extension extension
     * @return java.util.Set<io.github.kylinhunter.plat.file.detector.file.bean.FileType>
     * @title getFileTypeWrappers
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:48
     */
    @SuppressWarnings("unchecked")
    public Set<FileType> getFileTypesByExtension(String extension) {
        if (extension != null) {
            return extensionToFileTypes.getOrDefault(extension.toLowerCase(), Collections.EMPTY_SET);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * @return java.util.Set<io.github.kylinhunter.plat.file.detector.file.bean.FileType>
     * @title getFileTypesWithNoExtension
     * @description
     * @author BiJi'an
     * @date 2022-10-29 16:14
     */

    @SuppressWarnings("unchecked")
    public Set<FileType> getFileTypesWithNoExtension() {
        return extensionToFileTypes.getOrDefault(EXTENSIN_EMPTY, Collections.EMPTY_SET);
    }

    /**
     * @param id id
     * @return io.github.kylinhunter.plat.file.detector.file.bean.FileType
     * @title getFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-24 01:48
     */
    public FileType getFileTypeById(String id) {

        return idToFileTyes.get(id);

    }

    public int allExtensionSize() {
        return allExtensions.size();
    }

}
