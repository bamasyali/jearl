/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.model.status;

/**
 *
 * @author nodeser
 */
public interface EntityWithStatus<STATUS extends Status> {

    STATUS getStatus();

    void setStatus(STATUS status);
}
