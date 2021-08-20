/*
 * Copyright (C) 2010 HRPlus. All rights reserved.
 * HRPLUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base.converter;

import com.app.dao.base.CommonUtils;
import org.apache.commons.beanutils.Converter;

import java.util.Date;

/**
 * Convert ve String, can xu ly truong hop Date.
 * @author gpdn_hoangch
 * @since 1.0
 * @version 1.0
 */
public class DateToStringConverter implements Converter {

    @Override
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Date) {
            return CommonUtils.convertDateToString((Date) value);
        } else if (value instanceof Double) {
            return CommonUtils.formatNumber((Double) value);
        } else if (value instanceof Long) {
            return CommonUtils.formatNumber((Long) value);
        } else {
            return value.toString();
        }
    }
}
