/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.favorite;

import java.util.Date;

/**
 *
 * @author bamasyali
 */
public interface Favorite<USER> {

    FavoritePK getFavoritePK();

    void setFavoritePK(FavoritePK favoritePK);

    Date getDate();

    void setDate(Date fvrDate);

    String getIp();

    void setIp(String fvrIp);

    USER getUser();

    void setUser(USER value);
}
