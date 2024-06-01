package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.dao.base.CommonUtils;
import com.app.model.warehouseCard.WarehouseCardFlowModel;
import com.app.util.Constants;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class WarehouseCardFlowDao extends BaseHibernateDAO {

    public  WarehouseCardFlowModel getById(Long warehouseCardFlowId){
        String hql = "from WarehouseCardFlowModel where warehouseCardFlowId = :warehouseCardFlowId";
        Query q = createQuery(hql);
        q.setParameter("warehouseCardFlowId", warehouseCardFlowId);
        return  (WarehouseCardFlowModel) q.uniqueResult();
    }

    public  int delete(Long warehouseCardFlowId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete WarehouseCardFlowModel where warehouseCardFlowId = :warehouseCardFlowId");
        q.setParameter("warehouseCardFlowId", warehouseCardFlowId);
        return q.executeUpdate();
    }

    public  void deleteByWarehouseCardId(Long warehouseCardId)  throws HibernateException, ConstraintViolationException {
        SQLQuery q = createSQLQuery("delete from warehouse_card_flow where warehouse_card_id = :warehouseCardId");
        q.setParameter("warehouseCardId", warehouseCardId);
        q.executeUpdate();
    }

    public BigInteger getWarehouseCardFlowCount(Long warehouseCardId, Long suppliesId ) {
        String sql = createSqlWhereString(warehouseCardId, suppliesId);
        String countSql = "select count(*) " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (warehouseCardId >0)   { q.setParameter("warehouseCardId", warehouseCardId); }
        if (suppliesId >0)   { q.setParameter("suppliesId", suppliesId); }
        return (BigInteger)q.uniqueResult();
    }

    private  String createSqlWhereString(Long warehouseCardId, Long suppliesId ){
        String sqlWhere = " where  1 = 1 ";
        if (warehouseCardId > 0)   { sqlWhere = sqlWhere + " and wcf.warehouse_card_id = :warehouseCardId "; }
        if (suppliesId > 0)   { sqlWhere = sqlWhere + " and (dbf.supplies_id = :suppliesId || rf.supplies_id = :suppliesId) "; }
        return  " from warehouse_card_flow wcf " +
                " left join delivery_bill db on db.delivery_bill_id = wcf.delivery_bill_id" +
                " left join delivery_bill_flow dbf on db.delivery_bill_id = dbf.delivery_bill_id" +
                " left join receipt r on r.receipt_id = wcf.receipt_id" +
                " left join receipt_flow rf on r.receipt_id = rf.receipt_id" + sqlWhere;
    }

    public List<WarehouseCardFlowModel> getList(int from, int limit, Long warehouseCardId, Long suppliesId)  throws HibernateException, ConstraintViolationException {

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
        String finalSql = "select" +
                " wcf.warehouse_card_flow_id warehouseCardFlowId," +
                " wcf.type type," +
                " wcf.amount amount," +
                " wcf.warehouse_card_id warehouseCardId," +
                " wcf.description description," +
                " db.delivery_bill_id deliveryBillId," +
                " db.code deliveryBillCode," +
                " r.receipt_id receiptId," +
                " r.code receiptCode," +
                " (IF( ISNULL(db.delivery_bill_id ) , " +
                " (concat(emp.first_name, ' ' , emp.last_name))," +
                " (concat(e.first_name, ' ' , e.last_name)) ))  employeeName" +
                " from warehouse_card_flow wcf " +
                " left join delivery_bill db on db.delivery_bill_id = wcf.delivery_bill_id" +
                " left join delivery_bill_flow dbf on db.delivery_bill_id = dbf.delivery_bill_id" +
                " left join receipt r on r.receipt_id = wcf.receipt_id" +
                " left join receipt_flow rf on r.receipt_id = rf.receipt_id" +
                " left join employees e on e.employee_id = db.employee_id" +
                " left join employees emp on emp.employee_id = r.employee_id" +
                " where wcf.warehouse_card_id = :warehouseCardId" +
                " and (dbf.supplies_id = :suppliesId || rf.supplies_id = :suppliesId)";
        finalSql = finalSql + " order by wcf.warehouse_card_flow_id " + sqlLimit;
        SQLQuery q = createSQLQuery(finalSql);
        q.setParameter("warehouseCardId", warehouseCardId);
        q.setParameter("suppliesId", suppliesId);
        q.setResultTransformer(Transformers.aliasToBean(WarehouseCardFlowModel.class));
        setResultTransformer(q, WarehouseCardFlowModel.class);
        return q.list();
    }

    public List<WarehouseCardFlowModel> getByWarehouseCardId(Long warehouseCardId) {
        String finalSql = "select" +
                " wcf.warehouse_card_flow_id warehouseCardFlowId," +
                " wcf.type type," +
                " wcf.amount amount," +
                " wcf.warehouse_card_id warehouseCardId," +
                " wcf.description description," +
                " wcf.create_at createAt," +
                " db.delivery_bill_id deliveryBillId," +
                " db.code deliveryBillCode," +
                " r.receipt_id receiptId," +
                " r.code receiptCode," +
                " (IF( ISNULL(db.delivery_bill_id ) , " +
                " (concat(emp.first_name, ' ' , emp.last_name))," +
                " (concat(e.first_name, ' ' , e.last_name)) )) employeeName, " +
                " (IF( ISNULL(db.delivery_bill_id ) , " +
                " (r.date_warehousing)," +
                " (db.date_delivery_bill) )) date" +
                " from warehouse_card_flow wcf " +
                " left join delivery_bill db on db.delivery_bill_id = wcf.delivery_bill_id" +
                " left join receipt r on r.receipt_id = wcf.receipt_id" +
                " left join employees e on e.employee_id = db.employee_id" +
                " left join employees emp on emp.employee_id = r.employee_id" +
                " where wcf.warehouse_card_id = :warehouseCardId";
        finalSql = finalSql + " order by wcf.warehouse_card_flow_id ";
        SQLQuery q = createSQLQuery(finalSql);
        q.setParameter("warehouseCardId", warehouseCardId);
        q.setResultTransformer(Transformers.aliasToBean(WarehouseCardFlowModel.class));
        setResultTransformer(q, WarehouseCardFlowModel.class);
        return q.list();
    }

    public WarehouseCardFlowModel getByReceiptId(Long warehouseCardId, Long receiptId) {
        String hql = "from WarehouseCardFlowModel where receiptId = :receiptId and warehouseCardId = :warehouseCardId";
        Query q = createQuery(hql);
        q.setParameter("warehouseCardId", warehouseCardId);
        q.setParameter("receiptId", receiptId);
        return  (WarehouseCardFlowModel) q.uniqueResult();
    }

    public WarehouseCardFlowModel getByDeliveryBillId(Long warehouseCardId, Long deliveryBillId) {
        String hql = "from WarehouseCardFlowModel where deliveryBillId = :deliveryBillId and warehouseCardId = :warehouseCardId";
        Query q = createQuery(hql);
        q.setParameter("warehouseCardId", warehouseCardId);
        q.setParameter("deliveryBillId", deliveryBillId);
        return  (WarehouseCardFlowModel) q.uniqueResult();
    }
//
//    public List<WarehouseCardFlowModel> getByDeliveryBillId(Long warehouseCardId, Long deliveryBillId) {
//        String finalSql = "select" +
//                " wcf.warehouse_card_flow_id warehouseCardFlowId," +
//                " wcf.type type," +
//                " wcf.amount amount," +
//                " wcf.warehouse_card_id warehouseCardId," +
//                " wcf.description description," +
//                " wcf.create_at createAt," +
//                " db.delivery_bill_id deliveryBillId," +
//                " db.code deliveryBillCode," +
//                " r.receipt_id receiptId," +
//                " r.code receiptCode," +
//                " (IF( ISNULL(db.delivery_bill_id ) , " +
//                " (concat(emp.first_name, ' ' , emp.last_name))," +
//                " (concat(e.first_name, ' ' , e.last_name)) )) employeeName, " +
//                " (IF( ISNULL(db.delivery_bill_id ) , " +
//                " (r.date_warehousing)," +
//                " (db.date_delivery_bill) )) date" +
//                " from warehouse_card_flow wcf " +
//                " left join delivery_bill db on db.delivery_bill_id = wcf.delivery_bill_id" +
//                " left join receipt r on r.receipt_id = wcf.receipt_id" +
//                " left join employees e on e.employee_id = db.employee_id" +
//                " left join employees emp on emp.employee_id = r.employee_id" +
//                " where wcf.warehouse_card_id = :warehouseCardId";
//        finalSql = finalSql + " order by wcf.warehouse_card_flow_id ";
//        SQLQuery q = createSQLQuery(finalSql);
//        q.setParameter("warehouseCardId", warehouseCardId);
//        q.setResultTransformer(Transformers.aliasToBean(WarehouseCardFlowModel.class));
//        setResultTransformer(q, WarehouseCardFlowModel.class);
//        return q.list();
//}

//    public WarehouseCardFlowModel getByDeliveryBillId(Long warehouseId, Long suppliesId, Date dateDeliveryBill, Long deliveryBillId) {
//        String finalSql = "select" +
//                " wcf.warehouse_card_flow_id warehouseCardFlowId," +
//                " wcf.warehouse_card_id warehouseCardId," +
//                " wcf.delivery_bill_id deliveryBillId," +
//                " wcf.receipt_id receiptId," +
//                " wcf.amount amount," +
//                " wcf.description description," +
//                " wcf.type type," +
//                " wcf.create_at createAt" +
//                " from warehouse_card_flow wcf " +
//                " join warehouse_card wc on wc.warehouse_card_id = wcf.warehouse_card_id " +
//                " where wc.warehouse_id = :warehouseId " +
//                " and wcf.delivery_bill_id = :deliveryBillId " +
//                " and wc.date_created = :dateDeliveryBill " +
//                " and wc.supplies_id = :suppliesId ";
//        finalSql = finalSql + " order by wcf.warehouse_card_flow_id ";
//        SQLQuery q = createSQLQuery(finalSql);
//        q.setParameter("dateDeliveryBill", CommonUtils.convertDateToString(dateDeliveryBill, Constants.COMMON.SQLDATE_FORMAT));
//        q.setParameter("warehouseId", warehouseId);
//        q.setParameter("suppliesId", suppliesId);
//        q.setParameter("deliveryBillId", deliveryBillId);
//        q.setResultTransformer(Transformers.aliasToBean(WarehouseCardFlowModel.class));
//        setResultTransformer(q, WarehouseCardFlowModel.class);
//        return (WarehouseCardFlowModel) q.uniqueResult();
//    }
    }
