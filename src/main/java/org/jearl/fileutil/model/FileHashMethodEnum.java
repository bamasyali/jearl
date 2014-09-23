/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.fileutil.model;

/**
 *
 * @author bamasyali
 */
public enum FileHashMethodEnum {

    SHA("SHA"), MD5("MD5");
    private final String value;

    private FileHashMethodEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
