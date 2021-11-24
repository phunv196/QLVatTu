package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.warehouse.WarehouseModel;
import com.app.model.warehouseCard.WarehouseCardFlowModel;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

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

    public List<WarehouseModel> getByRecepit() {
        String hql = " select DISTINCT w.warehouse_id as warehouseId, w.name as name from warehouse w" +
                     " inner join receipt r on w.warehouse_id = r.warehouse_id";
        SQLQuery q = createSQLQuery(hql);
        q.setResultTransformer(Transformers.aliasToBean(WarehouseModel.class));
        setResultTransformer(q, WarehouseModel.class);
        return q.list();
    }
}
