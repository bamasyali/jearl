/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.favorite;

/**
 *
 * @author bamasyali
 */
public interface FavoritePK {

    Integer getUser();

    void setUser(Integer fvrUser);

    String getTable();

    void setTable(String fvrTable);

    Integer getRecord();

    void setRecord(Integer fvrRecord);
}
