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
     * getReportFile
     * @param path
     * @returns
     * @throws IOException 
     */
    public static InputStream getReportFile(String path) throws IOException {
        return new FileInputStream(new File(path));
    }
}