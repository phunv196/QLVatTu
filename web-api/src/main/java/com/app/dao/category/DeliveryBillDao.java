package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.DeliveryBillModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class DeliveryBillDao extends BaseHibernateDAO {

    public Long getSequence() throws Exception {
        Long id = getAutoIncrement("delivery_bill");
        return id == null ? 1 : id;
    }

    public DeliveryBillModel getById(Long deliveryBillId){
        String hql = "from DeliveryBillModel where deliveryBillId = :deliveryBillId";
        Query q = createQuery(hql);
        q.setParameter("deliveryBillId", deliveryBillId);
        return  (DeliveryBillModel) q.uniqueResult();
    }

    public int delete(Long deliveryBillId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete DeliveryBillModel where deliveryBillId = :deliveryBillId");
        q.setParameter("deliveryBillId", deliveryBillId);
        return q.executeUpdate();
    }

    public List<DeliveryBillModel> getList(int from, int limit, Long deliveryBillId, String name,
                                           String code, Date formDate, Date toDate) {
        String sql = createSqlWhereString(deliveryBillId, name, code, formDate, toDate);
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
        String finalSql = "select db.delivery_bill_id deliveryBillId," +
                " db.code code," +
                " db.name name," +
                " db.date_delivery_bill dateDeliveryBill," +
                " db.description description," +
                " w.warehouse_id warehouseId," +
                " w.code warehouseCode," +
                " f.factory_id factoryId, " +
                " f.code factoryCode, " +
                " (select sum(d.amount * s.price) " +
                " from delivery_bill_flow d" +
                " join supplies s on d.supplies_id = s.supplies_id " +
                " where d.delivery_bill_id = db.delivery_bill_id ) sumMoney " +
                " from delivery_bill db " +
                " left join factory f on f.factory_id = db.factory_id" +
                " left join warehouse w on w.warehouse_id = db.warehouse_id";
        finalSql = finalSql + sql + " order by db.delivery_bill_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (deliveryBillId >0)   { q.setParameter("deliveryBillId", deliveryBillId); }
        if (name != null)    { q.setParameter("name", "%" + name.toLowerCase() + "%"); }
        if (code != null)   { q.setParameter("code", "%" + code.toLowerCase() + "%"); }
        if (formDate != null)   { q.setParameter("formDate", formDate ); }
        if (toDate != null)   { q.setParameter("toDate", toDate ); }
        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(DeliveryBillModel.class));
        setResultTransformer(q, DeliveryBillModel.class);

        return q.list();
    }


    private static String createSqlWhereString(Long deliveryBillId, String name, String code,
                                               Date formDate, Date toDate){
        String sqlWhere = " where  1 = 1 ";

        if (deliveryBillId > 0)   { sqlWhere = sqlWhere + " and db.delivery_bill_id = :deliveryBillId "; }
        if (name != null)   { sqlWhere = sqlWhere + " and LOWER(db.name) LIKE :name "; }
        if (code != null)   { sqlWhere = sqlWhere + " and LOWER(db.code) LIKE :code "; }
        if (formDate != null)   { sqlWhere = sqlWhere + " and db.date_delivery_bill >= :formDate "; }
        if (toDate != null)   { sqlWhere = sqlWhere + " and db.date_delivery_bill <= :toDate "; }
        return  sqlWhere;
    }

    public BigInteger getDeliveryBillCount(Long deliveryBillId, String name, String code,
                                            Date formDate, Date toDate) {
        String sql = createSqlWhereString(deliveryBillId, name, code, formDate, toDate);
        String countSql = "select count(*)  from delivery_bill db" + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (deliveryBillId >0)   { q.setParameter("deliveryBillId", deliveryBillId); }
        if (name != null)    { q.setParameter("name", "% " + name + "%"); }
        if (code != null)   { q.setParameter("code", "% " + code + " %"); }
        if (formDate != null)   { q.setParameter("formDate", formDate); }
        if (toDate != null)   { q.setParameter("toDate", toDate); }
        return (BigInteger)q.uniqueResult();
    }

    public List<Integer> getListBySuppliersId(Integer suppliesId) {
        String finalSql = "select db.delivery_bill_id deliveryBillId" +
                " from delivery_bill db " +
                " left join delivery_bill_flow d on d.delivery_bill_id = db.delivery_bill_id" +
                " where d.supplies_id = :suppliesId";
        SQLQuery q = createSQLQuery(finalSql);
        q.setParameter("suppliesId", suppliesId);
        return (List<Integer>) q.list();
    }

    public List<DeliveryBillModel> getListBySupplies(List<Integer> arrDeliveryBill) {
        String finalSql = "select db.delivery_bill_id deliveryBillId, " +
                " db.code code" +
                " from delivery_bill db " +
                " where db.delivery_bill_id in (:arrDeliveryBill)";
        SQLQuery q = createSQLQuery(finalSql);
        if (arrDeliveryBill.size() >0)   { q.setParameterList("arrDeliveryBill", arrDeliveryBill); }

        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(DeliveryBillModel.class));
        setResultTransformer(q, DeliveryBillModel.class);
        return q.list();
    }
}
