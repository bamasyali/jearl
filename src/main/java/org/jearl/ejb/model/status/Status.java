/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.status;

/**
 *
 * @author nodeser
 */
public interface Status {

    Integer getStatusId();

    void setStatusId(Integer statusId);

    String getStatusName();

    void setStatusName(String statusName);
}
