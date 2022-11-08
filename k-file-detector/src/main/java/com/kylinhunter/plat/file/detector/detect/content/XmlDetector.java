package com.kylinhunter.plat.file.detector.detect.content;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.common.component.C;
import com.kylinhunter.plat.file.detector.common.util.ZipUtil;
import com.kylinhunter.plat.file.detector.detect.bean.DetectConext;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.file.FileTypeManager;
import com.kylinhunter.plat.file.detector.file.bean.FileType;
import com.kylinhunter.plat.file.detector.magic.bean.Magic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:45
 **/
@Slf4j
@C
public class XmlDetector implements ContentDetector {

    @Override
    public Magic getMagic() {
        return null;
    }

    @Override
    public DetectConext detect(DetectConext detectConext) {
        return null;
    }
}
