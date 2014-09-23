/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.report;

/**
 *
 * @author bamasyali
 */
public enum ReportColumnType {

    String(ReportCriteriaType.equal, ReportCriteriaType.like, ReportCriteriaType.notEqual),
    Integer(ReportCriteriaType.equal, ReportCriteriaType.between, ReportCriteriaType.notEqual, ReportCriteriaType.max, ReportCriteriaType.min),
    Long(ReportCriteriaType.equal, ReportCriteriaType.between, ReportCriteriaType.notEqual, ReportCriteriaType.max, ReportCriteriaType.min),
    Double(ReportCriteriaType.equal, ReportCriteriaType.between, ReportCriteriaType.notEqual, ReportCriteriaType.max, ReportCriteriaType.min),
    Date(ReportCriteriaType.equal, ReportCriteriaType.between, ReportCriteriaType.notEqual, ReportCriteriaType.max, ReportCriteriaType.min),
    Boolean(ReportCriteriaType.equal, ReportCriteriaType.notEqual),
    SelectItem(ReportCriteriaType.equal, ReportCriteriaType.notEqual);
    private final ReportCriteriaType[] criteriaTypes;

    private ReportColumnType(ReportCriteriaType... criteriaTypes) {
        this.criteriaTypes = criteriaTypes;
    }

    public ReportCriteriaType[] getCriteriaTypes() {
        return criteriaTypes;
    }

    public Boolean getString() {
        return this == ReportColumnType.String;
    }

    public Boolean getInteger() {
        return this == ReportColumnType.Integer;
    }

    public Boolean getLong() {
        return this == ReportColumnType.Long;
    }

    public Boolean getDouble() {
        return this == ReportColumnType.Double;
    }

    public Boolean getDate() {
        return this == ReportColumnType.Date;
    }

    public Boolean getBoolean() {
        return this == ReportColumnType.Boolean;
    }

    public Boolean getSelectItem() {
        return this == ReportColumnType.SelectItem;
    }
}
