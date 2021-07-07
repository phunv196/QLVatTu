package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.customer.CustomerModel;
import org.hibernate.*;
import jakarta.validation.ConstraintViolationException;

public class CustomerDao extends BaseHibernateDAO {

    public CustomerModel getById(Integer customerId) {
        String hql = "from CustomerModel where id = :customerId";
        Query q = createQuery(hql);
        q.setParameter("customerId", customerId);
        return  (CustomerModel)q.uniqueResult();
    }

    public void delete(Integer customerId)  throws HibernateException, ConstraintViolationException {
        // Remove customer from all the related tables (do not change the sequence of deleteion else it may give a referal intigrity error)
        String sqlDeleteOrderItems = "delete from order_items where order_id in (select id from orders where customer_id = :customerId)";
        String sqlDeleteOrders   = "delete from orders where customer_id = :customerId" ;
        String sqlDeleteCart     = "delete from cart where user_id in (select user_id from users where customer_id = :customerId)";
        String sqlDeleteUser     = "delete from users where customer_id = :customerId";
        String sqlDeleteCustomer = "delete from customers where id = :customerId";

        Query queryDeleteOrderItems = createSQLQuery(sqlDeleteOrderItems);
        queryDeleteOrderItems.setParameter("customerId", customerId);

        Query queryDeleteOrders = createSQLQuery(sqlDeleteOrders);
        queryDeleteOrders.setParameter("customerId", customerId);

        Query queryDeleteCart = createSQLQuery(sqlDeleteCart);
        queryDeleteCart.setParameter("customerId", customerId);

        Query queryDeleteUser = createSQLQuery(sqlDeleteUser);
        queryDeleteUser.setParameter("customerId", customerId);

        Query queryDeleteCustomer = createSQLQuery(sqlDeleteCustomer);
        queryDeleteCustomer.setParameter("customerId", customerId);

        queryDeleteOrderItems.executeUpdate();
        queryDeleteOrders.executeUpdate();
        queryDeleteCart.executeUpdate();
        queryDeleteUser.executeUpdate();
        queryDeleteCustomer.executeUpdate();
    }
}
