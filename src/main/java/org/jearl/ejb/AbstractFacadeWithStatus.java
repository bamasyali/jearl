/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb;

import java.util.Calendar;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.jearl.ejb.exception.EntityCRUDException;
import org.jearl.ejb.model.Entity;
import org.jearl.ejb.model.status.EntityWithStatus;
import org.jearl.ejb.model.status.Status;
import org.jearl.ejb.model.status.StatusLog;

/**
 *
 * @author bamasyali
 */
public abstract class AbstractFacadeWithStatus<T extends EntityWithStatus<STATUS>, STATUS extends Status, STATUSLOG extends StatusLog<T, STATUS, USER>, USER> extends AbstractFacade<T, USER> implements FacadeWithStatus<T, STATUS, STATUSLOG, USER> {

    private final Class<STATUSLOG> statusLogClass;

    public AbstractFacadeWithStatus(Class<T> entityClass, Class<STATUSLOG> statusLogClass, boolean boolLogModify) {
        super(entityClass, boolLogModify);
        this.statusLogClass = statusLogClass;
    }

    @Override
    public abstract STATUS getStatusNew();

    @Override
    public abstract StatusLog<T, STATUS, USER> initStatusLog();

    @Override
    public abstract String getForeignTableName();

    @Override
    public abstract String getForeignTablePrimaryKeyName();

    @Override
    public void create(T entity) throws EntityCRUDException {
        entity.setStatus(getStatusNew());
        super.create(entity);

        StatusLog<T, STATUS, USER> statusLog = initStatusLog();
        statusLog.setEntity(entity);
        statusLog.setLogStatusFrom(entity.getStatus());
        statusLog.setLogStatusFromTime(Calendar.getInstance().getTime());
        if (entity instanceof Entity) {
            statusLog.setUser(((Entity<T, USER>) entity).getModifier());
        }
        getEntityManager().persist(statusLog);
    }

    @Override
    public StatusLog<T, STATUS, USER> getLatestLog(T entity) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<STATUSLOG> root = cq.from(statusLogClass);
        cq = cq.select(root);
        cq = cq.where(cb.equal(root.get(getForeignTableName()).get(getForeignTablePrimaryKeyName()), ((Entity) entity).getPrimayKey()));
        cq.orderBy(cb.desc(root.get("logId")));
        Query query = getEntityManager().createQuery(cq);
        query.setMaxResults(1);
        List<STATUSLOG> statuslogs = query.getResultList();
        return statuslogs.get(0);
    }

    @Override
    public void changeStatus(T entity, STATUS status) throws EntityCRUDException {
        StatusLog<T, STATUS, USER> statusLog = initStatusLog();
        StatusLog<T, STATUS, USER> previousLog = getLatestLog(entity);
        if (previousLog != null) {
            previousLog.setLogStatusTo(status);
            previousLog.setLogStatusToTime(Calendar.getInstance().getTime());

            statusLog.setEntity(entity);
            statusLog.setLogStatusFrom(status);
            statusLog.setLogStatusFromTime(Calendar.getInstance().getTime());
            statusLog.setUser(((Entity<T, USER>) entity).getModifier());

            getEntityManager().merge(previousLog);
            getEntityManager().persist(statusLog);
        }
        entity.setStatus(status);
        getEntityManager().merge(entity);
        flush();
    }
}
