/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.exception;

/**
 *
 * @author bamasyali
 */
public class EntityCRUDException extends Exception {

    public EntityCRUDException(Throwable cause) {
        super(cause);
    }

    public EntityCRUDException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityCRUDException(String message) {
        super(message);
    }

    public EntityCRUDException() {
    }
}
