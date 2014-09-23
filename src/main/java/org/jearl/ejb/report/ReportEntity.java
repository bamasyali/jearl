/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.report;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import jxl.Workbook;
import jxl.write.*;
import org.jearl.ejb.exception.ReportException;
import org.jearl.log.JearlLogger;
import org.jearl.log.Logger;
import org.jearl.logback.ErrorCategory;
import org.jearl.util.PropertyUtils;

/**
 *
 * @author bamasyali
 */
public abstract class ReportEntity<T> implements Serializable {

    private final Class<T> entityClass;
    private final List<ReportColumn> reportColumns;
    private final Map<String, String> columnExpMap;
    private final JearlLogger logger;
    private List<String> columnsToView;
    private List<String> columnsToCriteria;
    private List<ReportCriteria> reportCriterias;
    private ReportColumn selectedColumn;
    private ReportCriteriaType selectedCriteriaType;
    private Object selectedValue;
    private Object selectedValue2;
    private static final Object THREAD_LOCK = new Object();

    public abstract EntityManager getEntityManager();

    public ReportEntity(Class<T> entityClass) throws ReportException {
        this.logger = Logger.getJearlLogger(ReportEntity.class);
        this.entityClass = entityClass;
        this.reportColumns = createColumns();
        this.columnsToView = new ArrayList<String>();
        this.columnsToCriteria = new ArrayList<String>();
        this.reportCriterias = new ArrayList<ReportCriteria>();
        this.columnExpMap = new HashMap<String, String>();
        for (ReportColumn column : reportColumns) {
            this.columnExpMap.put(column.getColumnName(), column.getColumnExplanation());
        }
    }

    private List<ReportColumn> createColumns() {
        Field[] fields = entityClass.getDeclaredFields();
        List<ReportColumn> columns = new ArrayList<ReportColumn>();
        for (Field field : fields) {
            if (field.getAnnotation(org.jearl.ejb.report.model.annotation.ReportColumn.class) != null) {
                try {
                    columns.add(new ReportColumn(field) {

                        @Override
                        public EntityManager getEntityManager() {
                            return ReportEntity.this.getEntityManager();
                        }
                    });
                } catch (Exception ex) {
                    logger.error(ex, ErrorCategory.DATABASE_ERROR, ex);
                }
            }
        }
        return columns;
    }

    public static Class loadClass(String className) throws ClassNotFoundException {
        Class.forName(className);
        ClassLoader classLoader = ReportEntity.class.getClassLoader();
        return classLoader.loadClass(className);
    }

    public String getEntityName() {
        return entityClass.getSimpleName();
    }

    public void addCriteria() throws ReportException {
        ReportCriteria criteria = new ReportCriteria(selectedColumn, selectedCriteriaType, selectedValue, selectedValue2);
        this.reportCriterias.add(criteria);
    }

    public File createFile(String fileName) throws ReportException, IOException, WriteException {
        synchronized (THREAD_LOCK) {
            try {
                List<T> list = getReport();
                File tempFile = new File(fileName);
                WritableWorkbook target = Workbook.createWorkbook(tempFile);
                WritableSheet targetSheet = target.createSheet("rapor", 0);
                int k = 0;
                List<String> columnsToViewId = getColumnsToViewId();
                for (String column : columnsToView) {
                    targetSheet.addCell(new Label(k++, 0, column));
                }

                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < columnsToViewId.size(); j++) {
                        try {
                            targetSheet.addCell(returnCell(j, i + 1, PropertyUtils.getProperty(list.get(i), columnsToViewId.get(j))));
                        } catch (Exception ex) {
                            logger.error(ex, ErrorCategory.DATABASE_ERROR, ex);
                        }
                    }
                }

                target.write();
                target.close();
                return tempFile;
            } catch (IOException ex) {
                throw ex;
            } catch (WriteException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ReportException(ex);
            }
        }
    }

    private WritableCell returnCell(int i, int j, Object value) throws WriteException {

        if (value != null) {
            if (value instanceof Date) {
                return new DateTime(i, j, (Date) value);
            } else if (value instanceof java.lang.Number) {
                if (value instanceof Integer) {
                    return new jxl.write.Number(i, j, (Integer) value);
                } else if (value instanceof Float) {
                    return new jxl.write.Number(i, j, (Float) value);
                } else if (value instanceof Double) {
                    return new jxl.write.Number(i, j, (Double) value);
                } else if (value instanceof Long) {
                    return new jxl.write.Number(i, j, (Long) value);
                } else if (value instanceof Short) {
                    return new jxl.write.Number(i, j, (Short) value);
                } else {
                    return new Label(i, j, value.toString());
                }

            } else {
                return new Label(i, j, value.toString());
            }
        } else {
            return new Blank(i, j);
        }
    }

    public List<T> getReport() throws ReportException {
        try {
            EntityManager em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<T> root = cq.from(this.entityClass);
            cq = cq.select(root);
            List<Predicate> predicates = new ArrayList<Predicate>();
            HashMap<String, Join> joins = new HashMap<String, Join>();

            for (ReportCriteria criteria : reportCriterias) {
                if (criteria.getReportColumn().getColumnType().getSelectItem()) {
                    if (joins.get(criteria.getReportColumn().getColumnName()) == null) {
                        joins.put(criteria.getReportColumn().getColumnName(), root.join(criteria.getReportColumn().getColumnName(), JoinType.LEFT));
                    }
                }
                if (criteria.getCriteriaType() == ReportCriteriaType.equal) {
                    if (criteria.getReportColumn().getColumnType().getSelectItem()) {
                        Join<T, Object> join = joins.get(criteria.getReportColumn().getColumnName());
                        predicates.add(cb.equal(join.get(criteria.getReportColumn().getSelectItemColumn().id()), criteria.getValue()));
                    } else {
                        predicates.add(cb.equal(root.get(criteria.getReportColumn().getColumnName()), criteria.getValue()));
                    }
                } else if (criteria.getCriteriaType() == ReportCriteriaType.notEqual) {
                    if (criteria.getReportColumn().getColumnType().getSelectItem()) {
                        Join<T, Object> join = joins.get(criteria.getReportColumn().getColumnName());
                        predicates.add(cb.notEqual(join.get(criteria.getReportColumn().getSelectItemColumn().id()), criteria.getValue()));
                    } else {
                        predicates.add(cb.notEqual(root.get(criteria.getReportColumn().getColumnName()), criteria.getValue()));
                    }
                } else if (criteria.getCriteriaType() == ReportCriteriaType.like) {
                    predicates.add(cb.like(root.<String>get(criteria.getReportColumn().getColumnName()), "%" + criteria.getValue() + "%"));
                } else if (criteria.getCriteriaType() == ReportCriteriaType.max) {
                    if (criteria.getValue() instanceof Integer) {
                        predicates.add(cb.lessThan(root.<Integer>get(criteria.getReportColumn().getColumnName()), (Integer) criteria.getValue()));
                    } else if (criteria.getValue() instanceof Long) {
                        predicates.add(cb.lessThan(root.<Long>get(criteria.getReportColumn().getColumnName()), (Long) criteria.getValue()));
                    } else if (criteria.getValue() instanceof Double) {
                        predicates.add(cb.lessThan(root.<Double>get(criteria.getReportColumn().getColumnName()), (Double) criteria.getValue()));
                    } else if (criteria.getValue() instanceof Date) {
                        predicates.add(cb.lessThan(root.<Date>get(criteria.getReportColumn().getColumnName()), (Date) criteria.getValue()));
                    }
                } else if (criteria.getCriteriaType() == ReportCriteriaType.min) {
                    if (criteria.getValue() instanceof Integer) {
                        predicates.add(cb.greaterThan(root.<Integer>get(criteria.getReportColumn().getColumnName()), (Integer) criteria.getValue()));
                    } else if (criteria.getValue() instanceof Long) {
                        predicates.add(cb.greaterThan(root.<Long>get(criteria.getReportColumn().getColumnName()), (Long) criteria.getValue()));
                    } else if (criteria.getValue() instanceof Double) {
                        predicates.add(cb.greaterThan(root.<Double>get(criteria.getReportColumn().getColumnName()), (Double) criteria.getValue()));
                    } else if (criteria.getValue() instanceof Date) {
                        predicates.add(cb.greaterThan(root.<Date>get(criteria.getReportColumn().getColumnName()), (Date) criteria.getValue()));
                    }
                } else if (criteria.getCriteriaType() == ReportCriteriaType.between) {
                    if (criteria.getValue() instanceof Integer) {
                        predicates.add(cb.between(root.<Integer>get(criteria.getReportColumn().getColumnName()), (Integer) criteria.getValue(), (Integer) criteria.getValue2()));
                    } else if (criteria.getValue() instanceof Long) {
                        predicates.add(cb.between(root.<Long>get(criteria.getReportColumn().getColumnName()), (Long) criteria.getValue(), (Long) criteria.getValue2()));
                    } else if (criteria.getValue() instanceof Double) {
                        predicates.add(cb.between(root.<Double>get(criteria.getReportColumn().getColumnName()), (Double) criteria.getValue(), (Double) criteria.getValue2()));
                    } else if (criteria.getValue() instanceof Date) {
                        predicates.add(cb.between(root.<Date>get(criteria.getReportColumn().getColumnName()), (Date) criteria.getValue(), (Date) criteria.getValue2()));
                    }
                }
            }
            cq = cq.where(predicates.toArray(new Predicate[predicates.size()]));
            return em.createQuery(cq).getResultList();
        } catch (Exception ex) {
            throw new ReportException(ex);
        }
    }

    public List<String> getColumns() {
        List<String> strings = new ArrayList<String>();
        for (ReportColumn column : reportColumns) {
            strings.add(column.getColumnExplanation());
        }
        return strings;
    }

    public List<ReportColumn> getReportColumns() {
        return reportColumns;
    }

    public List<SelectItem> getAllColumnsSelectItem() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (ReportColumn column : reportColumns) {
            selectItems.add(new SelectItem(column.getColumnName(), column.getColumnExplanation()));
        }
        return selectItems;
    }

    public List<SelectItem> getColumnsToCriteriaSelectItem() {
        if (columnsToCriteria == null) {
            return null;
        }
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(null, "_"));
        for (String column : columnsToCriteria) {
            items.add(new SelectItem(column, column));
        }
        return items;
    }

    public List<SelectItem> getCriteriaTypeSelectItem() {

        List<SelectItem> items = new ArrayList<SelectItem>();
        if (selectedColumn == null) {
            for (ReportCriteriaType criteriaType : ReportCriteriaType.values()) {
                items.add(new SelectItem(criteriaType.getValue(), criteriaType.getLabel()));
            }
        } else {
            for (ReportCriteriaType criteriaType : selectedColumn.getColumnType().getCriteriaTypes()) {
                items.add(new SelectItem(criteriaType.getValue(), criteriaType.getLabel()));
            }
        }
        return items;
    }

    public List<String> getColumnsToCriteria() {
        return columnsToCriteria;
    }

    public void setColumnsToCriteria(List<String> columnsToCriteria) {
        this.columnsToCriteria = columnsToCriteria;
    }

    public List<String> getColumnsToView() {
        return columnsToView;
    }

    public List<String> getColumnsToViewId() {
        List<String> list = new ArrayList<String>();
        if (columnsToView != null) {
            for (ReportColumn column : reportColumns) {
                for (String s : columnsToView) {
                    if (s.equals(column.getColumnExplanation())) {
                        list.add(column.getColumnName());
                    }
                }
            }
        }
        return list;
    }

    public void setColumnsToView(List<String> columnsToView) {
        this.columnsToView = columnsToView;
    }

    public String getSelectedColumn() {
        if (selectedColumn == null) {
            return null;
        } else {
            return selectedColumn.getColumnName();
        }
    }

    public ReportColumn getSelectedColumnClass() {
        return selectedColumn;
    }

    public void setSelectedColumn(String selectedColumn) {
        for (ReportColumn column : reportColumns) {
            if (column.getColumnExplanation().equals(selectedColumn)) {
                this.selectedColumn = column;
                return;
            }
        }
        this.selectedColumn = null;
    }

    public String getSelectedCriteria() {
        if (selectedCriteriaType == null) {
            return null;
        } else {
            return selectedCriteriaType.getValue();
        }
    }

    public ReportCriteriaType getSelectedCriteriaType() {
        return selectedCriteriaType;
    }

    public void setSelectedCriteria(String selectedCriteria) {
        this.selectedCriteriaType = ReportCriteriaType.getReportCriteriaType(selectedCriteria);
    }

    public ReportCriteria getRemoveCriteria() {
        return null;
    }

    public void setRemoveCriteria(ReportCriteria criteria) {
        if (this.reportCriterias != null) {
            this.reportCriterias.remove(criteria);
        }
    }

    public Object getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Object selectedValue) {
        this.selectedValue = selectedValue;
    }

    public Object getSelectedValue2() {
        return selectedValue2;
    }

    public void setSelectedValue2(Object selectedValue2) {
        this.selectedValue2 = selectedValue2;
    }

    public List<ReportCriteria> getReportCriterias() {
        return reportCriterias;
    }

    public void setReportCriterias(List<ReportCriteria> reportCriterias) {
        this.reportCriterias = reportCriterias;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public String getColumnName(String exp) {
        if (this.columnExpMap != null) {
            for (String s : this.columnExpMap.keySet()) {
                if (this.columnExpMap.get(s).equals(exp)) {
                    return s;
                }
            }
        }
        return null;
    }
}
