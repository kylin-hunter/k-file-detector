package com.kylinhunter.plat.file.detector.component;

import java.io.File;
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
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.common.util.ZipUtil;
import com.kylinhunter.plat.file.detector.config.bean.FileType;
import com.kylinhunter.plat.file.detector.exception.DetectException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:45
 **/
@Slf4j
@Component
public class MSOfficeDetector {
    private FileTypeManager fileTypeManager;
    private MagicManager magicManager;
    private FileType ppsx;
    private FileType dotx;
    private FileType xltx;

    private FileType docx;
    private FileType xlsx;
    private FileType pptx;
    private FileType potx;
    private static final Map<String, FileType> DIR_FILE_TYPES = Maps.newHashMap();

    public MSOfficeDetector(FileTypeManager fileTypeManager, MagicManager magicManager) {
        this.fileTypeManager = fileTypeManager;
        this.magicManager = magicManager;
        init();
    }

    private void init() {

        ppsx = fileTypeManager.getFileTypeById("100001");
        dotx = fileTypeManager.getFileTypeById("100002");
        xltx = fileTypeManager.getFileTypeById("100003");
        potx = fileTypeManager.getFileTypeById("100005");

        docx = fileTypeManager.getFileTypeById("0_docx");
        xlsx = fileTypeManager.getFileTypeById("0_xlsx");
        pptx = fileTypeManager.getFileTypeById("0_pptx");

        DIR_FILE_TYPES.put("word", fileTypeManager.getFileTypeById("0_docx"));
        DIR_FILE_TYPES.put("xl", fileTypeManager.getFileTypeById("0_xlsx"));
        DIR_FILE_TYPES.put("ppt", fileTypeManager.getFileTypeById("0_pptx"));

    }

    public FileType check(File file) {

        File tempDirectory = FileUtils.getTempDirectory();
        File extractPath = new File(tempDirectory, UUID.randomUUID().toString());
        //        log.info("exact to ==>" + extractPath.getAbsolutePath());
        try {
            ZipUtil.unzip(file, extractPath);
            if (extractPath.exists()) {
                for (File exFile : extractPath.listFiles()) {
                    if (exFile.isDirectory()) {
                        //                        log.info(exFile.getAbsolutePath());
                        FileType fileType = DIR_FILE_TYPES.get(exFile.getName());
                        if (fileType != null) {
                            return checkXml(extractPath, fileType);
                        }

                    }
                }

            }

        } catch (Exception e) {
            throw new DetectException("check error", e);
        } finally {
            FileUtils.deleteQuietly(extractPath);
            //            log.info("delete  ==>" + extractPath.getAbsolutePath());

        }

        return null;
    }

    private FileType checkXml(File extractPath, FileType defaultFileType) throws DocumentException {
        File contentXml = new File(extractPath, "[Content_Types].xml");
        SAXReader reader = new SAXReader();
        Document document = reader.read(contentXml);
        Map<String, String> xmlMap = new HashMap<>();
        xmlMap.put("default", "http://schemas.openxmlformats.org/package/2006/content-types");

        XPath xPath = document.createXPath("//default:Override");
        xPath.setNamespaceURIs(xmlMap);
        List<Node> nodes = xPath.selectNodes(document);
        for (Node node : nodes) {
            Element element = (Element) node;
            Attribute partName = element.attribute("PartName");
            Attribute contentType = element.attribute("ContentType");
            if (partName.getValue().equals("/word/document.xml")) {
                if (contentType.getValue().contains("document.main+xml")) {
                    return docx;
                } else if (contentType.getValue().contains("template.main+xml")) {
                    return dotx;

                }

            }

            if (partName.getValue().equals("/ppt/presentation.xml")) {
                if (contentType.getValue().contains("presentation.main+xml")) {
                    return pptx;
                } else if (contentType.getValue().contains("slideshow.main+xml")) {
                    return ppsx;
                } else if (contentType.getValue().contains("template.main+xml")) {
                    return potx;
                }

            }

            if (partName.getValue().equals("/xl/workbook.xml")) {
                if (contentType.getValue().contains("sheet.main+xml")) {
                    return xlsx;
                } else {
                    if (contentType.getValue().contains("template.main+xml")) {
                        return xltx;
                    }

                }

            }
        }
        return defaultFileType;

    }

}
