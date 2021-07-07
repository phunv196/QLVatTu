package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.QualityModel;
import org.hibernate.*;
import jakarta.validation.ConstraintViolationException;

public class QualityDao extends BaseHibernateDAO {

    public  QualityModel getById(Long qualityId){
        String hql = "from QualityModel where qualityId = :qualityId";
        Query q = createQuery(hql);
        q.setParameter("qualityId", qualityId);
        return  (QualityModel) q.uniqueResult();
    }

    public  int delete(Long qualityId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete QualityModel where qualityId = :qualityId");
        q.setParameter("qualityId", qualityId);
        return q.executeUpdate();
    }
}
