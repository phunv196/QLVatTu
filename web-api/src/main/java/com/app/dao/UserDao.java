package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.user.UserViewModel;
import org.hibernate.*;

public class UserDao extends BaseHibernateDAO {

    public UserViewModel getById(String userId){
        String hql = "from UserViewModel where userId = :userId";
        Query q = createQuery(hql);
        q.setParameter("userId", userId);
        return  (UserViewModel)q.uniqueResult();
    }

    public void delete(UserViewModel user, boolean deleteRelatedData ){

       /* To cleanly remove an user
          1. First delete the order-items and order that belong to the associated customer
          2. Then delete the cart that belong to the user
          3. Then delete the user
          4. Finally delete the associated customer
        */

        String sqlDeleteOrderItems = "delete from order_items where order_id " +
                " in (select id from orders where customer_id " +
                "     in (select id from customers where id " +
                "         in (Select customer_id from users where user_id = :userId ) " +
                "   )" +
                ")";

        String sqlDeleteOrders = "delete from orders where customer_id " +
                " in (select id  from customers where id " +
                "     in (Select customer_id from users where user_id = :userId) " +
                ")";

        String sqlDeleteCart = "delete from cart where  user_id = :userId";
        String sqlUser = "delete from users where user_id = :userId";
        String sqlDeleteCustomer = "delete from customers where id = :customerId";
        String sqlDeleteEmployee = "delete from employees where id = :employeeId";

        Query queryDeleteOrderItems = createSQLQuery(sqlDeleteOrderItems);
        queryDeleteOrderItems.setParameter("userId", user.getUserId());

        Query queryDeleteOrders = createSQLQuery(sqlDeleteOrders);
        queryDeleteOrders.setParameter("userId", user.getUserId());

        Query queryDeleteCart = createSQLQuery(sqlDeleteCart);
        queryDeleteCart.setParameter("userId", user.getUserId());

        Query queryDeleteUser = createSQLQuery(sqlUser);
        queryDeleteUser.setParameter("userId", user.getUserId());

        Query queryDeleteCustomer = createSQLQuery(sqlDeleteCustomer);
        if (deleteRelatedData && user.getCustomerId() != null) {
            queryDeleteCustomer.setParameter("customerId", user.getCustomerId());
        }

        Query queryDeleteEmployee = createSQLQuery(sqlDeleteEmployee);
        if (deleteRelatedData && user.getEmployeeId() != null) {
            queryDeleteEmployee.setParameter("employeeId", user.getEmployeeId());
        }
        if (deleteRelatedData && user.getCustomerId() != null) {
            queryDeleteOrderItems.executeUpdate();
            queryDeleteOrders.executeUpdate();
        }
        queryDeleteCart.executeUpdate();
        queryDeleteUser.executeUpdate();

        // Delete associated customer
        if (deleteRelatedData && user.getCustomerId() != null) {
            queryDeleteCustomer.executeUpdate();
        }
        // Delete associated employee
        if (deleteRelatedData && user.getEmployeeId() != null) {
            queryDeleteEmployee.executeUpdate();
        }
    }
}
