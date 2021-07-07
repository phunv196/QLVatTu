package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.PositionModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class PositionDao extends BaseHibernateDAO {

//    public  BigDecimal getReferenceCount(Long productId){
//        String sql = "Select sum(cnt) from ("+
//                "   select count(*) as cnt from northwind.cart where product_id = :productId "+
//                "   union "+
//                "   select count(*) as cnt from northwind.order_items where product_id = :productId "+
//                " )";
//
//        Query q = hbrSession.createSQLQuery(sql);
//        q.setParameter("productId", productId);
//        return (BigDecimal) q.uniqueResult();
//    }

    public PositionModel getById(Long positionId){
        String hql = "from PositionModel where positionId = :positionId";
        Query q = createQuery(hql);
        q.setParameter("positionId", positionId);
        return  (PositionModel) q.uniqueResult();
    }

    public  int delete(Long positionId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete PositionModel where positionId = :positionId");
        q.setParameter("positionId", positionId);
        return q.executeUpdate();
    }
}
