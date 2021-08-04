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

    public  List<SuppliesModel> getList(int from, int limit, Long suppliesId, String searchCode,String searchName,
                                       Long searchSupplier,Long searchSpecies,Long searchFormPrice,Long searchToPrice,
                                        Long searchQuality,String searchUnit)  throws HibernateException, ConstraintViolationException {

        String sql = createSqlWhereString(suppliesId, searchCode, searchName,
                searchSupplier, searchSpecies, searchFormPrice, searchToPrice, searchQuality, searchUnit);
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
                " q.name qualityName" +
                " from supplies s " +
                " left join species spec on spec.species_id = s.species_id" +
                " left join supplier sup on sup.supplier_id = s.supplier_id" +
                " left join quality q on q.quality_id = s.quality_id";
        //String finalSql = "select factory_id from factory";
        finalSql = finalSql + sql + " order by s.supplies_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (suppliesId >0)   { q.setParameter("factoryId", suppliesId); }
        if (searchCode != null)   { q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%"); }
        if (searchName != null)    { q.setParameter("searchName", "%" + searchName.toLowerCase() + "%"); }
        if (searchSupplier >0)   { q.setParameter("searchSupplier", searchSupplier); }
        if (searchSpecies >0)   { q.setParameter("searchSpecies", searchSpecies); }
        if (searchFormPrice >0)   { q.setParameter("searchFormPrice", searchFormPrice); }
        if (searchToPrice >0)   { q.setParameter("searchToPrice", searchToPrice); }
        if (searchQuality >0)   { q.setParameter("searchQuality", searchQuality); }
        if (searchUnit != null && !searchUnit.isEmpty())   { q.setParameter("searchUnit", searchUnit.toLowerCase() ); }
        //q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        //List rowList = q.list();
        q.setResultTransformer(Transformers.aliasToBean(SuppliesModel.class));
        setResultTransformer(q, SuppliesModel.class);

        return q.list();
    }


    public  BigInteger getSuppliesCount(Long suppliesId, String searchCode,String searchName,
                                        Long searchSupplier,Long searchSpecies,Long searchFormPrice,Long searchToPrice,
                                        Long searchQuality,String searchUnit) {
        String sql = createSqlWhereString(suppliesId, searchCode, searchName,
                searchSupplier, searchSpecies, searchFormPrice, searchToPrice, searchQuality, searchUnit);
        String countSql = "select count(*) from supplies s " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (suppliesId >0)   { q.setParameter("suppliesId", suppliesId); }
        if (searchCode != null)   { q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%"); }
        if (searchName != null)    { q.setParameter("searchName", "%" + searchName.toLowerCase() + "%"); }
        if (searchSupplier >0)   { q.setParameter("searchSupplier", searchSupplier); }
        if (searchSpecies >0)   { q.setParameter("searchSpecies", searchSpecies); }
        if (searchFormPrice >0)   { q.setParameter("searchFormPrice", searchFormPrice); }
        if (searchToPrice >0)   { q.setParameter("searchToPrice", searchToPrice); }
        if (searchQuality >0)   { q.setParameter("searchQuality", searchQuality); }
        if (searchUnit != null && !searchUnit.isEmpty())   { q.setParameter("searchUnit", searchUnit.toLowerCase() ); }
        return (BigInteger)q.uniqueResult();
    }

    private  String createSqlWhereString(Long suppliesId, String searchCode,String searchName,
                                         Long searchSupplier,Long searchSpecies,Long searchFormPrice,Long searchToPrice,
                                         Long searchQuality,String searchUnit){
        String sqlWhere = " where  1 = 1 ";

        if (suppliesId > 0)   { sqlWhere = sqlWhere + " and supplies_id = :suppliesId "; }
        if (searchCode != null)   { sqlWhere = sqlWhere + " and LOWER(s.code) LIKE :searchCode "; }
        if (searchName != null)   { sqlWhere = sqlWhere + " and LOWER(s.name) LIKE :searchName "; }
        if (searchSupplier >0)   { sqlWhere = sqlWhere + " and s.supplier_id = :searchSupplier "; }
        if (searchSpecies >0)   { sqlWhere = sqlWhere + " and s.species_id = :searchSpecies "; }
        if (searchFormPrice >0)   { sqlWhere = sqlWhere + " and s.price >= :searchFormPrice "; }
        if (searchToPrice >0)   { sqlWhere = sqlWhere + " and s.price <= :searchToPrice "; }
        if (searchQuality >0)   { sqlWhere = sqlWhere + " and s.quality_id = :searchQuality "; }
        if (searchUnit != null && !searchUnit.isEmpty())   { sqlWhere = sqlWhere + " and LOWER(unit) = :searchUnit "; }

        return sqlWhere;
    }
}
