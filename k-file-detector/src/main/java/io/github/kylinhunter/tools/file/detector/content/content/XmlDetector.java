package io.github.kylinhunter.tools.file.detector.content.content;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import io.github.kylinhunter.tools.file.detector.common.component.C;
import io.github.kylinhunter.tools.file.detector.content.constant.ContentDetectType;
import io.github.kylinhunter.tools.file.detector.file.FileTypeManager;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:45
 **/
@Slf4j
@C
public class XmlDetector extends AbstractContentDetector implements ContentDetector {
    @Getter
    private final ContentDetectType contentDetectType = ContentDetectType.XML;

    private final FileType xmlFileType;
    private final FileType htmlFileType;

    public XmlDetector(FileTypeManager fileTypeManager) {
        this.xmlFileType = fileTypeManager.getFileTypeById("k_xml");
        this.htmlFileType = fileTypeManager.getFileTypeById("k_html");
    }

    @Override
    public FileType[] detectContent(byte[] bytes) {
        String text = new String(bytes, StandardCharsets.UTF_8);

        Element xmlRootElement = checkXml(text);
        if (xmlRootElement != null) {
            if (xmlRootElement.getName().equalsIgnoreCase("html")) {
                return new FileType[] {htmlFileType};
            } else {
                return new FileType[] {xmlFileType};
            }
        } else {

            String headText = StringUtils.substring(text, 0, 250).toString().trim();
            String tailText = StringUtils.substring(text, -20).toString().trim();
            if (headText.contains("<html") && tailText.endsWith("html>")) {
                return new FileType[] {htmlFileType};
            }

        }

        return EMTPY;
    }

    /**
     * @param text text
     * @return boolean
     * @title checkXml
     * @description
     * @author BiJi'an
     * @date 2022-11-13 15:41
     */
    Element checkXml(String text) {
        try {
            Document document = DocumentHelper.parseText(text);
            if (document != null) {
                return document.getRootElement();
            }

        } catch (Exception e) {
            log.error("not xml");
        }
        return null;
    }

}
