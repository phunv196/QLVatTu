package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.species.SpeciesModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class SpeciesDao extends BaseHibernateDAO {

    public  SpeciesModel getById(Long speciesId){
        String hql = "from SpeciesModel where speciesId = :speciesId";
        Query q = createQuery(hql);
        q.setParameter("speciesId", speciesId);
        return  (SpeciesModel) q.uniqueResult();
    }

    public  int delete(Long speciesId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete SpeciesModel where speciesId = :speciesId");
        q.setParameter("speciesId", speciesId);
        return q.executeUpdate();
    }
}
