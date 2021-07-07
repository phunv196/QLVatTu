package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.FactoryModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.*;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.List;

public class FactoryDao extends BaseHibernateDAO {

//    public  BigDecimal getReferenceCount(Long productId){
//        String sql = "Select sum(cnt) from ("+
//                "   select count(*) as cnt from northwind.cart where product_id = :productId "+
//                "   union "+
//                "   select count(*) as cnt from northwind.order_items where product_id = :productId "+
//                " )";
//
//        Query q = createSQLQuery(sql);
//        q.setParameter("productId", productId);
//        return (BigDecimal) q.uniqueResult();
//    }

    public FactoryModel getById( Long factoryId){
        String hql = "from FactoryModel where factoryId = :factoryId";
        Query q = createQuery(hql);
        q.setParameter("factoryId", factoryId);
        return  (FactoryModel) q.uniqueResult();
    }

    public int delete(Long factoryId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete FactoryModel where factoryId = :factoryId");
        q.setParameter("factoryId", factoryId);
        return q.executeUpdate();
    }

    public List<FactoryModel> getList(int from, int limit, Long factoryId)  throws HibernateException, ConstraintViolationException {

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
        String finalSql = "select f.factory_id factoryId," +
                " f.code code," +
                " f.name name," +
                " f.email email," +
                " f.address address," +
                " f.description description, " +
                " concat(e.first_name, ' ' , e.last_name) employeeName," +
                " f.employee_id employeeId," +
                " f.date_construction dateConstruction," +
                " f.date_finish dateFinish" +
                " from factory f " +
                " left join employees e on e.id = f.employee_id";
        //String finalSql = "select factory_id from factory";
        finalSql = finalSql + " order by f.factory_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (factoryId >0)   { q.setParameter("factoryId", factoryId); }

        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(FactoryModel.class));
        setResultTransformer(q, FactoryModel.class);

        return q.list();
    }


    public BigInteger getFactoryCount(Long factoryId) {
        String sql = createSqlWhereString(factoryId);
        String countSql = "select count(*) " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (factoryId >0)   { q.setParameter("factoryId", factoryId); }
        return (BigInteger)q.uniqueResult();
    }

    private String createSqlWhereString(Long factoryId){
        String sqlWhere = " where  1 = 1 ";

        if (factoryId > 0)   { sqlWhere = sqlWhere + " and factoryId = :factoryId "; }

        return " from factory " + sqlWhere;
    }


}
