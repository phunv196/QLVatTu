package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.warehouse.WarehouseModel;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class WarehouseDao extends BaseHibernateDAO {

    public  WarehouseModel getById(Long warehouseId){
        String hql = "from WarehouseModel where warehouseId = :warehouseId";
        Query q = createQuery(hql);
        q.setParameter("warehouseId", warehouseId);
        return  (WarehouseModel) q.uniqueResult();
    }

    public  int delete(Long warehouseId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete WarehouseModel where warehouseId = :warehouseId");
        q.setParameter("warehouseId", warehouseId);
        return q.executeUpdate();
    }

    public List<WarehouseModel> getListExport(String code, String name, String email, String phone) {
        Criteria criteria = createCriteria(WarehouseModel.class);

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
