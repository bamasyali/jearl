/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.report;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.commons.beanutils.BeanUtils;
import org.jearl.ejb.exception.ReportException;
import org.jearl.ejb.report.model.annotation.SelectItemColumn;
import org.jearl.logback.ErrorCategory;
import org.jearl.log.JearlLogger;
import org.jearl.log.Logger;

/**
 *
 * @author bamasyali
 */
public abstract class ReportColumn implements Serializable {

    private final String columnName;
    private final String columnExplanation;
    private final ReportColumnType columnType;
    private final Field field;
    private final JearlLogger logger;
    private List<SelectItem> selectItems;
    private Map<String, String> selectItemsMap;

    public abstract EntityManager getEntityManager();

    public ReportColumn(Field field) throws ReportException {
        this.field = field;
        this.logger = Logger.getJearlLogger(ReportColumn.class);
        this.columnName = field.getName();
        org.jearl.ejb.report.model.annotation.ReportColumn rc = field.getAnnotation(org.jearl.ejb.report.model.annotation.ReportColumn.class);
        this.columnExplanation = rc.name();

        if (field.getAnnotation(SelectItemColumn.class) != null) {
            this.columnType = ReportColumnType.SelectItem;
        } else if (field.getType().equals(String.class)) {
            this.columnType = ReportColumnType.String;
        } else if (field.getType().equals(Integer.class)) {
            this.columnType = ReportColumnType.Integer;
        } else if (field.getType().equals(Long.class)) {
            this.columnType = ReportColumnType.Long;
        } else if (field.getType().equals(Double.class)) {
            this.columnType = ReportColumnType.Double;
        } else if (field.getType().equals(Date.class)) {
            this.columnType = ReportColumnType.Date;
        } else if (field.getType().equals(Boolean.class)) {
            this.columnType = ReportColumnType.Boolean;
        } else {
            throw new ReportException("Can not specify column type : " + field.getType());
        }
        this.selectItems = null;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnExplanation() {
        return columnExplanation;
    }

    public ReportColumnType getColumnType() {
        return columnType;
    }

    public List<SelectItem> getSelectItems() {
        try {
            if (selectItems == null) {
                this.selectItems = new ArrayList<SelectItem>();
                this.selectItemsMap = new HashMap<String, String>();
                selectItems.add(new SelectItem(null, "_"));
                if (this.columnType == ReportColumnType.SelectItem) {
                    SelectItemColumn selectItemColumn = field.getAnnotation(SelectItemColumn.class);
                    EntityManager em = getEntityManager();
                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery cq = cb.createQuery();
                    Root r = cq.from(selectItemColumn.entity());
                    cq.orderBy(cb.asc(r.get(selectItemColumn.name())));
                    List list = em.createQuery(cq).getResultList();
                    for (Object o : list) {
                        String id = getFieldValue(o, selectItemColumn.id());
                        String label = getFieldValue(o, selectItemColumn.name());
                        this.selectItems.add(new SelectItem(id, label));
                        this.selectItemsMap.put(id, label);
                    }
                } else {
                    this.selectItems = null;
                }
            }
        } catch (Exception ex) {
            logger.error(ex, ErrorCategory.DATABASE_ERROR, ex);
        }
        return selectItems;
    }

    public SelectItemColumn getSelectItemColumn() {
        return field.getAnnotation(SelectItemColumn.class);
    }

    public Map<String, String> getSelectItemsMap() {
        return selectItemsMap;
    }

    public Boolean isBoolean() {
        return this.columnType == ReportColumnType.Boolean;
    }

    private String getFieldValue(Object obj, String fieldName) {
        try {
            return String.valueOf(BeanUtils.getProperty(obj, fieldName));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReportColumn other = (ReportColumn) obj;
        if ((this.columnName == null) ? (other.columnName != null) : !this.columnName.equals(other.columnName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.columnName != null ? this.columnName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getColumnName();
    }
}
