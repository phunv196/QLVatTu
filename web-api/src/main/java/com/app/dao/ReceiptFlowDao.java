package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.receipt.ReceiptFlowModel;
import com.app.model.unit.UnitModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.List;

public class ReceiptFlowDao extends BaseHibernateDAO {

    public  ReceiptFlowModel getById(Long receiptFlowId){
        String hql = "from ReceiptFlowModel where receiptFlowId = :receiptFlowId";
        Query q = createQuery(hql);
        q.setParameter("receiptFlowId", receiptFlowId);
        return  (ReceiptFlowModel) q.uniqueResult();
    }

    public  int delete(Long receiptFlowId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete ReceiptFlowModel where receiptFlowId = :receiptFlowId");
        q.setParameter("receiptFlowId", receiptFlowId);
        return q.executeUpdate();
    }

    public  void deleteByRreceiptId(Long receiptId)  throws HibernateException, ConstraintViolationException {
        SQLQuery q = createSQLQuery("delete from receipt_flow where receipt_id = :receiptId");
        q.setParameter("receiptId", receiptId);
        q.executeUpdate();
    }

    public  BigInteger getReceiptFlowCount(Long receiptId) {
        String sql = createSqlWhereString(receiptId);
        String countSql = "select count(*) " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (receiptId >0)   { q.setParameter("receiptId", receiptId); }
        return (BigInteger)q.uniqueResult();
    }

    private  String createSqlWhereString(Long receiptId){
        String sqlWhere = " where  1 = 1 ";
        if (receiptId > 0)   { sqlWhere = sqlWhere + " and receipt_id = :receiptId "; }
        return " from receipt_flow " + sqlWhere;
    }

    public  List<ReceiptFlowModel> getList(int from, int limit, Long receiptId)  throws HibernateException, ConstraintViolationException {
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
        String finalSql = "select rf.receipt_flow_id receiptFlowId," +
                " rf.amount amount," +
                " if(rf.received is null, 0, rf.received) received," +
                " (rf.amount - if(rf.received is null, 0, rf.received)) missing," +
                " rf.receipt_id receiptId," +
                " rf.description description," +
                " s.supplier_id supplierId," +
                " sr.code supplierCode," +
                " sr.name supplierName," +
                " s.supplies_id suppliesId," +
                " s.code suppliesCode," +
                " s.name suppliesName," +
                " s.price suppliesPrice," +
                " u.name suppliesUnit," +
                " (s.price * rf.amount) calculatePrice," +
                " sp.name speciesName" +
                " from receipt_flow rf " +
                " left join supplies s on s.supplies_id = rf.supplies_id" +
                " left join supplier sr on sr.supplier_id = s.supplier_id" +
                " left join unit u on u.unit_id = s.unit_id" +
                " left join species sp on sp.species_id = s.species_id" +
                " where rf.receipt_id = :receiptId";
        finalSql = finalSql +" order by rf.receipt_flow_id "+ sqlLimit;
        SQLQuery q = createSQLQuery(finalSql);
        q.setParameter("receiptId", receiptId);
        q.setResultTransformer(Transformers.aliasToBean(ReceiptFlowModel.class));
        setResultTransformer(q, ReceiptFlowModel.class);
        return q.list();
    }

    public List<ReceiptFlowModel> getByReceiptId(Long receiptId) {
        String finalSql = "select rf.receipt_flow_id receiptFlowId," +
                " rf.amount amount," +
                " rf.receipt_id receiptId," +
                " rf.description description," +
                " sr.supplier_id supplierId," +
                " sr.code supplierCode," +
                " sr.name supplierName," +
                " s.supplies_id suppliesId," +
                " s.code suppliesCode," +
                " s.name suppliesName," +
                " s.price suppliesPrice," +
                " u.name suppliesUnit," +
                " (s.price * rf.amount) calculatePrice," +
                " rf.received received," +
                " (rf.amount - if(rf.received is null, 0, rf.received)) missing," +
                " sp.name speciesName" +
                " from receipt_flow rf " +
                " left join supplies s on s.supplies_id = rf.supplies_id" +
                " left join supplier sr on sr.supplier_id = s.supplier_id" +
                " left join unit u on u.unit_id = s.unit_id" +
                " left join species sp on sp.species_id = s.species_id" +
                " where rf.receipt_id = :receiptId";
        SQLQuery q = createSQLQuery(finalSql);
        q.setParameter("receiptId", receiptId);
        q.setResultTransformer(Transformers.aliasToBean(ReceiptFlowModel.class));
        setResultTransformer(q, ReceiptFlowModel.class);
        return q.list();
    }

    public ReceiptFlowModel getModel(Long receiptId, Long suppliesId) {
        String hql = "from ReceiptFlowModel where receiptId = :receiptId and suppliesId = :suppliesId";
        Query q = createQuery(hql);
        q.setParameter("receiptId", receiptId);
        q.setParameter("suppliesId", suppliesId);
        return  (ReceiptFlowModel) q.uniqueResult();
    }
}
