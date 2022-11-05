package com.kylinhunter.plat.file.detector.config.bean;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FileTypesWrapper {
    private List<FileType> fileTypes;

    protected List<String> extensions = Lists.newArrayList();

    public FileTypesWrapper(List<FileType> fileTypes) {
        this.fileTypes = fileTypes;
        fileTypes.forEach(fileType -> extensions.add(fileType.getExtension()));
    }

    @Override
    public String toString() {
        return extensions.toString();
    }
}