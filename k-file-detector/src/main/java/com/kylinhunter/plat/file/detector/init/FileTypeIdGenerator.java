package com.kylinhunter.plat.file.detector.init;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.exception.DetectException;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-30 15:07
 **/
public class FileTypeIdGenerator {

    private static final String BASE_EXTENSION = "0_";

    private static final String BASE_NO_EXTENSION = "1_";

    public Map<String, String> duplicateId = Maps.newHashMap();

    public String generateId(String extension, String desc) {

        Preconditions.checkArgument(!StringUtils.isEmpty(desc));
        String content = extension + "@" + desc;
        String id;
        if (!StringUtils.isEmpty(extension)) {
            id = BASE_EXTENSION + extension;
            String oldContent = duplicateId.get(id);
            if (oldContent != null && !content.equals(oldContent)) {
                id = BASE_EXTENSION + extension + "_" + Math.abs(desc.hashCode());
                oldContent = duplicateId.get(id);
                if (oldContent != null && !content.equals(oldContent)) {
                    throw new DetectException("duplicate id=> " + id + "," + oldContent + "," + content);
                }
            }

        } else {
            id = BASE_NO_EXTENSION + Math.abs(desc.hashCode());
            String oldContent = duplicateId.get(id);
            if (oldContent != null && !content.equals(oldContent)) {
                throw new DetectException("duplicate id=> " + id + "," + oldContent + "," + content);
            }

        }

        duplicateId.put(id, content);
        return id;

    }
}

