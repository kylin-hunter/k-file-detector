package io.github.kylinhunter.tools.file.detector.prepared;

import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-30 15:07
 **/
public class FileTypeIdGenerator {

    private static final String PREFIX_NO_EXTENSION = "none";

    public Map<String, String> idDescs = Maps.newHashMap();

    public String generateId(String magicNumber, FileType fileType) {
        StringJoiner stringJoiner = new StringJoiner(",");
        fileType.getExtensions().forEach(ex -> {
            stringJoiner.add(ex);
        });
        String extension = stringJoiner.toString();
        String desc = fileType.getDesc();
        Preconditions.checkArgument(!StringUtils.isEmpty(desc));

        String id = "";
        int magicPrefLen = 4;

        if (!StringUtils.isEmpty(extension)) {
            id = extension;
        } else {
            id = PREFIX_NO_EXTENSION + "_" + StringUtils.substring(magicNumber, 0, magicPrefLen);
        }
        String oldDesc = idDescs.get(id);
        if (oldDesc != null && !desc.equals(oldDesc)) {
            id = "";
            String prex;
            if (!StringUtils.isEmpty(extension)) {
                prex = extension + "_";
            } else {
                prex = PREFIX_NO_EXTENSION + "_";
            }

            if (magicNumber.length() >= magicPrefLen) {
                for (int i = magicPrefLen; i <= magicNumber.length(); i += 2) {
                    String tmpId = prex + magicNumber.substring(0, i);
                    oldDesc = idDescs.get(tmpId);
                    if (oldDesc == null || desc.equals(oldDesc)) {
                        id = tmpId;
                        break;
                    }
                }
            } else {
                if (StringUtils.isEmpty(id)) {
                    for (int i = magicNumber.length(); i >= 2; i -= 2) {
                        String tmpId = prex + magicNumber.substring(0, i);
                        oldDesc = idDescs.get(tmpId);
                        if (oldDesc == null || desc.equals(oldDesc)) {
                            id = tmpId;
                            break;
                        }
                    }
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

