/*
 * Copyright (C) 2010 HRPlus. All rights reserved.
 * HRPLUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base;

/**
 * Doi tuong tuong ung voi cac option trong combo box.
 * @author DUONGPV
 * @since 1.0
 * @version 1.0
 */
public class ListBoxBean {

    /** Name */
    private String name;
    /** Value */
    private String value;
    
    public ListBoxBean() {
    }
    
    /**
     * Ham tao.
     * @param name Ten
     * @param value Gia tri
     */
    public ListBoxBean(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    /**
     * Ham tao.
     * @param name Ten
     * @param value Gia tri
     */
    public ListBoxBean(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public ListBoxBean(String name, Long value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
