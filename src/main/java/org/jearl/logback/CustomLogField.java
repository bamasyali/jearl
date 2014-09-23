/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.logback;

/**
 *
 * @author can
 */
public enum CustomLogField {

    LOG_CONTAINER("application"),
    ERROR_CATEGORY("category"),
    LOG_USER("user"),
    TEMP_STACKTRACE("temp");
    private String value;

    private CustomLogField(String string) {
        value = string;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
