/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.report;

/**
 *
 * @author bamasyali
 */
public class ReportColumnPojo {

    private final String columnName;
    private final String columnLabel;

    public ReportColumnPojo(String columnName, String columnLabel) {
        this.columnName = columnName;
        this.columnLabel = columnLabel;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public String toString() {
        return columnName + ":" + columnLabel;
    }
}
