/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.jsf;

import java.util.List;
import javax.faces.model.SelectItem;
import org.jearl.ejb.Facade;
import org.jearl.ejb.model.document.Document;
import org.jearl.ejb.translatable.LanguageEnum;
import org.jearl.web.jsf.message.BeanMessages;

/**
 *
 * @author bamasyali
 */
public interface ManagedBean<T, PRIMARY, USER> {

    T newEntity();

    T newEntity(PRIMARY id);

    PRIMARY getId(T entity);

    String getName(T entity);

    Facade<T, USER> getFacade();

    BeanMessages getBeanMessages();

    List<T> getList();

    List<SelectItem> getSelectItems();

    LanguageEnum getLanguage();

    PRIMARY getId();

    void setId(PRIMARY value);

    T getEntity();

    void setEntity(T entity);

    boolean isHasObject();

    void loadBaseVariables(Boolean deleted, Boolean isCreate);

    String create();

    String edit();

    String remove();

    String removePhysical();

    String reload();

    String reloadNoChange();

    String clear();

    List<T> findAll();

    Document handleFileUpload(String fileName, byte[] fileContent);

    String removeDocument(Integer docId);

    List<Document> getFileList();

    Integer getDocumentValue();

    void setDocumentValue(Integer docId);

    String makeFavorite();

    List<T> getFavoriteList();

    Boolean getFavorite();

    String removeFavorite();
    
    String createReport();
}
