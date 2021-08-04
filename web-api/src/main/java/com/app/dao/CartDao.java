package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.cart.CartModel;
import com.app.model.cart.CartViewModel;
import org.hibernate.*;
import jakarta.validation.ConstraintViolationException;
import java.util.List;

public class CartDao extends BaseHibernateDAO {
    public CartModel getProductsInCart( String loginName, Integer productId) throws HibernateException, ConstraintViolationException {
        Query q = createQuery( "From CartModel where loginName = :loginName and productId = :productId");
        q.setParameter("loginName", loginName);
        q.setParameter("productId", productId);
        return (CartModel)q.uniqueResult();
    }

    public List<CartModel> getProductsInCart(String loginName) throws HibernateException, ConstraintViolationException {
        Query q = createQuery("From CartModel where loginName = :loginName");
        q.setParameter("loginName", loginName);
        return q.list();
    }

    public int updateProductQuantityInCart(String loginName, Integer productId, Long quantity)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("Update CartModel set quantity = :quantity where loginName = :loginName and productId = :productId");
        q.setParameter("loginName", loginName);
        q.setParameter("productId", productId);
        q.setParameter("quantity", quantity);
        return q.executeUpdate();
    }

    public int removeFromCart(String loginName, Integer productId)  throws HibernateException, ConstraintViolationException {
        String hql = "";
        if (productId != null){
            hql = "delete CartModel where loginName = :loginName and productId = :productId";
        } else {
            hql = "delete CartModel where loginName = :loginName";
        }
        Query q = createQuery(hql);
        q.setParameter("loginName", loginName);
        if (productId != null) {
            q.setParameter("productId", productId);
        }
        return q.executeUpdate();
    }

    public List<CartViewModel> listCartItemsOfUser(String loginName) throws HibernateException, ConstraintViolationException {
        Query q = createQuery("From CartViewModel where loginName = :loginName");
        q.setParameter("loginName", loginName);
        return q.list();
    }
}
