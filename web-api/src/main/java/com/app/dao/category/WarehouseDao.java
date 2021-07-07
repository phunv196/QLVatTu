package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.WarehouseModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

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
}
