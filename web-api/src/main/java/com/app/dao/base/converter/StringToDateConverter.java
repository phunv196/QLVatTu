/*
 * Copyright (C) 2010 HRPlus. All rights reserved.
 * HRPLUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base.converter;

import com.app.dao.base.CommonUtils;
import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert Date, can xu ly truong hop String.
 *
 * @author gpdn_hoangch
 * @since 1.0
 * @version 1.0
 */
public class StringToDateConverter implements Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringToDateConverter.class);

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
