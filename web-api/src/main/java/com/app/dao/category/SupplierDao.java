package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.SupplierModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class SupplierDao extends BaseHibernateDAO {

    public  SupplierModel getById(Long supplierId){
        String hql = "from SupplierModel where supplierId = :supplierId";
        Query q = createQuery(hql);
        q.setParameter("supplierId", supplierId);
        return  (SupplierModel) q.uniqueResult();
    }

    public  int delete(Long supplierId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete SupplierModel where supplierId = :supplierId");
        q.setParameter("supplierId", supplierId);
        return q.executeUpdate();
    }
}
