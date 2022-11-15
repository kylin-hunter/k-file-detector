package io.github.kylinhunter.tools.file.detector.content.content;

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

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.github.kylinhunter.tools.file.detector.common.component.C;
import io.github.kylinhunter.tools.file.detector.common.util.ZipUtil;
import io.github.kylinhunter.tools.file.detector.content.constant.ContentDetectType;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.FileTypeManager;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.MagicManager;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:45
 **/
@Slf4j
@C
@Getter
public class MSOfficeDetector extends AbstractContentDetector implements ContentDetector {
    private final FileTypeManager fileTypeManager;
    private final MagicManager magicManager;
    private final ContentDetectType contentDetectType = ContentDetectType.MS_OFFICE_2007;
    private FileType ppsxFileType;
    private FileType dotxFileType;
    private FileType xltxFileType;

    private FileType docxFileType;
    private FileType xlsxFileType;
    private FileType pptxFileType;
    private FileType potxFileType;
    private static final Map<String, FileType> DIR_NAME_TO_FILE_TYPES_MAPS = Maps.newHashMap();

    public MSOfficeDetector(FileTypeManager fileTypeManager, MagicManager magicManager) {
        this.fileTypeManager = fileTypeManager;
        this.magicManager = magicManager;
        init();

    }

    private void init() {

        ppsxFileType = fileTypeManager.getFileTypeById("k_ppsx");
        Preconditions.checkNotNull(ppsxFileType);
        dotxFileType = fileTypeManager.getFileTypeById("k_dotx");
        Preconditions.checkNotNull(dotxFileType);
        xltxFileType = fileTypeManager.getFileTypeById("k_xltx");
        Preconditions.checkNotNull(xltxFileType);
        potxFileType = fileTypeManager.getFileTypeById("k_potx");
        Preconditions.checkNotNull(potxFileType);

        docxFileType = fileTypeManager.getFileTypeById("k_docx");
        Preconditions.checkNotNull(docxFileType);
        xlsxFileType = fileTypeManager.getFileTypeById("k_xlsx");
        Preconditions.checkNotNull(xlsxFileType);
        pptxFileType = fileTypeManager.getFileTypeById("k_pptx");
        Preconditions.checkNotNull(pptxFileType);

        DIR_NAME_TO_FILE_TYPES_MAPS.put("word", docxFileType);
        DIR_NAME_TO_FILE_TYPES_MAPS.put("xl", xlsxFileType);
        DIR_NAME_TO_FILE_TYPES_MAPS.put("ppt", pptxFileType);

    }


    @Override
    public FileType[] detectContent(byte[] bytes) {
        try {
            File tempDirectory = FileUtils.getTempDirectory();
            File extractPath = new File(tempDirectory, UUID.randomUUID().toString());
            ZipUtil.unzip(bytes, extractPath);
            FileType fileType = detectUnzipContent(extractPath);
            if (fileType != null) {
                return new FileType[] {fileType};
            }

        } catch (IOException e) {
            throw new DetectException("check error", e);

        }
        return EMTPY;
    }



    public FileType detectUnzipContent(File extractPath) {

        //        log.info("exact to ==>" + extractPath.getAbsolutePath());
        try {
            File[] files;
            if (extractPath.exists() && (files = extractPath.listFiles()) != null) {
                for (File exFile : files) {
                    if (exFile.isDirectory()) {
                        //                        log.info(exFile.getAbsolutePath());
                        FileType fileType = DIR_NAME_TO_FILE_TYPES_MAPS.get(exFile.getName());
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
                    return docxFileType;
                } else if (contentType.getValue().contains("template.main+xml")) {
                    return dotxFileType;

                }

            }

            if (partName.getValue().equals("/ppt/presentation.xml")) {
                if (contentType.getValue().contains("presentation.main+xml")) {
                    return pptxFileType;
                } else if (contentType.getValue().contains("slideshow.main+xml")) {
                    return ppsxFileType;
                } else if (contentType.getValue().contains("template.main+xml")) {
                    return potxFileType;
                }

            }

            if (partName.getValue().equals("/xl/workbook.xml")) {
                if (contentType.getValue().contains("sheet.main+xml")) {
                    return xlsxFileType;
                } else {
                    if (contentType.getValue().contains("template.main+xml")) {
                        return xltxFileType;
                    }

                }

            }
        }
        return defaultFileType;

    }

}
