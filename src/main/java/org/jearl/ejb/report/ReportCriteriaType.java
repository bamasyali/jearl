/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.report;

/**
 *
 * @author bamasyali
 */
public enum ReportCriteriaType {

    equal("equal", "Eşit"),
    like("like", "Benzer"),
    notEqual("notEqual", "Eşit Değil"),
    between("between", "Arasında"),
    min("min", "En Az"),
    max("max", "En Çok");
    private final String value;
    private final String label;

    private ReportCriteriaType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static ReportCriteriaType getReportCriteriaType(String value) {
        if (value != null) {
            for (ReportCriteriaType criteriaType : values()) {
                if (value.equals(criteriaType.getValue())) {
                    return criteriaType;
                }
            }
        }
        return null;
    }

    public Boolean getBetween(){
        return this.equals(ReportCriteriaType.between);
    }
}
