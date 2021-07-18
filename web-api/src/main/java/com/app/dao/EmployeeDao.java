package com.app.dao;

import com.app.dao.base.BaseHibernateDAO;
import com.app.model.category.FactoryModel;
import com.app.model.employee.EmployeeModel;
import org.hibernate.*;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

public class EmployeeDao extends BaseHibernateDAO {
    public EmployeeModel getById(Long employeeId){
        String hql = "from EmployeeModel where id = :employeeId";
        Query q = createQuery(hql);
        q.setParameter("employeeId", employeeId);
        return  (EmployeeModel)q.uniqueResult();
    }

    public void delete(Long employeeId)  throws HibernateException, ConstraintViolationException {
        String sqlDeleteUser     = "delete from users where employee_id = :employeeId";
        String sqlDeleteEmployee = "delete from employees where id = :employeeId";

        Query queryDeleteUser = createSQLQuery(sqlDeleteUser);
        queryDeleteUser.setParameter("employeeId", employeeId);

        Query queryDeleteEmployee = createSQLQuery(sqlDeleteEmployee);
        queryDeleteEmployee.setParameter("employeeId", employeeId);

        queryDeleteUser.executeUpdate();
        queryDeleteEmployee.executeUpdate();
    }

    public List<EmployeeModel> getList(int from, int limit,Long employeeId, String searchCode, String searchName,
                                      String searchEmail,String searchPhone, Long searchDepartment,Long searchPosition)  throws HibernateException, ConstraintViolationException {
        String sql = createSqlWhereString(employeeId, searchCode,
                searchName, searchEmail, searchPhone, searchDepartment, searchPosition);
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
        String finalSql = "select e.id employeeId," +
                " e.code code," +
                " e.full_name fullName," +
                " e.phone phone," +
                " e.email email," +
                " e.department_id departmentId, " +
                " d.name departmentName, " +
                " e.position_id positionId, " +
                " p.name positionName, " +
                " e.address1 address1 " +
                " from employees e " +
                " left join department d on e.department_id = d.department_id" +
                " left join position p on e.position_id = p.position_id";
        //String finalSql = "select factory_id from factory";
        finalSql = finalSql + sql + " order by e.id " + sqlLimit;

        SQLQuery q = createSQLQuery(finalSql);
        if (employeeId >0)   { q.setParameter("id", employeeId); }
        if (searchCode != null)   { q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%"); }
        if (searchName != null)    { q.setParameter("searchName", "%" + searchName.toLowerCase() + "%"); }
        if (searchEmail != null)    { q.setParameter("searchEmail", "%" + searchEmail.toLowerCase() + "%"); }
        if (searchPhone != null)    { q.setParameter("searchPhone", "%" + searchPhone.toLowerCase() + "%"); }
        if (searchDepartment > 0)   { q.setParameter("searchDepartment", searchDepartment ); }
        if (searchPosition > 0)   { q.setParameter("searchPosition", searchPosition ); }
        q.setResultTransformer(Transformers.aliasToBean(EmployeeModel.class));
        setResultTransformer(q, EmployeeModel.class);

        return q.list();
    }


    public BigInteger getEmployeeCount(Long employeeId, String searchCode, String searchName,
                                      String searchEmail,String searchPhone, Long searchDepartment,Long searchPosition) {
        String sql = createSqlWhereString(employeeId, searchCode,
                searchName, searchEmail, searchPhone, searchDepartment, searchPosition);
        String countSql = "select count(*) from employees e " + sql ;
        SQLQuery q = createSQLQuery(countSql);
        if (employeeId >0)   { q.setParameter("id", employeeId); }
        if (searchCode != null)   { q.setParameter("searchCode", "%" + searchCode.toLowerCase() + "%"); }
        if (searchName != null)    { q.setParameter("searchName", "%" + searchName.toLowerCase() + "%"); }
        if (searchEmail != null)    { q.setParameter("searchEmail", "%" + searchEmail.toLowerCase() + "%"); }
        if (searchPhone != null)    { q.setParameter("searchPhone", "%" + searchPhone.toLowerCase() + "%"); }
        if (searchDepartment > 0)   { q.setParameter("searchDepartment", searchDepartment ); }
        if (searchPosition > 0)   { q.setParameter("searchPosition", searchPosition ); }
        return (BigInteger)q.uniqueResult();
    }

    private String createSqlWhereString(Long employeeId, String searchCode, String searchName,
                                        String searchEmail,String searchPhone, Long searchDepartment,Long searchPosition){
        String sqlWhere = " where  1 = 1 ";

        if (employeeId > 0)   { sqlWhere = sqlWhere + " and e.id = :employeeId "; }
        if (searchCode != null)   { sqlWhere = sqlWhere + " and LOWER(e.code) LIKE :searchCode "; }
        if (searchName != null)   { sqlWhere = sqlWhere + " and LOWER(e.full_name) LIKE :searchName "; }
        if (searchEmail != null)   { sqlWhere = sqlWhere + " and LOWER(e.email) LIKE :searchEmail "; }
        if (searchPhone != null )   { sqlWhere = sqlWhere + " and LOWER(e.phone) LIKE :searchPhone "; }
        if (searchDepartment > 0)   { sqlWhere = sqlWhere + " and e.department_id = :searchDepartment "; }
        if (searchPosition > 0)   { sqlWhere = sqlWhere + " and e.position_id = :searchPosition "; }

        return sqlWhere;
    }
}
