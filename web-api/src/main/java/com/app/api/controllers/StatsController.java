package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.StatsDao;
import com.app.dao.base.CommonUtils;
import com.app.model.stats.*;
import com.app.model.stats.DailyOrderCountModel.DailyOrderCountResponse;
import com.app.model.stats.DailySaleModel.DailySaleResponse;
import com.app.model.stats.QuarterModel.QuarterResponse;
import com.app.model.stats.MonthStatsModel.MonthStatsResponse;
import com.app.model.stats.SuppliesStatsModel.SuppliesStatsResponse;
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
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("stats")
@Tag(name = "Statistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatsController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(StatsController.class);
    StatsDao statsDao = new StatsDao();

    @GET
    @Path("supplies-stats")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Get Sales by date",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SuppliesStatsResponse.class)))}
    )
    public Response getSuppliesStats() {
        SuppliesStatsResponse resp = new SuppliesStatsResponse();

        try {
            List<SuppliesStatsModel> suppliesStats = statsDao.getSuppliesStats();
            statsDao.commitTransaction();
            resp.setSuccessMessage("Supplies Stats");
            resp.setList(suppliesStats);
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Internal Error - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("month-stats")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Get Daily order count",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = DailyOrderCountResponse.class)))}
    )
    public Response getMonthStats() {
        MonthStatsResponse resp = new MonthStatsResponse();
        List<MonthStatsModel> models = new ArrayList<>();
        int year = Year.now().getValue();
        try {
            for( int i = 1; i < 13 ; i++) {
                Date startDate = CommonUtils.getFirstDayOfMonth(i , year);
                Date endDate = CommonUtils.getLastDayOfMonth(i , year);
                MonthStatsModel model = new MonthStatsModel();
                model.setMonth("Tháng " + i);
                Long sumReceipt = statsDao.getSumReceipt(startDate, endDate);
                model.setSumReceipt(sumReceipt == null ? 0L : sumReceipt);
                Long sumDeliveryBill = statsDao.getDeliveryBill(startDate, endDate);
                model.setSumDeliveryBill(sumDeliveryBill == null ? 0L : sumDeliveryBill);
                model.setSumInventory((sumReceipt == null ? 0L : sumReceipt) - (sumDeliveryBill == null ? 0L : sumDeliveryBill));
                models.add(model);
            }
            resp.setSuccessMessage("Month Stats");
            resp.setList(models);
            return Response.ok(resp).build();
        } catch (Exception e) {
            resp.setErrorMessage("Internal Error - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("{stats: by-receipt|by-delivery-bill}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Get stats",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = QuarterResponse.class)))}
    )
    public Response getStats(
      @Parameter(schema = @Schema(allowableValues = {"by-receipt","by-delivery-bill"}), example="by-delivery-bill")
      @PathParam("stats") String stats
    ) {
        QuarterResponse resp = new QuarterResponse();
        List<QuarterModel> quarterList;
        try {
            statsDao.beginTransaction();
            if (stats.equals("by-receipt")) {
                quarterList = statsDao.getQuarterReceipt();
            } else {
                quarterList = statsDao.getQuarterDeliveryBill();
            }
            statsDao.commitTransaction();
            resp.setSuccessMessage("Orders by status");
            resp.setList(quarterList);
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Internal Error - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
}
