/*
 * Copyright (C) 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Doi tuong header de export Excel dong.
 * @author HuyenNV
 * @since 1.0
 * @version 1.0
 */
public class ExportHeaderBean {

    /** Cot dau tien */
    private int firstColumn;
    /** Cot cuoi cung */
    private int lastColumn;
    /** Cot cho cac gia tri null */
    private int nullColumn;
    /** Cac thanh phan */
    private Map<Long, Integer> elements;

    public ExportHeaderBean() {
        elements = new HashMap<Long, Integer>();
    }
    
    public ExportHeaderBean(int firstColumn) {
        this.firstColumn = firstColumn;
        this.lastColumn = firstColumn;
        elements = new HashMap<Long, Integer>();
    }

    public void addNullColumn() {
        nullColumn = lastColumn;
        lastColumn++;
    }

    public void addColumn(Long objectId) {
        elements.put(objectId, lastColumn);
        lastColumn++;
    }
    
    public void addColumn(Long objectId, Integer column) {
        elements.put(objectId, column);
    }
    
    public Integer getColumn(Long objectId) {
        if (objectId == null) {
            return nullColumn;
        } else {
            return elements.get(objectId);
        }
    }

    public int getLastColumn() {
        return lastColumn;
    }

    public int getFirstColumn() {
        return firstColumn;
    }
}
