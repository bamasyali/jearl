/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.log;

import java.util.Date;

/**
 *
 * @author bamasyali
 */
public interface ModifyLog<USER> {

    String getTable();

    void setTable(String value);

    int getTablekey();

    void setTablekey(Integer value);

    Date getModified();

    void setModified(Date mlogModified);

    String getModifierip();

    void setModifierip(String mlogModifierip);

    USER getModifier();

    void setModifier(USER value);

    Integer getType();

    void setType(Integer mlogType);
}
