/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.report;

import java.io.Serializable;
import org.jearl.ejb.exception.ReportException;

/**
 *
 * @author bamasyali
 */
public class ReportCriteria implements Serializable {

    private final ReportColumn reportColumn;
    private final ReportCriteriaType criteriaType;
    private final Object value;
    private final Object value2;
    private String explanation;

    public ReportCriteria(ReportColumn reportColumn, ReportCriteriaType criteriaType, Object value, Object value2) throws ReportException {
        if (reportColumn == null || criteriaType == null || value == null) {
            throw new ReportException("Column or Criteria can not be null");
        }
        this.reportColumn = reportColumn;
        this.criteriaType = criteriaType;
        this.value = value;
        this.value2 = value2;
        if (this.reportColumn.getColumnType().getSelectItem()) {
            this.explanation = this.reportColumn.getSelectItemsMap().get(value.toString());
            if (this.criteriaType.getBetween()) {
                this.explanation += " - " + this.reportColumn.getSelectItemsMap().get(value2.toString());
            }
        } else {
            this.explanation = this.value.toString();
            if (this.criteriaType.getBetween()) {
                this.explanation += " - " + this.value2.toString();
            }
        }
    }

    public ReportCriteriaType getCriteriaType() {
        return criteriaType;
    }

    public ReportColumn getReportColumn() {
        return reportColumn;
    }

    public Object getValue() {
        return value;
    }

    public Object getValue2() {
        return value2;
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReportCriteria other = (ReportCriteria) obj;
        if (this.reportColumn != other.reportColumn && (this.reportColumn == null || !this.reportColumn.equals(other.reportColumn))) {
            return false;
        }
        if (this.criteriaType != other.criteriaType) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.reportColumn != null ? this.reportColumn.hashCode() : 0);
        hash = 79 * hash + (this.criteriaType != null ? this.criteriaType.hashCode() : 0);
        return hash;
    }
}
