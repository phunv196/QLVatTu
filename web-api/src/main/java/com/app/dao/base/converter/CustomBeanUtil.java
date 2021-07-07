/*
 * Copyright (C) 2010 HRPlus. All rights reserved.
 * HRPLUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base.converter;

import com.app.dao.base.CommonUtils;
import org.apache.commons.beanutils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Tiện ích hỗ trợ việc đính kèm file.
 *
 * @author HuyenNV
 * @since 1.0
 * @version 1.0
 */
public class CustomBeanUtil {

    /**
     * Doi tuong BeanUtilsBean
     */
    private final BeanUtilsBean beanUtilsBean;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomBeanUtil.class);

    /**
     * Lay doi tuong BeanUtilsBean.
     */
    public CustomBeanUtil() {
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new LongConverter(), Long.class);
        convertUtilsBean.register(new DoubleConverter(), Double.class);
        convertUtilsBean.register(new DateConverter(), Date.class);
        convertUtilsBean.register(new StringConverter(), String.class);
        beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
    }

    /**
     * Copy properties tu form sang BO va nguoc lai.
     *
     * @param dest Doi tuong dich
     * @param orig Doi tuong nguon
     * @throws Exception Exception
     */
    public void copyProperties(Object dest, Object orig) throws Exception {
        beanUtilsBean.copyProperties(dest, orig);
    }

    /**
     * Reset cac thuoc tinh ve null. VD co khi chung ta can reset form.
     *
     * @param obj Doi tuong
     * @throws Exception
     */
    public void reset(Object obj) throws Exception {
        Map map1 = BeanUtils.describe(obj);
        Map<String, String> map2 = new HashMap();
        Set<Entry<String, String>> entrySet = map1.entrySet();
        for (Entry<String, String> e : entrySet) {
            map2.put(e.getKey(), null);
        }
        beanUtilsBean.populate(obj, map2);
    }

    public Map describe(Object obj) throws Exception {
        return beanUtilsBean.describe(obj);
    }

    /**
     * Convert ve Double, can xu ly truong hop String.
     *
     * @author gpdn_hoangch
     * @since 1.0
     * @version 1.0
     */
    class DoubleConverter implements Converter {

        @Override
        public Object convert(Class type, Object value) {
            if (value == null) {
                return null;
            } else if (value instanceof String) {
                return (value.toString().length() == 0) ? null : Double.parseDouble(value.toString());
            } else {
                return value;
            }
        }
    }

    /**
     * Convert Date, can xu ly truong hop String.
     *
     * @author gpdn_hoangch
     * @since 1.0
     * @version 1.0
     */
    class DateConverter implements Converter {

        @Override
        public Object convert(Class type, Object value) {
            if (value == null) {
                return null;
            } else if (value instanceof String) {
                try {
                    return CommonUtils.convertStringToDate(value.toString());
                } catch (Exception ex) {
                    LOGGER.debug("debug", ex);
                    return null;
                }
            } else {
                return value;
            }
        }
    }

    /**
     * Cac truong hop Combobox, khi -chon tat ca-, -chon-, khong co gia tri gi
     * nhung submit ve bang 0.
     *
     * @author gpdn_hoangch
     * @since 1.0
     * @version 1.0
     */
    class LongConverter implements Converter {

        @Override
        public Object convert(Class type, Object value) {
            if (value == null) {
                return null;
            } else if (value instanceof Long) {
                return (value.equals(0L)) ? null : value;
            } else if (value instanceof String) {
                try {
                    return (value.toString().length() == 0) ? null : Long.parseLong(value.toString());
                } catch (NumberFormatException ex) {
                    return null;
                }
            } else {
                return value;
            }
        }
    }

    /**
     * Convert ve String, can xu ly truong hop Date.
     *
     * @author gpdn_hoangch
     * @since 1.0
     * @version 1.0
     */
    class StringConverter implements Converter {

        @Override
        public Object convert(Class type, Object value) {
            if (value == null) {
                return null;
            } else if (value instanceof Date) {
                return CommonUtils.convertDateToString((Date) value);
            } else if (value instanceof Long) {
                return CommonUtils.formatNumber((Long) value);
            } else if (value instanceof Double) {
                return CommonUtils.formatNumber((Double) value);
            } else {
                return value.toString();
            }
        }
    }
}
