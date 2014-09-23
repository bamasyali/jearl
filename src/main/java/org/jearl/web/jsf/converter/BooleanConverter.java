/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author bamasyali
 */
public class BooleanConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }
}
