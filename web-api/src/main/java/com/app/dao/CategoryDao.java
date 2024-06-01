package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.CategoryModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;

public class CategoryDao extends BaseHibernateDAO {

    public  CategoryModel getById(Long categoryId){
        String hql = "from CategoryModel where categoryId = :categoryId";
        Query q = createQuery(hql);
        q.setParameter("categoryId", categoryId);
        return  (CategoryModel) q.uniqueResult();
    }
    public  CategoryModel getByCode(String code){
        String hql = "from CategoryModel where code = :code";
        Query q = createQuery(hql);
        q.setParameter("code", code);
        return (CategoryModel) q.list().get(0);
    }

    public List<CategoryModel> getByParentCode(String parentCode){
        String hql = "from CategoryModel where parentCode = :parentCode";
        Query q = createQuery(hql);
        q.setParameter("parentCode", parentCode);
        return (List<CategoryModel>) q.list();
    }

    public  int delete(Long categoryId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete CategoryModel where categoryId = :categoryId");
        q.setParameter("categoryId", categoryId);
        return q.executeUpdate();
    }
}
