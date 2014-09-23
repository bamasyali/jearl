/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.jearl.ejb.Facade;
import org.jearl.ejb.exception.DocumentException;
import org.jearl.ejb.exception.EntityCRUDException;
import org.jearl.ejb.exception.FavoriteException;
import org.jearl.ejb.model.Entity;
import org.jearl.ejb.model.document.Document;
import org.jearl.ejb.translatable.LanguageEnum;
import org.jearl.logback.ErrorCategory;
import org.jearl.logback.Log;
import org.jearl.logback.LogService;
import org.jearl.web.jsf.exception.ManagedBeanException;
import org.jearl.web.jsf.message.BeanMessages;
import org.jearl.web.jsf.message.BeanMessagesImpl;
import org.jearl.web.jsf.message.Message;
import org.jearl.web.session.exception.SessionException;

/**
 *
 * @author amasyalim
 */
public abstract class AbstractManagedBean<T, PRIMARY, USER> extends AbstractBasicManagedBean implements ManagedBean<T, PRIMARY, USER>, Serializable {

    private static final String CONTAINER = "JEARL";
    private static final Log log = new LogService(AbstractManagedBean.class);
    private static final String ERROR_MESSAGE = "Error";
    private T entity;
    private final String className;
    private final boolean hasSession;
    private List<Document> documentList;

    public AbstractManagedBean(boolean hasSession) {
        super();
        this.className = this.getClass().getSimpleName();
        this.hasSession = hasSession;
        this.documentList = null;
        try {
            T temp = session.<T>readFromSession(className);
            if (temp != null && hasSession) {
                this.entity = temp;
            } else {
                this.entity = newEntity();
            }
        } catch (SessionException ex) {
            this.entity = newEntity();
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
        } catch (Exception ex) {
            this.entity = newEntity();
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
        }
        try {
            onValueChange();
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
        }
    }

    @Override
    public abstract T newEntity();

    @Override
    public abstract T newEntity(PRIMARY id);

    @Override
    public abstract PRIMARY getId(T entity);

    @Override
    public abstract String getName(T entity);

    @Override
    public abstract Facade<T, USER> getFacade();

    @Override
    public BeanMessages getBeanMessages() {
        return new BeanMessagesImpl();
    }

    @Override
    public abstract List<T> getList();

    @Override
    public abstract List<SelectItem> getSelectItems();

    @Override
    public abstract LanguageEnum getLanguage();

    protected void onValueChange() {
        this.documentList = null;
    }

    @Override
    public PRIMARY getId() {
        return getId(entity);
    }

    @Override
    public void setId(PRIMARY value) {
        setId(value, getBeanMessages().getSetIdSuccessMessage(), getBeanMessages().getSetIdErrorMessage());
    }

    protected final Boolean setId(PRIMARY value, Message successMessage, Message errorMessage) {
        try {
            setEntity(getFacade().find(value));
            setInfo(successMessage);
            return true;
        } catch (EntityCRUDException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public void setEntity(T entity) {
        setEntity(entity, getBeanMessages().getSetEntitySuccessMessage(), getBeanMessages().getSetEntityErrorMessage());
    }

    protected final Boolean setEntity(T entity, Message successMessage, Message errorMessage) {
        try {
            if (entity == null) {
                log.warn("Entity Set Null");
                this.entity = newEntity();
            } else {
                this.entity = entity;
            }
            if (hasSession) {
                if (this.entity == null) {
                    session.deleteFromSession(className);
                } else {
                    session.writeToSession(className, this.entity);
                }
            }
            onValueChange();
            setInfo(successMessage);
            return true;
        } catch (SessionException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public boolean isHasObject() {
        return getId() != null;
    }

    @Override
    public final void loadBaseVariables(Boolean deleted, Boolean isCreate) {
        if (isInheritedFromLoggerInterface()) {
            Entity myEntity = (Entity) this.entity;
            myEntity.setDeleted(deleted);
            myEntity.setModified(new Date(System.currentTimeMillis()));
            if (userId != null) {
                myEntity.setModifier(getFacade().getUser(userId));
            }
            myEntity.setModifierip(userIp);
            if (isCreate) {
                myEntity.setVersion(0);
            }
        }
    }

    @Override
    public String create() {
        create(getBeanMessages().getCreateSuccessMesage(), getBeanMessages().getCreateErrorMesage());
        return null;
    }

    protected final Boolean create(Message successMessage, Message errorMessage) {
        try {
            loadBaseVariables(false, true);
            getFacade().create(entity);
            getFacade().updateDocuments(getEntity(), getDocumentList());
            setEntity(entity);
            setInfo(successMessage);
            return true;
        } catch (EntityCRUDException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (DocumentException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public String edit() {
        edit(getBeanMessages().getEditSuccessMessage(), getBeanMessages().getEditErrorMessage());
        return null;
    }

    protected final Boolean edit(Message successMessage, Message errorMessage) {
        try {
            loadBaseVariables(false, false);
            getFacade().edit(entity);
            getFacade().updateDocuments(getEntity(), getDocumentList());
            setEntity(getFacade().find(getId()));
            setInfo(successMessage);
            return true;
        } catch (EntityCRUDException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (DocumentException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public String remove() {
        remove(getBeanMessages().getEditSuccessMessage(), getBeanMessages().getRemoveErrorMessage());
        return null;
    }

    protected final Boolean remove(Message successMessage, Message errorMessage) {
        try {
            loadBaseVariables(true, false);
            getFacade().edit(entity);
            setEntity(null);
            setInfo(successMessage);
            return true;
        } catch (EntityCRUDException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public String removePhysical() {
        removePhysical(getBeanMessages().getRemovePyhsicalSuccessMessage(), getBeanMessages().getRemovePyhsicalErrorMessage());
        return null;
    }

    protected final Boolean removePhysical(Message successMessage, Message errorMessage) {
        try {
            loadBaseVariables(true, false);
            getFacade().remove(entity);
            setEntity(null);
            setInfo(successMessage);
            return true;
        } catch (EntityCRUDException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public String reload() {
        reload(getBeanMessages().getReloadSuccessMessage(), getBeanMessages().getReloadErrorMessage());
        return null;
    }

    protected final Boolean reload(Message successMessage, Message errorMessage) {
        try {
            setEntity(getFacade().find(getId()));
            setInfo(successMessage);
            return true;
        } catch (EntityCRUDException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public String reloadNoChange() {
        reloadNoChange(getBeanMessages().getReloadNoChangeSuccessMessage(), getBeanMessages().getReloadNoChangeErrorMessage());
        return null;
    }

    protected final Boolean reloadNoChange(Message successMessage, Message errorMessage) {
        try {
            this.entity = getFacade().find(getId());
            if (hasSession) {
                if (this.entity == null) {
                    session.deleteFromSession(className);
                } else {
                    session.writeToSession(className, this.entity);
                }
            }
            setInfo(successMessage);
            return true;
        } catch (EntityCRUDException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (SessionException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public String clear() {
        setEntity(null);
        return null;
    }

    protected final List<SelectItem> getSelectItemsEntity(List<T> myList, Boolean addNull) throws ManagedBeanException {
        List<SelectItem> items = new ArrayList<SelectItem>();
        try {
            if (addNull) {
                items.add(new SelectItem(null, "_"));
            }
            if (myList != null) {
                for (int i = 0; i < myList.size(); i++) {
                    items.add(new SelectItem(myList.get(i), getName(myList.get(i))));
                }
            }
        } catch (Exception ex) {
            throw new ManagedBeanException(ex);
        }
        return items;
    }

    protected final List<SelectItem> getSelectItems(List<T> myList, Boolean addNull) throws ManagedBeanException {
        List<SelectItem> items = new ArrayList<SelectItem>();
        try {
            if (addNull) {
                items.add(new SelectItem(null, "_"));
            }
            if (myList != null) {
                for (int i = 0; i < myList.size(); i++) {
                    items.add(new SelectItem(getId(myList.get(i)), getName(myList.get(i))));
                }
            }
        } catch (Exception ex) {
            throw new ManagedBeanException(ex);
        }
        return items;
    }

    @Override
    public List<T> findAll() {
        return findAll(getBeanMessages().getFindAllSuccessMessage(), getBeanMessages().getFindAllErrorMessage());
    }

    protected final List<T> findAll(Message successMessage, Message errorMessage) {
        try {
            List<T> list = getFacade().findAll();
            setInfo(successMessage);
            return list;
        } catch (EntityCRUDException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return null;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return null;
        }
    }

    @Override
    public Document handleFileUpload(String fileName, byte[] fileContent) {
        return handleFileUpload(fileName, fileContent, getBeanMessages().getHandleFileUploadSuccessMesage(), getBeanMessages().getHandleFileUploadErrorMesage());
    }

    protected final Document handleFileUpload(String fileName, byte[] fileContent, Message successMessage, Message errorMessage) {
        try {
            Document docuemnt;
            docuemnt = getFacade().uploadDocument(getEntity(), fileName, fileContent, userId, userIp);
            getDocumentList().add(docuemnt);
            setInfo(successMessage);
            return docuemnt;
        } catch (DocumentException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return null;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return null;
        }
    }

    @Override
    public String removeDocument(Integer docId) {
        removeDocument(docId, getBeanMessages().getRemoveDocumentSuccessMessage(), getBeanMessages().getRemoveDocumentErrorMessage());
        return null;
    }

    protected final Boolean removeDocument(Integer docId, Message successMessage, Message errorMessage) {
        try {
            getFacade().removeDocument(docId);
            setInfo(successMessage);
            return true;
        } catch (DocumentException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public List<Document> getFileList() {
        return getFileList(getBeanMessages().getFileListSuccessMessage(), getBeanMessages().getFileListErrorMessage());
    }

    protected final List<Document> getFileList(Message successMessage, Message errorMessage) {
        if (isHasObject()) {
            try {
                List<Document> documents = getFacade().findDocuments(getEntity());
                setInfo(successMessage);
                return documents;
            } catch (DocumentException ex) {
                log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
                setError(errorMessage, ex);
                return null;
            } catch (Exception ex) {
                log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
                setError(errorMessage, ex);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Integer getDocumentValue() {
        return getDocumentValue(getBeanMessages().getGetDocumentValueErrorMessage());
    }

    protected final Integer getDocumentValue(Message errorMessage) {
        try {
            return session.<Integer>readFromSession("DOC_Document");
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return null;
        }
    }

    @Override
    public void setDocumentValue(Integer docId) {
        setDocumentValue(docId, getBeanMessages().getSetDocumentValueErrorMessage());
    }

    protected final void setDocumentValue(Integer docId, Message errorMessage) {
        try {
            session.writeToSession("DOC_Document", docId);
        } catch (SessionException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
        }
    }

    @Override
    public String makeFavorite() {
        makeFavorite(getBeanMessages().getMakeFavoriteSuccessMessage(), getBeanMessages().getMakeFavoriteErrorMessage());
        return null;
    }

    protected final Boolean makeFavorite(Message successMessage, Message errorMessage) {
        try {
            getFacade().makeFavorite(userId, getEntity(), new Date(System.currentTimeMillis()), userIp);
            setInfo(successMessage);
            return true;
        } catch (FavoriteException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    @Override
    public List<T> getFavoriteList() {
        return getFavoriteList(getBeanMessages().getGetFavoriteListErrorMessage());
    }

    protected final List<T> getFavoriteList(Message errorMessage) {
        try {
            return getFacade().findFavorites(userId);
        } catch (FavoriteException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return null;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return null;
        }
    }

    @Override
    public Boolean getFavorite() {
        return getFavorite(getBeanMessages().getGetFavoriteErrorMessage());
    }

    protected final Boolean getFavorite(Message message) {
        try {
            return getFacade().isFavorite(userId, getEntity());
        } catch (FavoriteException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(message);
            return null;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(message);
            return null;
        }
    }

    @Override
    public String removeFavorite() {
        removeFavorite(getBeanMessages().getRemoveFavoriteSuccessMessage(), getBeanMessages().getRemoveFavoriteErrorMessage());
        return null;
    }

    protected final Boolean removeFavorite(Message successMessage, Message errrorMessage) {
        try {
            getFacade().removeFavorite(getEntity(), userId);
            setInfo(successMessage);
            return true;
        } catch (FavoriteException ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errrorMessage);
            return false;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errrorMessage);
            return false;
        }
    }

    @Override
    public String createReport() {
        createReport(getBeanMessages().getCreateReportSuccessMessage(), getBeanMessages().getCreateReportErrorMessage());
        return null;
    }

    protected final Boolean createReport(Message successMessage, Message errorMessage) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().dispatch("/incm/Document");
            setInfo(successMessage);
            return true;
        } catch (Exception ex) {
            log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            setError(errorMessage, ex);
            return false;
        }
    }

    protected final boolean isInheritedFromLoggerInterface() {
        return entity instanceof Entity;
    }

    public List<Document> getDocumentList() {
        if (documentList == null) {
            try {
                documentList = getFacade().findDocuments(getEntity());
            } catch (DocumentException ex) {
                log.error(AbstractManagedBean.CONTAINER, userId, ErrorCategory.SYSTEM_ERROR, ERROR_MESSAGE, ex);
            }
        }
        return documentList;
    }

    protected final boolean isHasSession() {
        return hasSession;
    }
}
