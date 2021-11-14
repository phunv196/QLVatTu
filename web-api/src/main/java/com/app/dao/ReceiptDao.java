package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.dao.base.CommonUtils;
import com.app.model.receipt.ReceiptModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDao extends BaseHibernateDAO {

    public Long getSequence() throws Exception {
        Long id = getAutoIncrement("receipt");
        return id == null ? 0 : id;
    }

    public ReceiptModel getById(Long receiptId) {
        String hql = "from ReceiptModel where receiptId = :receiptId";
        Query q = createQuery(hql);
        q.setParameter("receiptId", receiptId);
        return (ReceiptModel) q.uniqueResult();
    }

    public int delete(Long receiptId) throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete ReceiptModel where receiptId = :receiptId");
        q.setParameter("receiptId", receiptId);
        return q.executeUpdate();
    }

    public List<ReceiptModel> getList(int from, int limit, Long receiptId, String searchCode, String searchName,
                                      Long searchEmployee,
                                      Long searchWarehouse,
                                      String searchFormDate,
                                      String searchToDate) {
        String sql = createSqlWhereString(receiptId, searchCode,
                searchName,
                searchEmployee,
                searchWarehouse,
                searchFormDate,
                searchToDate);
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
        String finalSql = "select r.receipt_id receiptId," +
                " r.code code," +
                " r.name name," +
                " r.date_warehousing dateWarehousing," +
                " r.description description," +
                " w.warehouse_id warehouseId," +
                " w.code warehouseCode," +
                " e.full_name fullName," +
                " (select sum(rf.amount * s.price) " +
                " from receipt_flow rf" +
                " join supplies s on rf.supplies_id = s.supplies_id " +
                " where rf.receipt_id = r.receipt_id ) sumMoney " +
                " from receipt r " +
                " left join warehouse w on w.warehouse_id = r.warehouse_id" +
                " left join employees e on e.employee_id = r.employee_id";
        finalSql = finalSql + sql + " order by r.receipt_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (receiptId > 0) {
            q.setParameter("receiptId", receiptId);
        }
        if (searchCode != null) {
            q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%");
        }
        if (searchName != null) {
            q.setParameter("searchName", "%" + searchName.toLowerCase() + "%");
        }
        if (searchEmployee > 0) {
            q.setParameter("searchEmployee", searchEmployee);
        }
        if (searchWarehouse > 0) {
            q.setParameter("searchWarehouse", searchWarehouse);
        }
        if (searchFormDate != null && !searchFormDate.isEmpty()) {
            q.setParameter("searchFormDate", CommonUtils.convertStringToSQLDate(searchFormDate));
        }
        if (searchToDate != null && !searchToDate.isEmpty()) {
            q.setParameter("searchToDate", CommonUtils.convertStringToSQLDate(searchToDate));
        }
        q.setResultTransformer(Transformers.aliasToBean(ReceiptModel.class));
        setResultTransformer(q, ReceiptModel.class);

        return q.list();
    }

    private String createSqlWhereString(Long receiptId, String searchCode, String searchName,
                                        Long searchEmployee,
                                        Long searchWarehouse,
                                        String searchFormDate,
                                        String searchToDate) {
        String sqlWhere = " where  1 = 1 ";
        if (receiptId > 0) {
            sqlWhere = sqlWhere + " and r.receipt_id = :receiptId ";
        }
        if (searchCode != null) {
            sqlWhere = sqlWhere + " and LOWER(r.code) LIKE :searchCode ";
        }
        if (searchName != null) {
            sqlWhere = sqlWhere + " and LOWER(r.name) LIKE :searchName ";
        }
        if (searchEmployee > 0) {
            sqlWhere = sqlWhere + " and r.employee_id = :searchEmployee ";
        }
        if (searchWarehouse > 0) {
            sqlWhere = sqlWhere + " and r.warehouse_id = :searchWarehouse ";
        }
        if (searchFormDate != null && !searchFormDate.isEmpty()) {
            sqlWhere = sqlWhere + " and r.date_warehousing >= :searchFormDate ";
        }
        if (searchToDate != null && !searchToDate.isEmpty()) {
            sqlWhere = sqlWhere + " and r.date_warehousing <= :searchToDate ";
        }
        return sqlWhere;
    }

    public BigInteger getReceiptCount(Long receiptId, String searchCode, String searchName,
                                      Long searchEmployee,
                                      Long searchWarehouse,
                                      String searchFormDate,
                                      String searchToDate) {
        String sql = createSqlWhereString(receiptId, searchCode,
                searchName,
                searchEmployee,
                searchWarehouse,
                searchFormDate,
                searchToDate);
        String countSql = "select count(*) from receipt r " + sql;
        SQLQuery q = createSQLQuery(countSql);
        if (receiptId > 0) {
            q.setParameter("receiptId", receiptId);
        }
        if (searchCode != null) {
            q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%");
        }
        if (searchName != null) {
            q.setParameter("searchName", "%" + searchName.toLowerCase() + "%");
        }
        if (searchEmployee > 0) {
            q.setParameter("searchEmployee", searchEmployee);
        }
        if (searchWarehouse > 0) {
            q.setParameter("searchWarehouse", searchWarehouse);
        }
        if (searchFormDate != null && !searchFormDate.isEmpty()) {
            q.setParameter("searchFormDate", CommonUtils.convertStringToSQLDate(searchFormDate));
        }
        if (searchToDate != null && !searchToDate.isEmpty()) {
            q.setParameter("searchToDate", CommonUtils.convertStringToSQLDate(searchToDate));
        }
        return (BigInteger) q.uniqueResult();
    }

    public List<Integer> getListBySuppliersId(Integer suppliesId) {
        String finalSql = "select r.receipt_id receiptId" +
                " from receipt r " +
                " left join receipt_flow rf on rf.receipt_id = r.receipt_id" +
                " where rf.supplies_id = :suppliesId" +
                " and (rf.amount - if(rf.received is null, 0, rf.received)) > 0";
        SQLQuery q = createSQLQuery(finalSql);
        q.setParameter("suppliesId", suppliesId);
        return (List<Integer>) q.list();
    }

    public List<ReceiptModel> getListBySupplies(List<Integer> arrReceipt) {
        String finalSql = "select r.receipt_id receiptId, " +
                " r.code code" +
                " from receipt r " +
                " where r.receipt_id in (:arrReceipt)";
        SQLQuery q = createSQLQuery(finalSql);
        if (arrReceipt.size() > 0) {
            q.setParameterList("arrReceipt", arrReceipt);
        }

        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(ReceiptModel.class));
        setResultTransformer(q, ReceiptModel.class);
        return q.list();
    }

    public List<ReceiptModel> getListExport(String code, String name, Long employeeId, Long warehouseId,
                                            String formDate, String toDate) throws Exception {
        StringBuilder querySelect = new StringBuilder("select r.receipt_id receiptId," +
                " r.code code," +
                " r.name name," +
                " r.date_warehousing dateWarehousing," +
                " r.description description," +
                " w.warehouse_id warehouseId," +
                " w.code warehouseCode," +
                " w.name warehouseName," +
                " r.employee_id employeeId," +
                " e.full_name fullName," +
                " (select sum(rf.amount * s.price) " +
                " from receipt_flow rf" +
                " join supplies s on rf.supplies_id = s.supplies_id " +
                " where rf.receipt_id = r.receipt_id ) sumMoney " +
                " from receipt r " +
                " left join warehouse w on w.warehouse_id = r.warehouse_id" +
                " left join employees e on e.employee_id = r.employee_id");
        List<Object> paramList = new ArrayList<>();
        StringBuilder strCondition = new StringBuilder(" WHERE 1 = 1");
        CommonUtils.filter(code, strCondition, paramList, "r.code");
        CommonUtils.filter(name, strCondition, paramList, "r.name");
        CommonUtils.filter(employeeId, strCondition, paramList, "r.employee_id");
        CommonUtils.filter(warehouseId, strCondition, paramList, "r.warehouse_id");
        CommonUtils.filterBetweenDate(CommonUtils.convertStringToDateOther(formDate),CommonUtils.convertStringToDateOther(toDate)
                , strCondition, paramList, "r.date_warehousing","r.date_warehousing");
        querySelect.append(strCondition);
        querySelect.append(" ORDER BY r.receipt_id ");
        SQLQuery q = createSQLQuery(querySelect.toString());
        for (int i = 0; i < paramList.size(); i++) {
            q.setParameter(i, paramList.get(i));
        }
        q.setResultTransformer(Transformers.aliasToBean(ReceiptModel.class));
        setResultTransformer(q, ReceiptModel.class);
        return q.list();
    }
}
