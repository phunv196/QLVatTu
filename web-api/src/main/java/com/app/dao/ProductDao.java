package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.product.ProductModel;
import org.hibernate.*;
import jakarta.validation.ConstraintViolationException;
import java.math.BigDecimal;

public class ProductDao extends BaseHibernateDAO {

    public BigDecimal getReferenceCount(Integer productId){
        String sql = "Select sum(cnt) from ("+
                "   select count(*) as cnt from cart where product_id = :productId "+
                "   union "+
                "   select count(*) as cnt from order_items where product_id = :productId "+
                " )";

        Query q = createSQLQuery(sql);
        q.setParameter("productId", productId);
        return (BigDecimal) q.uniqueResult();
    }

    public ProductModel getById(Integer productId){
        String hql = "from ProductModel where id = :productId";
        Query q = createQuery(hql);
        q.setParameter("productId", productId);
        return  (ProductModel)q.uniqueResult();
    }

    public int delete(Integer productId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete ProductModel where id = :productId");
        q.setParameter("productId", productId);
        return q.executeUpdate();
    }
}
