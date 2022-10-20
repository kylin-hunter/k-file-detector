package com.kylinhunter.file.detector.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-04 10:32
 **/
@Slf4j
public class MultipartFileHelper {

    private static FileItemFactory fileItemFactory = new DiskFileItemFactory(1024, null);

    /**
     * @param file
     * @return org.springframework.web.multipart.MultipartFile
     * @throws
     * @title getMultipartFile
     * @description
     * @author BiJi'an
     * @date 2022-10-04 10:33
     */
    public static MultipartFile getMultipartFile(File file) throws IOException {
        return new CommonsMultipartFile(createFileItem(file));

    }

    /**
     * @param file
     * @return org.apache.commons.fileupload.FileItem
     * @throws
     * @title createFileItem
     * @description
     * @author BiJi'an
     * @date 2022-10-04 10:33
     */
    private static FileItem createFileItem(File file) throws IOException {

        FileItem item = fileItemFactory.createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, file.getName());
        try (InputStream in = new FileInputStream(file); OutputStream out = item.getOutputStream()) {
            IOUtils.copy(in, out);
        } catch (Exception e) {
            throw new IOException("createFileItem erorr", e);
        }

        return item;
    }
}
