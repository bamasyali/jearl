/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author bamasyali
 */
public interface Entity<PRIMARY, USER> extends BaseEntity<PRIMARY>, Serializable {

    Integer getVersion();

    void setVersion(Integer version);

    USER getModifier();

    void setModifier(USER modifier);

    Date getModified();

    void setModified(Date modified);

    String getModifierip();

    void setModifierip(String modifierip);

    Boolean getDeleted();

    void setDeleted(Boolean deleted);
}
