package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.SuppliesModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.List;

public class SuppliesDao extends BaseHibernateDAO {


    public  SuppliesModel getById(Long suppliesId){
        String hql = "from SuppliesModel where suppliesId = :suppliesId";
        Query q = createQuery(hql);
        q.setParameter("suppliesId", suppliesId);
        return  (SuppliesModel) q.uniqueResult();
    }

    public  int delete(Long suppliesId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete SuppliesModel where suppliesId = :suppliesId");
        q.setParameter("suppliesId", suppliesId);
        return q.executeUpdate();
    }

    public  List<SuppliesModel> getList(int from, int limit, Long suppliesId)  throws HibernateException, ConstraintViolationException {

        String sql = createSqlWhereString(suppliesId);
        String sqlLimit = "";
        if ( limit <= 0 || limit > 1000 ){
            sqlLimit = " limit 1000 ";
        } else {
            sqlLimit = " limit " +  limit;
        }
        if (from <= 0) {
            from = 0;
        } else {
            from = (from - 1) * limit;
        }

        sqlLimit = sqlLimit + " offset " + from;
        String finalSql = "select s.supplies_id suppliesId," +
                " s.code code," +
                " s.name name," +
                " s.unit unit," +
                " s.price price," +
                " s.description description, " +
                " spec.species_id speciesId," +
                " spec.name speciesName," +
                " sup.supplier_id supplierId," +
                " sup.name supplierName," +
                " q.quality_Id qualityId," +
                " q.quality_name qualityName," +
                " from supplies s " +
                " left join species spec on spec.species_id = s.species_id" +
                " left join supplier sup on sup.supplier_id = s.supplier_id" +
                " left join quality q on q.quality_id = s.quality_id" +
                " where supplies_id in ";
        //String finalSql = "select factory_id from factory";
        finalSql = finalSql + "( select supplies_id " + sql + sqlLimit + ") order by supplies_id ";

        SQLQuery q = createSQLQuery(finalSql);
        if (suppliesId >0)   { q.setParameter("factoryId", suppliesId); }

        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(SuppliesModel.class));
        setResultTransformer(q, SuppliesModel.class);

        return q.list();
    }


    public  BigInteger getSuppliesCount(Long suppliesId) {
        String sql = createSqlWhereString(suppliesId);
        String countSql = "select count(*) " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (suppliesId >0)   { q.setParameter("suppliesId", suppliesId); }
        return (BigInteger)q.uniqueResult();
    }

    private  String createSqlWhereString(Long suppliesId){
        String sqlWhere = " where  1 = 1 ";

        if (suppliesId > 0)   { sqlWhere = sqlWhere + " and suppliesId = :suppliesId "; }

        return " from supplies " + sqlWhere;
    }
}
