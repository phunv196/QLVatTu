package com.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

public class TemplateResouces {
    /**
     * getImportFile
     * @param path
     * @return
     * @throws IOException 
     */
    public static String getImportFilePath(String path) throws IOException {
        return new ClassPathResource("public/template/import/" + path).getFile().getPath();
    }
    /**
     * getImportFile
     * @param path
     * @return
     * @throws IOException 
     */
    public static InputStream getImportFile(String path) throws IOException {
        return new ClassPathResource("public/template/import/" + path).getInputStream();
    }
    /**
     * getReportFile
     * @param path
     * @returns
     * @throws IOException 
     */
    public static InputStream getReportFile(String path) throws IOException {
        return new FileInputStream(new File(path));
    }
    
    public static InputStream getDefaultImageFile(String path) throws IOException {
        return new ClassPathResource("public/template/images/" + path).getInputStream();
    }

    /**
     * Doc noi dung file theo duong dan file
     * 
     * @param path duong dan file
     * @return
     * @throws IOException
     */
    public static String readFileByPath(String path) throws IOException {
        InputStream inputStream = new ClassPathResource(path).getInputStream();
        if (inputStream == null) {
            return "";
        }

        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }
}