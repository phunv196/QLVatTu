package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.position.PositionModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class PositionDao extends BaseHibernateDAO {

    public PositionModel getById(Long positionId){
        String hql = "from PositionModel where positionId = :positionId";
        Query q = createQuery(hql);
        q.setParameter("positionId", positionId);
        return  (PositionModel) q.uniqueResult();
    }

    public  int delete(Long positionId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete PositionModel where positionId = :positionId");
        q.setParameter("positionId", positionId);
        return q.executeUpdate();
    }
}
