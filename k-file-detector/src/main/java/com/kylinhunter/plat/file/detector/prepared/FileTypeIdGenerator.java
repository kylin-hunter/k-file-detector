package com.kylinhunter.plat.file.detector.prepared;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.file.bean.FileType;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-30 15:07
 **/
public class FileTypeIdGenerator {

    private static final String BASE_NO_EXTENSION = "none_";

    public Map<String, String> idDescs = Maps.newHashMap();

    public String generateId(String magicNumber, FileType fileType) {
        String extension = fileType.getExtension();
        String desc = fileType.getDesc();
        Preconditions.checkArgument(!StringUtils.isEmpty(desc));

        String id = "";

        if (!StringUtils.isEmpty(extension)) {
            id = extension;
            String oldDesc = idDescs.get(id);
            if (oldDesc != null && !desc.equals(oldDesc)) {
                id = "";

                for (int i = 4; i <= magicNumber.length(); i += 2) {
                    String tmpId = extension + "_" + magicNumber.substring(0, i);
                    oldDesc = idDescs.get(tmpId);
                    if (oldDesc == null || desc.equals(oldDesc)) {
                        id = tmpId;
                        break;
                    }
                }
                if (StringUtils.isEmpty(id)) {
                    for (int i = 2; i <= magicNumber.length(); i += 2) {
                        String tmpId = extension + "_" + magicNumber.substring(0, i);
                        oldDesc = idDescs.get(tmpId);
                        if (oldDesc == null || desc.equals(oldDesc)) {
                            id = tmpId;
                            break;
                        }
                    }
                }



            }

        } else {

            for (int i = 4; i <= magicNumber.length(); i += 2) {
                String tmpId = BASE_NO_EXTENSION + magicNumber.substring(0, i);
                String oldDesc = idDescs.get(tmpId);
                if (oldDesc == null || desc.equals(oldDesc)) {
                    id = tmpId;
                    break;
                }
            }

        }
        if (StringUtils.isEmpty(id)) {
            throw new DetectException("empty id=> " + id + ",extension" + extension + ",desc" + desc);

        }

        idDescs.put(id, desc);
        return id;

    }
}

