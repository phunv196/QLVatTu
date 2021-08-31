package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.dao.base.CommonUtils;
import com.app.model.stats.*;
import org.hibernate.*;
import org.hibernate.transform.Transformers;

import java.util.*;

public class StatsDao extends BaseHibernateDAO {
    public List<QuarterModel> getQuarterReceipt() {
        String finalSql =
                " select QUARTER(date_warehousing) as quarter, " +
                " (select sum(rf.amount * s.price) " +
                " from receipt_flow rf" +
                " join supplies s on rf.supplies_id = s.supplies_id " +
                " where rf.receipt_id = r.receipt_id ) as sumAmount " +
                " from receipt r " +
                " WHERE r.date_warehousing " +
                        "BETWEEN CONCAT(YEAR(NOW()),'-01-01') " +
                        "AND CONCAT(YEAR(NOW()),'-12-31') " +
                " GROUP BY QUARTER(date_warehousing)";
        SQLQuery q = createSQLQuery(finalSql);
        q.setResultTransformer(Transformers.aliasToBean(QuarterModel.class));
        setResultTransformer(q, QuarterModel.class);
        return q.list();
    }

    public List<QuarterModel> getQuarterDeliveryBill() {
        String finalSql =
                " select QUARTER(db.date_delivery_bill) as quarter, " +
                " (select sum(d.amount * s.price) " +
                " from delivery_bill_flow d" +
                " join supplies s on d.supplies_id = s.supplies_id " +
                " where d.delivery_bill_id = db.delivery_bill_id ) as sumAmount " +
                " from delivery_bill db " +
                " WHERE db.date_delivery_bill " +
                        "BETWEEN CONCAT(YEAR(NOW()),'-01-01') " +
                        "AND CONCAT(YEAR(NOW()),'-12-31') " +
                " GROUP BY QUARTER(db.date_delivery_bill)";
        SQLQuery q = createSQLQuery(finalSql);
        q.setResultTransformer(Transformers.aliasToBean(QuarterModel.class));
        setResultTransformer(q, QuarterModel.class);
        return q.list();
    }

    public List<SuppliesStatsModel> getSuppliesStats() {
        String finalSql =
                " SELECT ss.name suppliesName, " +
                " (SELECT sum(rf.amount * s.price)" +
                " FROM receipt_flow rf" +
                " JOIN supplies s ON rf.supplies_id = s.supplies_id " +
                " JOIN receipt r ON r.receipt_id = rf.receipt_id " +
                " WHERE ss.supplies_id = rf.supplies_id" +
                " and r.date_warehousing " +
                " BETWEEN CONCAT(YEAR(NOW()),'-01-01') " +
                " AND CONCAT(YEAR(NOW()),'-12-31')) sumReceipt ," +
                " (SELECT sum(db.amount * s.price)" +
                " FROM delivery_bill_flow db" +
                " JOIN supplies s ON db.supplies_id = s.supplies_id " +
                " JOIN delivery_bill d ON d.delivery_bill_id = db.delivery_bill_id " +
                " WHERE ss.supplies_id = db.supplies_id" +
                " and d.date_delivery_bill " +
                " BETWEEN CONCAT(YEAR(NOW()),'-01-01') " +
                " AND CONCAT(YEAR(NOW()),'-12-31')) sumDeliveryBill " +
                " FROM supplies ss" +
                " ORDER BY sumReceipt - sumDeliveryBill DESC " +
                " limit 10";
        SQLQuery q = createSQLQuery(finalSql);
        q.setResultTransformer(Transformers.aliasToBean(SuppliesStatsModel.class));
        setResultTransformer(q, SuppliesStatsModel.class);
        return q.list();
    }

    public Long getDeliveryBill(Date startDate, Date endDate) {
        List<Object> paramList = new ArrayList<>();
        StringBuilder querySelect = new StringBuilder(
                " select sum(d.amount) " +
                " from delivery_bill_flow d, delivery_bill db" );
        StringBuilder strCondition = new StringBuilder(" WHERE d.delivery_bill_id = db.delivery_bill_id");
        CommonUtils.filterBetweenDate(startDate,endDate
                , strCondition, paramList, "db.date_delivery_bill","db.date_delivery_bill");
        querySelect.append(strCondition);
        SQLQuery q = createSQLQuery(querySelect.toString());
        for (int i = 0; i < paramList.size(); i++) {
            q.setParameter(i, paramList.get(i));
        }
        return q.uniqueResult() == null ? null : Long.parseLong(q.uniqueResult().toString());
    }

    public Long getSumReceipt(Date startDate, Date endDate) {
        List<Object> paramList = new ArrayList<>();
        StringBuilder querySelect = new StringBuilder(
                " select sum(rf.amount) " +
                " from receipt_flow rf, receipt r");
        StringBuilder strCondition = new StringBuilder(" WHERE rf.receipt_id = r.receipt_id");
        CommonUtils.filterBetweenDate(startDate,endDate
                , strCondition, paramList, "r.date_warehousing","r.date_warehousing");
        querySelect.append(strCondition);
        SQLQuery q = createSQLQuery(querySelect.toString());
        for (int i = 0; i < paramList.size(); i++) {
            q.setParameter(i, paramList.get(i));
        }
        return q.uniqueResult() == null ? null : Long.parseLong(q.uniqueResult().toString());
    }
}
