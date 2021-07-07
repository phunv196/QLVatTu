package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.DeliveryBillFlowModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.List;

public class DeliveryBillFlowDao extends BaseHibernateDAO {


    public  DeliveryBillFlowModel getById( Long deliveryBillFlowId){
        String hql = "from DeliveryBillFlowModel where deliveryBillFlowId = :deliveryBillFlowId";
        Query q = createQuery(hql);
        q.setParameter("deliveryBillFlowId", deliveryBillFlowId);
        return  (DeliveryBillFlowModel) q.uniqueResult();
    }

    public  int delete(Long deliveryBillFlowId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete DeliveryBillFlowModel where deliveryBillFlowId = :deliveryBillFlowId");
        q.setParameter("deliveryBillFlowId", deliveryBillFlowId);
        return q.executeUpdate();
    }

    public  void deleteByDeliveryBillId(Long deliveryBillId)  throws HibernateException, ConstraintViolationException {
        SQLQuery q = createSQLQuery("delete from delivery_bill_flow where delivery_bill_id = :deliveryBillId");
        q.setParameter("deliveryBillId", deliveryBillId);
        q.executeUpdate();
    }

    public  BigInteger getDeliveryBillFlowCount(Long deliveryBillId) {
        String sql = createSqlWhereString(deliveryBillId);
        String countSql = "select count(*) " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (deliveryBillId >0)   { q.setParameter("deliveryBillId", deliveryBillId); }
        return (BigInteger)q.uniqueResult();
    }

    private  String createSqlWhereString(Long deliveryBillId){
        String sqlWhere = " where  1 = 1 ";
        if (deliveryBillId > 0)   { sqlWhere = sqlWhere + " and delivery_bill_id = :deliveryBillId "; }
        return " from delivery_bill_flow " + sqlWhere;
    }

    public  List<DeliveryBillFlowModel> getList(int from, int limit, Long deliveryBillId)  throws HibernateException, ConstraintViolationException {

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
        String finalSql = "select dbf.delivery_bill_flow_id deliveryBillFlowId," +
                " dbf.amount amount," +
                " dbf.delivery_bill_id deliveryBillId," +
                " s.supplies_id suppliesId," +
                " s.code suppliesCode," +
                " s.name suppliesName," +
                " s.price suppliesPrice," +
                " s.unit suppliesUnit," +
                " (s.price * dbf.amount) calculatePrice," +
                " sp.name speciesName" +
                " from delivery_bill_flow dbf " +
                " left join supplies s on s.supplies_id = dbf.supplies_id" +
                " left join species sp on sp.species_id = s.species_id" +
                " where dbf.delivery_bill_id = :deliveryBillId";
        finalSql = finalSql + " order by dbf.delivery_bill_flow_id " + sqlLimit;
        SQLQuery q = createSQLQuery(finalSql);
        q.setParameter("deliveryBillId", deliveryBillId);
        q.setResultTransformer(Transformers.aliasToBean(DeliveryBillFlowModel.class));
        setResultTransformer(q, DeliveryBillFlowModel.class);
        return q.list();
    }
}
