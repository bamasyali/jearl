/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.jearl.ejb.exception.DocumentException;
import org.jearl.ejb.exception.EntityCRUDException;
import org.jearl.ejb.exception.FavoriteException;
import org.jearl.ejb.model.document.Document;
import org.jearl.ejb.model.favorite.Favorite;
import org.jearl.ejb.model.favorite.FavoritePK;
import org.jearl.ejb.model.log.ModifyLog;

/**
 *
 * @author bamasyali
 */
public interface Facade<T, USER> {

    EntityManager getEntityManager();

    USER getUser(Integer value);

    String getTableName();

    <Y extends ModifyLog> Y initModifyLog();

    <Y extends Document> Y initDocument();

    <Y extends Favorite<USER>> Y initFavorite();

    <Y extends FavoritePK> Y initFavoritePK();

    void create(T entity) throws EntityCRUDException;

    void edit(T entity) throws EntityCRUDException;

    void remove(T entity) throws EntityCRUDException;

    void refresh(T entity) throws EntityCRUDException;

    void flush() throws EntityCRUDException;

    void evictAll() throws EntityCRUDException;

    T find(Object id) throws EntityCRUDException;

    <Y extends Object> Y find(Class<Y> entityClass, Object id) throws EntityCRUDException;

    T findNative(Object id) throws EntityCRUDException;

    <Y extends Object> Y findNative(Class<Y> entityClass, Object id) throws EntityCRUDException;

    List<T> findAll() throws EntityCRUDException;

    <Y extends Object> List<Y> findAll(Class<Y> c) throws EntityCRUDException;

    List<T> findAllNative() throws EntityCRUDException;

    List<T> findByQuery(String query, Map<String, Object> parameters) throws EntityCRUDException;

    List<T> findByNativeQuery(String query, Map<Integer, Object> parameters) throws EntityCRUDException;

    Number getCount() throws EntityCRUDException;

    <Y extends Document> Y uploadDocument(T entity, String fileName, byte[] file, Integer userId, String userIp) throws DocumentException;

    <Y extends Document> Y findDocument(Integer id) throws DocumentException;

    <Y extends Document> List<Y> findDocuments(T entity) throws DocumentException;

    <Y extends Document> void updateDocuments(T entity, List<Y> documentList) throws DocumentException;

    void removeDocument(Integer id) throws DocumentException;

    void makeFavorite(Integer user, T entity, Date fvrDate, String fvrIp) throws FavoriteException;

    void removeFavorite(T entity, Integer user) throws FavoriteException;

    boolean isFavorite(Integer user, T entity) throws FavoriteException;

    List<T> findFavorites(Integer user) throws FavoriteException;
}
