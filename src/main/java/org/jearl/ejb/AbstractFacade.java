/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import org.jearl.ejb.exception.DocumentException;
import org.jearl.ejb.exception.EntityCRUDException;
import org.jearl.ejb.exception.FavoriteException;
import org.jearl.ejb.model.Entity;
import org.jearl.ejb.model.document.Document;
import org.jearl.ejb.model.favorite.Favorite;
import org.jearl.ejb.model.favorite.FavoritePK;
import org.jearl.ejb.model.log.ModifyLog;
import org.jearl.ejb.model.log.ModifyLogType;
import org.jearl.log.JearlLogger;
import org.jearl.log.Logger;
import org.jearl.logback.ErrorCategory;
import org.jearl.util.PropertyUtils;

/**
 * @author Mustafa Burak Amasyalı <bamasyali@hotmail.com.tr>
 * @version 2011.1203 (E.g. ISO 8601 YYYY.MMDD)
 * @since 1.6 (The Java version used)
 */
public abstract class AbstractFacade<T, USER> implements Facade<T, USER> {

    private final Class<T> entityClass;
    private final EntityKey entityKeyName;
    private final String tableName;
    private final boolean boolModifyLog;
    private final JearlLogger logger;

    public AbstractFacade(Class<T> entityClass, boolean boolLogModify) {
        this.entityClass = entityClass;
        this.tableName = entityClass.getAnnotation(Table.class).name();
        this.entityKeyName = initEntityKeyName(entityClass);
        this.boolModifyLog = boolLogModify;
        this.logger = Logger.getJearlLogger(AbstractFacade.class);
        this.logger.info("AbstractFacade created successfully.");
    }

    @Override
    public abstract <Y extends ModifyLog> Y initModifyLog();

    @Override
    public abstract <Y extends Document> Y initDocument();

    @Override
    public abstract <Y extends Favorite<USER>> Y initFavorite();

    @Override
    public abstract <Y extends FavoritePK> Y initFavoritePK();

    @Override
    public abstract EntityManager getEntityManager();

    public static EntityManager getEntityManager(String persistenceUnit) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
        return emf.createEntityManager();
    }

    @Override
    public abstract <Y extends Document> Y findDocument(Integer id) throws DocumentException;

    @Override
    public abstract <Y extends Document> List<Y> findDocuments(T entity) throws DocumentException;

    @Override
    public abstract USER getUser(Integer value);

    @Override
    public String getTableName() {
        return tableName;
    }

    protected String getEntityKeyName() {
        return this.entityKeyName.entityKeyName;
    }

    protected String getTableKeyName() {
        return this.entityKeyName.tableKeyName;
    }

    private static EntityKey initEntityKeyName(Class entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Id primaryKey = field.getAnnotation(Id.class);
            EmbeddedId primaryKey2 = field.getAnnotation(EmbeddedId.class);
            if (primaryKey != null || primaryKey2 != null) {
                String keyName = field.getName();
                String tableKeyName = "";
                if (primaryKey != null) {
                    Column column = field.getAnnotation(Column.class);
                    tableKeyName = column.name();
                }
                return new EntityKey(keyName, tableKeyName);
            }
        }
        return null;
    }

    protected Integer getEntityKey(T entity) {
        this.logger.info("getEntityKey sources initialized.");
        Object temp;
        try {
            temp = PropertyUtils.getProperty(entity, getEntityKeyName());
            this.logger.debug("primary key value = " + temp);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.ACCESS_ERROR, ex);
            temp = null;
        }
        if (temp == null) {
            return null;
        } else if (temp instanceof Integer) {
            return (Integer) temp;
        } else {
            throw new UnsupportedOperationException("Class Type : " + temp.getClass().getName());
        }
    }

    @Override
    public void create(T entity) throws EntityCRUDException {
        this.logger.info("create sources initialized.");
        try {
            getEntityManager().persist(entity);
            this.logger.debug("create successfull..");
            getEntityManager().flush();
            this.logger.debug("flush successfull..");
            if (boolModifyLog && entity instanceof Entity) {
                this.logger.debug("modify log true");
                ModifyLog modifyLog = initModifyLog();
                Entity<T, USER> baseEntity = (Entity) entity;
                modifyLog.setType(ModifyLogType.insert.getValue());
                modifyLog.setModified(baseEntity.getModified());
                modifyLog.setModifierip(baseEntity.getModifierip());
                modifyLog.setTable(tableName);
                modifyLog.setTablekey((Integer) baseEntity.getPrimayKeyAsInteger());
                modifyLog.setModifier(baseEntity.getModifier());
                getEntityManager().persist(modifyLog);
                this.logger.debug("modify log successfull");
            }
            this.logger.info("create finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public void edit(T entity) throws EntityCRUDException {
        this.logger.info("edit sources initialized.");
        try {
            getEntityManager().merge(entity);
            this.logger.debug("edit successfull..");
            getEntityManager().flush();
            this.logger.debug("flush successfull..");
            if (boolModifyLog && entity instanceof Entity) {
                this.logger.debug("modify log true");
                ModifyLog modifyLog = initModifyLog();
                Entity<T, USER> baseEntity = (Entity) entity;
                if (baseEntity.getDeleted()) {
                    modifyLog.setType(ModifyLogType.delete.getValue());
                } else {
                    modifyLog.setType(ModifyLogType.update.getValue());
                }
                modifyLog.setModified(baseEntity.getModified());
                modifyLog.setModifierip(baseEntity.getModifierip());
                modifyLog.setTable(tableName);
                modifyLog.setTablekey((Integer) baseEntity.getPrimayKeyAsInteger());
                modifyLog.setModifier(baseEntity.getModifier());
                getEntityManager().persist(modifyLog);
                this.logger.debug("modify log successfull");
            }
            this.logger.info("edit finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public void remove(T entity) throws EntityCRUDException {
        this.logger.info("remove sources initialized.");
        try {
            if (boolModifyLog && entity instanceof Entity) {
                this.logger.debug("modify log true");
                ModifyLog modifyLog = initModifyLog();
                Entity<T, USER> baseEntity = (Entity) entity;
                modifyLog.setType(ModifyLogType.deletePhysical.getValue());
                modifyLog.setModified(baseEntity.getModified());
                modifyLog.setModifierip(baseEntity.getModifierip());
                modifyLog.setTable(tableName);
                modifyLog.setTablekey((Integer) baseEntity.getPrimayKeyAsInteger());
                modifyLog.setModifier(baseEntity.getModifier());
                getEntityManager().persist(modifyLog);
                this.logger.debug("modify log successfull");
            }
            getEntityManager().remove(getEntityManager().merge(entity));
            this.logger.info("remove finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public void refresh(T entity) throws EntityCRUDException {
        this.logger.info("refresh sources initialized.");
        try {
            getEntityManager().refresh(entity);
            this.logger.info("refresh finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (EntityNotFoundException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public void flush() throws EntityCRUDException {
        this.logger.info("flush sources initialized.");
        try {
            getEntityManager().flush();
            this.logger.info("flush finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public void evictAll() throws EntityCRUDException {
        this.logger.info("evictAll sources initialized.");
        try {
            getEntityManager().getEntityManagerFactory().getCache().evictAll();
            this.logger.info("evictAll finished successfully.");
        } catch (IllegalStateException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.ACCESS_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public T find(Object id) throws EntityCRUDException {
        this.logger.info("find sources initialized.");
        if (id == null) {
            logger.warn("Null Parameter");
            return null;
        }
        try {
            T temp = getEntityManager().find(entityClass, id);
            this.logger.info("find finished successfully.");
            return temp;
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public <Y> Y find(Class<Y> entityClass, Object id) throws EntityCRUDException {
        this.logger.info("find sources initialized.");
        if (id == null) {
            logger.warn("Null Parameter");
            return null;
        }
        try {
            Y temp = getEntityManager().find(entityClass, id);
            this.logger.info("find finished successfully.");
            return temp;
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public T findNative(Object id) throws EntityCRUDException {
        this.logger.info("findNative sources initialized.");
        try {
            String qString = "SELECT * FROM " + tableName + " WHERE " + getTableKeyName() + " = ?";
            this.logger.debug("query = " + qString);
            Query query = getEntityManager().createNativeQuery(qString, entityClass);
            query.setParameter(1, id);
            Object o = (T) query.getSingleResult();
            getEntityManager().refresh(o);
            this.logger.info("findNative finished successfully.");
            return (T) o;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalStateException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (QueryTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PessimisticLockException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (LockTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public <Y> Y findNative(Class<Y> entityClass, Object id) throws EntityCRUDException {
        this.logger.info("findNative sources initialized.");
        try {
            EntityKey keyName = initEntityKeyName(entityClass);
            String qString = "SELECT * FROM " + entityClass.getAnnotation(Table.class).name() + " WHERE " + keyName.getTableKeyName() + " = ?";
            this.logger.debug("query = " + qString);
            Query query = getEntityManager().createNativeQuery(qString, entityClass);
            query.setParameter(1, id);
            Object o = (Y) query.getSingleResult();
            getEntityManager().refresh(o);
            this.logger.info("findNative finished successfully.");
            return (Y) o;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalStateException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (QueryTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PessimisticLockException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (LockTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public List<T> findAll() throws EntityCRUDException {
        return findAll(entityClass);
    }

    @Override
    public <Y extends Object> List<Y> findAll(Class<Y> c) throws EntityCRUDException {
        this.logger.info("findAll sources initialized.");
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(c));
            List<Y> list = getEntityManager().createQuery(cq).getResultList();
            //setLanguage(language, list);
            this.logger.info("findAll finished successfully.");
            return list;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalStateException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (QueryTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PessimisticLockException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (LockTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public List<T> findAllNative() throws EntityCRUDException {
        this.logger.info("findAllNative sources initialized.");
        try {
            String qString = "SELECT * FROM " + tableName;
            this.logger.debug("query = " + qString);
            Query query = getEntityManager().createNativeQuery(qString, entityClass);
            List<T> list = query.getResultList();
            this.logger.info("findAllNative finished successfully.");
            return list;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalStateException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (QueryTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PessimisticLockException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (LockTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public List<T> findByQuery(String queryString, Map<String, Object> parameters) throws EntityCRUDException {
        this.logger.info("findByQuery sources initialized.");
        try {
            this.logger.debug("query = " + queryString);
            Query query = getEntityManager().createQuery(queryString, entityClass);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            List<T> list = query.getResultList();
            this.logger.info("findByQuery finished successfully.");
            return list;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalStateException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (QueryTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PessimisticLockException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (LockTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public List<T> findByNativeQuery(String queryString, Map<Integer, Object> parameters) throws EntityCRUDException {
        this.logger.info("findByNativeQuery sources initialized.");
        try {
            this.logger.debug("query = " + queryString);
            Query query = getEntityManager().createNativeQuery(queryString, entityClass);
            for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            List<T> list = query.getResultList();
            this.logger.info("findByNativeQuery finished successfully.");
            return list;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalStateException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (QueryTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PessimisticLockException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (LockTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public Number getCount() throws EntityCRUDException {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT(t." + getEntityKeyName() + ") FROM " + entityClass.getSimpleName() + " t");
            return (Number) query.getSingleResult();
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (IllegalStateException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (QueryTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PessimisticLockException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (LockTimeoutException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new EntityCRUDException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new EntityCRUDException(ex);
        }
    }

    @Override
    public <Y extends Document> Y uploadDocument(T entity, String fileName, byte[] file, Integer userId, String userIp) throws DocumentException {
        this.logger.info("uploadDocument sources initialized.");
        if (entity == null) {
            this.logger.warn("Null Parameter");
            return null;
        }
        try {
            Y document = initDocument();
            document.setDocTable(tableName);
            document.setDocRecord(getEntityKey(entity) == null ? 0 : getEntityKey(entity));
            document.setDocFilename(fileName);
            document.setDocFile(file);
            document.setVersion(0);
            document.setModifier(getUser(userId));
            document.setModified(new Date(System.currentTimeMillis()));
            document.setModifierip(userIp);
            document.setDeleted(false);
            getEntityManager().persist(document);
            flush();
            this.logger.info("uploadDocument finished successfully.");
            return document;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new DocumentException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new DocumentException(ex);
        }
    }

    @Override
    public <Y extends Document> void updateDocuments(T entity, List<Y> documentList) throws DocumentException {
        this.logger.info("updateDocuments sources initialized.");
        try {
            for (Document document : documentList) {
                if (document.getDocRecord() == 0) {
                    document.setDocRecord(getEntityKey(entity));
                    getEntityManager().merge(document);
                }
            }
            flush();
            this.logger.info("updateDocuments finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new DocumentException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new DocumentException(ex);
        }
    }

    @Override
    public void removeDocument(Integer id) throws DocumentException {
        this.logger.info("removeDocument sources initialized.");
        try {
            Document document = findDocument(id);
            getEntityManager().remove(document);
            this.logger.info("removeDocument finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new DocumentException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new DocumentException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new DocumentException(ex);
        }
    }

    @Override
    public void makeFavorite(Integer user, T entity, Date fvrDate, String fvrIp) throws FavoriteException {
        this.logger.info("makeFavorite sources initialized.");
        if (getEntityKey(entity) == null) {
            this.logger.warn("Null Parameter");
            return;
        }
        try {
            FavoritePK favoritePK = initFavoritePK();
            favoritePK.setTable(tableName);
            favoritePK.setRecord(getEntityKey(entity));
            favoritePK.setUser(user);
            Favorite favorite = initFavorite();
            favorite.setFavoritePK(favoritePK);
            favorite.setUser(user);
            favorite.setDate(fvrDate);
            favorite.setIp(fvrIp);
            getEntityManager().persist(favorite);
            this.logger.info("makeFavorite finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new FavoriteException(ex);
        }
    }

    @Override
    public void removeFavorite(T entity, Integer user) throws FavoriteException {
        this.logger.info("removeFavorite sources initialized.");
        try {
            Favorite favorite = findFavorite(entity, user);
            getEntityManager().remove(favorite);
            this.logger.info("removeFavorite finished successfully.");
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new FavoriteException(ex);
        }
    }

    private Favorite findFavorite(T entity, Integer user) throws FavoriteException {
        this.logger.info("findFavorite sources initialized.");
        if (getEntityKey(entity) == null || user == null) {
            this.logger.warn("Null Parameter");
            return null;
        }
        try {
            FavoritePK favoritePK = initFavoritePK();
            favoritePK.setTable(tableName);
            favoritePK.setRecord(getEntityKey(entity));
            favoritePK.setUser(user);
            Favorite f = getEntityManager().find(Favorite.class, favoritePK);
            this.logger.info("findFavorite finished successfully.");
            return f;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new FavoriteException(ex);
        }
    }

    @Override
    public boolean isFavorite(Integer user, T entity) throws FavoriteException {
        this.logger.info("isFavorite sources initialized.");
        if (user == null || entity == null) {
            this.logger.warn("Null Parameter");
            return false;
        }
        try {
            Favorite favorite = findFavorite(entity, user);
            this.logger.info("isFavorite finished successfully.");
            return favorite != null;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new FavoriteException(ex);
        }
    }

    @Override
    public List<T> findFavorites(Integer user) throws FavoriteException {
        this.logger.info("findFavorites sources initialized.");
        if (user == null) {
            this.logger.warn("Null Parameter");
            return null;
        }
        try {
            Query temp = getEntityManager().createQuery("SELECT f FROM FvrFavorite f WHERE f.FvrFavoritePK.fvrTable = :fvrTable and f.FvrFavoritePK.fvrUser = :fvrUser", Favorite.class);
            temp.setParameter("fvrTable", tableName);
            temp.setParameter("fvrUser", user);
            List<Favorite> result = temp.getResultList();
            List<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < result.size(); i++) {
                list.add(result.get(i).getFavoritePK().getRecord());
            }
            if (list.isEmpty()) {
                return null;
            }
            StringBuilder queryString = new StringBuilder("select t from " + entityClass.getSimpleName() + " t where t." + getEntityKeyName() + "  in (");
            for (Integer id : list) {
                queryString.append(id.toString()).append(",");
            }
            queryString.delete(queryString.length() - 1, queryString.length());
            queryString.append(") and t.deleted = false");
            temp = getEntityManager().createQuery(queryString.toString(), entityClass);
            List<T> returnValue = temp.getResultList();
            this.logger.info("findFavorites finished successfully.");
            return returnValue;
        } catch (TransactionRequiredException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (IllegalArgumentException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.WRONG_PARAMETER_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (EntityExistsException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (PersistenceException ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.DATABASE_ERROR, ex);
            throw new FavoriteException(ex);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ErrorCategory.SYSTEM_ERROR, ex);
            throw new FavoriteException(ex);
        }
    }

    private static class EntityKey {

        private final String entityKeyName;
        private final String tableKeyName;

        public EntityKey(String entityKeyName, String tableKeyName) {
            this.entityKeyName = entityKeyName;
            this.tableKeyName = tableKeyName;
        }

        public String getEntityKeyName() {
            return entityKeyName;
        }

        public String getTableKeyName() {
            return tableKeyName;
        }
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
