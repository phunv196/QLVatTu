/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.dao.base;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class QueryUtils {

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param str : xau
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(String str, StringBuilder queryString, List<Object> paramList, String field) {
        if ((str != null) && !"".equals(str.trim())) {
            queryString.append(" AND LOWER(").append(field).append(") LIKE ? ESCAPE '/'");
            str = str.replace("  ", " ");
            str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
            paramList.add(str);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param str : xau
     * @param queryString
     * @param paramList
     */
    public static void filter(String str, StringBuilder queryString, List<Object> paramList, String... fields) {
        if ((str != null) && !"".equals(str.trim())) {
            queryString.append(" AND ( 0 = 1 ");
            str = str.replace("  ", " ");
            str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
            for (String field : fields) {
                queryString.append(" OR LOWER(").append(field).append(") LIKE ? ESCAPE '/'");
                paramList.add(str);
            }
            queryString.append(")");
        }
    }

    /**
     *
     * @param str
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterEq(String str, StringBuilder queryString, List<Object> paramList, String field) {
        if ((str != null) && !"".equals(str.trim())) {
            queryString.append(" AND LOWER(").append(field).append(") LIKE ? ESCAPE '/'");
            str = str.replace("  ", " ");
            str = str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%");
            paramList.add(str);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param n So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Long n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0L)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param n So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Integer n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param n So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Double n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param date So
     * @param queryString
     * @param field
     * @param paramList
     */
    public static void filter(Date date, StringBuilder queryString, List<Object> paramList, String field) {
        if ((date != null)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(date);
        }
    }

    /**
     * Kiem tra lon hon hoac bang.
     *
     * @param obj So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterGe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" >= ? ");
            paramList.add(obj);
        }
    }
    
    /**
     * Kiem tra lon hon hoac bang.
     *
     * @param obj So
     * @param queryString
     * @param field
     */
    public static void filterGe(Object obj, StringBuilder queryString, Map<String, Object> paramMap, String key, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" >= :" + key);
            paramMap.put(key, obj);
        }
    }

    /**
     * Kiem tra nho hon hoac bang.
     *
     * @param obj So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterLe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" <= ? ");
            paramList.add(obj);
        }
    }
    
    /**
     * Kiem tra nho hon hoac bang.
     *
     * @param obj So
     * @param queryString
     * @param field
     */
    public static void filterLe(Object obj, StringBuilder queryString, Map<String, Object> paramMap, String key, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" <= :" + key);
            paramMap.put(key, obj);
        }
    }

    /**
     * filter for inserting preparedStatement
     *
     * @param value
     * @param index
     * @param preparedStatement
     * @throws Exception
     */
    public static void filter(String value, PreparedStatement preparedStatement, int index)
            throws Exception {

        if (value != null) {
            preparedStatement.setString(index, value.trim());
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     *
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Double value, PreparedStatement preparedStatement, int index)
            throws Exception {

        if (value != null) {
            preparedStatement.setDouble(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     *
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Long value, PreparedStatement preparedStatement, int index)
            throws Exception {
        if (value != null) {
            preparedStatement.setLong(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    public static void filter(BigDecimal value, PreparedStatement preparedStatement, int index)
            throws Exception {
        if (value != null) {
            preparedStatement.setBigDecimal(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     *
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Object value, PreparedStatement preparedStatement, int index)
            throws Exception {
        if (value != null) {
            if (value instanceof Date) {
                Date temp = (Date) value;
                preparedStatement.setObject(index, new java.sql.Timestamp(temp.getTime()));
            } else {
                preparedStatement.setObject(index, value);
            }

        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     *
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(java.sql.Date value, PreparedStatement preparedStatement, int index)
            throws Exception {

        if (value != null) {
            preparedStatement.setDate(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    public static void filter(String str, StringBuilder queryString, Map paramList, String key, String property) {
        if ((str != null) && !"".equals(str.trim())) {
            queryString.append(" AND LOWER(").append(property).append(") LIKE :").append(key).append(" ESCAPE '/'");
            str = str.replace("  ", " ");
            str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
            paramList.put(key, str);
        }
    }

    public static void filter(Long str, StringBuilder queryString, Map paramList, String key, String property) {
        if ((str != null) && (str > 0)) {
            queryString.append(" AND ").append(property).append(" = :").append(key);
            paramList.put(key, str);
        }
    }
}
