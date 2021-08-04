package com.app.service;

import com.app.dao.base.converter.DynamicExport;
import com.app.util.TemplateResouces;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeService {
    static String EXPORT_FOLDER = "D:/khoa luan tn/QLVatTu/web-api/src/main/resources";
    public ResponseEntity<InputStreamResource> processExport(HttpServletRequest req)
            throws IOException, Exception {
        // Check quyen xuat bao cao
        // Khoi tao dynamicExport
        // voi duong dan den file template
        boolean isExport = true;

        //String pathFile = "brief/briefResult1/cblt/bao_cao_danh_sach_ho_so_vi_cblt_vi.xls";
//        String pathFile = "D:/khoa luan tn/templateExcel/filelicense-52.xlsx";
        String pathFile = "D:/khoa luan tn/QLVatTu/web-api/src/main/resources/aaa.xls";
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(pathFile), 0, true);

        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = EXPORT_FOLDER + "/" + prefixOutPutFile +  "danh_sach_chi_so_dien_toa_nha";
        String fileName = dynamicExport.exportFile(fileExport, req);
        File file = new File(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }

//    private static final String EXTERNAL_FILE_PATH = "D:/khoa luan tn/QLVatTu/";
//
//    public void processExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        File file = new File(EXTERNAL_FILE_PATH + "filelicense-52.xlsx");
//        if (file.exists()) {
//
//            //get the mimetype
//            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
//            if (mimeType == null) {
//                //unknown mimetype so set the mimetype to application/octet-stream
//                mimeType = "application/octet-stream";
//            }
//
//            response.setContentType(mimeType);
//
//            /**
//             * In a regular HTTP response, the Content-Disposition response header is a
//             * header indicating if the content is expected to be displayed inline in the
//             * browser, that is, as a Web page or as part of a Web page, or as an
//             * attachment, that is downloaded and saved locally.
//             *
//             */
//
//            /**
//             * Here we have mentioned it to show inline
//             */
//            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
//
//            //Here we have mentioned it to show as attachment
//            // response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
//
//            response.setContentLength((int) file.length());
//
//            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
//
//            FileCopyUtils.copy(inputStream, response.getOutputStream());
//
//        }
//    }

}
