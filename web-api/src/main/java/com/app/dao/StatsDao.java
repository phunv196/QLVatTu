package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.stats.CategoryCountModel;
import com.app.model.stats.DailyOrderCountModel;
import com.app.model.stats.DailySaleModel;
import org.hibernate.*;
import java.math.*;
import java.util.*;

public class StatsDao extends BaseHibernateDAO {
    public List<DailySaleModel> getDailySales() {
        String sql = "select sum( (unit_price * quantity) - discount) as sale_amount, sum(discount) as discount, order_date as date from  ORDER_DETAILS "
            + " where order_date > DATE_ADD(NOW(),INTERVAL -10000 DAY) "
            + " group by DAYOFYEAR (order_date) order by order_date desc limit 100";

        SQLQuery q = createSQLQuery(sql);
        List rowList = q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        List<DailySaleModel> dailySaleList = new ArrayList<>();

        for (Object object : rowList) {
            Map row =  (Map) object;
            DailySaleModel dailyRow = new DailySaleModel((Date)row.get("date"), (BigDecimal)row.get("sale_amount"), (BigDecimal)row.get("discount"));
            dailySaleList.add(dailyRow);
        }
        return dailySaleList;
    }

    public List<DailyOrderCountModel> getDailyOrderCount() {
        String sql = "select count(*) as order_count, order_date as date from  ORDER_DETAILS "
                + " where order_date >  DATE_ADD(NOW(),INTERVAL -10000 DAY) "
                + " group by DAYOFYEAR(order_date) "
                + " order by order_date desc limit 100";

        SQLQuery q = createSQLQuery(sql);
        List rowList = q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        List<DailyOrderCountModel> dailyOrderCount = new ArrayList<>();

        for (Object object : rowList) {
            Map row =  (Map) object;
            DailyOrderCountModel dailyRow = new DailyOrderCountModel((Date)row.get("date"), (BigInteger)row.get("order_count"));
            dailyOrderCount.add(dailyRow);
        }
        return dailyOrderCount;
    }

    public List<CategoryCountModel> getOrdersByPaymentType() {
        String sql = "select count(*) as count, payment_type as category from orders where order_date > DATE_ADD(NOW(),INTERVAL -10000 DAY) group by payment_type" ;

        SQLQuery q = createSQLQuery(sql);
        List rowList = q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        List<CategoryCountModel> categoryCountList = new ArrayList<>();

        for (Object object : rowList) {
            Map row =  (Map) object;
            CategoryCountModel categoryAndCount = new CategoryCountModel((String)row.get("category"), (BigInteger)row.get("count"));
            categoryCountList.add(categoryAndCount);
        }
        return categoryCountList;
    }

    public List<CategoryCountModel> getOrdersByStatus() {
        String sql = "select count(*) as count, order_status as category from orders where order_date > DATE_ADD(NOW(),INTERVAL -10000 DAY) group by order_status" ;

        SQLQuery q = createSQLQuery(sql);
        List rowList = q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        List<CategoryCountModel> categoryCountList = new ArrayList<>();

        for (Object object : rowList) {
            Map row =  (Map) object;
            CategoryCountModel categoryAndCount = new CategoryCountModel((String)row.get("category"), (BigInteger)row.get("count"));
            categoryCountList.add(categoryAndCount);
        }
        return categoryCountList;
    }
}
