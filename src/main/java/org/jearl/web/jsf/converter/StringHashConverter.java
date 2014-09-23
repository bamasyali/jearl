/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.jsf.converter;

import java.math.BigInteger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author bamasyali
 */
public class StringHashConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        }
        BigInteger bigInteger = new BigInteger(value.getBytes());
        return bigInteger.toString();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        BigInteger bigInteger = new BigInteger(value.toString());
        return new String(bigInteger.toByteArray());
    }
}
