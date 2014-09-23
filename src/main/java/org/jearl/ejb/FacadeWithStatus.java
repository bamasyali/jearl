/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb;

import org.jearl.ejb.exception.EntityCRUDException;
import org.jearl.ejb.model.status.EntityWithStatus;
import org.jearl.ejb.model.status.Status;
import org.jearl.ejb.model.status.StatusLog;

/**
 *
 * @author bamasyali
 */
public interface FacadeWithStatus<T extends EntityWithStatus<STATUS>, STATUS extends Status, STATUSLOG extends StatusLog<T, STATUS, USER>, USER> extends Facade<T, USER> {

    STATUS getStatusNew();

    StatusLog<T, STATUS, USER> initStatusLog();

    String getForeignTableName();

    String getForeignTablePrimaryKeyName();

    StatusLog<T, STATUS, USER> getLatestLog(T entity);

    void changeStatus(T entity, STATUS status) throws EntityCRUDException;
}
