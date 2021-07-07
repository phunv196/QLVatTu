package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.WarehouseCardModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.List;

public class WarehouseCardDao extends BaseHibernateDAO {

    public  Long getSequence() throws Exception {
        Long id = getAutoIncrement("warehouse_card");
        return id == null ? 0 : id;
    }


    public  WarehouseCardModel getById(Long warehouseCardId){
        String hql = "from WarehouseCardModel where warehouseCardId = :warehouseCardId";
        Query q = createQuery(hql);
        q.setParameter("warehouseCardId", warehouseCardId);
        return  (WarehouseCardModel) q.uniqueResult();
    }

    public  int delete(Long warehouseCardId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete WarehouseCardModel where warehouseCardId = :warehouseCardId");
        q.setParameter("warehouseCardId", warehouseCardId);
        return q.executeUpdate();
    }

    public List<WarehouseCardModel> getList(int from, int limit, Long warehouseCardId) {
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
        String finalSql = "select wc.warehouse_card_id warehouseCardId," +
                " wc.code code," +
                " wc.name name," +
                " wc.description description," +
                " wc.date_created dateCreated," +
                " e.id employeeId," +
                " concat(e.first_name, ' ' , e.last_name) employeeName," +
                " s.supplies_id suppliesId," +
                " s.code suppliesCode," +
                " s.name suppliesName," +
                " s.price suppliesPrice," +
                " w.warehouse_id warehouseId," +
                " w.code warehouseCode," +
                " (select COUNT(*) from warehouse_card_flow wcf where wcf.delivery_bill_id IS NOT NULL " +
                " and wcf.warehouse_card_id = wc.warehouse_card_id ) countDeliveryBill," +
                " (select COUNT(*) from warehouse_card_flow wcf where wcf.receipt_id IS NOT NULL " +
                " and wcf.warehouse_card_id = wc.warehouse_card_id ) countReceipt," +
                " (select sum(amount) from warehouse_card_flow wcf where wcf.delivery_bill_id IS NOT NULL " +
                " and wcf.warehouse_card_id = wc.warehouse_card_id ) amountDeliveryBill," +
                " (select sum(amount) from warehouse_card_flow wcf where wcf.receipt_id IS NOT NULL " +
                " and wcf.warehouse_card_id = wc.warehouse_card_id ) amountReceipt" +
                " from warehouse_card wc " +
                " left join supplies s on s.supplies_id = wc.supplies_id" +
                " left join employees e on e.id = wc.employee_id" +
                " left join warehouse w on w.warehouse_id = wc.warehouse_id";
        finalSql = finalSql + " order by wc.warehouse_card_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        q.setResultTransformer(Transformers.aliasToBean(WarehouseCardModel.class));
        setResultTransformer(q, WarehouseCardModel.class);

        return q.list();
    }

    private static String createSqlWhereString(Long warehouseCardId){
        String sqlWhere = " where  1 = 1 ";

        if (warehouseCardId > 0)   { sqlWhere = sqlWhere + " and warehouse_card_id = :warehouseCardId "; }

        return " from warehouse_card " + sqlWhere;
    }

    public BigInteger getWarehouseCardCount(Long warehouseCardId) {
        String sql = createSqlWhereString(warehouseCardId);
        String countSql = "select count(*) " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (warehouseCardId >0)   { q.setParameter("warehouseCardId", warehouseCardId); }
        return (BigInteger)q.uniqueResult();
    }

}
