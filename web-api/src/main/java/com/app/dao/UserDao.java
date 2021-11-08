package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.dao.base.CommonUtils;
import com.app.model.user.UserModel;
import com.app.model.user.UserOutputModel;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.*;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends BaseHibernateDAO {

    public  Long getSequence() throws Exception {
        Long id = getAutoIncrement("users");
        return id == null ? 0 : id;
    }

    public UserModel getById(String loginName){
        String hql = "from UserModel where loginName = :loginName";
        Query q = createQuery(hql);
        q.setParameter("loginName", loginName);
        return  (UserModel)q.uniqueResult();
    }

    public UserModel getById(Integer id){
        String hql = "from UserModel where user_id = :id";
        Query q = createQuery(hql);
        q.setParameter("id", id);
        return  (UserModel)q.uniqueResult();
    }

    public void delete(UserModel user ){
        String sqlUser = "delete from users where login_name = :loginName";
        Query queryDeleteUser = createSQLQuery(sqlUser);
        queryDeleteUser.setParameter("loginName", user.getLoginName());
        queryDeleteUser.executeUpdate();
    }

    public List<UserOutputModel> getList(int from, int limit, String searchLoginName, String searchRole, String searchFullName,
                                         String searchEmail, String searchPhone, Integer searchEmployeeId)  throws HibernateException, ConstraintViolationException {

        String sql = createSqlWhereString(searchLoginName, searchRole, searchFullName, searchEmail, searchPhone,
                searchEmployeeId);
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
        String finalSql = "select u.user_id userId," +
                " u.login_name loginName," +
                " u.full_name fullName," +
                " u.email email," +
                " u.phone phone," +
                " u.role role," +
                " u.employee_id employeeId," +
                " e.code employeeCode" +
                " from users u " +
                " left join employees e on e.employee_id = u.employee_id" ;
        finalSql = finalSql + sql + " order by u.user_id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (!CommonUtils.isNullOrEmpty(searchLoginName))   { q.setParameter("searchLoginName", "%" + searchLoginName.toLowerCase() + "%"); }
        if (!CommonUtils.isNullOrEmpty(searchFullName))   { q.setParameter("searchFullName", "%" + searchFullName.toLowerCase() + "%"); }
        if (!CommonUtils.isNullOrEmpty(searchEmail))   { q.setParameter("searchEmail", "%" + searchEmail.toLowerCase() + "%"); }
        if (!CommonUtils.isNullOrEmpty(searchPhone))   { q.setParameter("searchPhone", "%" + searchPhone.toLowerCase() + "%"); }
        if (!CommonUtils.isNullOrEmpty(searchRole))    { q.setParameter("searchRole", searchRole.toLowerCase()); }
        if (searchEmployeeId >0)   { q.setParameter("searchEmployeeId", searchEmployeeId); }
        q.setResultTransformer(Transformers.aliasToBean(UserOutputModel.class));
        setResultTransformer(q, UserOutputModel.class);

        return q.list();
    }

    public BigInteger getCount(String searchLoginName, String searchRole, String searchFullName,
                               String searchEmail, String searchPhone, Integer searchEmployeeId) {
        String sql = createSqlWhereString(searchLoginName, searchRole, searchFullName, searchEmail, searchPhone,
                searchEmployeeId);
        String countSql = "select count(*) from users u " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (!CommonUtils.isNullOrEmpty(searchLoginName))   { q.setParameter("searchLoginName", "%" + searchLoginName.toLowerCase() + "%"); }
        if (!CommonUtils.isNullOrEmpty(searchFullName))   { q.setParameter("searchFullName", "%" + searchFullName.toLowerCase() + "%"); }
        if (!CommonUtils.isNullOrEmpty(searchEmail))   { q.setParameter("searchEmail", "%" + searchEmail.toLowerCase() + "%"); }
        if (!CommonUtils.isNullOrEmpty(searchPhone))   { q.setParameter("searchPhone", "%" + searchPhone.toLowerCase() + "%"); }
        if (!CommonUtils.isNullOrEmpty(searchRole))    { q.setParameter("searchRole", searchRole.toLowerCase()); }
        if (searchEmployeeId >0)   { q.setParameter("searchEmployeeId", searchEmployeeId); }
        return (BigInteger)q.uniqueResult();
    }

    private  String createSqlWhereString(String searchLoginName, String searchRole, String searchFullName,
                                         String searchEmail, String searchPhone, Integer searchEmployeeId){
        String sqlWhere = " where  1 = 1 ";
        if (!CommonUtils.isNullOrEmpty(searchLoginName))   { sqlWhere = sqlWhere + " and LOWER(u.login_name) like :searchLoginName "; }
        if (!CommonUtils.isNullOrEmpty(searchFullName))   { sqlWhere = sqlWhere + " and LOWER(u.full_name) like :searchFullName "; }
        if (!CommonUtils.isNullOrEmpty(searchEmail))   { sqlWhere = sqlWhere + " and LOWER(u.email) like :searchEmail "; }
        if (!CommonUtils.isNullOrEmpty(searchPhone))   { sqlWhere = sqlWhere + " and LOWER(u.phone) like :searchPhone "; }
        if (!CommonUtils.isNullOrEmpty(searchRole))    { sqlWhere = sqlWhere + " and LOWER(u.role) = :searchRole "; }
        if (searchEmployeeId >0)   { sqlWhere = sqlWhere + " and u.employee_id = :searchEmployeeId "; }
        return sqlWhere;
    }


    public List<UserOutputModel> getListExport(String searchLoginName, String searchRole, String searchFullName,
                                         String searchEmail, String searchPhone, Integer searchEmployeeId)  throws HibernateException, ConstraintViolationException {

        StringBuilder querySelect = new StringBuilder("select u.user_id userId," +
                " u.login_name loginName," +
                " u.full_name fullName," +
                " u.email email," +
                " u.phone phone," +
                " u.role role," +
                " u.employee_id employeeId," +
                " e.code employeeCode" +
                " from users u " +
                " left join employees e on e.employee_id = u.employee_id" );
        List<Object> paramList = new ArrayList<>();
        StringBuilder strCondition = new StringBuilder(" WHERE 1 = 1");
        CommonUtils.filter(searchLoginName, strCondition, paramList, "u.login_name");
        CommonUtils.filter(searchRole, strCondition, paramList, "u.role");
        CommonUtils.filter(searchFullName, strCondition, paramList, "u.login_name");
        CommonUtils.filter(searchEmail, strCondition, paramList, "u.email");
        CommonUtils.filter(searchPhone, strCondition, paramList, "u.phone");
        CommonUtils.filter(searchEmployeeId, strCondition, paramList, "u.employee_id");
        querySelect.append(strCondition);
        querySelect.append(" ORDER BY u.login_name ");
        SQLQuery q = createSQLQuery(querySelect.toString());
        for (int i = 0; i < paramList.size(); i++) {
            q.setParameter(i, paramList.get(i));
        }
        q.setResultTransformer(Transformers.aliasToBean(UserOutputModel.class));
        setResultTransformer(q, UserOutputModel.class);
        return q.list();
    }
}
