package com.app.dao.category;

import com.app.dao.base.BaseHibernateDAO;
import com.app.dao.base.CommonUtils;
import com.app.model.category.FactoryModel;
import com.app.model.category.SuppliesModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.*;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FactoryDao extends BaseHibernateDAO {

//    public  BigDecimal getReferenceCount(Long productId){
//        String sql = "Select sum(cnt) from ("+
//                "   select count(*) as cnt from northwind.cart where product_id = :productId "+
//                "   union "+
//                "   select count(*) as cnt from northwind.order_items where product_id = :productId "+
//                " )";
//
//        Query q = createSQLQuery(sql);
//        q.setParameter("productId", productId);
//        return (BigDecimal) q.uniqueResult();
//    }

    public FactoryModel getById( Long factoryId){
        String hql = "from FactoryModel where factoryId = :factoryId";
        Query q = createQuery(hql);
        q.setParameter("factoryId", factoryId);
        return  (FactoryModel) q.uniqueResult();
    }

    public int delete(Long factoryId)  throws HibernateException, ConstraintViolationException {
        Query q = createQuery("delete FactoryModel where factoryId = :factoryId");
        q.setParameter("factoryId", factoryId);
        return q.executeUpdate();
    }

    public List<FactoryModel> getList(int from, int limit, Long factoryId, String searchCode, String searchName,
                                      String searchEmail, Long searchEmployee, Date searchFormDate, Date searchToDate, Date searchFormSuccessDate,
                                      Date searchToSuccessDate)  throws HibernateException, ConstraintViolationException {
        String sql = createSqlWhereString(factoryId,searchCode, searchName,
                searchEmail, searchEmployee, searchFormDate, searchToDate, searchFormSuccessDate, searchToSuccessDate);
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
        String finalSql = "select f.factory_id factoryId," +
                " f.code code," +
                " f.name name," +
                " f.email email," +
                " f.address address," +
                " f.description description, " +
                " e.full_name employeeName," +
                " f.employee_id employeeId," +
                " f.date_construction dateConstruction," +
                " f.date_finish dateFinish" +
                " from factory f " +
                " left join employees e on e.employee_id = f.employee_id";
        //String finalSql = "select factory_id from factory";
        finalSql = finalSql + sql + " order by f.factory_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (factoryId >0)   { q.setParameter("factoryId", factoryId); }
        if (searchCode != null)   { q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%"); }
        if (searchName != null)    { q.setParameter("searchName", "%" + searchName.toLowerCase() + "%"); }
        if (searchEmail != null)    { q.setParameter("searchEmail", "%" + searchEmail.toLowerCase() + "%"); }
        if (searchEmployee > 0)   { q.setParameter("searchEmployee", searchEmployee ); }
        if (searchFormDate != null)   { q.setParameter("searchFormDate", searchFormDate ); }
        if (searchToDate != null)   { q.setParameter("searchToDate", searchToDate ); }
        if (searchFormSuccessDate != null)   { q.setParameter("searchFormSuccessDate", searchFormSuccessDate ); }
        if (searchToSuccessDate != null)   { q.setParameter("searchToSuccessDate", searchToSuccessDate ); }
        q.setResultTransformer(Transformers.aliasToBean(FactoryModel.class));
        setResultTransformer(q, FactoryModel.class);

        return q.list();
    }


    public BigInteger getFactoryCount(Long factoryId, String searchCode, String searchName,
                                      String searchEmail, Long searchEmployee, Date searchFormDate, Date searchToDate, Date searchFormSuccessDate,
                                      Date searchToSuccessDate) {
        String sql = createSqlWhereString(factoryId,searchCode, searchName,
                searchEmail, searchEmployee, searchFormDate, searchToDate, searchFormSuccessDate, searchToSuccessDate);
        String countSql = "select count(*) from factory f " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (factoryId >0)   { q.setParameter("factoryId", factoryId); }
        if (searchCode != null)   { q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%"); }
        if (searchName != null)    { q.setParameter("searchName", "%" + searchName.toLowerCase() + "%"); }
        if (searchEmail != null)    { q.setParameter("searchEmail", "%" + searchEmail.toLowerCase() + "%"); }
        if (searchEmployee > 0)   { q.setParameter("searchEmployee", searchEmployee ); }
        if (searchFormDate != null)   { q.setParameter("searchFormDate", searchFormDate ); }
        if (searchToDate != null)   { q.setParameter("searchToDate", searchToDate ); }
        if (searchFormSuccessDate != null)   { q.setParameter("searchFormSuccessDate", searchFormSuccessDate ); }
        if (searchToSuccessDate != null)   { q.setParameter("searchToSuccessDate", searchToSuccessDate ); }
        return (BigInteger)q.uniqueResult();
    }

    private String createSqlWhereString(Long factoryId, String searchCode, String searchName,
                                        String searchEmail, Long searchEmployee, Date searchFormDate, Date searchToDate, Date searchFormSuccessDate,
                                        Date searchToSuccessDate){
        String sqlWhere = " where  1 = 1 ";

        if (factoryId > 0)   { sqlWhere = sqlWhere + " and f.factory_id = :factoryId "; }
        if (searchCode != null)   { sqlWhere = sqlWhere + " and LOWER(f.code) LIKE :searchCode "; }
        if (searchName != null)   { sqlWhere = sqlWhere + " and LOWER(f.name) LIKE :searchName "; }
        if (searchEmail != null)   { sqlWhere = sqlWhere + " and LOWER(f.email) LIKE :searchEmail "; }
        if (searchEmployee > 0)   { sqlWhere = sqlWhere + " and f.employee_id = :searchEmployee "; }
        if (searchFormDate != null)   { sqlWhere = sqlWhere + " and f.date_construction >= :searchFormDate "; }
        if (searchToDate != null)   { sqlWhere = sqlWhere + " and f.date_construction <= :searchToDate "; }
        if (searchFormSuccessDate != null)   { sqlWhere = sqlWhere + " and f.date_finish >= :searchFormSuccessDate "; }
        if (searchToSuccessDate != null)   { sqlWhere = sqlWhere + " and f.date_finish <= :searchToSuccessDate "; }

        return sqlWhere;
    }


    public List<FactoryModel> getListExport(String code, String name, String email, Long employeeId,
                            String formDate, String toDate, String formSuccessDate, String toSuccessDate) throws Exception {
        StringBuilder querySelect = new StringBuilder("select f.factory_id factoryId," +
                " f.code code," +
                " f.name name," +
                " f.email email," +
                " f.address address," +
                " f.description description, " +
                " e.full_name employeeName," +
                " f.employee_id employeeId," +
                " f.date_construction dateConstruction," +
                " f.date_finish dateFinish" +
                " from factory f " +
                " left join employees e on e.employee_id = f.employee_id");
        List<Object> paramList = new ArrayList<>();
        StringBuilder strCondition = new StringBuilder(" WHERE 1 = 1");
        CommonUtils.filter(code, strCondition, paramList, "f.code");
        CommonUtils.filter(name, strCondition, paramList, "f.name");
        CommonUtils.filter(email, strCondition, paramList, "f.email");
        CommonUtils.filter(employeeId, strCondition, paramList, "f.employee_id");
        CommonUtils.filterBetweenDate(CommonUtils.convertStringToDateOther(formDate),CommonUtils.convertStringToDateOther(toDate)
                    , strCondition, paramList, "f.date_construction","f.date_construction");
        CommonUtils.filterBetweenDate(CommonUtils.convertStringToDateOther(formSuccessDate),CommonUtils.convertStringToDateOther(toSuccessDate)
                    , strCondition, paramList, "f.date_finish","f.date_finish");
        querySelect.append(strCondition);
        querySelect.append(" ORDER BY f.factory_id ");
        SQLQuery q = createSQLQuery(querySelect.toString());
        for (int i = 0; i < paramList.size(); i++) {
            q.setParameter(i, paramList.get(i));
        }
        q.setResultTransformer(Transformers.aliasToBean(FactoryModel.class));
        setResultTransformer(q, FactoryModel.class);
        return q.list();
    }
}
