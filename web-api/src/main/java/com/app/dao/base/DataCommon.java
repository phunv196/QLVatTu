package com.app.dao.base;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCommon {
    private EntityManager entityManager;

    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public SQLQuery createSQLQuery(String sql) {
        return getSession().createSQLQuery(sql);
    }

    public void setResultTransformer(SQLQuery query, Class obj) {
        Field[] fileds = obj.getDeclaredFields();
        Map<String, String> mapFileds = new HashMap<>();
        for (Field filed : fileds) {
            mapFileds.put(filed.getName(), filed.getGenericType().toString());
        }
        List<String> aliasColumns = getReturnAliasColumns(query);
        for (String aliasColumn : aliasColumns) {
            String dataType = mapFileds.get(aliasColumn);
            if (dataType != null) {
                Type hbmType = null;
                if ("class java.lang.Long".equals(dataType)) {
                    hbmType = LongType.INSTANCE;
                } else if ("class java.lang.Integer".equals(dataType)) {
                    hbmType = IntegerType.INSTANCE;
                } else if ("class java.lang.Double".equals(dataType)) {
                    hbmType = DoubleType.INSTANCE;
                } else if ("class java.lang.String".equals(dataType)) {
                    hbmType = StringType.INSTANCE;
                } else if ("class java.lang.Boolean".equals(dataType)) {
                    hbmType = BooleanType.INSTANCE;
                } else if ("class java.util.Date".equals(dataType)) {
                    hbmType = TimestampType.INSTANCE;
                } else if ("class java.math.BigDecimal".equals(dataType)) {
                    hbmType = new BigDecimalType();
                }

                if (hbmType != null) {
                    query.addScalar(aliasColumn, hbmType);
                }
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(obj));
    }

    private List<String> getReturnAliasColumns(SQLQuery query) {
        List<String> aliasColumns = new ArrayList<>();
        String sqlQuery = query.getQueryString();
        sqlQuery = sqlQuery.replace("\n", " ");
        sqlQuery = sqlQuery.replace("\t", " ");
        int numOfRightPythis = 0;
        int startPythis = -1;
        int endPythis = 0;
        boolean hasRightPythis = true;
        while (hasRightPythis) {
            char[] arrStr = sqlQuery.toCharArray();
            hasRightPythis = false;
            int idx = 0;
            for (char c : arrStr) {
                if (idx > startPythis) {
                    if ("(".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis == 0) {
                            startPythis = idx;
                        }
                        numOfRightPythis++;
                    } else if (")".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis > 0) {
                            numOfRightPythis--;
                            if (numOfRightPythis == 0) {
                                endPythis = idx;
                                break;
                            }
                        }
                    }
                }
                idx++;
            }
            if (endPythis > 0) {
                sqlQuery = sqlQuery.substring(0, startPythis) + " # " + sqlQuery.substring(endPythis + 1);
                hasRightPythis = true;
                endPythis = 0;
            }
        }
        String[] arrStr = sqlQuery.substring(0, sqlQuery.toUpperCase().indexOf(" FROM ")).split(",");
        for (String str : arrStr) {
            String[] temp = str.trim().split(" ");
            String alias = temp[temp.length - 1].trim();
            if (alias.contains(".")) {
                alias = alias.substring(alias.lastIndexOf(".") + 1).trim();
            }
            if (alias.contains(",")) {
                alias = alias.substring(alias.lastIndexOf(",") + 1).trim();
            }
            if (alias.contains("`")) {
                alias = alias.replace("`", "");
            }
            if (!aliasColumns.contains(alias)) {
                aliasColumns.add(alias);
            }
        }
        return aliasColumns;
    }
}
