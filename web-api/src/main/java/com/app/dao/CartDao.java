package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.cart.CartModel;
import com.app.model.cart.CartViewModel;
import org.hibernate.*;
import jakarta.validation.ConstraintViolationException;
import java.util.List;

public class CartDao extends BaseHibernateDAO {
    public CartModel getProductsInCart( String userId, Integer productId) throws HibernateException, ConstraintViolationException {
        Query q = createQuery( "From CartModel where userId = :userId and productId = :productId");
        q.setParameter("userId", userId);
        q.setParameter("productId", productId);
        return (CartModel)q.uniqueResult();
    }

    public List<CartModel> getProductsInCart(String userId) throws HibernateException, ConstraintViolationException {
        Query q = createQuery("From CartModel where userId = :userId");
        q.setParameter("userId", userId);
        return q.list();
    }

    public int updateProductQuantityInCart(String userId, Integer productId, Long quantity)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("Update CartModel set quantity = :quantity where userId = :userId and productId = :productId");
        q.setParameter("userId", userId);
        q.setParameter("productId", productId);
        q.setParameter("quantity", quantity);
        return q.executeUpdate();
    }

    public int removeFromCart(String userId, Integer productId)  throws HibernateException, ConstraintViolationException {
        String hql = "";
        if (productId != null){
            hql = "delete CartModel where userId = :userId and productId = :productId";
        } else {
            hql = "delete CartModel where userId = :userId";
        }
        Query q = createQuery(hql);
        q.setParameter("userId", userId);
        if (productId != null) {
            q.setParameter("productId", productId);
        }
        return q.executeUpdate();
    }

    public List<CartViewModel> listCartItemsOfUser(String userId) throws HibernateException, ConstraintViolationException {
        Query q = createQuery("From CartViewModel where userId = :userId");
        q.setParameter("userId", userId);
        return q.list();
    }
}
