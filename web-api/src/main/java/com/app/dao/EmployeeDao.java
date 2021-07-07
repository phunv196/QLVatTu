package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.employee.EmployeeModel;
import org.hibernate.*;
import jakarta.validation.ConstraintViolationException;

public class EmployeeDao extends BaseHibernateDAO {
    public EmployeeModel getById(Integer employeeId){
        String hql = "from EmployeeModel where id = :employeeId";
        Query q = createQuery(hql);
        q.setParameter("employeeId", employeeId);
        return  (EmployeeModel)q.uniqueResult();
    }

    public void delete(Integer employeeId)  throws HibernateException, ConstraintViolationException {
        String sqlDeleteUser     = "delete from users where employee_id = :employeeId";
        String sqlDeleteEmployee = "delete from employees where id = :employeeId";

        Query queryDeleteUser = createSQLQuery(sqlDeleteUser);
        queryDeleteUser.setParameter("employeeId", employeeId);

        Query queryDeleteEmployee = createSQLQuery(sqlDeleteEmployee);
        queryDeleteEmployee.setParameter("employeeId", employeeId);

        queryDeleteUser.executeUpdate();
        queryDeleteEmployee.executeUpdate();
    }
}
