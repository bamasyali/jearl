/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.logback;

/**
 *
 * @author can
 */
public enum ErrorCategory {

    SYSTEM_ERROR("100"),
    DATABASE_ERROR("101"),
    EMAIL_ERROR("102"),
    USER_ERROR("103"),
    ACCESS_ERROR("104"),
    NULL_PARAMETER_ERROR("105"),
    WRONG_PARAMETER_ERROR("106"),
    LOGIN_ERROR("107");
    private String value;

    private ErrorCategory(String i) {
        value = i;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
