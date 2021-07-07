package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.ReceiptModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.List;

public class ReceiptDao extends BaseHibernateDAO {

    public Long getSequence() throws Exception {
        Long id = getAutoIncrement("receipt");
        return id == null ? 0 : id;
    }

    public  ReceiptModel getById(Long receiptId){
        String hql = "from ReceiptModel where receiptId = :receiptId";
        Query q = createQuery(hql);
        q.setParameter("receiptId", receiptId);
        return  (ReceiptModel) q.uniqueResult();
    }

    public  int delete(Long receiptId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete ReceiptModel where receiptId = :receiptId");
        q.setParameter("receiptId", receiptId);
        return q.executeUpdate();
    }

    public  List<ReceiptModel> getList(int from, int limit, Long receiptId) {
        String sql = createSqlWhereString(receiptId);
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
        String finalSql = "select r.receipt_id receiptId," +
                " r.code code," +
                " r.name name," +
                " r.date_warehousing dateWarehousing," +
                " r.description description," +
                " w.warehouse_id warehouseId," +
                " w.code warehouseCode," +
                " (select sum(rf.amount * s.price) " +
                " from receipt_flow rf" +
                " join supplies s on rf.supplies_id = s.supplies_id " +
                " where rf.receipt_id = r.receipt_id ) sumMoney " +
                " from receipt r " +
                " left join warehouse w on w.warehouse_id = r.warehouse_id";
        finalSql = finalSql + " order by r.receipt_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (receiptId >0)   { q.setParameter("receiptId", receiptId); }
        q.setResultTransformer(Transformers.aliasToBean(ReceiptModel.class));
        setResultTransformer(q, ReceiptModel.class);

        return q.list();
    }

    private  String createSqlWhereString(Long receiptId){
        String sqlWhere = " where  1 = 1 ";
        if (receiptId > 0)   { sqlWhere = sqlWhere + " and receipt_id = :receiptId "; }
        return " from receipt " + sqlWhere;
    }

    public  BigInteger getReceiptCount(Long receiptId) {
        String sql = createSqlWhereString(receiptId);
        String countSql = "select count(*) " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (receiptId >0)   { q.setParameter("receiptId", receiptId); }
        return (BigInteger)q.uniqueResult();
    }

    public List<Integer> getListBySuppliersId(Integer suppliesId) {
        String finalSql = "select r.receipt_id receiptId" +
                " from receipt r " +
                " left join receipt_flow rf on rf.receipt_id = r.receipt_id" +
                " where rf.supplies_id = :suppliesId";
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
        if (arrReceipt.size() >0)   { q.setParameterList("arrReceipt", arrReceipt); }

        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(ReceiptModel.class));
        setResultTransformer(q, ReceiptModel.class);
        return q.list();
    }
}
