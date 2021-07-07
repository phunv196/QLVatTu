package com.app.dao.base;/*
 * Copyright (C) 2010 HRPlus. All rights reserved.
 * HRPLUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.*;

/**
 * Lop co ban de thao tac voi CSDL.
 *
 * @author HoangCH, HuyenNV
 * @version 1.0
 * @since 1.0
 */
public class BaseHibernateDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseHibernateDAO.class);
    /**
     * Ngon ngu su dung trong sap xep
     */
    private static final String SORTING_LANGUAGE = "Vietnamese";
    //<editor-fold defaultstate="collapsed" desc="Hang, bien, phuong thuc static">
    public static ThreadLocal<Session> threadLocal = new ThreadLocal();
    public static final String configFile = "hibernate.cfg.xml";
    public static final Configuration configuration = new Configuration();
    private static SessionFactory sessionFactory;
    Session session = null;
    static {
        try {
            configuration.configure(configFile);
//            loadEncryptedDBConfig(configuration);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cac phuong thuc co ban">
    public Session getSession() throws HibernateException {
        if (session == null) {
            session = (Session) sessionFactory.openSession();
        }
        return session;
    }

    public void beginTransaction() {
        getSession().beginTransaction();
    }

    public void commitTransaction() {
        Session session = getSession();
        if (session != null && session.isOpen() && session.getTransaction().isActive()) {
            session.getTransaction().commit();
            flushSession();
            session.close();
            session = null;
        }
    }

    public void rollbackTransaction() {
        // Rollback when error occurs
        Session session = getSession();
        if (session != null && session.isOpen() && session.getTransaction().isActive()) {
            session.getTransaction().rollback();
            session.clear();
        }
    }

    public Session openThreadSession() throws Exception {
        Session session = (Session) threadLocal.get();
        if (session != null) {
            if (session.isOpen()) {
                session.close();
            }
            threadLocal.remove();
        }
        if (session == null || !session.isOpen()) {
            BaseHibernateDAO.setSessionFactory(getSessionFactory());
            session = sessionFactory == null ? null : ((Session) (sessionFactory.openSession()));
            threadLocal.set(session);
        }
        return session;
    }

    public static SessionFactory getSessionFactory() throws Exception {
        if (sessionFactory != null) {
            return sessionFactory;
        }
        configuration.configure(configFile);
        //loadEncryptedDBConfig(configuration);
        return configuration.buildSessionFactory();

    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        BaseHibernateDAO.sessionFactory = sessionFactory;
    }

    public void closeSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tim kiem">
    public <T> List<T> getAll(Class<T> tableName, String orderColumn) {
        Session session = getSession();
        String hql = " FROM " + tableName.getName() + (CommonUtils.isNullOrEmpty(orderColumn) ? "" : (" ORDER BY " + orderColumn));
        Query query = session.createQuery(hql);
        return query.list();
    }

    public <T> List<T> findByProperty(Class<T> tableName, String propertyName, Object value, String orderClause) {
        String hql = " FROM " + tableName.getName() + " t WHERE t." + propertyName + " = ? ";
        if (!CommonUtils.isNullOrEmpty(orderClause)) {
            hql += " ORDER BY " + orderClause;
        }
        Query query = getSession().createQuery(hql);
        query.setParameter(0, value);
        return query.list();
    }

    /**
     *
     * @param <T>
     * @param tableName
     * @param pairs
     * @return
     */
    public <T> List<T> findByForeignKey(Class<T> tableName, Object... pairs) {
        String hql = " FROM " + tableName.getName() + " t WHERE 1 = 1 ";

        if (pairs != null && pairs.length % 2 == 0) {
            int index = 0;
            for (Object obj : pairs) {
                if (index % 2 == 0) {
                    hql += " AND t." + (String) obj + " = ";
                } else {
                    hql += (Long) obj;
                }
                index++;
            }
        }
        Query query = getSession().createQuery(hql);
        return query.list();
    }

    public <T> List<T> findByProperties(Class<T> tableName, Object... pairs) {
        String hql = " FROM " + tableName.getName() + " t WHERE 1 = 1 ";
        List<Object> lstParam = new ArrayList();

        if (pairs != null && pairs.length % 2 == 0) {
            int index = 0;
            for (Object obj : pairs) {
                if (index % 2 == 0) {
                    hql += " AND t." + (String) obj + " = ?";
                } else {
                    lstParam.add(obj);
                }
                index++;
            }
        }
        Query query = getSession().createQuery(hql);
        for (Integer pos = 0; pos < lstParam.size(); pos++) {
            query.setParameter(pos, lstParam.get(pos));
        }
        return query.list();
    }

    public <T> List<T> findByProperties( String orderClause, Class<T> tableName, Object... pairs) {
        String hql = " FROM " + tableName.getName() + " t WHERE 1 = 1 ";
        List<Object> lstParam = new ArrayList();

        if (pairs != null && pairs.length % 2 == 0) {
            int index = 0;
            for (Object obj : pairs) {
                if (index % 2 == 0) {
                    hql += " AND t." + (String) obj + " = ?";
                } else {
                    lstParam.add(obj);
                }
                index++;
            }
        }
        if (!CommonUtils.isNullOrEmpty(orderClause)) {
            hql += " ORDER BY " + orderClause;
        }
        Query query = getSession().createQuery(hql);
        for (Integer pos = 0; pos < lstParam.size(); pos++) {
            query.setParameter(pos, lstParam.get(pos));
        }
        return query.list();
    }

    public <T> List<T> findByIds(Class<T> tableName, String idColumn, String ids, String orderColumn) {
        StringBuilder hql = new StringBuilder(" FROM " + tableName.getName() + " t ");
        if (!ids.isEmpty()) {
            hql.append(" WHERE t.").append(idColumn).append(" IN (:ids) ");
        }
        if (!CommonUtils.isNullOrEmpty(orderColumn)) {
            hql.append(" ORDER BY ").append("t.").append(orderColumn);
        }
        Query query = getSession().createQuery(hql.toString());
        if (!ids.isEmpty()) {
            query.setParameterList("ids", CommonUtils.string2ListLong(ids, ","));
        }
        return query.list();
    }

    public <T> List getResultQueryList(Query query, String parameter, List<T> listParams) {
        List result = new ArrayList();
        List<List<T>> partitions = CommonUtils.partition(listParams, 999);
        for (List<T> partition : partitions) {
            query.setParameterList(parameter, partition);
            result.addAll(query.list());
        }
        return result;
    }

    public <T> List<T> findByIds(Class<T> tableName, String idColumn, List ids, String... orderColumn) {
        StringBuilder hql = new StringBuilder(" FROM " + tableName.getName() + " t");
        hql.append(" where ").append(idColumn).append(" in (:ids)");
        int length = orderColumn.length;
        if (length > 0) {
            String temp = "";
            for (int i = 0; i < length; i++) {
                if (!CommonUtils.isNullOrEmpty(orderColumn[i])) {
                    temp += (i > 0 ? ", " : "") + "t." + orderColumn[i];
                }
            }
            if (!temp.isEmpty()) {
                hql.append(" ORDER BY ").append(temp);
            }
        }
        Query query = getSession().createQuery(hql.toString());
        return getResultQueryList(query, "ids", ids);
    }

    public <T> T get(Class<T> entityClass, Serializable id) throws Exception {
        return id == null ? null : get(entityClass, id, null);
    }

    public <T> Object getProperty(Class<T> entityClass, Serializable id, String property) throws Exception {
        Object obj = get(entityClass, id, null);
        if (obj == null) {
            return null;
        } else {
            return new PropertyUtilsBean().describe(obj).get(property);
        }
    }

    /**
     * Can bo di (NEED DELETE)
     *
     * @param <T>
     * @param entityClass
     * @param id
     * @param lockMode
     * @return
     */
    public <T> T get(final Class<T> entityClass, final Serializable id, final LockMode lockMode) {
        if (lockMode != null) {
            return (T) getSession().get(entityClass, id, lockMode);
        } else {
            return (T) getSession().get(entityClass, id);

        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Luu vao DB">
    public void save(Object objectToSave) {
        getSession().save(objectToSave);
    }

    public void update(Object entity) {
        getSession().evict(entity);
        getSession().merge(entity);
    }

    public void saveOrUpdate(final Object entity) {
        getSession().saveOrUpdate(entity);

    }

    /**
     * Can bo di (NEED DELETE)
     *
     * @param entities
     */
    public void saveOrUpdateAll(final Collection entities) {
        for (Object entity : entities) {
            getSession().saveOrUpdate(entity);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Delete">
    public void delete(Object entity) {
        getSession().delete(entity);
    }

    public void deleteByIds(List<Long> arrId, Class className, String idColumn) {
        if ((arrId != null) && !arrId.isEmpty()) {
            StringBuilder hql = new StringBuilder(" DELETE FROM " + className.getName() + " t WHERE 1 = 1 ");
            List<List<Long>> parList = CommonUtils.partition(arrId, 999);
            int parSize = parList.size();
            if (parSize > 0) {
                for (int i = 0; i < parSize; i++) {
                    hql.append(" AND t.").append(idColumn).append(" IN (:ids_").append(i).append(") ");
                }
                Query query = createQuery(hql.toString());
                for (int i = 0; i < parSize; i++) {
                    query.setParameterList("ids_" + i, parList.get(i));
                }
                query.executeUpdate();
            }
        }
    }

    public void deleteByIds(Long[] arrId, Class className, String idColumn) {
        if ((arrId != null) && (arrId.length > 0)) {
            String hql = " DELETE FROM " + className.getName() + " t WHERE t." + idColumn + " IN (:arrId) ";
            Query query = createQuery(hql);
            query.setParameterList("arrId", arrId);
            query.executeUpdate();
        }
    }

    public void deleteById(Long id, Class className, String idColumn) {
        String hql = " DELETE FROM " + className.getName() + " t WHERE t." + idColumn + " = ? ";
        Query query = createQuery(hql);
        query.setParameter(0, id);
        query.executeUpdate();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cac phuong thuc chung">
    public void flushSession() {
        getSession().flush();
        getSession().clear();
    }

    public Query createQuery(String hql) {
        return getSession().createQuery(hql);
    }

    public SQLQuery createSQLQuery(String sql) {
        return getSession().createSQLQuery(sql);
    }

    public Criteria createCriteria(Class type) {
        return getSession().createCriteria(type);
    }

    public void setReadOnly(Object o, boolean flag) {
        getSession().setReadOnly(o, flag);
    }

//    public CallableStatement prepareCall(String sql) throws Exception {
//        return getSession().connection().prepareCall(sql);
//    }
//
//    public PreparedStatement prepareStatement(String sql) throws Exception {
//        return getSession().connection().prepareStatement(sql);
//    }

    public long getSequence(String sequenceName) throws Exception {
        String sql = "SELECT " + sequenceName + " .NextVal FROM Dual";
        Query query = getSession().createSQLQuery(sql);
        BigDecimal bigDecimal = (BigDecimal) query.uniqueResult();
        return bigDecimal.longValue();
    }

    public long getAutoIncrement(String tableName) throws Exception {
        String sql = "SELECT AUTO_INCREMENT"
                + " FROM information_schema.tables"
                + " WHERE table_name = :tableName and table_schema=DATABASE()";
        SQLQuery query = createSQLQuery(sql);
        query.setParameter("tableName", tableName);
        BigInteger bigDecimal = (BigInteger) query.uniqueResult();
        //set next AUTO_INCREMENT
//        query = createSQLQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT  = :nextValue");
//        query.setParameter("nextValue", bigDecimal.longValue() + 1);
//        query.executeUpdate();
        return bigDecimal.longValue() + 1;
    }

    public List<String> getLstUserSequence() throws Exception {
        String sql = "SELECT lower(sequence_name) FROM USER_SEQUENCES";
        SQLQuery query = getSession().createSQLQuery(sql);
        return query.list();
    }

    public boolean duplicate(Class className, String idColumn, Long idValue, String codeColumn, String codeValue) {
        String hql = " SELECT COUNT(*) FROM " + className.getName() + " t WHERE LOWER(t." + codeColumn + ") = ? ";
        if (idValue != null && idValue.intValue() > 0) {
            hql += " AND t." + idColumn + " != ? ";
        }
        Query query = createQuery(hql);
        query.setParameter(0, codeValue.trim().toLowerCase());
        if (idValue != null && idValue.intValue() > 0) {
            query.setParameter(1, idValue);
        }
        query.setMaxResults(1);
        Long count = (Long) query.uniqueResult();
        return count > 0;
    }

    public boolean hasConstraint(Class className, String idColumn, Long idValue) {
        String hql = " SELECT COUNT(*) FROM " + className.getName() + " t WHERE t." + idColumn + " = ? ";
        Query query = createQuery(hql);
        query.setParameter(0, idValue);
        query.setMaxResults(1);
        Long count = (Long) query.uniqueResult();
        return count > 0;
    }

    /**
     * ham set result transformer cua cau query
     *
     * @param query cau query
     * @param obj doi tuong
     */
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

    public List getResultQueryList(Query query, List params, String param) {
        List result = new ArrayList();
        List<List> partitions = CommonUtils.partition(params, 900);
        for (List partition : partitions) {
            query.setParameterList(param, partition);
            result.addAll(query.list());
        }
        return result;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Cac ham tien ich dung khi Order theo Unicode">
    public static String buildOrderString(String... columns) {
        StringBuilder fragment = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
//            if ("".equals(SORTING_LANGUAGE)) {
            fragment.append(columns[i]);
//            } else {
//                fragment.append(" nlssort(").append(columns[i]).append(",'NLS_SORT=").append(SORTING_LANGUAGE).append("') ");
//            }
            fragment.append(" asc");
            if (i < columns.length - 1) {
                fragment.append(", ");
            }
        }

        return fragment.toString();
    }

    public Order asc(String propertyName) {
        return new CustomOrder(propertyName, true);
    }

    public Order ascNullsFirst(String propertyName) {
        return new CustomOrder(propertyName, true, true);
    }

    public Order ascNullsLast(String propertyName) {
        return new CustomOrder(propertyName, true, false);
    }

    public Order desc(String propertyName) {
        return new CustomOrder(propertyName, false);
    }

    public Order descNullsFirst(String propertyName) {
        return new CustomOrder(propertyName, false, true);
    }

    public Order descNullsLast(String propertyName) {
        return new CustomOrder(propertyName, false, false);
    }

    public class CustomOrder extends Order implements Serializable {

        private final boolean ascending;
        private boolean ignoreCase;
        private final String propertyName;
        private boolean nulls = false; //nulls first nulls last

        @Override
        public String toString() {
            return propertyName + ' ' + (ascending ? "asc" : "desc");
        }

        @Override
        public CustomOrder ignoreCase() {
            ignoreCase = true;
            return this;
        }

        /**
         * Constructor for CustomOrder.
         *
         * @param propertyName
         * @param ascending
         */
        protected CustomOrder(String propertyName, boolean ascending) {
            super(propertyName, ascending);
            this.propertyName = propertyName;
            this.ascending = ascending;
        }

        protected CustomOrder(String propertyName, boolean ascending, boolean nulls) {
            super(propertyName, ascending);
            this.propertyName = propertyName;
            this.ascending = ascending;
            this.nulls = nulls;
        }

        /**
         * Render the SQL fragment
         *
         * @param criteria
         * @param criteriaQuery
         * @return
         */
        @Override
        public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
                throws HibernateException {
            String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
            Type type = criteriaQuery.getTypeUsingProjection(criteria, propertyName);
            StringBuilder fragment = new StringBuilder();
            for (int i = 0; i < columns.length; i++) {
                SessionFactoryImplementor factory = criteriaQuery.getFactory();
                boolean lower = ignoreCase && type.sqlTypes(factory)[i] == Types.VARCHAR;

                if (lower) {
                    fragment.append(factory.getDialect().getLowercaseFunction()).append('(');
                }

                if ("".equals(SORTING_LANGUAGE)) {
                    fragment.append(columns[i]);
                    fragment.append(ascending ? " asc" : " desc");
                } else {
                    fragment.append(" nlssort(").append(columns[i]).append(",'NLS_SORT=").append(SORTING_LANGUAGE).append("')");
                    fragment.append(ascending ? " asc" : " desc");
                    fragment.append(" nulls ").append(this.nulls == false ? "first" : "last");
                }

                if (lower) {
                    fragment.append(')');
                }

                if (i < columns.length - 1) {
                    fragment.append(", ");
                }
            }
            return fragment.toString();
        }
    }
    //</editor-fold>
}
