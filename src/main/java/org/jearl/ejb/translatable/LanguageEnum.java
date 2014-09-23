/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.translatable;

/**
 *
 * @author bamasyali
 */
public enum LanguageEnum {

    TR("tr_TR"), EN("en_US");
    private final String langCode;

    private LanguageEnum(String langCode) {
        this.langCode = langCode;
    }

    public String getLangCode() {
        return langCode;
    }
}
