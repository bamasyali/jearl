/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.web.jsf.exception;

/**
 *
 * @author bamasyali
 */
public class ManagedBeanException extends Exception {

    public ManagedBeanException(Throwable cause) {
        super(cause);
    }

    public ManagedBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagedBeanException(String message) {
        super(message);
    }

    public ManagedBeanException() {
    }
}
