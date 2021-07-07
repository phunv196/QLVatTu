package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.DepartmentModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class DepartmentDao extends BaseHibernateDAO {

    public  DepartmentModel getById(Long departmentId){
        String hql = "from DepartmentModel where departmentId = :departmentId";
        Query q = createQuery(hql);
        q.setParameter("departmentId", departmentId);
        return  (DepartmentModel) q.uniqueResult();
    }

    public  int delete(Long departmentId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete DepartmentModel where departmentId = :departmentId");
        q.setParameter("departmentId", departmentId);
        return q.executeUpdate();
    }
}
