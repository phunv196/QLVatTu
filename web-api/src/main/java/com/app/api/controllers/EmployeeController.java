package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.EmployeeDao;
import com.app.dao.base.CommonUtils;
import com.app.model.BaseResponse;
import com.app.model.employee.EmployeeModel;
import com.app.model.employee.EmployeeModel.EmployeeResponse;
import com.app.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.File;
import java.math.BigInteger;
import java.util.Base64;
import java.util.List;

@Path("employees")
@Tag(name = "Employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin
public class EmployeeController extends BaseController {
    EmployeeDao employeeDao = new EmployeeDao();
    EmployeeService service = new EmployeeService();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Get list of employees",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = EmployeeResponse.class)))}
    )
    public Response getEmployeeList(
        @Parameter(description="Employee Id", example="202") @QueryParam("employee-id") Long employeeId,
        @Parameter(description = "Order Id") @QueryParam("searchCode") String searchCode,
        @Parameter(description = "Order Id") @QueryParam("searchName") String searchName,
        @Parameter(description = "Order Id") @QueryParam("searchEmail") String searchEmail,
        @Parameter(description = "Order Id") @QueryParam("searchPhone") String searchPhone,
        @Parameter(description = "Order Id") @QueryParam("searchDepartment") Long searchDepartment,
        @Parameter(description = "Order Id") @QueryParam("searchPosition") Long searchPosition,
        @Parameter(description="Search by name or email - Use % for wildcard like '%ra%'", example="%ra%") @QueryParam("search") String search,
        @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1")  @QueryParam("page")  int page,
        @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) throws Exception {
//        service.processExport(req);
        EmployeeResponse resp = new EmployeeResponse();
        try {
            if (employeeId == null) {
                employeeId = 0l;
            }

            if (searchDepartment == null) {
                searchDepartment = 0l;
            }

            if (searchPosition == null) {
                searchPosition = 0l;
            }

            List<EmployeeModel> employeeList = employeeDao.getList(page, pageSize, employeeId, searchCode,
                    searchName, searchEmail, searchPhone, searchDepartment, searchPosition);
            BigInteger total = employeeDao.getEmployeeCount(employeeId, searchCode,
                    searchName, searchEmail, searchPhone, searchDepartment, searchPosition);
            resp.setList(employeeList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(), pageSize, page, "");
            resp.setSuccessMessage("List of Orders and nested details " + (employeeId > 0 ? "- Customer:" + employeeId : ""));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause() != null ? e.getCause().getMessage() : ""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Add an employee",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addEmployee(EmployeeModel emp) {
        BaseResponse resp = new BaseResponse();
        try {
            employeeDao.beginTransaction();
            employeeDao.save(emp);
            employeeDao.commitTransaction();
            resp.setSuccessMessage("Employee Added");
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add employee - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Update an Employee",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateEmployee(EmployeeModel emp) {

        BaseResponse resp = new BaseResponse();
        try {
            EmployeeModel foundEmp  = employeeDao.getById(emp.getEmployeeId());
            if (foundEmp != null){
                employeeDao.beginTransaction();
                employeeDao.update(emp);
                employeeDao.commitTransaction();
                resp.setSuccessMessage(String.format("Employee Updated (id:%s)", emp.getEmployeeId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Employee not found (id:%s)", emp.getEmployeeId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Employee - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{employeeId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Delete an Employee",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteEmployee(@Parameter(description="Employee Id", example="1") @PathParam("employeeId") Long employeeId) {
        BaseResponse resp = new BaseResponse();
        if (employeeId == 201 || employeeId == 205){
            resp.setErrorMessage("Employee 201 and 205 are special, they cannot be deleted");
            return Response.ok(resp).build();
        }
        try {
            if (employeeId == null) {
                employeeId = 0l;
            }
            EmployeeModel foundEmp  = employeeDao.getById(employeeId);
            if (foundEmp==null) {
                resp.setErrorMessage(String.format("Cannot delete - Employee do not exist (id:%s)", employeeId));
                return Response.ok(resp).build();
            } else {
                employeeDao.beginTransaction();
                employeeDao.delete(employeeId);
                employeeDao.commitTransaction();
                resp.setSuccessMessage(String.format("Employee deleted (id:%s)", employeeId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Customer - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }


    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of employees",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = EmployeeResponse.class)))}
    )
    public Response getAll(
    ) {
        int recordFrom=0;
        Criteria criteria = employeeDao.createCriteria(EmployeeModel.class);
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        // Execute the Main Query
        criteria.setProjection(null);
        criteria.setFirstResult(recordFrom);
        List<EmployeeModel> empUserList = criteria.list();
        EmployeeResponse resp = new EmployeeResponse();
        resp.setList(empUserList);

        resp.setSuccessMessage("List of employees");
        return Response.ok(resp).build();
    }

    @GET
    @Path("dowloadTemplate")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response dowloadTemplate() throws Exception {
        String path = "C:/Users/Admin/Downloads/31072021_brief_list.xls";
        String path1 = "C:/Users/Admin/Downloads/phunv.xls";
        File file = new File(path);
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);


        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        FileUtils.writeByteArrayToFile(new File(path1), decodedBytes);
        return Response.ok(encodedString).build();
    }

//    @GET
//    @Path("dowloadTemplate")
//    @RolesAllowed({"ADMIN", "SUPPORT", "CUSTOMER"})
//    @Produces("application/vnd.ms-excel")
//    public Response downloadPdfFile()
//    {
////        File file = new File("D:/khoa luan tn/QLVatTu/web-api/src/main/resources/abc.xlsx");
////
////        Response.ResponseBuilder response = Response.ok((Object) file);
////        response.header("Content-Disposition",
////                "attachment; filename=new-excel-file.xlsx");
////        return response.build();
//
//        StreamingOutput fileStream =  new StreamingOutput()
//        {
//            @Override
//            public void write(java.io.OutputStream output) throws IOException, WebApplicationException
//            {
//                try
//                {
//                    File file = new File("D:/khoa luan tn/QLVatTu/web-api/src/main/resources/aaa.xls");
//                    byte[] data = Files.readAllBytes(file.toPath());
//                    output.write(data);
//                    output.flush();
//                    output.close();
//                }
//                catch (Exception e)
//                {
//                    throw new WebApplicationException("File Not Found !!");
//                }
//            }
//        };
//        return Response
//                .ok(fileStream)
//                .header("content-disposition","attachment; filename = myfile.xls")
//                .build();
//    }



//    @GET
//    @Path("/dowloadTemplate")
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response getFile() {
//
//        File file = new File("D:/khoa luan tn/QLVatTu/web-api/src/main/resources/aaa.xls");
//
//        ResponseBuilder response = Response.ok((Object) file);
//        response.header("Content-Disposition",
//                "attachment; filename=new-android-book.xls");
//        return response.build();
//
//    }

    @POST
    @Path("byCode")
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            EmployeeModel model
    ) {
        Criteria criteria = employeeDao.createCriteria(EmployeeModel.class);
        if (model.getEmployeeId() != null){
            criteria.add(Restrictions.ne("employeeId", model.getEmployeeId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getCode())){
            criteria.add(Restrictions.eq("code", model.getCode()).ignoreCase());
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }


    @GET
    @Path("getEmployeeById/{employeeId}")
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getEmployeeById(
            @Parameter(description="Employee Id", example="1") @PathParam("employeeId") Long employeeId
    ) {
        Criteria criteria = employeeDao.createCriteria(EmployeeModel.class);
        if (employeeId != null){
            criteria.add(Restrictions.eq("employeeId", employeeId));
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        EmployeeModel model = (EmployeeModel) criteria.uniqueResult();
        return Response.ok(model).build();
    }

    @POST
    @Path("uploadFile")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add an employee",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response uploadFile(EmployeeModel emp) {
        BaseResponse resp = new BaseResponse();
        try {
            employeeDao.beginTransaction();
            employeeDao.save(emp);
            employeeDao.commitTransaction();
            resp.setSuccessMessage("Employee Added");
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add employee - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }
}
