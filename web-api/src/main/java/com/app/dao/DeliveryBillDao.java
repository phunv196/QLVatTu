package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.dao.base.CommonUtils;
import com.app.model.delivery.DeliveryBillModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DeliveryBillDao extends BaseHibernateDAO {

    public Long getSequence() throws Exception {
        Long id = getAutoIncrement("delivery_bill");
        return id == null ? 0 : id;
    }

    public DeliveryBillModel getById(Long deliveryBillId) {
        String hql = "from DeliveryBillModel where deliveryBillId = :deliveryBillId";
        Query q = createQuery(hql);
        q.setParameter("deliveryBillId", deliveryBillId);
        return (DeliveryBillModel) q.uniqueResult();
    }

    public int delete(Long deliveryBillId) throws HibernateException, ConstraintViolationException {
        Query query = createQuery("delete DeliveryBillFlowModel where deliveryBillId = :deliveryBillId");
        query.setParameter("deliveryBillId", deliveryBillId);
        query.executeUpdate();
        Query q = createQuery("delete DeliveryBillModel where deliveryBillId = :deliveryBillId");
        q.setParameter("deliveryBillId", deliveryBillId);
        return q.executeUpdate();
    }

    public List<DeliveryBillModel> getList(int from, int limit, Long deliveryBillId, String searchCode, String searchName,
                                           Long searchEmployee,
                                           Long searchWarehouse,
                                           String searchFormDate,
                                           String searchToDate,
                                           Long searchFactory) {
        String sql = createSqlWhereString(deliveryBillId, searchCode,
                searchName,
                searchEmployee,
                searchWarehouse,
                searchFormDate,
                searchToDate,
                searchFactory);
        String sqlLimit = "";
        if (limit <= 0 || limit > 1000) {
            sqlLimit = " limit 1000 ";
        } else {
            sqlLimit = " limit " + limit;
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
                " e.full_name fullName, " +
                " (select sum(d.amount * s.price) " +
                " from delivery_bill_flow d" +
                " join supplies s on d.supplies_id = s.supplies_id " +
                " where d.delivery_bill_id = db.delivery_bill_id ) sumMoney " +
                " from delivery_bill db " +
                " left join factory f on f.factory_id = db.factory_id" +
                " left join employees e on e.employee_id = db.employee_id" +
                " left join warehouse w on w.warehouse_id = db.warehouse_id";
        finalSql = finalSql + sql + " order by db.delivery_bill_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (deliveryBillId > 0) {
            q.setParameter("deliveryBillId", deliveryBillId);
        }
        if (searchCode != null)   { q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%"); }
        if (searchName != null)    { q.setParameter("searchName", "%" + searchName.toLowerCase() + "%"); }
        if (searchEmployee > 0)   { q.setParameter("searchEmployee", searchEmployee ); }
        if (searchWarehouse > 0)   { q.setParameter("searchWarehouse", searchWarehouse ); }
        if (searchFormDate != null && !searchFormDate.isEmpty())   { q.setParameter("searchFormDate", CommonUtils.convertStringToSQLDate(searchFormDate) ); }
        if (searchToDate != null && !searchToDate.isEmpty())   { q.setParameter("searchToDate", CommonUtils.convertStringToSQLDate(searchToDate) ); }
        if (searchFactory > 0)   { q.setParameter("searchFactory", searchFactory ); }

        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(DeliveryBillModel.class));
        setResultTransformer(q, DeliveryBillModel.class);

        return q.list();
    }


    private static String createSqlWhereString(Long deliveryBillId, String searchCode, String searchName,
                                               Long searchEmployee,
                                               Long searchWarehouse,
                                               String searchFormDate,
                                               String searchToDate,
                                               Long searchFactory) {
        String sqlWhere = " where  1 = 1 ";

        if (deliveryBillId > 0) {
            sqlWhere = sqlWhere + " and db.delivery_bill_id = :deliveryBillId ";
        }
        if (searchCode != null)   { sqlWhere = sqlWhere + " and LOWER(db.code) LIKE :searchCode "; }
        if (searchName != null)   { sqlWhere = sqlWhere + " and LOWER(db.name) LIKE :searchName "; }
        if (searchEmployee > 0)   { sqlWhere = sqlWhere + " and db.employee_id = :searchEmployee "; }
        if (searchWarehouse > 0)   { sqlWhere = sqlWhere + " and db.warehouse_id = :searchWarehouse "; }
        if (searchFormDate != null && !searchFormDate.isEmpty())   { sqlWhere = sqlWhere + " and db.date_delivery_bill >= :searchFormDate "; }
        if (searchToDate != null && !searchToDate.isEmpty() )   { sqlWhere = sqlWhere + " and db.date_delivery_bill <= :searchToDate "; }
        if (searchFactory > 0)   { sqlWhere = sqlWhere + " and db.factory_id = :searchFactory "; }
        return sqlWhere;
    }

    public BigInteger getDeliveryBillCount(Long deliveryBillId, String searchCode, String searchName,
                                           Long searchEmployee,
                                           Long searchWarehouse,
                                           String searchFormDate,
                                           String searchToDate,
                                           Long searchFactory) {
        String sql = createSqlWhereString(deliveryBillId, searchCode,
                searchName,
                searchEmployee,
                searchWarehouse,
                searchFormDate,
                searchToDate,
                searchFactory);
        String countSql = "select count(*)  from delivery_bill db" + sql;
        SQLQuery q = createSQLQuery(countSql);
        if (deliveryBillId > 0) {
            q.setParameter("deliveryBillId", deliveryBillId);
        }
        if (searchCode != null)   { q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%"); }
        if (searchName != null)    { q.setParameter("searchName", "%" + searchName.toLowerCase() + "%"); }
        if (searchEmployee > 0)   { q.setParameter("searchEmployee", searchEmployee ); }
        if (searchWarehouse > 0)   { q.setParameter("searchWarehouse", searchWarehouse ); }
        if (searchFormDate != null && !searchFormDate.isEmpty())   { q.setParameter("searchFormDate", CommonUtils.convertStringToSQLDate(searchFormDate) ); }
        if (searchToDate != null && !searchToDate.isEmpty())   { q.setParameter("searchToDate", CommonUtils.convertStringToSQLDate(searchToDate) ); }
        if (searchFactory > 0)   { q.setParameter("searchFactory", searchFactory ); }
        return (BigInteger) q.uniqueResult();
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
        if (arrDeliveryBill.size() > 0) {
            q.setParameterList("arrDeliveryBill", arrDeliveryBill);
        }

        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(DeliveryBillModel.class));
        setResultTransformer(q, DeliveryBillModel.class);
        return q.list();
    }

    public List<DeliveryBillModel> getListExport(String code, String name, Long employeeId,
                             Long warehouseId, String formDate, String toDate, Long factoryId) throws Exception{
        StringBuilder querySelect = new StringBuilder("select db.delivery_bill_id deliveryBillId," +
                " db.code code," +
                " db.name name," +
                " db.date_delivery_bill dateDeliveryBill," +
                " db.description description," +
                " db.warehouse_id warehouseId," +
                " w.code warehouseCode," +
                " w.name warehouseName," +
                " db.factory_id factoryId, " +
                " f.code factoryCode, " +
                " f.name factoryName, " +
                " db.employee_id employeeId," +
                " e.full_name fullName, " +
                " (select sum(d.amount * s.price) " +
                " from delivery_bill_flow d" +
                " join supplies s on d.supplies_id = s.supplies_id " +
                " where d.delivery_bill_id = db.delivery_bill_id ) sumMoney " +
                " from delivery_bill db " +
                " left join factory f on f.factory_id = db.factory_id" +
                " left join employees e on e.employee_id = db.employee_id" +
                " left join warehouse w on w.warehouse_id = db.warehouse_id");
        List<Object> paramList = new ArrayList<>();
        StringBuilder strCondition = new StringBuilder(" WHERE 1 = 1");
        CommonUtils.filter(code, strCondition, paramList, "db.code");
        CommonUtils.filter(name, strCondition, paramList, "db.name");
        CommonUtils.filter(employeeId, strCondition, paramList, "db.employee_id");
        CommonUtils.filter(warehouseId, strCondition, paramList, "db.warehouse_id");
        CommonUtils.filterBetweenDate(CommonUtils.convertStringToDateOther(formDate),CommonUtils.convertStringToDateOther(toDate)
                , strCondition, paramList, "db.date_delivery_bill","db.date_delivery_bill");
        querySelect.append(strCondition);
        CommonUtils.filter(factoryId, strCondition, paramList, "db.factory_id");
        querySelect.append(" ORDER BY db.delivery_bill_id ");
        SQLQuery q = createSQLQuery(querySelect.toString());
        for (int i = 0; i < paramList.size(); i++) {
            q.setParameter(i, paramList.get(i));
        }
        q.setResultTransformer(Transformers.aliasToBean(DeliveryBillModel.class));
        setResultTransformer(q, DeliveryBillModel.class);
        return q.list();
    }
}
