package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.role.RoleModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class RoleDao extends BaseHibernateDAO {

    public  RoleModel getById(Long roleId){
        String hql = "from RoleModel where roleId = :roleId";
        Query q = createQuery(hql);
        q.setParameter("roleId", roleId);
        return  (RoleModel) q.uniqueResult();
    }

    public  int delete(Long roleId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete RoleModel where roleId = :roleId");
        q.setParameter("roleId", roleId);
        return q.executeUpdate();
    }
}
