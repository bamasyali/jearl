/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.ejb.exception;

/**
 *
 * @author bamasyali
 */
public class DocumentException extends Exception {

    public DocumentException(Throwable cause) {
        super(cause);
    }

    public DocumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentException(String message) {
        super(message);
    }

    public DocumentException() {
    }
}
