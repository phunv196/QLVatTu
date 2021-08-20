package com.app.service;

import com.app.dao.EmployeeDao;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.converter.DynamicExport;
import com.app.model.employee.EmployeeModel;
import com.app.util.TemplateResouces;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.springframework.core.io.InputStreamResource;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeService {
    EmployeeDao employeeDao = new EmployeeDao();
    static String EXPORT_FOLDER = "D:/templateExcel";
    public boolean processExport(HttpServletRequest req)
            throws IOException, Exception {
        // Check quyen xuat bao cao
        // Khoi tao dynamicExport
        // voi duong dan den file template
        boolean isExport = true;

        //String pathFile = "brief/briefResult1/cblt/bao_cao_danh_sach_ho_so_vi_cblt_vi.xls";
//        String pathFile = "D:/khoa luan tn/templateExcel/filelicense-52.xlsx";
        String pathFile = "D:/templateExcel/phunv.xls";
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(pathFile), 11, false);
        Criteria criteria = employeeDao.createCriteria(EmployeeModel.class);
        List<EmployeeModel> models = criteria.list();
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                EmployeeModel model = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(model.getCode(), index++);
                dynamicExport.setText(model.getFullName(), index++);
                dynamicExport.setText(CommonUtils.convertDateToString(model.getBirth()), index++);
                dynamicExport.setText(model.getAddress(), index++);
                dynamicExport.setText(model.getEmail(), index++);
                dynamicExport.setText(model.getPhone(), index++);
            }
        }
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = EXPORT_FOLDER + "/" + prefixOutPutFile +  "danh_sach_chi_so_dien_toa_nha";
        String fileName = dynamicExport.exportFile(fileExport, req);
        File file = new File(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return true;
    }

}
