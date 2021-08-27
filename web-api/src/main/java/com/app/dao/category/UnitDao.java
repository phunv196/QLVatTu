package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.UnitModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class UnitDao extends BaseHibernateDAO {

    public UnitModel getById(Long unitId){
        String hql = "from UnitModel where unitId = :unitId";
        Query q = createQuery(hql);
        q.setParameter("unitId", unitId);
        return  (UnitModel) q.uniqueResult();
    }

    public  int delete(Long unitId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete UnitModel where unitId = :unitId");
        q.setParameter("unitId", unitId);
        return q.executeUpdate();
    }
}
