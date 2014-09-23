/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.util;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author bamasyali
 */
public final class PropertyUtils {

    private PropertyUtils() {
    }

    public static <Y extends Object> Y getProperty(Object entity, String fieldName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return (Y) org.apache.commons.beanutils.PropertyUtils.getProperty(entity, fieldName);
    }

    public static void setProperty(Object entity, String fieldName, Object value) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        org.apache.commons.beanutils.PropertyUtils.setProperty(entity, fieldName, value);
    }
}
