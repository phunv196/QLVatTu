package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.supplier.SupplierModel;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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

    public List<SupplierModel> getListExport(String code, String name, String email, String phone) {
        Criteria criteria = createCriteria(SupplierModel.class);

        if (StringUtils.isNotBlank(name)){
            criteria.add(Restrictions.like("name", "%"+name+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(code)){
            criteria.add(Restrictions.like("code", "%"+code+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(email)){
            criteria.add(Restrictions.like("email", "%"+email+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(phone)){
            criteria.add(Restrictions.like("phone", "%"+phone+"%" ).ignoreCase());
        }
        return criteria.list();
    }
}
