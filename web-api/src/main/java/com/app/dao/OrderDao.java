package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.order.OrderModel;
import com.app.model.order.OrderWithNestedDetailModel;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import jakarta.validation.ConstraintViolationException;
import java.math.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
// import java.util.*;

public class OrderDao extends BaseHibernateDAO {
    public OrderModel getById(Integer orderId) {
        String hql = "from OrderModel where id = :orderId";
        Query q = createQuery(hql);
        q.setParameter("orderId", orderId);
        return  (OrderModel)q.uniqueResult();
    }

    private String createSqlWhereString(Integer orderId, Integer customerId, String paymetType, String orderStatus){
        String sqlWhere = " where  1 = 1 ";

        if (orderId > 0)   { sqlWhere = sqlWhere + " and id = :orderId "; }
        if (customerId >0){ sqlWhere = sqlWhere + " and customer_id = :customerId "; }
        if (StringUtils.isNotBlank(paymetType)) { sqlWhere = sqlWhere + " and payment_type = :paymetType "; }
        if (StringUtils.isNotBlank(orderStatus)){ sqlWhere = sqlWhere + " and order_status = :orderStatus "; }

        return " from orders " + sqlWhere;
    }

    public BigInteger getOrderCount(Integer orderId, Integer customerId, String paymetType, String orderStatus)  throws HibernateException{

        String sqlOrders = createSqlWhereString(orderId, customerId, paymetType, orderStatus);
        String countSql = "select count(*) " + sqlOrders ;
        SQLQuery q = createSQLQuery(countSql);
        if (orderId >0)   { q.setParameter("orderId", orderId); }
        if (customerId >0){ q.setParameter("customerId", customerId); }
        if (StringUtils.isNotBlank(paymetType)) { q.setParameter("paymetType", paymetType);   }
        if (StringUtils.isNotBlank(orderStatus)){ q.setParameter("orderStatus", orderStatus); }

        return (BigInteger)q.uniqueResult();
    }

    public List<OrderWithNestedDetailModel> getWithOrderLines(int from, int limit, Integer orderId, Integer customerId, String paymetType, String orderStatus)  throws HibernateException, ConstraintViolationException {

        String sqlOrders = createSqlWhereString(orderId, customerId, paymetType, orderStatus);
        String sqlLimit = "";
        if ( limit <= 0 || limit > 1000 ){
            sqlLimit = " limit 1000 ";
        } else {
            sqlLimit = " limit " +  limit;
        }
        if (from <= 0) {
            from = 0;
        } else {
            from = (from - 1) * limit;
        }

        sqlLimit = sqlLimit + " offset " + from;
        String finalSql = "select order_id, product_id   , customer_id   , order_date, order_status  , shipped_date    , payment_type, paid_date, "
            + " ship_name            , ship_address1, ship_address2 , ship_city , ship_state    , ship_postal_code, ship_country, "
            + " product_code         , product_name , category      , quantity  , unit_price    , discount        , date_allocated, order_item_status, "
            + " shipping_fee         , customer_name, customer_email, customer_company "
            + " from order_details ";

        finalSql = finalSql + " order by order_id, product_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (orderId >0)   { q.setParameter("orderId", orderId); }
        if (customerId >0){ q.setParameter("customerId", customerId); }
        if (StringUtils.isNotBlank(paymetType)) { q.setParameter("paymetType", paymetType); }
        if (StringUtils.isNotBlank(orderStatus)){ q.setParameter("orderStatus", orderStatus); }

        q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List rowList = q.list();
        long prevOrderId = -1, newOrderId=0;
        BigDecimal orderTotal = new BigDecimal(0);

        List<OrderWithNestedDetailModel> orderDetailList = new ArrayList<>();
        OrderWithNestedDetailModel orderDetail = new OrderWithNestedDetailModel();

        for (Object object : rowList) {
            Map row =  (Map) object;
            newOrderId = (Integer) row.get("order_id");
            if (prevOrderId != newOrderId) {
                if (prevOrderId != -1){
                    orderDetail.setOrderTotal(orderTotal);
                    orderTotal = BigDecimal.ZERO;
                }
                orderDetail = new OrderWithNestedDetailModel(
                    (int) row.get("order_id"),
                    (Date) row.get("order_date"),
                    (String) row.get("order_status"),
                    (Date) row.get("shipped_date"),
                    (String) row.get("ship_name"),
                    (String) row.get("ship_address1"),
                    (String) row.get("ship_address2"),
                    (String) row.get("ship_city"),
                    (String) row.get("ship_state"),
                    (String) row.get("ship_postal_code"),
                    (String) row.get("ship_country"),
                    (BigDecimal) row.get("shipping_fee"),
                    (Integer) row.get("customer_id"),
                    (String) row.get("customer_name"),
                    (String) row.get("customer_email"),
                    (String) row.get("COMPANY"),
                    (String) row.get("payment_type"),
                    (Date) row.get("paid_date")
                );
                orderDetail.addOrderLine(
                    (int) row.get("product_id"),
                    (String) row.get("product_code"),
                    (String) row.get("product_name"),
                    (String) row.get("category"),
                    (BigDecimal) row.get("quantity"),
                    (BigDecimal) row.get("unit_price"),
                    (BigDecimal) row.get("discount"),
                    (Date) row.get("date_allocated"),
                    (String) row.get("order_item_status")
                );
                orderDetailList.add(orderDetail);
                prevOrderId = newOrderId;
                BigDecimal lineTotal = ((BigDecimal)row.get("unit_price")).multiply((BigDecimal)row.get("quantity")).subtract((BigDecimal)row.get("discount"));
                orderTotal = orderTotal.add(lineTotal);
            } else {
                orderDetail.addOrderLine(
                    (int) row.get("product_id"),
                    (String) row.get("product_code"),
                    (String) row.get("category"),
                    (String) row.get("product_name"),
                    (BigDecimal) row.get("quantity"),
                    (BigDecimal) row.get("unit_price"),
                    (BigDecimal) row.get("discount"),
                    (Date) row.get("date_allocated"),
                    (String) row.get("order_item_status")
                );
                BigDecimal lineTotal = ((BigDecimal)row.get("unit_price")).multiply((BigDecimal)row.get("quantity")).subtract((BigDecimal)row.get("discount"));
                orderTotal = orderTotal.add(lineTotal);
            }
        }
        // Set the last ones order total;
        orderDetail.setOrderTotal(orderTotal);
        return orderDetailList;
    }


    public int delete(Integer orderId)  throws HibernateException, ConstraintViolationException {
        String hql1 = "delete OrderItemModel where orderId = :orderId";
        String hql2 = "delete OrderModel where id = :orderId";

        Query q1 = createQuery(hql1).setParameter("orderId", orderId);
        Query q2 = createQuery(hql2).setParameter("orderId", orderId);

        int rowsEffected1 = q1.executeUpdate();
        int rowsEffected2 = q2.executeUpdate();

        return (rowsEffected1+rowsEffected2);
    }

    public int deleteOrderLine(Integer orderId, Integer productId)  throws HibernateException, ConstraintViolationException {
        String hql = "delete OrderItemModel where orderId = :orderId and productId = :productId";
        Query q = createQuery(hql);
        q.setParameter("orderId"  , orderId);
        q.setParameter("productId", productId);

        int rowsEffected = q.executeUpdate();
        return rowsEffected;
    }

    public int deleteOrderLines(Integer orderId, ArrayList<Integer> productIdList)  throws HibernateException, ConstraintViolationException {
        String hql = "delete OrderItemModel where orderId = :orderId AND productId IN (:productIds)";
        Query q = createQuery(hql);
        q.setParameter("orderId"  , orderId);
        q.setParameterList("productIds", productIdList);
        int rowsEffected = q.executeUpdate();
        return rowsEffected;
    }

}
