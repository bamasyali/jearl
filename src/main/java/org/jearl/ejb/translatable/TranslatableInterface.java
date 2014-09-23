/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.translatable;

/**
 *
 * @author bamasyali
 */
public interface TranslatableInterface {

    String getValue(String field);

    void setValue(String field, String value);
}
